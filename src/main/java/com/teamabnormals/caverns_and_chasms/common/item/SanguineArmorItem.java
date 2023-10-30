package com.teamabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.caverns_and_chasms.client.model.SanguineArmorModel;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.UUID;
import java.util.function.Consumer;

public class SanguineArmorItem extends ArmorItem {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(CCItems.SILVER_BOOTS);

	public SanguineArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getAttributeModifiers(this.getSlot(), stack));
		UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()];
		builder.put(CCAttributes.LIFESTEAL.get(), new AttributeModifier(uuid, "Lifesteal", 0.05D, AttributeModifier.Operation.MULTIPLY_BASE));
		return slot == this.slot ? builder.build() : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return CavernsAndChasms.MOD_ID + ":textures/models/armor/sanguine_armor.png";
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
				return SanguineArmorModel.getModel(slot, entity);
			}
		});
	}
}
