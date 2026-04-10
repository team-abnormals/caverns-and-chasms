package com.teamabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.util.AttributeUtil;

import java.util.List;
import java.util.function.Consumer;

public class TetherPotionItem extends PotionItem implements Equipable {
	private static final Component NO_EFFECT = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);

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

	@Override
	public Holder<SoundEvent> getEquipSound() {
		return CCSoundEvents.TETHER_POTION_EQUIP;
	}

	@Override
	public Component getName(ItemStack stack) {
		Component component = super.getName(stack);
		if (component.getString().contains("item.")) {
			MutableComponent intro = Component.translatable(this.getDescriptionId() + ".null");
			Potion potion = stack.get(DataComponents.POTION_CONTENTS).potion().get().value();
			if (potion instanceof SubtlePotion subtlePotion) {
				potion = subtlePotion.getPotion();
				intro = Component.translatable("item.caverns_and_chasms.potion.subtle").append(" ").append(intro);
			}
			ItemStack regularPotion = PotionContents.createItemStack(Items.POTION, Holder.direct(potion));
			String newComponent = regularPotion.getDescriptionId();
			return intro.append(Component.translatable(newComponent));
		} else {
			return component;
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
		if (contents != null) {
			this.addPotionTooltip(contents.getAllEffects(), tooltip::add, 1.0F, context.tickRate());
		}
	}

	public void addPotionTooltip(Iterable<MobEffectInstance> effects, Consumer<Component> tooltipAdder, float durationFactor, float ticksPerSecond) {
		List<Pair<Holder<Attribute>, AttributeModifier>> list = Lists.newArrayList();
		boolean flag = true;

		List<Component> instanttooltip = Lists.newArrayList();
		List<Component> continuoustooltip = Lists.newArrayList();

		for (MobEffectInstance effect : effects) {
			flag = false;
			MutableComponent mutablecomponent = Component.translatable(effect.getDescriptionId());
			Holder<MobEffect> holder = effect.getEffect();
			holder.value().createModifiers(effect.getAmplifier(), (p_331556_, p_330860_) -> list.add(new Pair<>(p_331556_, p_330860_)));
			if (effect.getAmplifier() > 0) {
				mutablecomponent = Component.translatable(
						"potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + effect.getAmplifier())
				);
			}

			if (effect.getEffect().value().isInstantenous()) {
				instanttooltip.add(mutablecomponent.withStyle(effect.getEffect().value().getCategory().getTooltipFormatting()));
			} else {
				mutablecomponent = Component.translatable("potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(effect, durationFactor, ticksPerSecond));
				continuoustooltip.add(mutablecomponent.withStyle(effect.getEffect().value().getCategory().getTooltipFormatting()));
			}

			tooltipAdder.accept(mutablecomponent.withStyle(holder.value().getCategory().getTooltipFormatting()));
		}

		if (flag) {
			tooltipAdder.accept(NO_EFFECT);
		}

		instanttooltip.forEach(tooltipAdder);
		if (!continuoustooltip.isEmpty()) {
			tooltipAdder.accept(Component.empty());
			tooltipAdder.accept((toolTipHeader()).withStyle(ChatFormatting.GRAY));
			continuoustooltip.forEach(tooltipAdder);
		}

		if (!list.isEmpty()) {
			tooltipAdder.accept(CommonComponents.EMPTY);
			tooltipAdder.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
			AttributeUtil.addPotionTooltip(list, tooltipAdder);
		}
	}

	public MutableComponent toolTipHeader() {
		return Component.translatable("item.modifiers." + EquipmentSlot.HEAD.getName());
	}

	public static int getTetherPotionDuration(int originalDuration) {
		int duration = Math.round(10 - 1 / ((originalDuration / 20.0F + 200) * 0.0005F)) * 20;
		return Math.max(duration, 20);
	}

	public static void updateTetherPotionEffects(LivingEntity entity, ItemStack stack, boolean infiniteDuration) {
		for (MobEffectInstance instance : stack.get(DataComponents.POTION_CONTENTS).getAllEffects()) {
			if (!instance.getEffect().value().isInstantenous()) {
				int i = infiniteDuration ? -1 : getTetherPotionDuration(instance.getDuration());
				MobEffectInstance currentinstance = entity.getEffect(instance.getEffect());
				MobEffectInstance newinstance = new MobEffectInstance(instance.getEffect(), i, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon());

				if (currentinstance == null || currentinstance.getAmplifier() < instance.getAmplifier()) {
					entity.addEffect(newinstance);
				} else if (currentinstance.getAmplifier() == instance.getAmplifier()) {
					entity.getActiveEffectsMap().put(instance.getEffect(), newinstance);
					if (entity instanceof ServerPlayer serverPlayer)
						serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(entity.getId(), newinstance, false));
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