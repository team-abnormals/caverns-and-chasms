package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.blueprint.common.network.particle.SpawnParticlesPayload.ParticleInstance;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.client.model.SanguineArmorModel;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, value = Dist.CLIENT)
public class SanguineArmorItem extends ArmorItem {

	public SanguineArmorItem(Holder<ArmorMaterial> material, ArmorItem.Type slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
		ItemAttributeModifiers modifiers = super.getDefaultAttributeModifiers(stack);
		EquipmentSlotGroup slot = EquipmentSlotGroup.bySlot(type.getSlot());
		ResourceLocation name = ResourceLocation.withDefaultNamespace("armor." + type.getName());
		modifiers = modifiers.withModifierAdded(CCAttributes.LIFESTEAL, new AttributeModifier(name, 0.05D, Operation.ADD_VALUE), slot);
		return modifiers;
	}

	public static void causeHealEffects(LivingEntity entity) {
		RandomSource random = entity.getRandom();

		if (entity.level() instanceof ServerLevel serverLevel) {
			List<ParticleInstance> particles = new ArrayList<>();
			for (int i = 0; i < 3; ++i) {
				double d0 = entity.getRandomX(0.75D);
				double d1 = entity.getEyeY() + 0.1F + random.nextDouble() * 0.3F;
				double d2 = entity.getRandomZ(0.75D);

				double d3 = random.nextGaussian() * 0.02D;
				double d4 = random.nextGaussian() * 0.02D;
				double d5 = random.nextGaussian() * 0.02D;

				particles.add(new ParticleInstance(d0, d1, d2, d3, d4, d5));
			}
			NetworkUtil.spawnParticle(serverLevel, ParticleTypes.HEART, particles);
		}
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), CCSoundEvents.SANGUINE_HEAL.get(), entity.getSoundSource(), 1.0F, 1.0F);
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> properties) {
				return SanguineArmorModel.INSTANCE;
			}
		}, CCItems.SANGUINE_HELMET, CCItems.SANGUINE_CHESTPLATE);
	}
}
