package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.RollerDoorBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.RollerDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class RollerDoorHeaderBlock extends RollerDoorBlock {
	private static final Map<Direction, VoxelShape> WALL_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, box(0, 12, 0, 16, 16, 4),
			Direction.SOUTH, box(0, 12, 12, 16, 16, 16),
			Direction.WEST, box(0, 12, 0, 4, 16, 16),
			Direction.EAST, box(12, 12, 0, 16, 16, 16)));
	private static final Map<Direction, VoxelShape> CEILING_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, box(0, 0, 0, 16, 4, 4),
			Direction.SOUTH, box(0, 0, 12, 16, 4, 16),
			Direction.WEST, box(0, 0, 0, 4, 4, 16),
			Direction.EAST, box(12, 0, 0, 16, 4, 16)));
	private static final Map<Direction, VoxelShape> FLOOR_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, box(0, 12, 0, 16, 16, 4),
			Direction.SOUTH, box(0, 12, 12, 16, 16, 16),
			Direction.WEST, box(0, 12, 0, 4, 16, 16),
			Direction.EAST, box(12, 12, 0, 16, 16, 16)));

	public RollerDoorHeaderBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RollerDoorHeaderBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		return createTickerHelper(entityType, CCBlockEntityTypes.ROLLER_DOOR_HEADER.get(), RollerDoorHeaderBlockEntity::tick);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof RollerDoorBlockEntity rollerdoorentity) {
			Direction facing = state.getValue(RollerDoorBlock.FACING);
			AttachFace face = state.getValue(RollerDoorBlock.FACE);
			return Shapes.or(rollerdoorentity.getDoorShape(state), face == AttachFace.WALL ? WALL_SHAPES.get(facing) : face == AttachFace.CEILING ? CEILING_SHAPES.get(facing) : FLOOR_SHAPES.get(facing));
		} else {
			return Shapes.empty();
		}
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return new ItemStack(CCBlocks.ROLLER_DOOR.get());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof RollerDoorHeaderBlockEntity rollerDoor) {
			ObjectArrayList<ItemStack> list = new ObjectArrayList<>();
			int itemcount = rollerDoor.getBlockCount() + 1;
			int stackcount = itemcount / 64;
			for (int i = 0; i < stackcount; i++) {
				list.add(new ItemStack(CCBlocks.ROLLER_DOOR.get(), 64));
			}
			list.add(new ItemStack(CCBlocks.ROLLER_DOOR.get(), itemcount % 64));
			return list;
		}
		return super.getDrops(state, builder);
	}
}