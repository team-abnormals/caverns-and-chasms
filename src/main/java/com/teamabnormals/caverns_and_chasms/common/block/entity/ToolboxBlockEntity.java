package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import com.teamabnormals.caverns_and_chasms.common.inventory.ToolboxMenu;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class ToolboxBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
	private static final int[] SLOTS = IntStream.range(0, 9).toArray();
	private NonNullList<ItemStack> itemStacks = NonNullList.withSize(9, ItemStack.EMPTY);
	private int openCount;
	private ToolboxBlockEntity.AnimationStatus animationStatus = ToolboxBlockEntity.AnimationStatus.CLOSED;
	private float progress;
	private float progressOld;

	public ToolboxBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.TOOLBOX.get(), pos, state);
	}

	public static void tick(Level p_155673_, BlockPos p_155674_, BlockState p_155675_, ToolboxBlockEntity p_155676_) {
		p_155676_.updateAnimation(p_155673_, p_155674_, p_155675_);
	}

	private void updateAnimation(Level p_155680_, BlockPos p_155681_, BlockState p_155682_) {
		this.progressOld = this.progress;
		switch (this.animationStatus) {
			case CLOSED -> this.progress = 0.0F;
			case OPENING -> {
				this.progress += 0.1F;
				if (this.progress >= 1.0F) {
					this.animationStatus = AnimationStatus.OPENED;
					this.progress = 1.0F;
					doNeighborUpdates(p_155680_, p_155681_, p_155682_);
				}
			}
			case CLOSING -> {
				this.progress -= 0.1F;
				if (this.progress <= 0.0F) {
					this.animationStatus = AnimationStatus.CLOSED;
					this.progress = 0.0F;
					doNeighborUpdates(p_155680_, p_155681_, p_155682_);
				}
			}
			case OPENED -> this.progress = 1.0F;
		}

	}

	public ToolboxBlockEntity.AnimationStatus getAnimationStatus() {
		return this.animationStatus;
	}

	@Override
	public int getContainerSize() {
		return this.itemStacks.size();
	}

	@Override
	public boolean triggerEvent(int p_59678_, int p_59679_) {
		if (p_59678_ == 1) {
			this.openCount = p_59679_;
			if (p_59679_ == 0) {
				this.animationStatus = ToolboxBlockEntity.AnimationStatus.CLOSING;
				doNeighborUpdates(this.getLevel(), this.worldPosition, this.getBlockState());
			}

			if (p_59679_ == 1) {
				this.animationStatus = ToolboxBlockEntity.AnimationStatus.OPENING;
				doNeighborUpdates(this.getLevel(), this.worldPosition, this.getBlockState());
			}

			return true;
		} else {
			return super.triggerEvent(p_59678_, p_59679_);
		}
	}

	private static void doNeighborUpdates(Level p_155688_, BlockPos p_155689_, BlockState p_155690_) {
		p_155690_.updateNeighbourShapes(p_155688_, p_155689_, 3);
	}

	public void startOpen(Player p_59692_) {
		if (!p_59692_.isSpectator()) {
			if (this.openCount < 0) {
				this.openCount = 0;
			}

			++this.openCount;
			this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
			if (this.openCount == 1) {
				this.level.gameEvent(p_59692_, GameEvent.CONTAINER_OPEN, this.worldPosition);
				this.level.playSound(null, this.worldPosition, CCSoundEvents.TOOLBOX_OPEN.get(), SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
			}
		}

	}

	@Override
	public void stopOpen(Player player) {
		if (!player.isSpectator()) {
			--this.openCount;
			this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
			if (this.openCount <= 0) {
				this.level.gameEvent(player, GameEvent.CONTAINER_CLOSE, this.worldPosition);
				this.level.playSound(null, this.worldPosition, CCSoundEvents.TOOLBOX_CLOSE.get(), SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
			}
		}

	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container." + CavernsAndChasms.MOD_ID + ".toolbox");
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.loadFromTag(tag);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (!this.trySaveLootTable(tag)) {
			ContainerHelper.saveAllItems(tag, this.itemStacks, false);
		}

	}

	public void loadFromTag(CompoundTag tag) {
		this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(tag) && tag.contains("Items", 9)) {
			ContainerHelper.loadAllItems(tag, this.itemStacks);
		}
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.itemStacks;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		this.itemStacks = items;
	}

	@Override
	public int[] getSlotsForFace(Direction direction) {
		return SLOTS;
	}

	@Override
	public boolean canPlaceItemThroughFace(int p_59663_, ItemStack stack, @Nullable Direction direction) {
		return !(Block.byItem(stack.getItem()) instanceof ToolboxBlock) && (stack.getItem().canBeDepleted() || stack.is(CCItemTags.ADDITIONAL_TOOLBOX_TOOLS));
	}

	@Override
	public boolean canTakeItemThroughFace(int p_59682_, ItemStack stack, Direction direction) {
		return true;
	}

	public float getProgress(float p_59658_) {
		return Mth.lerp(p_59658_, this.progressOld, this.progress);
	}

	protected AbstractContainerMenu createMenu(int p_59660_, Inventory p_59661_) {
		return new ToolboxMenu(p_59660_, p_59661_, this);
	}

	@Override
	protected net.minecraftforge.items.IItemHandler createUnSidedHandler() {
		return new SidedInvWrapper(this, Direction.UP);
	}

	public enum AnimationStatus {
		CLOSED, OPENING, OPENED, CLOSING
	}
}
