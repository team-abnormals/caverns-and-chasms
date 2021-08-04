package com.minecraftabnormals.caverns_and_chasms.common.block;

import com.minecraftabnormals.caverns_and_chasms.core.other.CCTags;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class CursedFireBlock extends AbstractFireBlock {

	public CursedFireBlock(Properties builder) {
		super(builder, 4.0F);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return this.canSurvive(stateIn, worldIn, currentPos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return isCursedFireBase(worldIn.getBlockState(pos.below()).getBlock());
	}

	public static boolean isCursedFireBase(Block block) {
		return block.is(CCTags.Blocks.CURSED_FIRE_BASE_BLOCKS);
	}

	@Override
	protected boolean canBurn(BlockState stateIn) {
		return true;
	}

	@Override
	public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if (entityIn instanceof LivingEntity && ((LivingEntity) entityIn).isInvertedHealAndHarm()) {
			entityIn.setRemainingFireTicks(entityIn.getRemainingFireTicks() + 1);
			if (entityIn.getRemainingFireTicks() == 0) {
				entityIn.setSecondsOnFire(8);
			}

			entityIn.hurt(DamageSource.IN_FIRE, 4.0F);
		}
	}
}