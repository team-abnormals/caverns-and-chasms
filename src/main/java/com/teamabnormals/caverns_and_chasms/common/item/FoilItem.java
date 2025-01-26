package com.teamabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class FoilItem extends SwordItem implements DyeableLeatherItem {
	private final Multimap<Attribute, AttributeModifier> defaultModifiers;
	protected static final UUID ENTITY_REACH_UUID = UUID.fromString("FEC017E3-F1CF-4879-8FE5-11093CBFF3B9");

	public FoilItem(Tier tier, int damage, float speed, Item.Properties properties) {
		super(tier, damage, speed, properties);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(ENTITY_REACH_UUID, "Weapon modifier", 3.0F, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", speed, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	@Override
	public int getColor(ItemStack stack) {
		CompoundTag tag = stack.getTagElement("display");
		return tag != null && tag.contains("color", 99) ? tag.getInt("color") : -1;
	}

	@Override
	public float getDamage() {
		return 0.0F;
	}

	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_43274_) {
		return p_43274_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43274_);
	}

}