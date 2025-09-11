package com.teamabnormals.caverns_and_chasms.common.item.copper;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.teamabnormals.caverns_and_chasms.client.model.CopperArmorModel;
import com.teamabnormals.caverns_and_chasms.core.mixin.ItemStackAccessor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class CopperArmorItem extends ArmorItem implements WeatheringCopperItem {
	private final WeatherState weatherState;

	public CopperArmorItem(WeatherState weatherState, ArmorMaterial material, ArmorItem.Type slot, Properties properties) {
		super(material, slot, properties);
		this.weatherState = weatherState;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getAttributeModifiers(slot, stack));
		UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_TYPE.get(this.type);
		builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Copper armor slowness", -0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		builder.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(uuid, "Copper armor gravity", 0.04F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		return slot == this.getEquipmentSlot() ? builder.build() : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
		this.updateOxidation(stack, level);
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return this.getOrCreateDescriptionId(stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> properties) {
				return slot == EquipmentSlot.HEAD ? CopperArmorModel.INSTANCE : properties;
			}
		});
	}

	@Override
	public WeatherState getAge() {
		return this.weatherState;
	}
}
