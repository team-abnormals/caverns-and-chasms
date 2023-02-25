package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.item.silver.SilverItem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

	@Inject(method = "doPostDamageEffects", at = @At("HEAD"))
	private static void doPostDamageEffects(LivingEntity attacker, Entity target, CallbackInfo ci) {
		if (target instanceof LivingEntity livingTarget) {
			ItemStack stack = attacker.getMainHandItem();
			Collection<AttributeModifier> magicDamageModifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(CCAttributes.MAGIC_DAMAGE.get());

			if (!magicDamageModifiers.isEmpty() && !stack.is(CCItems.SILVER_PICKAXE.get())) {
				float magicDamage = (float) magicDamageModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
				if (target instanceof WitherBoss)
					magicDamage *= 3.0F;
				livingTarget.invulnerableTime = 0;
				livingTarget.hurt(DamageSource.MAGIC, magicDamage);
				SilverItem.causeMagicDamageParticles(livingTarget);
			}

			Collection<AttributeModifier> slownessModifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(CCAttributes.SLOWNESS_INFLICTION.get());
			if (!slownessModifiers.isEmpty()) {
				int slownessLevel = (int) slownessModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
				livingTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, slownessLevel));
			}
		}
	}
}