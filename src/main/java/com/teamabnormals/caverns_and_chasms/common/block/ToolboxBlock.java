package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.ToolboxBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.List;

public class ToolboxBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty HANGING = BooleanProperty.create("hanging");
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final ResourceLocation CONTENTS = new ResourceLocation("contents");
	private final WeatherState weatherState;

	public static final VoxelShape SHAPE_X = Block.box(4.0F, 0.0F, 0.0F, 12.0F, 8.0F, 16.0F);
	public static final VoxelShape SHAPE_Z = Block.box(0.0F, 0.0F, 4.0F, 16.0F, 8.0F, 12.0F);
	public static final VoxelShape SHAPE_X_HANGING = SHAPE_X.move(0.0F, 5.0F / 16.0F, 0.0F);
	public static final VoxelShape SHAPE_Z_HANGING = SHAPE_Z.move(0.0F, 5.0F / 16.0F, 0.0F);

	public ToolboxBlock(WeatherState p_56188_, BlockBehaviour.Properties p_56189_) {
		super(p_56189_);
		this.weatherState = p_56188_;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HANGING, false).setValue(WATERLOGGED, false));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ToolboxBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		return createTickerHelper(entityType, CCBlockEntityTypes.TOOLBOX.get(), ToolboxBlockEntity::tick);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}


	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (!(this instanceof WeatheringToolboxBlock) && player.getItemInHand(hand).is(Tags.Items.TOOLS_AXES)) {
			return InteractionResult.PASS;
		}

		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else if (player.isSpectator()) {
			return InteractionResult.CONSUME;
		} else {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof ToolboxBlockEntity toolbox) {
				player.openMenu(toolbox);
//				player.awardStat(Stats.OPEN_SHULKER_BOX);
				PiglinAi.angerNearbyPiglins(player, true);
				return InteractionResult.CONSUME;
			} else {
				return InteractionResult.PASS;
			}
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getHorizontalDirection().getOpposite();
		boolean hanging = context.getClickedFace() == Direction.DOWN;
		FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());

		return this.defaultBlockState().setValue(FACING, direction).setValue(HANGING, hanging).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, direction, offsetState, level, pos, offsetPos);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType pathComputationType) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HANGING, WATERLOGGED);
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof ToolboxBlockEntity toolbox) {
			if (!level.isClientSide && player.isCreative() && !toolbox.isEmpty()) {
				ItemStack itemstack = getWeatheredItemStack(this.getWeatherState(), this instanceof CCWeatheringCopper);
				blockentity.saveToItem(itemstack);
				if (toolbox.hasCustomName()) {
					itemstack.setHoverName(toolbox.getCustomName());
				}

				ItemEntity itemEntity = new ItemEntity(level, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, itemstack);
				itemEntity.setDefaultPickUpDelay();
				level.addFreshEntity(itemEntity);
			} else {
				toolbox.unpackLootTable(player);
			}
		}

		super.playerWillDestroy(level, pos, state, player);
	}

	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof ToolboxBlockEntity toolbox) {
			builder = builder.withDynamicDrop(CONTENTS, (p_56218_, p_56219_) -> {
				for (int i = 0; i < toolbox.getContainerSize(); ++i) {
					p_56219_.accept(toolbox.getItem(i));
				}

			});
		}

		return super.getDrops(state, builder);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof ToolboxBlockEntity toolbox) {
				toolbox.setCustomName(stack.getHoverName());
			}
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState p_56237_, boolean p_56238_) {
		if (!state.is(p_56237_.getBlock())) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof ToolboxBlockEntity) {
				level.updateNeighbourForOutputSignal(pos, state.getBlock());
			}

			super.onRemove(state, level, pos, p_56237_, p_56238_);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> components, TooltipFlag flag) {
		super.appendHoverText(stack, level, components, flag);
		CompoundTag tag = BlockItem.getBlockEntityData(stack);
		if (tag != null) {
			if (tag.contains("LootTable", 8)) {
				components.add(Component.literal("???????"));
			}

			if (tag.contains("Items", 9)) {
				NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
				ContainerHelper.loadAllItems(tag, nonnulllist);
				int i = 0;
				int j = 0;

				for (ItemStack itemstack : nonnulllist) {
					if (!itemstack.isEmpty()) {
						++j;
						if (i <= 4) {
							++i;
							components.add(itemstack.getHoverName().copy());
						}
					}
				}

				if (j - i > 0) {
					components.add(Component.translatable("container." + CavernsAndChasms.MOD_ID + ".toolbox.more", j - i).withStyle(ChatFormatting.ITALIC));
				}
			}
		}

	}

	@Override
	public PushReaction getPistonPushReaction(BlockState p_56265_) {
		return PushReaction.DESTROY;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		boolean hanging = state.getValue(HANGING);
		return state.getValue(FACING).getAxis() == Axis.X ? !hanging ? SHAPE_X : SHAPE_X_HANGING : !hanging ? SHAPE_Z : SHAPE_Z_HANGING;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState p_56221_) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState p_56223_, Level p_56224_, BlockPos p_56225_) {
		return AbstractContainerMenu.getRedstoneSignalFromContainer((Container) p_56224_.getBlockEntity(p_56225_));
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		ItemStack stack = super.getCloneItemStack(level, pos, state);
		level.getBlockEntity(pos, CCBlockEntityTypes.TOOLBOX.get()).ifPresent(block -> block.saveToItem(stack));
		return stack;
	}

	public static Block getBlockByWeatherState(WeatherState weatherState, boolean weathers) {
		if (weathers) {
			return switch (weatherState) {
				case UNAFFECTED -> CCBlocks.TOOLBOX.get();
				case EXPOSED -> CCBlocks.EXPOSED_TOOLBOX.get();
				case WEATHERED -> CCBlocks.WEATHERED_TOOLBOX.get();
				case OXIDIZED -> CCBlocks.OXIDIZED_TOOLBOX.get();
			};
		} else {
			return switch (weatherState) {
				case UNAFFECTED -> CCBlocks.WAXED_TOOLBOX.get();
				case EXPOSED -> CCBlocks.WAXED_EXPOSED_TOOLBOX.get();
				case WEATHERED -> CCBlocks.WAXED_WEATHERED_TOOLBOX.get();
				case OXIDIZED -> CCBlocks.WAXED_OXIDIZED_TOOLBOX.get();
			};
		}
	}

	public WeatherState getWeatherState() {
		return this.weatherState;
	}

	public static ItemStack getWeatheredItemStack(WeatherState weatherState, boolean weathers) {
		return new ItemStack(getBlockByWeatherState(weatherState, weathers));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
}