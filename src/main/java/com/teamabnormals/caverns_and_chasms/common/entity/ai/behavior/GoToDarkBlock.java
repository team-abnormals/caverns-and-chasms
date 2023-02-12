package com.teamabnormals.caverns_and_chasms.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.glare.Glare;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.glare.GlareAi;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;

public class GoToDarkBlock<E extends Glare> extends Behavior<E> {
	private final MemoryModuleType<BlockPos> locationMemory;
	private final int closeEnoughDist;
	private final float speedModifier;

	public GoToDarkBlock(MemoryModuleType<BlockPos> memoryModuleType, int closeEnough, float speedModifier) {
		super(ImmutableMap.of(memoryModuleType, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
		this.locationMemory = memoryModuleType;
		this.closeEnoughDist = closeEnough;
		this.speedModifier = speedModifier;
	}

	protected boolean checkExtraStartConditions(ServerLevel level, E glare) {
		return GlareAi.getLikedPlayer(glare).isPresent();
	}

	protected void start(ServerLevel level, Glare glare, long p_217245_) {
		glare.getBrain().setMemory(this.locationMemory, findNearestDarkBlock(level, glare));
		BlockPos blockpos = this.getTargetLocation(glare);
		BehaviorUtils.setWalkAndLookTargetMemories(glare, blockpos, this.speedModifier, this.closeEnoughDist);
	}

	public static Optional<BlockPos> findNearestDarkBlock(ServerLevel level, LivingEntity entity) {
		return BlockPos.findClosestMatch(entity.blockPosition(), 12, 8, (pos) -> Glare.shouldBeGrumpy(level, pos) && !level.getBlockState(pos.below()).getCollisionShape(level, pos.below()).isEmpty() && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty());
	}

	private BlockPos getTargetLocation(Glare p_217249_) {
		return p_217249_.getBrain().getMemory(this.locationMemory).get();
	}
}