package com.teamabnormals.caverns_and_chasms.core.mixin.entity;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collections;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements RatHolder {
	@Shadow
	public abstract void push(Entity entity);

	@Shadow
	protected ItemStack useItem;
	@Unique
	private List<Rat> attachedRats = Lists.newArrayList();

	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Definition(id = "entity1", local = @Local(type = Entity.class))
	@Expression("entity1 != null")
	@ModifyExpressionValue(method = "hurt", at = @At("MIXINEXTRAS:EXPRESSION"))
	public boolean cancelKnockback(boolean original, @Local Entity entity1) {
		LivingEntity living = (LivingEntity) (Object) this;
		return original && !(entity1 instanceof Rat rat && rat.getAttachedEntity() == living);
	}

	@Override
	public List<Rat> getAttachedRats() {
		return this.attachedRats.isEmpty() ? Collections.emptyList() : Lists.newArrayList(this.attachedRats);
	}

	@Override
	public void attachRat(Rat rat) {
		this.attachedRats.add(rat);
	}

	@Override
	public void detachRat(Rat rat) {
		this.attachedRats.remove(rat);
	}

	@Override
	public void detachAllRats() {
		for (Rat rat : this.getAttachedRats())
			rat.detachFromEntity();
	}

	@Override
	public void tickRats(EntityTickList entityTickList) {
		LivingEntity livingentity = (LivingEntity) (Object) this;
		for (Rat rat : this.getAttachedRats()) {
			if (!rat.isRemoved() && rat.getAttachedEntity() == livingentity) {
				if (entityTickList.contains(rat)) {
					rat.setOldPosAndRot();
					rat.tickCount++;
					rat.tickAttached();
				}
			} else {
				rat.detachFromEntity();
			}
		}
	}

	@WrapOperation(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/PlayerHurtEntityTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;FFZ)V"))
	private void playerHurtSelf(PlayerHurtEntityTrigger instance, ServerPlayer player, Entity entity, DamageSource source, float f, float f1, boolean flag, Operation<Void> original) {
		original.call(instance, player, entity, source, f, f1, flag);
		if (player.is(entity)) {
			CCCriteriaTriggers.PLAYER_HURT_SELF.trigger(player, entity, source, f, f1, flag);
		}
	}

	@WrapOperation(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"))
	private void hurt(Level level, Entity entity, byte b, Operation<Void> original) {
		if (!this.useItem.is(CCItems.AEGIS.get())) {
			original.call(level, entity, b);
		} else if (entity instanceof Player player) {
			player.getCooldowns().addCooldown(CCItems.AEGIS.get(), 100);
			level.playSound(null, player.getX(), player.getY(), player.getZ(), CCSoundEvents.AEGIS_STUN.get(), player.getSoundSource(), 0.8F, 0.8F + level.random.nextFloat() * 0.4F);
			player.releaseUsingItem();
		}
	}

	@Override
	public int getMaxRats() {
		if (this.getType().is(CCEntityTypeTags.RATS_CANNOT_ATTACH_EXTRA_TYPES)) {
			return 0;
		}
		return this.getBbHeight() >= 0.8F ? (int) (3.5F * this.getBbWidth() * this.getBbHeight()) : 0;
	}

	@Override
	public boolean canHoldMoreRats() {
		return this.attachedRats.size() < this.getMaxRats();
	}
}