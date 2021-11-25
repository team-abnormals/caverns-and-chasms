package com.teamabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.caverns_and_chasms.client.model.SanguineArmorModel;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.NonNullList;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.UUID;
import java.util.function.Consumer;

public class SanguineArmorItem extends ArmorItem implements IItemRenderProperties {
	private final LazyLoadedValue<Multimap<Attribute, AttributeModifier>> attributes;
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.GOLDEN_BOOTS);

	public SanguineArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
		super(material, slot, properties);
		this.attributes = new LazyLoadedValue<>(() -> {
			UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()];
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(super.getDefaultAttributeModifiers(slot));
			builder.put(CCAttributes.LIFESTEAL.get(), new AttributeModifier(uuid, "Lifesteal", SanguineArmorType.slotToType(slot).getLifestealAmount(), AttributeModifier.Operation.ADDITION));
			return builder.build();
		});
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
		return equipmentSlot == this.slot ? this.attributes.get() : super.getDefaultAttributeModifiers(equipmentSlot);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return CavernsAndChasms.MOD_ID + ":textures/models/armor/sanguine_armor.png";
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new IItemRenderProperties() {
			@Override
			public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, A properties) {
				return SanguineArmorModel.getModel(slot, entity);
			}
		});
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlot armorSlot, A _default) {
		return SanguineArmorModel.getModel(armorSlot, entityLiving);
	}

	public enum SanguineArmorType implements StringRepresentable {
		HEAD("head", EquipmentSlot.HEAD, 4),
		CHEST("chest", EquipmentSlot.CHEST, 9),
		LEGS("legs", EquipmentSlot.LEGS, 8),
		FEET("feet", EquipmentSlot.FEET, 4);

		private final String name;
		private final EquipmentSlot slot;
		private final int reduction;

		SanguineArmorType(String name, EquipmentSlot slot, int reduction) {
			this.name = name;
			this.slot = slot;
			this.reduction = reduction;
		}

		public static SanguineArmorType slotToType(EquipmentSlot slot) {
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
