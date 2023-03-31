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
import net.minecraft.stats.Stats;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class ToolboxBlock extends BaseEntityBlock {
	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final ResourceLocation CONTENTS = new ResourceLocation("contents");
	private final WeatherState weatherState;

	public static final VoxelShape SHAPE_X = Block.box(4.0F, 0.0F, 0.0F, 12.0F, 8.0F, 16.0F);
	public static final VoxelShape SHAPE_Z = Block.box(0.0F, 0.0F, 4.0F, 16.0F, 8.0F, 12.0F);

	public ToolboxBlock(WeatherState p_56188_, BlockBehaviour.Properties p_56189_) {
		super(p_56189_);
		this.weatherState = p_56188_;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	public BlockEntity newBlockEntity(BlockPos p_154552_, BlockState p_154553_) {
		return new ToolboxBlockEntity(p_154552_, p_154553_);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_154543_, BlockState p_154544_, BlockEntityType<T> p_154545_) {
		return createTickerHelper(p_154545_, CCBlockEntityTypes.TOOLBOX.get(), ToolboxBlockEntity::tick);
	}

	public RenderShape getRenderShape(BlockState p_56255_) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	public InteractionResult use(BlockState p_56227_, Level p_56228_, BlockPos p_56229_, Player p_56230_, InteractionHand p_56231_, BlockHitResult p_56232_) {
		if (p_56228_.isClientSide) {
			return InteractionResult.SUCCESS;
		} else if (p_56230_.isSpectator()) {
			return InteractionResult.CONSUME;
		} else {
			BlockEntity blockentity = p_56228_.getBlockEntity(p_56229_);
			if (blockentity instanceof ToolboxBlockEntity toolbox) {
				p_56230_.openMenu(toolbox);
				p_56230_.awardStat(Stats.OPEN_SHULKER_BOX);
				PiglinAi.angerNearbyPiglins(p_56230_, true);
				return InteractionResult.CONSUME;
			} else {
				return InteractionResult.PASS;
			}
		}
	}

	public BlockState getStateForPlacement(BlockPlaceContext p_56198_) {
		return this.defaultBlockState().setValue(FACING, p_56198_.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_56249_) {
		p_56249_.add(FACING);
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
		BlockEntity blockentity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockentity instanceof ToolboxBlockEntity toolbox) {
			builder = builder.withDynamicDrop(CONTENTS, (p_56218_, p_56219_) -> {
				for (int i = 0; i < toolbox.getContainerSize(); ++i) {
					p_56219_.accept(toolbox.getItem(i));
				}

			});
		}

		return super.getDrops(state, builder);
	}

	@Override
	public void setPlacedBy(Level p_56206_, BlockPos p_56207_, BlockState p_56208_, LivingEntity p_56209_, ItemStack p_56210_) {
		if (p_56210_.hasCustomHoverName()) {
			BlockEntity blockentity = p_56206_.getBlockEntity(p_56207_);
			if (blockentity instanceof ToolboxBlockEntity toolbox) {
				toolbox.setCustomName(p_56210_.getHoverName());
			}
		}

	}

	@Override
	public void onRemove(BlockState p_56234_, Level p_56235_, BlockPos p_56236_, BlockState p_56237_, boolean p_56238_) {
		if (!p_56234_.is(p_56237_.getBlock())) {
			BlockEntity blockentity = p_56235_.getBlockEntity(p_56236_);
			if (blockentity instanceof ToolboxBlockEntity) {
				p_56235_.updateNeighbourForOutputSignal(p_56236_, p_56234_.getBlock());
			}

			super.onRemove(p_56234_, p_56235_, p_56236_, p_56237_, p_56238_);
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
		return state.getValue(FACING).getAxis() == Axis.X ? SHAPE_X : SHAPE_Z;
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
	public ItemStack getCloneItemStack(BlockGetter p_56202_, BlockPos p_56203_, BlockState p_56204_) {
		ItemStack itemstack = super.getCloneItemStack(p_56202_, p_56203_, p_56204_);
		p_56202_.getBlockEntity(p_56203_, CCBlockEntityTypes.TOOLBOX.get()).ifPresent((p_187446_) -> {
			p_187446_.saveToItem(itemstack);
		});
		return itemstack;
	}

	public static WeatherState getWeatherStateFromBlock(Block block) {
		return block instanceof ToolboxBlock toolboxBlock ? toolboxBlock.getWeatherState() : null;
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