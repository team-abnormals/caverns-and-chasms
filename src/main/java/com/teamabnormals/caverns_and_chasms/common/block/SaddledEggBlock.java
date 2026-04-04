package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.SaddledGrazer;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;

public class SaddledEggBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 8.0D, 11.0D);

	public SaddledEggBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (!entity.isSteppingCarefully())
			this.destroyEgg(level, state, pos, entity, 100);
		super.stepOn(level, pos, state, entity);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		this.destroyEgg(level, state, pos, entity, 3);
		super.fallOn(level, state, pos, entity, fallDistance);
	}

	private void destroyEgg(Level level, BlockState state, BlockPos pos, Entity entity, int chance) {
		if (!level.isClientSide && this.canDestroyEgg(level, entity) && level.random.nextInt(chance) == 0 && state.is(CCBlocks.SADDLED_EGG.get())) {
			level.playSound(null, pos, CCSoundEvents.SADDLED_EGG_HATCH.get(), SoundSource.BLOCKS, 0.7F, 0.9F + level.getRandom().nextFloat() * 0.2F);
			level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
			level.levelEvent(2001, pos, Block.getId(state));
			level.removeBlock(pos, false);

			if (entity instanceof ServerPlayer serverPlayer) {
				CCCriteriaTriggers.HATCH_SADDLED_GRAZER.trigger(serverPlayer);
			}

			SaddledGrazer saddledgrazer = CCEntityTypes.SADDLED_GRAZER.get().create(level);
			if (saddledgrazer != null) {
				saddledgrazer.setBaby(true);
				saddledgrazer.setPersistenceRequired();
				saddledgrazer.moveTo(pos, state.getValue(FACING).toYRot() + level.getRandom().nextFloat() * 90.0F - 45.0F, 0.0F);
				saddledgrazer.setYHeadRot(saddledgrazer.getYRot());
				saddledgrazer.setYBodyRot(saddledgrazer.getYRot());
				level.addFreshEntity(saddledgrazer);
			}
		}
	}

	private boolean canDestroyEgg(Level level, Entity entity) {
		if (entity instanceof LivingEntity && !(entity instanceof Bat))
			return entity instanceof Player || ForgeEventFactory.getMobGriefingEvent(level, entity);
		else
			return false;
	}


	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos) {
		if (!state.canSurvive(level, pos)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			if (state.getValue(WATERLOGGED)) {
				level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
			}
			return super.updateShape(state, direction, otherState, level, pos, otherPos);
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}
}