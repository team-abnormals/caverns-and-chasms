package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.blueprint.common.network.particle.SpawnParticlesPayload.ParticleInstance;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.mixin.entity.LivingEntityAccessor;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEnchantmentEffects;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent.LivingVisibilityEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.List;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CowlItem extends ArmorItem {

	public CowlItem(Holder<ArmorMaterial> material, ArmorItem.Type slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
		ItemAttributeModifiers modifiers = super.getDefaultAttributeModifiers(stack);
		EquipmentSlotGroup slot = EquipmentSlotGroup.bySlot(type.getSlot());
		ResourceLocation name = ResourceLocation.withDefaultNamespace("armor." + type.getName());
		modifiers = modifiers.withModifierAdded(CCAttributes.STEALTH, new AttributeModifier(name, 0.4F, Operation.ADD_VALUE), slot);
		return modifiers;
	}

	@Override
	public boolean isEnderMask(ItemStack stack, Player player, EnderMan enderman) {
		return true;
	}

	@SubscribeEvent
	public static void hoodEquippedEvent(LivingEquipmentChangeEvent event) {
		if (event.getTo().is(CCItems.COWL.get()) || event.getFrom().is(CCItems.COWL.get())) {
			if (event.getEntity() instanceof Player player) {
				player.refreshDisplayName();
			}
		}
	}

	public static boolean shouldBeInvisible(LivingEntity entity) {
		ItemStack headStack = entity.getItemBySlot(EquipmentSlot.HEAD);
		return entity.isCrouching() && headStack.is(CCItems.COWL.get()) && EnchantmentHelper.has(headStack, CCEnchantmentEffects.INVISIBLE_WHEN_CROUCHING.get());
	}

	@SubscribeEvent
	public static void onLivingUpdate(EntityTickEvent.Post event) {
		Entity entity = event.getEntity();
		Level level = entity.level();
		if (level instanceof ServerLevel serverLevel && entity instanceof LivingEntity living) {
			ItemStack headstack = living.getItemBySlot(EquipmentSlot.HEAD);
			if (headstack.is(CCItems.COWL.get()) && EnchantmentHelper.has(headstack, CCEnchantmentEffects.INVISIBLE_WHEN_CROUCHING.get())) {
				living.setInvisible(living.isCrouching());
				if (!living.isCrouching() && entity instanceof LivingEntityAccessor accessor) {
					accessor.invokeUpdateInvisibilityStatus();
				}
			}

			IDataManager dataManager = ((IDataManager) living);
			boolean isInvisible = dataManager.getValue(CCDataProcessors.OBSCURITY_INVISIBILITY);
			boolean shouldBeInvisible = shouldBeInvisible(living);
			if (isInvisible != shouldBeInvisible(living)) {
				dataManager.setValue(CCDataProcessors.OBSCURITY_INVISIBILITY, shouldBeInvisible);
				poofParticles(serverLevel, living.getBoundingBox(), 6);
			}
		}
	}

	public static void poofParticles(ServerLevel level, AABB box, int loops) {
		RandomSource random = level.getRandom();
		for (int i = 0; i < loops; i++) {
			double x = box.min(Direction.Axis.X) + (random.nextFloat() * box.getXsize());
			double y = box.min(Direction.Axis.Y) + (random.nextFloat() * box.getYsize());
			double z = box.min(Direction.Axis.Z) + (random.nextFloat() * box.getZsize());
			NetworkUtil.spawnParticle(level, ParticleTypes.POOF, List.of(new ParticleInstance(x, y, z, 0.0D, 0.0D, 0.0D)));
		}
	}

	@SubscribeEvent
	public static void onLivingVisiblity(LivingVisibilityEvent event) {
		LivingEntity entity = event.getEntity();
		double stealth = 1.0D;

		if (entity.getAttribute(CCAttributes.STEALTH) != null) {
			stealth -= entity.getAttribute(CCAttributes.STEALTH).getValue();
		}

		if (entity.isCrouching() && EnchantmentHelper.has(entity.getItemBySlot(EquipmentSlot.HEAD), CCEnchantmentEffects.INVISIBLE_WHEN_CROUCHING.get())) {
			stealth = 0.0D;
		}

		if (stealth < 1.0D) {
			event.modifyVisibility(Math.max(0.0D, stealth));
		}
	}
}