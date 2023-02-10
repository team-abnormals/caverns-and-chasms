package com.teamabnormals.caverns_and_chasms.common.item.silver;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;

import java.util.UUID;

public class SilverArmorItem extends ArmorItem implements SilverItem {
	public static final float[] MAGIC_PROTECTION_PER_ITEM = new float[]{0.05F, 0.08F, 0.11F, 0.06F};
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.GOLDEN_BOOTS);

	public SilverArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getAttributeModifiers(slot, stack));
		UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()];
		builder.put(CCAttributes.MAGIC_PROTECTION.get(), new AttributeModifier(uuid, "Magic protection", MAGIC_PROTECTION_PER_ITEM[slot.getIndex()], AttributeModifier.Operation.MULTIPLY_BASE));
		return slot == this.slot ? builder.build() : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}
