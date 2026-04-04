package com.teamabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.teamabnormals.caverns_and_chasms.client.model.ToolbeltModel;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEnchantments;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.UUID;
import java.util.function.Consumer;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class ToolbeltItem extends DyeableArmorItem {

	public ToolbeltItem(ArmorMaterial material, Type slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getAttributeModifiers(slot, stack));
		UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_TYPE.get(this.type);
		double amount = 2.0D + 1.0D * stack.getEnchantmentLevel(CCEnchantments.EXTENDING.get());
		builder.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(uuid, "Reach", amount, Operation.ADDITION));
		return slot == this.getEquipmentSlot() ? builder.build() : super.getAttributeModifiers(slot, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> properties) {
				return slot == EquipmentSlot.LEGS ? (entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmorItem ? ToolbeltModel.ARMOR_INSTANCE : ToolbeltModel.INSTANCE) : properties;
			}
		});
	}
}