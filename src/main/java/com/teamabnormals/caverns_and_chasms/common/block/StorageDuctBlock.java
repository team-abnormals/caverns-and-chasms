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

import java.util.List;

public class StorageDuctBlock extends BaseEntityBlock {
	public static final DirectionProperty START_FACE = DirectionProperty.create("start_face", Direction.values());
	public static final DirectionProperty END_FACE = DirectionProperty.create("end_face", Direction.values());

	public StorageDuctBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(START_FACE, Direction.UP).setValue(END_FACE, Direction.DOWN));
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
		List<StorageDuctBlockEntity> list = getConnectedStorageDucts(level, pos, level.getBlockState(pos));
		return list.isEmpty() ? null : new StorageDuctContainer<>(list);
	}

	public static List<StorageDuctBlockEntity> getConnectedStorageDucts(Level level, BlockPos pos, BlockState state) {
		List<StorageDuctBlockEntity> list = Lists.newArrayList();

		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof StorageDuctBlockEntity storageDuct)
			list.add(storageDuct);

		label:
		for (Face face : Face.values()) {
			MutableBlockPos mutable = pos.mutable();
			BlockState blockState = state;
			Face face1 = face;

			while (true) {
				Direction direction = blockState.getValue(face1.getDirectionProperty());
				mutable.move(direction);

				if (mutable.equals(pos))
					break label;

				blockState = level.getBlockState(mutable);

				if (blockState.getBlock() instanceof StorageDuctBlock) {
					boolean flag = false;

					for (Face face2 : Face.values()) {
						Direction direction1 = blockState.getValue(face2.getDirectionProperty());
						if (direction1 == direction.getOpposite()) {
							BlockEntity blockEntity1 = level.getBlockEntity(mutable);
							if (blockEntity1 instanceof StorageDuctBlockEntity storageDuct1)
								list.add(storageDuct1);

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
				getConnectedStorageDucts(level, pos, state);
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
				Containers.dropContents(level, pos, (Container) blockentity);
				level.updateNeighbourForOutputSignal(pos, this);
			}

			for (StorageDuctBlockEntity storageDuct : getConnectedStorageDucts(level, pos, state))
				storageDuct.resetHandler();

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!state.is(oldState.getBlock())) {
			for (StorageDuctBlockEntity storageDuct : getConnectedStorageDucts(level, pos, state))
				storageDuct.resetHandler();

			super.onPlace(state, level, pos, oldState, isMoving);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos blockPos = context.getClickedPos();
		Direction clickedDirection = context.getClickedFace().getOpposite();
		BlockPos clickedPos = blockPos.relative(clickedDirection);
		BlockState clickedState = level.getBlockState(clickedPos);

		boolean flag = clickedState.getBlock() instanceof StorageDuctBlock && (!hasNeighborFaceAt(Face.START, level, clickedPos, clickedState) || !hasNeighborFaceAt(Face.END, level, clickedPos, clickedState));
		Direction startFace = flag ? clickedDirection : context.getNearestLookingDirection().getOpposite();
		Direction endFace = startFace.getOpposite();

		if (!hasNeighborFaceAt(endFace, level, blockPos)) {
			for (Direction direction : Direction.values()) {
				if (direction != startFace && hasNeighborFaceAt(direction, level, blockPos)) {
					endFace = direction;
					break;
				}
			}
		}

		return this.defaultBlockState().setValue(START_FACE, startFace).setValue(END_FACE, endFace);
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
		if (facingState.getBlock() instanceof StorageDuctBlock && hasFaceAt(direction.getOpposite(), facingState)) {
			for (Face face : Face.values()) {
				if (direction != state.getValue(face.getDirectionProperty()) && !hasNeighborFaceAt(face.getOpposite(), level, pos, state))
					return state.setValue(face.getOpposite().getDirectionProperty(), direction);
			}
		}
		return super.updateShape(state, direction, facingState, level, pos, facingPos);
	}

	public static boolean hasNeighborFaceAt(Face face, LevelAccessor level, BlockPos pos, BlockState state) {
		return hasNeighborFaceAt(state.getValue(face.getDirectionProperty()), level, pos);
	}

	public static boolean hasNeighborFaceAt(Direction direction, LevelAccessor level, BlockPos pos) {
		BlockState neighborState = level.getBlockState(pos.relative(direction));
		return neighborState.getBlock() instanceof StorageDuctBlock && hasFaceAt(direction.getOpposite(), neighborState);
	}

	public static boolean hasFaceAt(Direction direction, BlockState state) {
		return state.getValue(START_FACE) == direction || state.getValue(END_FACE) == direction;
	}

	public static boolean canOpen(Level level, BlockPos pos, BlockState state) {
		for (Face face : Face.values()) {
			Direction direction = state.getValue(face.getDirectionProperty());
			BlockPos blockPos = pos.relative(direction);
			if (!level.getBlockState(blockPos).isFaceSturdy(level, blockPos, direction.getOpposite()))
				return true;
		}
		return false;
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
		builder.add(START_FACE, END_FACE);
	}

	public enum Face {
		START(START_FACE),
		END(END_FACE);

		private final DirectionProperty directionProperty;

		Face(DirectionProperty directionProperty) {
			this.directionProperty = directionProperty;
		}

		public DirectionProperty getDirectionProperty() {
			return this.directionProperty;
		}

		public Face getOpposite() {
			return this == START ? END : START;
		}
	}
}