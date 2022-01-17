package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class CursedFireBlock extends BaseFireBlock {

	public CursedFireBlock(Properties builder) {
		super(builder, 4.0F);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		return this.canSurvive(stateIn, worldIn, currentPos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return canSurviveOnBlock(level.getBlockState(pos.below()));
	}

	public static boolean canSurviveOnBlock(BlockState state) {
		return state.is(CCBlockTags.CURSED_FIRE_BASE_BLOCKS);
	}

	@Override
	protected boolean canBurn(BlockState stateIn) {
		return true;
	}

	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
		if (entityIn instanceof LivingEntity && ((LivingEntity) entityIn).isInvertedHealAndHarm()) {
			entityIn.setRemainingFireTicks(entityIn.getRemainingFireTicks() + 1);
			if (entityIn.getRemainingFireTicks() == 0) {
				entityIn.setSecondsOnFire(8);
			}

			entityIn.hurt(DamageSource.IN_FIRE, 4.0F);
		}
	}
}