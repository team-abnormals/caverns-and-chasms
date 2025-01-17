package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class AtoningTableBlockEntity extends BlockEntity implements Nameable {
	public int time;
	public float flip;
	public float oFlip;
	public float flipT;
	public float flipA;
	public float open;
	public float oOpen;
	public float rot;
	public float oRot;
	public float tRot;
	private static final RandomSource RANDOM = RandomSource.create();
	private Component name;

	public AtoningTableBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.ATONING_TABLE.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (this.hasCustomName()) {
			tag.putString("CustomName", Component.Serializer.toJson(this.name));
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("CustomName", 8)) {
			this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
		}
	}

	public static void bookAnimationTick(Level level, BlockPos pos, BlockState state, AtoningTableBlockEntity entity) {
		entity.oOpen = entity.open;
		entity.oRot = entity.rot;
		Player player = level.getNearestPlayer((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 3.0D, false);
		if (player != null) {
			double d0 = player.getX() - ((double) pos.getX() + 0.5D);
			double d1 = player.getZ() - ((double) pos.getZ() + 0.5D);
			entity.tRot = (float) Mth.atan2(d1, d0);
			entity.open += 0.1F;
			if (entity.open < 0.5F || RANDOM.nextInt(40) == 0) {
				float f1 = entity.flipT;

				do {
					entity.flipT += (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
				} while (f1 == entity.flipT);
			}
		} else {
			entity.tRot += 0.02F;
			entity.open -= 0.1F;
		}

		while (entity.rot >= (float) Math.PI) {
			entity.rot -= ((float) Math.PI * 2F);
		}

		while (entity.rot < -(float) Math.PI) {
			entity.rot += ((float) Math.PI * 2F);
		}

		while (entity.tRot >= (float) Math.PI) {
			entity.tRot -= ((float) Math.PI * 2F);
		}

		while (entity.tRot < -(float) Math.PI) {
			entity.tRot += ((float) Math.PI * 2F);
		}

		float f2;
		for (f2 = entity.tRot - entity.rot; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F)) {
		}

		while (f2 < -(float) Math.PI) {
			f2 += ((float) Math.PI * 2F);
		}

		entity.rot += f2 * 0.4F;
		entity.open = Mth.clamp(entity.open, 0.0F, 1.0F);
		++entity.time;
		entity.oFlip = entity.flip;
		float f = (entity.flipT - entity.flip) * 0.4F;
		float f3 = 0.2F;
		f = Mth.clamp(f, -f3, f3);
		entity.flipA += (f - entity.flipA) * 0.9F;
		entity.flip += entity.flipA;
	}

	@Override
	public Component getName() {
		return this.name != null ? this.name : Component.translatable("container.enchant");
	}

	public void setCustomName(@Nullable Component component) {
		this.name = component;
	}

	@Nullable
	@Override
	public Component getCustomName() {
		return this.name;
	}
}