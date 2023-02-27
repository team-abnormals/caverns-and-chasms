package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Glare;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class NearestViableOwnerGoal<T extends LivingEntity> extends TargetGoal {
	protected final Class<T> targetType;
	protected final int randomInterval;
	@Nullable
	protected LivingEntity target;
	protected TargetingConditions targetConditions;

	public NearestViableOwnerGoal(Glare p_199891_, Class<T> p_199892_, boolean p_199893_, Predicate<LivingEntity> p_199894_) {
		this(p_199891_, p_199892_, 10, p_199893_, false, p_199894_);
	}

	public NearestViableOwnerGoal(Glare p_26053_, Class<T> targetType, int randomInterval, boolean p_26056_, boolean p_26057_, @Nullable Predicate<LivingEntity> conditions) {
		super(p_26053_, p_26056_, p_26057_);
		this.targetType = targetType;
		this.randomInterval = reducedTickDelay(randomInterval);
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
		this.targetConditions = TargetingConditions.forNonCombat().range(this.getFollowDistance()).selector(conditions);
	}

	public boolean canUse() {
		if (this.mob instanceof Glare glare && glare.getLikedPlayerUUID() == null) {
			if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
				return false;
			} else {
				this.findTarget();

				if (this.target != null) {
					IDataManager manager = (IDataManager) this.target;
					Optional<UUID> ownedGlare = manager.getValue(CCDataProcessors.OWNED_GLARE_UUID);
					return ownedGlare.isEmpty() || this.mob.level.getPlayerByUUID(ownedGlare.get()) != null;
				}

				return false;
			}
		}

		return false;
	}

	protected void findTarget() {
		this.target = this.mob.level.getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
	}

	public void start() {
		if (this.mob instanceof Glare glare) {
			glare.setLikedPlayerUUID(this.target.getUUID());
			glare.playSound(CCSoundEvents.ENTITY_GLARE_TAME.get(), 1.0F, 1.0F);
			IDataManager manager = (IDataManager) this.target;
			manager.setValue(CCDataProcessors.OWNED_GLARE_UUID, Optional.of(glare.getUUID()));
			super.start();
		}
	}

	public void setTarget(@Nullable LivingEntity entity) {
		this.target = entity;
	}
}