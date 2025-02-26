package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.StorageDuctBlock;
import com.teamabnormals.caverns_and_chasms.common.inventory.StorageDuctMenu;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class StorageDuctBlockEntity extends RandomizableContainerBlockEntity {
	private LazyOptional<IItemHandlerModifiable> ductHandler;
	private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);

	public StorageDuctBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.STORAGE_DUCT.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (!this.trySaveLootTable(tag))
			ContainerHelper.saveAllItems(tag, this.items);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(tag))
			ContainerHelper.loadAllItems(tag, this.items);
	}

	@Override
	public int getContainerSize() {
		return 9;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> list) {
		this.items = list;
	}

	@Override
	public boolean stillValid(Player player) {
		Level level = this.getLevel();
		BlockPos blockpos = this.getBlockPos();
		if (level == null) {
			return false;
		} else if (level.getBlockEntity(blockpos) != this) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container." + CavernsAndChasms.MOD_ID + ".storage_duct");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
		return new StorageDuctMenu(containerId, inventory, StorageDuctBlock.getContainer(this.getLevel(), this.getBlockPos()));
	}

	@Override
	public void setBlockState(BlockState state) {
		super.setBlockState(state);
		this.resetHandler();
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
			if (this.ductHandler == null)
				this.ductHandler = LazyOptional.of(this::createHandler);
			return this.ductHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	private net.minecraftforge.items.IItemHandlerModifiable createHandler() {
		BlockState state = this.getBlockState();
		if (!(state.getBlock() instanceof StorageDuctBlock)) {
			return new InvWrapper(this);
		}
		Container inv = StorageDuctBlock.getContainer(this.getLevel(), this.getBlockPos());
		return new InvWrapper(inv == null ? this : inv);
	}

	public void resetHandler() {
		if (ductHandler != null) {
			ductHandler.invalidate();
			ductHandler = null;
		}
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		this.resetHandler();
	}
}