package com.teamabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TetherPotionItem extends PotionItem implements Equipable {

	public TetherPotionItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return this.swapWithEquipmentSlot(this, level, player, hand);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return this.getEquipmentSlot(new ItemStack(this));
	}

	@Nullable
	@Override
	public SoundEvent getEquipSound() {
		return CCSoundEvents.TETHER_POTION_EQUIP.get();
	}

	@Override
	public Component getName(ItemStack stack) {
		Component component = super.getName(stack);
		if (component.getString().contains("item.")) {
			MutableComponent intro = Component.translatable(this.getDescriptionId() + ".null");
			ItemStack regularPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), PotionUtils.getPotion(stack.getTag()));
			String newComponent = regularPotion.getDescriptionId();
			return intro.append(Component.translatable(newComponent));
		} else {
			return component;
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		List<MobEffectInstance> list = PotionUtils.getMobEffects(stack);
		List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();

		List<Component> instanttooltip = Lists.newArrayList();
		List<Component> continuoustooltip = Lists.newArrayList();

		if (list.isEmpty()) {
			continuoustooltip.add((Component.translatable("effect.none")).withStyle(ChatFormatting.GRAY));
		} else {
			for (MobEffectInstance mobeffectinstance : list) {
				MutableComponent mutablecomponent = Component.translatable(mobeffectinstance.getDescriptionId());
				MobEffect effect = mobeffectinstance.getEffect();
				Map<Attribute, AttributeModifier> map = effect.getAttributeModifiers();
				if (!map.isEmpty()) {
					for (Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierValue(mobeffectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						list1.add(new Pair<>(entry.getKey(), attributemodifier1));
					}
				}

				if (mobeffectinstance.getAmplifier() > 0) {
					mutablecomponent = Component.translatable("potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + mobeffectinstance.getAmplifier()));
				}

				if (effect.isInstantenous()) {
					instanttooltip.add(mutablecomponent.withStyle(effect.getCategory().getTooltipFormatting()));
				} else {
					mutablecomponent = Component.translatable("potion.withDuration", mutablecomponent, StringUtil.formatTickDuration(shortDuration() ? getTetherPotionDuration(mobeffectinstance.getDuration()) : mobeffectinstance.getDuration()));
					continuoustooltip.add(mutablecomponent.withStyle(effect.getCategory().getTooltipFormatting()));
				}
			}
		}

		tooltip.addAll(instanttooltip);
		if (!continuoustooltip.isEmpty()) {
			tooltip.add(Component.empty());
			tooltip.add((toolTipHeader()).withStyle(ChatFormatting.GRAY));
			tooltip.addAll(continuoustooltip);
		}

		if (!list1.isEmpty()) {
			tooltip.add(Component.empty());
			tooltip.add((Component.translatable("potion.whenDrank")).withStyle(ChatFormatting.DARK_PURPLE));

			for (Pair<Attribute, AttributeModifier> pair : list1) {
				AttributeModifier attributemodifier2 = pair.getSecond();
				double d0 = attributemodifier2.getAmount();
				double d1;
				if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
					d1 = attributemodifier2.getAmount();
				} else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D) {
					tooltip.add((Component.translatable("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.BLUE));
				} else if (d0 < 0.0D) {
					d1 *= -1.0D;
					tooltip.add((Component.translatable("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.RED));
				}
			}
		}
	}

	public MutableComponent toolTipHeader() {
		return Component.translatable("item.modifiers." + EquipmentSlot.HEAD.getName());
	}

	public boolean shortDuration() {
		return true;
	}

	public static int getTetherPotionDuration(int originalDuration) {
		int duration = Math.round(10 - 1 / ((originalDuration / 20.0F + 200) * 0.0005F)) * 20;
		return Math.max(duration, 20);
	}

	public static void updateTetherPotionEffects(LivingEntity entity, ItemStack stack, boolean infiniteDuration) {
		for (MobEffectInstance instance : PotionUtils.getMobEffects(stack)) {
			if (!instance.getEffect().isInstantenous()) {
				int i = infiniteDuration ? -1 : getTetherPotionDuration(instance.getDuration());
				MobEffectInstance currentinstance = entity.getEffect(instance.getEffect());
				MobEffectInstance newinstance = new MobEffectInstance(instance.getEffect(), i, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon());

				if (currentinstance == null || currentinstance.getAmplifier() < instance.getAmplifier()) {
					entity.addEffect(newinstance);
				} else if (currentinstance.getAmplifier() == instance.getAmplifier()) {
					entity.getActiveEffectsMap().put(instance.getEffect(), newinstance);
					if (entity instanceof ServerPlayer)
						((ServerPlayer) entity).connection.send(new ClientboundUpdateMobEffectPacket(entity.getId(), newinstance));
				}
			}
		}
	}

	public static void instantEffectParticlesAndSound(Level level, BlockPos pos, int color) {
		Vec3 vec3 = Vec3.atBottomCenterOf(pos);

		for (int i = 0; i < 8; ++i) {
			level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SPLASH_POTION)), vec3.x, vec3.y, vec3.z, level.random.nextGaussian() * 0.15D, level.random.nextDouble() * 0.2D, level.random.nextGaussian() * 0.15D);
		}

		float f3 = (float) (color >> 16 & 255) / 255.0F;
		float f4 = (float) (color >> 8 & 255) / 255.0F;
		float f6 = (float) (color >> 0 & 255) / 255.0F;

		for (int k2 = 0; k2 < 100; ++k2) {
			double d13 = level.random.nextDouble() * 4.0D;
			double d19 = level.random.nextDouble() * Math.PI * 2.0D;
			double d25 = Math.cos(d19) * d13;
			double d30 = 0.01D + level.random.nextDouble() * 0.5D;
			double d31 = Math.sin(d19) * d13;
			Particle particle1 = Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.INSTANT_EFFECT, vec3.x + d25 * 0.1D, vec3.y + 0.3D, vec3.z + d31 * 0.1D, d25, d30, d31);
			if (particle1 != null) {
				float f2 = 0.75F + level.random.nextFloat() * 0.25F;
				particle1.setColor(f3 * f2, f4 * f2, f6 * f2);
				particle1.setPower((float) d13);
			}
		}
		level.playSound(null, pos, CCSoundEvents.TETHER_POTION_EQUIP.get(), SoundSource.NEUTRAL, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
	}
}