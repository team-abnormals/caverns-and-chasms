package com.teamabnormals.caverns_and_chasms.common.block;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.block.entity.StorageDuctBlockEntity;
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
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StorageDuctBlock extends BaseEntityBlock {
	public static final DirectionProperty START_FACE = DirectionProperty.create("start_face", Direction.values());
	public static final DirectionProperty END_FACE = DirectionProperty.create("end_face", Direction.values());
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

	public StorageDuctBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(START_FACE, Direction.UP).setValue(END_FACE, Direction.DOWN).setValue(OPEN, false));
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
	public static Container getContainer(Level level, BlockPos pos) {
		List<StorageDuctBlockEntity> list = getConnectedStorageDucts(level, pos);
		return list.isEmpty() ? null : new StorageDuctContainer<>(list);
	}

	public static List<StorageDuctBlockEntity> getConnectedStorageDucts(Level level, BlockPos pos) {
		List<StorageDuctBlockEntity> list = Lists.newArrayList();
		list.add((StorageDuctBlockEntity) level.getBlockEntity(pos));

		for (StorageDuctFace face : StorageDuctFace.values()) {
			MutableBlockPos mutable = pos.mutable();
			BlockState blockState = level.getBlockState(mutable);
			StorageDuctFace face1 = face;

			while (true) {
				Direction direction = blockState.getValue(face1.getDirectionProperty());
				mutable.move(direction);

				if (mutable.equals(pos))
					break;

				blockState = level.getBlockState(mutable);

				if (blockState.getBlock() instanceof StorageDuctBlock) {
					boolean flag = false;

					for (StorageDuctFace face2 : StorageDuctFace.values()) {
						Direction direction1 = blockState.getValue(face2.getDirectionProperty());
						if (direction1 == direction.getOpposite()) {
							BlockEntity blockEntity = level.getBlockEntity(mutable);
							if (blockEntity instanceof StorageDuctBlockEntity storageDuct)
								list.add(storageDuct);

							face1 = face2.getOpposite();
							flag = true;
							break;
						}
					}

					if (!flag)
						break;
				} else {
					break;
				}
			}
		}

		return list;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (!player.getItemInHand(hand).is(CCBlocks.STORAGE_DUCT.get().asItem()) && blockEntity instanceof StorageDuctBlockEntity storageDuct && canOpen(level, pos, state)) {
			if (!level.isClientSide) {
				getConnectedStorageDucts(level, pos);
				openMenu((ServerPlayer) player, level, pos);
				PiglinAi.angerNearbyPiglins(player, true);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	public static void openMenu(ServerPlayer player, Level level, BlockPos pos) {
		Container container = getContainer(level, pos);
		if (container != null) {
			if (player.containerMenu != player.inventoryMenu)
				player.closeContainer();

			player.nextContainerCounter();
			CavernsAndChasms.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2COpenStorageDuctMessage(player.containerCounter, container.getContainerSize(), pos));
			player.containerMenu = new StorageDuctMenu(player.containerCounter, player.getInventory(), container);
			player.initMenu(player.containerMenu);
			MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.containerMenu));
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof Container) {
				Containers.dropContents(level, pos, (Container)blockentity);
				level.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos blockPos = context.getClickedPos();
		Direction startFace = context.getClickedFace().getOpposite();
		Direction endFace = startFace.getOpposite();

		if (hasNeighborConnectedToFace(startFace, level, blockPos)) {
			for (Direction face : Direction.values()) {
				if (face != startFace && hasNeighborConnectedToFace(face, level, blockPos)) {
					endFace = face;
					break;
				}
			}
		}

		return this.defaultBlockState().setValue(START_FACE, startFace).setValue(END_FACE, endFace);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		if (facing != state.getValue(START_FACE) && !hasEndConnection(level, pos, state) && facingState.getBlock() instanceof StorageDuctBlock && hasFace(facing.getOpposite(), facingState))
			return state.setValue(END_FACE, facing);
		return super.updateShape(state, facing, facingState, level, pos, facingPos);
	}

	public static boolean hasStartConnection(LevelAccessor level, BlockPos pos, BlockState state) {
		return hasNeighborConnectedToFace(state.getValue(START_FACE), level, pos);
	}

	public static boolean hasEndConnection(LevelAccessor level, BlockPos pos, BlockState state) {
		return hasNeighborConnectedToFace(state.getValue(END_FACE), level, pos);
	}

	public static boolean hasNeighborConnectedToFace(Direction face, LevelAccessor level, BlockPos pos) {
		BlockState neighborState = level.getBlockState(pos.relative(face));
		return neighborState.getBlock() instanceof StorageDuctBlock && hasFace(face.getOpposite(), neighborState);
	}

	public static boolean hasFace(Direction face, BlockState state) {
		return state.getValue(START_FACE) == face || state.getValue(END_FACE) == face;
	}

	public static boolean canOpen(Level level, BlockPos pos, BlockState state) {
		Direction startFace = state.getValue(START_FACE);
		Direction endFace = state.getValue(END_FACE);
		BlockPos startFacePos = pos.relative(startFace);
		BlockPos endFacePos = pos.relative(endFace);
		return !level.getBlockState(startFacePos).isFaceSturdy(level, startFacePos, startFace.getOpposite()) || !level.getBlockState(endFacePos).isFaceSturdy(level, endFacePos, endFace.getOpposite());
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
		return state.setValue(START_FACE, rotation.rotate(state.getValue(START_FACE))).setValue(END_FACE, rotation.rotate(state.getValue(END_FACE)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(START_FACE, mirror.getRotation(state.getValue(START_FACE)).rotate(state.getValue(START_FACE))).setValue(END_FACE, mirror.getRotation(state.getValue(END_FACE)).rotate(state.getValue(END_FACE)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(START_FACE, END_FACE, OPEN);
	}

	public enum StorageDuctFace {
		START(START_FACE),
		END(END_FACE);

		private final DirectionProperty directionProperty;

		StorageDuctFace(DirectionProperty directionProperty) {
			this.directionProperty = directionProperty;
		}

		public DirectionProperty getDirectionProperty() {
			return this.directionProperty;
		}

		public StorageDuctFace getOpposite() {
			return this == START ? END : START;
		}
	}
}