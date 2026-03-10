package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TinplateBlock extends RotatedPillarBlock {

	public TinplateBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void onProjectileHit(Level level, BlockState state, BlockHitResult result, Projectile projectile) {
		super.onProjectileHit(level, state, result, projectile);
		IDataManager data = (IDataManager) projectile;
	}
}
