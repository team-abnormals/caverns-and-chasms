package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class CupricFireBlock extends BaseFireBlock {

	public CupricFireBlock(Properties builder) {
		super(builder, 0.5F);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		return this.canSurvive(stateIn, worldIn, currentPos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return canSurviveOnBlock(level.getBlockState(pos.below())) && level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
	}

	public static boolean canSurviveOnBlock(BlockState state) {
		return state.is(CCBlockTags.CUPRIC_FIRE_BASE_BLOCKS);
	}

	@Override
	protected boolean canBurn(BlockState stateIn) {
		return true;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		level.scheduleTick(pos, this, getFireTickDelay(level.random));
		if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
			if (!state.canSurvive(level, pos)) {
				level.removeBlock(pos, false);
			}

			BlockState below = level.getBlockState(pos.below());
			if (!below.isFireSource(level, pos, Direction.UP) && level.isRaining() && this.isNearRain(level, pos) && random.nextFloat() < 0.40F) {
				level.removeBlock(pos, false);
			}
		}
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState p_53482_, boolean p_53483_) {
		super.onPlace(state, level, pos, p_53482_, p_53483_);
		level.scheduleTick(pos, this, getFireTickDelay(level.random));
	}

	private static int getFireTickDelay(RandomSource random) {
		return 30 + random.nextInt(10);
	}

	protected boolean isNearRain(Level level, BlockPos pos) {
		return level.isRainingAt(pos) || level.isRainingAt(pos.west()) || level.isRainingAt(pos.east()) || level.isRainingAt(pos.north()) || level.isRainingAt(pos.south());
	}
}