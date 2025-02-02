package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.HoopBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class HoopBlockEntity extends BlockEntity {

	public HoopBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.HOOP.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, HoopBlockEntity blockEntity) {
		if (!level.isClientSide()) {
			int power = 0;
			Vec3 vec3 = pos.getCenter();
			Axis axis = state.getValue(HoopBlock.AXIS);
			int size = state.getValue(HoopBlock.SIZE);
			double d0 = 1.0D / 16.0D * (size == 0 ? 1.0D : size * 2.0D);

			List<Entity> entities = level.getEntities(null, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(pos)).inflate(8.0D));

			for (Entity entity : entities) {
				double d2;
				double d3;

				if (axis == Axis.X) {
					if (entity.getX() > vec3.x == entity.xOld > vec3.x)
						continue;
					double d1 = (vec3.x - entity.getX()) / (entity.getX() - entity.xOld);
					d2 = entity.getY() - vec3.y + (entity.getY() - entity.yOld) * d1;
					d3 = entity.getZ() - vec3.z + (entity.getZ() - entity.zOld) * d1;
				} else if (axis == Axis.Y) {
					if (entity.getY() > vec3.y == entity.yOld > vec3.y)
						continue;
					double d1 = (vec3.y - entity.getY()) / (entity.getY() - entity.yOld);
					d2 = entity.getX() - vec3.x + (entity.getX() - entity.xOld) * d1;
					d3 = entity.getZ() - vec3.z + (entity.getZ() - entity.zOld) * d1;
				} else {
					if (entity.getZ() > vec3.z == entity.zOld > vec3.z)
						continue;
					double d1 = (vec3.z - entity.getZ()) / (entity.getZ() - entity.zOld);
					d2 = entity.getX() - vec3.x + (entity.getX() - entity.xOld) * d1;
					d3 = entity.getY() - vec3.y + (entity.getY() - entity.yOld) * d1;
				}

				if (Math.max(Math.abs(d2), Math.abs(d3)) <= d0) {
					power = Math.max(Math.min((int) Math.ceil(entity.getDeltaMovement().length() * 5.0D), 15), power);
				}
			}

			if (power != state.getValue(HoopBlock.OUTPUT_POWER)) {
				level.setBlock(pos, state.setValue(HoopBlock.OUTPUT_POWER, power), 3);
			}
		}
	}
}