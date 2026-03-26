package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.common.block.HoopBlock;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class HoopBlockEntity extends BlockEntity {

	public HoopBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.HOOP.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, HoopBlockEntity blockEntity) {
		if (!level.isClientSide() && state.getValue(HoopBlock.OUTPUT_POWER) == 0) {
			int power = 0;
			Vec3 vec3 = pos.getCenter();
			Axis axis = state.getValue(HoopBlock.AXIS);
			int size = state.getValue(HoopBlock.SIZE);
			double radius = 1.0D / 16.0D * (size == 0 ? 1.0D : size * 2.0D);

			List<Entity> entities = level.getEntities((Entity) null, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(pos)).inflate(8.0D), entity -> entity instanceof Projectile || entity instanceof ItemEntity);

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

				if (Math.max(Math.abs(d2), Math.abs(d3)) <= radius) {
					Vec3 vec31;
					if (entity instanceof Projectile && ((IDataManager) entity).getValue(CCDataProcessors.SHOULD_DEFLECT)) {
						IDataManager data = (IDataManager) entity;
						vec31 = new Vec3(data.getValue(CCDataProcessors.DEFLECT_X), data.getValue(CCDataProcessors.DEFLECT_Y), data.getValue(CCDataProcessors.DEFLECT_Z));
					} else {
						vec31 = entity.getDeltaMovement();
					}

					power = Math.max(Math.min((int) Math.ceil(vec31.length() * 5.0D), 15), power);
				}
			}

			if (power > 0) {
				level.setBlock(pos, state.setValue(HoopBlock.OUTPUT_POWER, power), 3);
				level.scheduleTick(pos, state.getBlock(), 8);
				level.playSound(null, pos, CCSoundEvents.HOOP_SCORE.get(), SoundSource.BLOCKS, 2.0F, 0.8F + power / 15.0F * 0.6F);
				level.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(state));

				for (Entity entity : entities) {
					if (entity instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer serverPlayer) {
						CCCriteriaTriggers.HOOP_ENTERED.trigger(serverPlayer, entity, size, power);
					} else if (entity instanceof ItemEntity itemEntity && itemEntity.getOwner() instanceof ServerPlayer serverPlayer) {
						CCCriteriaTriggers.HOOP_ENTERED.trigger(serverPlayer, entity, size, power);
					}
				}

				for (Direction direction : Direction.values()) {
					if (direction.getAxis() != axis) {
						level.updateNeighborsAt(pos.relative(direction), state.getBlock());
					}
				}
			}
		}
	}
}