package com.teamabnormals.caverns_and_chasms.common.block;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.block.entity.StorageDuctBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.StorageDuctHatchBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.inventory.StorageDuctContainer;
import com.teamabnormals.caverns_and_chasms.common.inventory.StorageDuctMenu;
import com.teamabnormals.caverns_and_chasms.common.network.S2COpenStorageDuctMessage;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StorageDuctBlock extends BaseEntityBlock {
	public static final DirectionProperty FIRST_END = DirectionProperty.create("first_end", Direction.values());
	public static final DirectionProperty SECOND_END = DirectionProperty.create("second_end", Direction.values());

	public StorageDuctBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FIRST_END, Direction.UP).setValue(SECOND_END, Direction.DOWN));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new StorageDuctBlockEntity(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	public static StorageDuctContainer getContainer(Level level, BlockPos pos, DuctEnd startend) {
		List<StorageDuctBlockEntity> list = getConnectedStorageDucts(level, pos, level.getBlockState(pos), startend);
		return list.isEmpty() ? null : new StorageDuctContainer(list, pos);
	}

	// TODO: Maybe add special behavior for hoppers sucking items, because its impossible for them to determine an item reading direction objectively.
	private static List<StorageDuctBlockEntity> getConnectedStorageDucts(Level level, BlockPos pos, BlockState state, DuctEnd startend) {
		List<StorageDuctBlockEntity> ducts = Lists.newArrayList();

		for (int i = 0; i < 2; i++) {
			MutableBlockPos mutable = pos.mutable();
			BlockState blockstate = state;
			DuctEnd searchend = i == 0 ? startend : startend.getOpposite();

			if (i == 1) {
				Collections.reverse(ducts);
				BlockEntity blockentity = level.getBlockEntity(mutable);
				if (blockentity instanceof StorageDuctBlockEntity duct)
					ducts.add(duct);
			}

			label:
			while (true) {
				Direction direction = blockstate.getValue(searchend.getDirectionProperty());
				mutable.move(direction);

				blockstate = level.getBlockState(mutable);

				if (blockstate.getBlock() instanceof StorageDuctBlock) {
					for (DuctEnd end1 : DuctEnd.values()) {
						Direction direction1 = blockstate.getValue(end1.getDirectionProperty());
						if (direction1 == direction.getOpposite()) {
							BlockEntity blockentity1 = level.getBlockEntity(mutable);
							if (blockentity1 instanceof StorageDuctBlockEntity duct) {
								ducts.add(duct);

								if (mutable.equals(pos)) {
									Collections.reverse(ducts);
									return ducts;
								}
							}

							searchend = end1.getOpposite();
							continue label;
						}
					}

					break;
				} else {
					break;
				}
			}
		}

		return ducts;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		ItemStack itemstack = player.getItemInHand(hand);
		if (!itemstack.is(CCBlocks.STORAGE_DUCT.get().asItem()) && !itemstack.is((CCBlocks.STORAGE_DUCT_HATCH).get().asItem()) && blockentity instanceof StorageDuctBlockEntity) {
			Direction direction = result.getDirection();
			List<DuctEnd> openableends = getOpenableEnds(level, pos, state);
			DuctEnd endtoopen = null;

			for (DuctEnd end : openableends) {
				if (state.getValue(end.getDirectionProperty()) == direction) {
					endtoopen = end;
					break;
				}
			}

			if (endtoopen != null) {
				if (!level.isClientSide) {
					openMenu((ServerPlayer) player, level, pos, endtoopen, null);
					PiglinAi.angerNearbyPiglins(player, true);
				}
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}

		return InteractionResult.PASS;
	}

	public static void openMenu(ServerPlayer player, Level level, BlockPos pos, DuctEnd startend, StorageDuctHatchBlockEntity hatch) {
		Container container = getContainer(level, pos, startend);
		if (container != null) {
			if (player.containerMenu != player.inventoryMenu)
				player.closeContainer();

			player.nextContainerCounter();
			CavernsAndChasms.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2COpenStorageDuctMessage(player.containerCounter, container.getContainerSize(), pos));
			player.containerMenu = new StorageDuctMenu(player.containerCounter, player.getInventory(), container, hatch);
			player.initMenu(player.containerMenu);
			MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.containerMenu));
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof Container) {
				Containers.dropContents(level, pos, (Container) blockentity);
				level.updateNeighbourForOutputSignal(pos, this);
			}

			for (StorageDuctBlockEntity storageDuct : getConnectedStorageDucts(level, pos, state, DuctEnd.FIRST))
				storageDuct.resetHandler();

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!state.is(oldState.getBlock())) {
			for (StorageDuctBlockEntity storageDuct : getConnectedStorageDucts(level, pos, state, DuctEnd.FIRST))
				storageDuct.resetHandler();

			super.onPlace(state, level, pos, oldState, isMoving);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		Direction clickeddirection = context.getClickedFace().getOpposite();
		BlockPos clickedopos = blockpos.relative(clickeddirection);
		BlockState clickedstate = level.getBlockState(clickedopos);

		boolean flag = clickedstate.getBlock() instanceof StorageDuctBlock && (!hasConnectionAt(DuctEnd.FIRST, level, clickedopos, clickedstate) || !hasConnectionAt(DuctEnd.SECOND, level, clickedopos, clickedstate));
		Direction firstend = flag ? clickeddirection : context.getNearestLookingDirection().getOpposite();
		Direction secondend = firstend.getOpposite();

		if (!hasConnectionAt(secondend, level, blockpos)) {
			for (Direction direction : Direction.values()) {
				if (direction != firstend && hasConnectionAt(direction, level, blockpos)) {
					secondend = direction;
					break;
				}
			}
		}

		return this.defaultBlockState().setValue(FIRST_END, firstend).setValue(SECOND_END, secondend);
	}


	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof StorageDuctBlockEntity storageDuctBlockEntity) {
				storageDuctBlockEntity.setCustomName(stack.getHoverName());
			}
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		if (facingState.getBlock() instanceof StorageDuctBlock && hasEndAt(direction.getOpposite(), facingState)) {
			for (DuctEnd ductEnd : DuctEnd.values()) {
				if (direction != state.getValue(ductEnd.getDirectionProperty()) && !hasConnectionAt(ductEnd.getOpposite(), level, pos, state))
					return state.setValue(ductEnd.getOpposite().getDirectionProperty(), direction);
			}
		}
		return super.updateShape(state, direction, facingState, level, pos, facingPos);
	}

	public static boolean hasConnectionAt(DuctEnd ductEnd, LevelAccessor level, BlockPos pos, BlockState state) {
		return hasConnectionAt(state.getValue(ductEnd.getDirectionProperty()), level, pos);
	}

	public static boolean hasConnectionAt(Direction direction, LevelAccessor level, BlockPos pos) {
		BlockState neighborstate = level.getBlockState(pos.relative(direction));
		return neighborstate.getBlock() instanceof StorageDuctBlock && hasEndAt(direction.getOpposite(), neighborstate);
	}

	public static boolean hasEndAt(Direction direction, BlockState state) {
		return state.getValue(FIRST_END) == direction || state.getValue(SECOND_END) == direction;
	}

	public static List<DuctEnd> getOpenableEnds(Level level, BlockPos pos, BlockState state) {
		List<DuctEnd> list = new ArrayList<>();
		for (DuctEnd end : DuctEnd.values()) {
			Direction direction = state.getValue(end.getDirectionProperty());
			BlockPos offsetpos = pos.relative(direction);
			if (!level.getBlockState(offsetpos).isFaceSturdy(level, offsetpos, direction.getOpposite()))
				list.add(end);
		}
		return list;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FIRST_END, rotation.rotate(state.getValue(FIRST_END))).setValue(SECOND_END, rotation.rotate(state.getValue(SECOND_END)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(FIRST_END, mirror.getRotation(state.getValue(FIRST_END)).rotate(state.getValue(FIRST_END))).setValue(SECOND_END, mirror.getRotation(state.getValue(SECOND_END)).rotate(state.getValue(SECOND_END)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FIRST_END, SECOND_END);
	}

	public enum DuctEnd {
		FIRST(FIRST_END),
		SECOND(SECOND_END);

		private final DirectionProperty directionProperty;

		DuctEnd(DirectionProperty directionProperty) {
			this.directionProperty = directionProperty;
		}

		public DirectionProperty getDirectionProperty() {
			return this.directionProperty;
		}

		public DuctEnd getOpposite() {
			return this == FIRST ? SECOND : FIRST;
		}
	}
}