package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

public class AmbientBubbleColumnBlock extends Block implements BucketPickup {

	public AmbientBubbleColumnBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		BlockState blockstate = level.getBlockState(pos.above());
		if (blockstate.isAir()) {
			if (!level.isClientSide) {
				ServerLevel serverlevel = (ServerLevel) level;

				for (int i = 0; i < 2; ++i) {
					serverlevel.sendParticles(ParticleTypes.SPLASH, (double) pos.getX() + level.random.nextDouble(), (double) (pos.getY() + 1), (double) pos.getZ() + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
					serverlevel.sendParticles(ParticleTypes.BUBBLE, (double) pos.getX() + level.random.nextDouble(), (double) (pos.getY() + 1), (double) pos.getZ() + level.random.nextDouble(), 1, 0.0D, 0.01D, 0.0D, 0.2D);
				}
			}
		}
	}

	@Override
	public void tick(BlockState p_220888_, ServerLevel p_220889_, BlockPos p_220890_, RandomSource p_220891_) {
		updateColumn(p_220889_, p_220890_, p_220888_, p_220889_.getBlockState(p_220890_.below()));
	}

	@Override
	public FluidState getFluidState(BlockState p_51016_) {
		return Fluids.WATER.getSource(false);
	}

	public static void updateColumn(LevelAccessor p_152708_, BlockPos p_152709_, BlockState p_152710_) {
		updateColumn(p_152708_, p_152709_, p_152708_.getBlockState(p_152709_), p_152710_);
	}

	public static void updateColumn(LevelAccessor p_152703_, BlockPos p_152704_, BlockState p_152705_, BlockState p_152706_) {
		if (canExistIn(p_152705_)) {
			BlockState blockstate = getColumnState(p_152706_);
			p_152703_.setBlock(p_152704_, blockstate, 2);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = p_152704_.mutable().move(Direction.UP);

			while (canExistIn(p_152703_.getBlockState(blockpos$mutableblockpos))) {
				if (!p_152703_.setBlock(blockpos$mutableblockpos, blockstate, 2)) {
					return;
				}

				blockpos$mutableblockpos.move(Direction.UP);
			}

		}
	}

	private static boolean canExistIn(BlockState p_152716_) {
		return p_152716_.is(CCBlocks.AMBIENT_BUBBLE_COLUMN.get()) || p_152716_.is(Blocks.WATER) && p_152716_.getFluidState().getAmount() >= 8 && p_152716_.getFluidState().isSource();
	}

	private static BlockState getColumnState(BlockState state) {
		if (state.is(CCBlocks.AMBIENT_BUBBLE_COLUMN.get())) {
			return state;
		} else {
			return state.is(CCBlocks.MAGMATIC_RHYOLITE.get()) ? CCBlocks.AMBIENT_BUBBLE_COLUMN.get().defaultBlockState() : Blocks.WATER.defaultBlockState();
		}
	}

	@Override
	public void animateTick(BlockState p_220893_, Level p_220894_, BlockPos p_220895_, RandomSource p_220896_) {
		double d0 = (double) p_220895_.getX();
		double d1 = (double) p_220895_.getY();
		double d2 = (double) p_220895_.getZ();
		p_220894_.addAlwaysVisibleParticle(ParticleTypes.CURRENT_DOWN, d0 + 0.5D, d1 + 0.8D, d2, 0.0D, 0.0D, 0.0D);
		if (p_220896_.nextInt(200) == 0) {
			p_220894_.playLocalSound(d0, d1, d2, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundSource.BLOCKS, 0.2F + p_220896_.nextFloat() * 0.2F, 0.9F + p_220896_.nextFloat() * 0.15F, false);
		}
	}

	@Override
	public BlockState updateShape(BlockState p_50990_, Direction p_50991_, BlockState p_50992_, LevelAccessor p_50993_, BlockPos p_50994_, BlockPos p_50995_) {
		p_50993_.scheduleTick(p_50994_, Fluids.WATER, Fluids.WATER.getTickDelay(p_50993_));
		if (!p_50990_.canSurvive(p_50993_, p_50994_) || p_50991_ == Direction.DOWN || p_50991_ == Direction.UP && !p_50992_.is(CCBlocks.AMBIENT_BUBBLE_COLUMN.get()) && canExistIn(p_50992_)) {
			p_50993_.scheduleTick(p_50994_, this, 5);
		}

		return super.updateShape(p_50990_, p_50991_, p_50992_, p_50993_, p_50994_, p_50995_);
	}

	@Override
	public boolean canSurvive(BlockState p_50986_, LevelReader p_50987_, BlockPos p_50988_) {
		BlockState blockstate = p_50987_.getBlockState(p_50988_.below());
		return blockstate.is(CCBlocks.AMBIENT_BUBBLE_COLUMN.get()) || blockstate.is(CCBlocks.MAGMATIC_RHYOLITE.get());
	}

	@Override
	public VoxelShape getShape(BlockState p_51005_, BlockGetter p_51006_, BlockPos p_51007_, CollisionContext p_51008_) {
		return Shapes.empty();
	}

	@Override
	public RenderShape getRenderShape(BlockState p_51003_) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public ItemStack pickupBlock(LevelAccessor p_152712_, BlockPos p_152713_, BlockState p_152714_) {
		p_152712_.setBlock(p_152713_, Blocks.AIR.defaultBlockState(), 11);
		return new ItemStack(Items.WATER_BUCKET);
	}

	@Override
	public Optional<SoundEvent> getPickupSound() {
		return Fluids.WATER.getPickupSound();
	}
}
