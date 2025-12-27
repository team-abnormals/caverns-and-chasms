package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collections;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements RatHolder {
	@Unique
	private List<Rat> attachedRats = Lists.newArrayList();

	private float prevHostXRot;
	private float prevHostYRot;
	private float prevHostDeltaRot;

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

	@Override
	public int getMaxRats() {
		return this.getBbHeight() >= 0.8F ? (int) (3.5F * this.getBbWidth() * this.getBbHeight()) : 0;
	}

	@Override
	public boolean canHoldMoreRats() {
		return this.attachedRats.size() < this.getMaxRats();
	}
}