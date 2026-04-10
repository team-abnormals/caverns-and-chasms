package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.StorageDuctBlock;
import com.teamabnormals.caverns_and_chasms.common.block.StorageDuctBlock.DuctEnd;
import com.teamabnormals.caverns_and_chasms.common.inventory.StorageDuctMenu;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
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
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class StorageDuctBlockEntity extends RandomizableContainerBlockEntity {
	private Lazy<IItemHandlerModifiable> ductHandler;
	private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);

	public StorageDuctBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.STORAGE_DUCT.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, Provider registries) {
		super.saveAdditional(tag, registries);
		if (!this.trySaveLootTable(tag))
			ContainerHelper.saveAllItems(tag, this.items, registries);
	}

	@Override
	public void loadAdditional(CompoundTag tag, Provider registries) {
		super.loadAdditional(tag, registries);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(tag))
			ContainerHelper.loadAllItems(tag, this.items, registries);
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
		return level != null && level.getBlockEntity(this.getBlockPos()) == this;
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container." + CavernsAndChasms.MOD_ID + ".storage_duct");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
		return new StorageDuctMenu(containerId, inventory, StorageDuctBlock.getContainer(this.getLevel(), this.getBlockPos(), DuctEnd.FIRST), null);
	}

	@Override
	public void setBlockState(BlockState state) {
		super.setBlockState(state);
		this.resetHandler();
	}

	public IItemHandler getCapability(@Nullable Direction side) {
		if (this.ductHandler == null)
			this.ductHandler = Lazy.of(this::createHandler);
		return this.ductHandler.get();
	}

	private IItemHandlerModifiable createHandler() {
		BlockState state = this.getBlockState();
		if (!(state.getBlock() instanceof StorageDuctBlock)) {
			return new InvWrapper(this);
		}
		Container inv = StorageDuctBlock.getContainer(this.getLevel(), this.getBlockPos(), DuctEnd.FIRST);
		return new InvWrapper(inv == null ? this : inv);
	}

	public void resetHandler() {
		if (ductHandler != null) {
			ductHandler.invalidate();
			ductHandler = null;
		}
	}

	@Override
	public void invalidateCapabilities() {
		super.invalidateCapabilities();
		this.resetHandler();
	}
}