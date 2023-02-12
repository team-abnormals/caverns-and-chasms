package com.teamabnormals.caverns_and_chasms.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.glare.Glare;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.glare.GlareAi;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMemoryModuleTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class GetMadAtDarkness<E extends Glare> extends Behavior<E> {

	public GetMadAtDarkness() {
		super(ImmutableMap.of(CCMemoryModuleTypes.NEAREST_DARK_BLOCK.get(), MemoryStatus.VALUE_PRESENT));
	}

	protected boolean checkExtraStartConditions(ServerLevel level, E glare) {
		return GlareAi.getLikedPlayer(glare).isPresent();
	}

	protected void tick(ServerLevel level, E glare, long p_23123_) {
	}

	protected void start(ServerLevel level, Glare glare, long p_217245_) {
		for (int i = 0; i < 3; i++) {
			NetworkUtil.spawnParticle(ParticleTypes.ANGRY_VILLAGER.writeToString(), glare.getRandomX(1.0D), glare.getRandomY() + 0.5D, glare.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
		}

		glare.getBrain().eraseMemory(CCMemoryModuleTypes.NEAREST_DARK_BLOCK.get());
	}
}