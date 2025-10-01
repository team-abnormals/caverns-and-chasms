package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CoalBlock extends Block implements SimpleWaterloggedBlock {
	public static final IntegerProperty COAL = IntegerProperty.create("coal", 1, 4);
	public static final IntegerProperty HEAT = IntegerProperty.create("heat", 0, 2);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape ONE_AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D);
	protected static final VoxelShape TWO_AABB = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D);
	protected static final VoxelShape THREE_AABB = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);
	protected static final VoxelShape FOUR_AABB = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 7.0D, 14.0D);

	public CoalBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(COAL, 1).setValue(WATERLOGGED, false).setValue(HEAT, 0));
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		if (state.is(this)) {
			return state.setValue(COAL, Math.min(4, state.getValue(COAL) + 1));
		} else {
			FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
			return super.getStateForPlacement(context)
					.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER)
					.setValue(HEAT, getHeatForPos(context.getLevel(), context.getClickedPos()));
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.is(Items.STICK) && state.getValue(HEAT) == 2) {
			if (!level.isClientSide) {
				level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);

				if (level.getRandom().nextFloat() < 0.2F) {
					int coal = state.getValue(COAL);
					if (coal == 1) {
						level.destroyBlock(pos, false);
					} else {
						level.setBlock(pos, state.setValue(COAL, coal - 1), 11);
					}
				}

				stack.shrink(1);
				ItemStack torchStack = new ItemStack(Items.TORCH);
				if (stack.isEmpty()) {
					player.setItemInHand(hand, torchStack);
				} else if (!player.getInventory().add(torchStack)) {
					player.drop(torchStack, false);
				}

				level.gameEvent(player, GameEvent.SHEAR, pos);
				player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return super.use(state, level, pos, player, hand, result);
		}
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (state.getValue(HEAT) == 1 && !entity.isSteppingCarefully() && entity instanceof LivingEntity living && !EnchantmentHelper.hasFrostWalker(living)) {
			entity.hurt(level.damageSources().hotFloor(), 0.125F * state.getValue(COAL));
		}

		super.stepOn(level, pos, state, entity);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (state.getValue(HEAT) == 2) {
			if (!entity.fireImmune()) {
				entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
				if (entity.getRemainingFireTicks() == 0) {
					entity.setSecondsOnFire(state.getValue(COAL) * 2);
				}
			}
			entity.hurt(level.damageSources().inFire(), 0.25F * state.getValue(COAL));
		}

		super.entityInside(state, level, pos, entity);
	}

	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
		if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
			if (state.getValue(HEAT) == 2) {
				if (!level.isClientSide()) {
					level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
				}

				level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
			}

			level.setBlock(pos, state.setValue(WATERLOGGED, true).setValue(HEAT, getHeatForPos(level, pos)), 3);
			level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
			return true;
		} else {
			return false;
		}
	}

	public static int getHeatForPos(LevelAccessor level, BlockPos pos) {
		return level.getBlockState(pos.below()).is(Blocks.MAGMA_BLOCK) ? 1 : 0;
	}

	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return !state.getCollisionShape(level, pos).getFaceShape(Direction.UP).isEmpty() || state.isFaceSturdy(level, pos, Direction.UP);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos belowPos = pos.below();
		return this.mayPlaceOn(level.getBlockState(belowPos), level, belowPos);
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
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		return !context.isSecondaryUseActive() && context.getItemInHand().is(this.asItem()) && state.getValue(COAL) < 4 || super.canBeReplaced(state, context);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(COAL)) {
			case 1 -> ONE_AABB;
			case 2 -> TWO_AABB;
			case 3 -> THREE_AABB;
			default -> FOUR_AABB;
		};
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(COAL, WATERLOGGED, HEAT);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType pathType) {
		return false;
	}

	@Override
	public Item asItem() {
		return this == CCBlocks.CHARCOAL.get() ? Items.CHARCOAL : Items.COAL;
	}

	@Override
	public String getDescriptionId() {
		return Util.makeDescriptionId("item", new ResourceLocation(BuiltInRegistries.BLOCK.getKey(this).getPath()));
	}
}