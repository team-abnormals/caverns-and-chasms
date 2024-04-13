package com.teamabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.client.model.SanguineArmorModel;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.UUID;
import java.util.function.Consumer;

public class SanguineArmorItem extends ArmorItem {

	public SanguineArmorItem(ArmorMaterial material, ArmorItem.Type slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getAttributeModifiers(slot, stack));
		UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_TYPE.get(this.type);
		builder.put(CCAttributes.LIFESTEAL.get(), new AttributeModifier(uuid, "Lifesteal", 0.05D, AttributeModifier.Operation.MULTIPLY_BASE));
		return slot == this.getEquipmentSlot() ? builder.build() : super.getAttributeModifiers(slot, stack);
	}

	public static void causeHealParticles(LivingEntity entity, float amount) {
		RandomSource random = entity.getRandom();
		int count = 3;
		for (int i = 0; i < count; ++i) {
			double d0 = random.nextGaussian() * 0.02D;
			double d1 = random.nextGaussian() * 0.02D;
			double d2 = random.nextGaussian() * 0.02D;
			NetworkUtil.spawnParticle(ParticleTypes.HEART.writeToString(), entity.getRandomX(0.75D), entity.getEyeY() + 0.1F + random.nextDouble() * 0.3F, entity.getRandomZ(0.75D), d0, d1, d2);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> properties) {
				return slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST ? SanguineArmorModel.INSTANCE : properties;
			}
		});
	}
}
