package com.teamabnormals.caverns_and_chasms.common.block;

import java.util.Random;

import com.teamabnormals.caverns_and_chasms.core.other.CCDamageSources;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LavaLampBlock extends DirectionalBlock implements SimpleWaterloggedBlock {
	private static final VoxelShape X_LAVA_SHAPE = box(2.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D);
	private static final VoxelShape Y_LAVA_SHAPE = box(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D);
	private static final VoxelShape Z_LAVA_SHAPE = box(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 14.0D);
	private static final VoxelShape X_AXIS_SHAPE = Shapes.or(box(0.0D, 2.0D, 2.0D, 2.0D, 14.0D, 14.0D), box(14.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D), X_LAVA_SHAPE);
	private static final VoxelShape Y_AXIS_SHAPE = Shapes.or(box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D), box(2.0D, 14.0D, 2.0D, 14.0D, 16.0D, 14.0D), Y_LAVA_SHAPE);
	private static final VoxelShape Z_AXIS_SHAPE = Shapes.or(box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 2.0D), box(2.0D, 2.0D, 14.0D, 14.0D, 14.0D, 16.0D), Z_LAVA_SHAPE);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public LavaLampBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		switch (state.getValue(FACING).getAxis()) {
		case X:
		default:
			return X_AXIS_SHAPE;
		case Y:
			return Y_AXIS_SHAPE;
		case Z:
			return Z_AXIS_SHAPE;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(direction.getOpposite()));

		Direction facing = blockstate.is(this) && blockstate.getValue(FACING).getAxis() == direction.getAxis() ? blockstate.getValue(FACING) : direction;
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

		return super.getStateForPlacement(context).setValue(FACING, facing).setValue(WATERLOGGED, fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!level.isClientSide() && state.getValue(WATERLOGGED)) {
			Random random = level.random;
			level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (this.isEntityTouchingLava(state, pos, entity)) {
			if (!entity.fireImmune()) {
				entity.setSecondsOnFire(15);
				if (entity.hurt(CCDamageSources.LAVA_LAMP, 2.0F)) {
					entity.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + level.getRandom().nextFloat() * 0.4F);
				}
			}
		}
	}

	private boolean isEntityTouchingLava(BlockState state, BlockPos pos, Entity entity) {
		Axis axis = state.getValue(FACING).getAxis();
		VoxelShape voxelshape = axis == Axis.X ? X_LAVA_SHAPE : axis == Axis.Y ? Y_LAVA_SHAPE : Z_LAVA_SHAPE;
		VoxelShape voxelshape1 = voxelshape.move(pos.getX(), pos.getY(), pos.getZ());
		return entity.getBoundingBox().intersects(voxelshape1.bounds().inflate(1.0E-7D));
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
		if (state.getValue(WATERLOGGED)) {
			Axis axis = state.getValue(FACING).getAxis();
			for (int i = 0; i < 3; ++i) {
				boolean side = random.nextBoolean();
				double d0 = random.nextDouble() * 0.75D + 0.125D;
				double d1 = !side ? random.nextDouble() * 0.75D + 0.125D : random.nextBoolean() ? 0.0D : 1.0D;
				double d2 = side ? random.nextDouble() * 0.75D + 0.125D : axis == Axis.Y && random.nextBoolean() ? 0.0D : 1.0D;
				double x = axis == Axis.X ? d0 : d1;
				double y = axis == Axis.Y ? d0 : d2;
				double z = axis == Axis.X ? d1 : axis == Axis.Y ? d2 : d0;
				level.addParticle(CCParticleTypes.LAVA_LAMP_SMOKE.get(), pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}
}