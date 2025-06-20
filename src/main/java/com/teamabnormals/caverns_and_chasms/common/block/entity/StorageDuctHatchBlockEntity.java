package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.StorageDuctHatchBlock;
import com.teamabnormals.caverns_and_chasms.common.inventory.StorageDuctContainer;
import com.teamabnormals.caverns_and_chasms.common.inventory.StorageDuctMenu;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;

public class StorageDuctHatchBlockEntity extends BlockEntity {
	private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
		@Override
		protected void onOpen(Level level, BlockPos pos, BlockState state) {
			StorageDuctHatchBlockEntity.this.playSound(SoundEvents.BARREL_OPEN);
			StorageDuctHatchBlockEntity.this.updateBlockState(state, true);
		}

		@Override
		protected void onClose(Level level, BlockPos pos, BlockState state) {
			StorageDuctHatchBlockEntity.this.playSound(SoundEvents.BARREL_CLOSE);
			StorageDuctHatchBlockEntity.this.updateBlockState(state, false);
		}

		@Override
		protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
		}

		@Override
		protected boolean isOwnContainer(Player player) {
			if (player.containerMenu instanceof StorageDuctMenu menu && menu.getHatch() == StorageDuctHatchBlockEntity.this) {
				StorageDuctContainer container = (StorageDuctContainer) menu.getContainer();

				Level level = StorageDuctHatchBlockEntity.this.getLevel();
				BlockPos blockpos = StorageDuctHatchBlockEntity.this.getBlockPos();
				BlockState blockstate = StorageDuctHatchBlockEntity.this.getBlockState();
				BlockPos offsetpos = blockpos.relative(StorageDuctHatchBlock.getAttachDirection(blockstate));

				if (level.getBlockEntity(offsetpos) instanceof StorageDuctBlockEntity blockentity)
					return container.contains(blockentity);
			}

			return false;
		}
	};

	public StorageDuctHatchBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.STORAGE_DUCT_HATCH.get(), pos, state);
	}

	public void startOpen(Player player) {
		if (!this.remove && !player.isSpectator())
			this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
	}

	public void stopOpen(Player player) {
		if (!this.remove && !player.isSpectator())
			this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
	}

	public void recheckOpen() {
		if (!this.remove)
			this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
	}

	public boolean stillValid() {
		Level level = this.getLevel();
		return level != null && level.getBlockEntity(this.getBlockPos()) == this;
	}

	private void updateBlockState(BlockState state, boolean open) {
		this.level.setBlock(this.getBlockPos(), state.setValue(StorageDuctHatchBlock.OPEN, open), 3);
	}

	private void playSound(SoundEvent soundEvent) {
		double x = this.worldPosition.getX() + 0.5D;
		double y = this.worldPosition.getY() + 0.5D;
		double z = this.worldPosition.getZ() + 0.5D;
		this.level.playSound(null, x, y, z, soundEvent, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
	}
}