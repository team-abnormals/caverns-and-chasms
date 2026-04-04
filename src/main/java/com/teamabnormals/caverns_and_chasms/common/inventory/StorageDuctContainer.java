package com.teamabnormals.caverns_and_chasms.common.inventory;

import com.teamabnormals.caverns_and_chasms.common.block.StorageDuctBlock;
import com.teamabnormals.caverns_and_chasms.common.block.StorageDuctBlock.DuctEnd;
import com.teamabnormals.caverns_and_chasms.common.block.entity.StorageDuctBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class StorageDuctContainer implements Container {
	private final List<StorageDuctBlockEntity> containers;
	private final BlockPos openedAtPos;
	private final StorageDuctBlockEntity firstDuct;
	private final StorageDuctBlockEntity lastDuct;
	private final DuctEnd firstDuctOpenEnd;
	private final DuctEnd lastDuctOpenEnd;
	private final Level level;

	public StorageDuctContainer(List<StorageDuctBlockEntity> containers, BlockPos openedAtPos) {
		this.containers = containers;
		this.openedAtPos = openedAtPos;

		this.firstDuct = containers.get(0);
		this.lastDuct = containers.get(containers.size() - 1);

		this.firstDuctOpenEnd = getOpenEndForDuct(firstDuct);
		this.lastDuctOpenEnd = getOpenEndForDuct(lastDuct);

		this.level = containers.get(0).getLevel();
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
		if (player.distanceToSqr(this.openedAtPos.getX() + 0.5D, this.openedAtPos.getY() + 0.5D, this.openedAtPos.getZ() + 0.5D) > (double) (8 * 8))
			return false;
		else if (!this.containers.stream().allMatch(container -> container.stillValid(player)))
			return false;
		else if (this.firstDuctOpenEnd != null) {
			if (StorageDuctBlock.hasConnectionAt(this.firstDuctOpenEnd, this.level, this.firstDuct.getBlockPos(), this.firstDuct.getBlockState()))
				return false;
			else if (StorageDuctBlock.hasConnectionAt(this.lastDuctOpenEnd, this.level, this.lastDuct.getBlockPos(), this.lastDuct.getBlockState()))
				return false;
		}

		return true;
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

	private static DuctEnd getOpenEndForDuct(StorageDuctBlockEntity duct) {
		for (DuctEnd end : DuctEnd.values())
			if (!StorageDuctBlock.hasConnectionAt(end, duct.getLevel(), duct.getBlockPos(), duct.getBlockState()))
				return end;
		return null;
	}
}