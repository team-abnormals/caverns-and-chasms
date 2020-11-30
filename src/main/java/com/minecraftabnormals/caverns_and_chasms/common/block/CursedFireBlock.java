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
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return this.isValidPosition(stateIn, worldIn, currentPos) ? this.getDefaultState() : Blocks.AIR.getDefaultState();
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return isCursedFireBase(worldIn.getBlockState(pos.down()).getBlock());
	}

	public static boolean isCursedFireBase(Block block) {
		return block.isIn(CCTags.Blocks.CURSED_FIRE_BASE_BLOCKS);
	}

	@Override
	protected boolean canBurn(BlockState stateIn) {
		return true;
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if (entityIn instanceof LivingEntity && ((LivingEntity) entityIn).isEntityUndead()) {
			entityIn.forceFireTicks(entityIn.getFireTimer() + 1);
			if (entityIn.getFireTimer() == 0) {
				entityIn.setFire(8);
			}

			entityIn.attackEntityFrom(DamageSource.IN_FIRE, 4.0F);
		}
	}
}