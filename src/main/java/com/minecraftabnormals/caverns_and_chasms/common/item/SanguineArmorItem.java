package com.minecraftabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.minecraftabnormals.abnormals_core.core.util.item.filling.TargetedItemGroupFiller;
import com.minecraftabnormals.caverns_and_chasms.client.model.SanguineArmorModel;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.LazyValue;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

public class SanguineArmorItem extends ArmorItem {
	private final LazyValue<Multimap<Attribute, AttributeModifier>> attributes;
	private static final TargetedItemGroupFiller FILLER = new TargetedItemGroupFiller(() -> Items.GOLDEN_BOOTS);

	public SanguineArmorItem(IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
		super(material, slot, properties);
		this.attributes = new LazyValue<>(() -> {
			UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()];
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(super.getDefaultAttributeModifiers(slot));
			builder.put(CCAttributes.LIFESTEAL.get(), new AttributeModifier(uuid, "Lifesteal", SanguineArmorType.slotToType(slot).getLifestealAmount(), AttributeModifier.Operation.ADDITION));
			return builder.build();
		});
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
		return equipmentSlot == this.slot ? this.attributes.get() : super.getDefaultAttributeModifiers(equipmentSlot);
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return CavernsAndChasms.MOD_ID + ":textures/models/armor/sanguine_armor.png";
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlotType armorSlot, A _default) {
		return SanguineArmorModel.getModel(armorSlot, entityLiving);
	}


	public enum SanguineArmorType implements IStringSerializable {
		HEAD("head", EquipmentSlotType.HEAD, 4),
		CHEST("chest", EquipmentSlotType.CHEST, 9),
		LEGS("legs", EquipmentSlotType.LEGS, 8),
		FEET("feet", EquipmentSlotType.FEET, 4);

		private final String name;
		private final EquipmentSlotType slot;
		private final int reduction;

		SanguineArmorType(String name, EquipmentSlotType slot, int reduction) {
			this.name = name;
			this.slot = slot;
			this.reduction = reduction;
		}

		public static SanguineArmorType slotToType(EquipmentSlotType slot) {
			for (SanguineArmorType type : values())
				if (type.slot == slot)
					return type;
			return HEAD;
		}

		public float getLifestealAmount() {
			return this.reduction * 0.01F;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}
	}
}
