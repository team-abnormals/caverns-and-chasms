package com.teamabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEnchantments;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingVisibilityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Collection;
import java.util.UUID;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CowlItem extends DyeableArmorItem {

	public CowlItem(ArmorMaterial material, ArmorItem.Type slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getAttributeModifiers(slot, stack));
		UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_TYPE.get(this.type);
		double amount = 0.4D + 0.1D * stack.getEnchantmentLevel(CCEnchantments.CONCEAL.get());
		builder.put(CCAttributes.STEALTH.get(), new AttributeModifier(uuid, "Stealth", amount, Operation.ADDITION));
		return slot == this.getEquipmentSlot() ? builder.build() : super.getAttributeModifiers(slot, stack);
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
		return entity.isCrouching() && headStack.is(CCItems.COWL.get()) && headStack.getEnchantmentLevel(CCEnchantments.OBSCURITY.get()) > 0;
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		IDataManager dataManager = ((IDataManager) entity);
		Level level = entity.level();
		if (!level.isClientSide()) {
			boolean isInvisible = dataManager.getValue(CCDataProcessors.OBSCURITY_INVISIBILITY);
			boolean shouldBeInvisible = shouldBeInvisible(entity);
			if (isInvisible != shouldBeInvisible(entity)) {
				dataManager.setValue(CCDataProcessors.OBSCURITY_INVISIBILITY, shouldBeInvisible);
				poofParticles(level, entity.getBoundingBox(), 6);
			}
		}
	}

	public static void poofParticles(Level level, AABB box, int loops) {
		if (!level.isClientSide()) {
			RandomSource random = level.getRandom();
			for (int i = 0; i < loops; i++) {
				double x = box.min(Direction.Axis.X) + (random.nextFloat() * box.getXsize());
				double y = box.min(Direction.Axis.Y) + (random.nextFloat() * box.getYsize());
				double z = box.min(Direction.Axis.Z) + (random.nextFloat() * box.getZsize());
				NetworkUtil.spawnParticle("minecraft:poof", level.dimension(), x, y, z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingVisiblity(LivingVisibilityEvent event) {
		LivingEntity entity = event.getEntity();
		double stealth = 1.0D;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.ARMOR) {
				ItemStack stack = entity.getItemBySlot(slot);
				Collection<AttributeModifier> stealthModifiers = stack.getAttributeModifiers(slot).get(CCAttributes.STEALTH.get());
				if (!stealthModifiers.isEmpty()) {
					stealth -= stealthModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
				}
			}
		}

		if (entity.isCrouching() && entity.getItemBySlot(EquipmentSlot.HEAD).getEnchantmentLevel(CCEnchantments.OBSCURITY.get()) > 0) {
			stealth = 0.0D;
		}

		if (stealth < 1.0D) {
			event.modifyVisibility(Math.max(0.0D, stealth));
		}
	}
}