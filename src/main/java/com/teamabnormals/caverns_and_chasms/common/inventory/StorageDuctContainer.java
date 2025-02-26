package com.teamabnormals.caverns_and_chasms.common.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class StorageDuctContainer<C extends Container> implements Container {
	private final List<C> containers;

	public StorageDuctContainer(List<C> containers) {
		this.containers = containers;
	}

	@Override
	public int getContainerSize() {
		return this.containers.stream().mapToInt(Container::getContainerSize).sum();
	}

	@Override
	public boolean isEmpty() {
		return this.containers.stream().allMatch(Container::isEmpty);
	}

	public boolean contains(Container container) {
		return this.containers.contains(container);
	}

	@Override
	public ItemStack getItem(int index) {
		return this.containers.get(index / 9).getItem(index % 9);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return this.containers.get(index / 9).removeItem(index % 9, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return this.containers.get(index / 9).removeItemNoUpdate(index % 9);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		this.containers.get(index / 9).setItem(index % 9, stack);
	}

	@Override
	public int getMaxStackSize() {
		return this.containers.get(0).getMaxStackSize();
	}

	@Override
	public void setChanged() {
		this.containers.forEach(Container::setChanged);
	}

	@Override
	public boolean stillValid(Player player) {
		BlockPos blockpos = ((BlockEntity) this.containers.get(0)).getBlockPos();
		return player.distanceToSqr(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D) <= (double)(8 * 8) && this.containers.stream().allMatch(container -> container.stillValid(player));
	}

	@Override
	public void startOpen(Player player) {
		this.containers.forEach(container -> container.startOpen(player));
	}

	@Override
	public void stopOpen(Player player) {
		this.containers.forEach(container -> container.stopOpen(player));
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return this.containers.get(index / 9).canPlaceItem(index % 9, stack);
	}

	@Override
	public void clearContent() {
		this.containers.forEach(Container::clearContent);
	}
}