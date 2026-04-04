package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.DeeperHat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.nullness.qual.NonNull;

public class DeeperSkullBlockEntity extends SkullBlockEntity {
	private DeeperHat hat;

	public DeeperSkullBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
		this.hat = DeeperHat.NONE;
	}

	@Override
	public BlockEntityType<?> getType() {
		return CCBlockEntityTypes.DEEPER_HEAD.get();
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putString("hat", this.hat.getSerializedName());
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.hat = DeeperHat.byName(compound.getString("hat"));
	}

	@NonNull
	public DeeperHat getHat() {
		return this.hat;
	}

	public void setHat(DeeperHat hat) {
		this.hat = hat;
		this.markUpdated();
	}

	private void markUpdated() {
		this.setChanged();
		this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
	}
}