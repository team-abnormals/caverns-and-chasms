package com.teamabnormals.caverns_and_chasms.common.inventory;

import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMenuTypes;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

public class AtoningMenu extends AbstractContainerMenu {
	private final Container enchantSlots = new SimpleContainer(3) {
		public void setChanged() {
			super.setChanged();
			AtoningMenu.this.slotsChanged(this);
		}
	};
	private final ContainerLevelAccess access;
	private final RandomSource random = RandomSource.create();
	private final DataSlot enchantmentSeed = DataSlot.standalone();
	public final int[] costs = new int[3];
	public final int[] enchantClue = new int[]{-1, -1, -1};
	public final int[] levelClue = new int[]{-1, -1, -1};

	public AtoningMenu(int i, Inventory inventory) {
		this(i, inventory, ContainerLevelAccess.NULL);
	}

	public AtoningMenu(int p_39457_, Inventory inventory, ContainerLevelAccess access) {
		super(CCMenuTypes.ATONING.get(), p_39457_);
		this.access = access;
		this.addSlot(new Slot(this.enchantSlots, 0, 25, 26) {
			public boolean mayPlace(ItemStack stack) {
				return true;
			}

			public int getMaxStackSize() {
				return 1;
			}
		});
		this.addSlot(new Slot(this.enchantSlots, 1, 36, 48) {
			public boolean mayPlace(ItemStack stack) {
				return stack.is(Tags.Items.ENCHANTING_FUELS);
			}
		});

		this.addSlot(new Slot(this.enchantSlots, 2, 14, 48) {
			public boolean mayPlace(ItemStack stack) {
				return stack.is(CCItemTags.ATONING_FUELS);
			}
		});

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
		}

		this.addDataSlot(DataSlot.shared(this.costs, 0));
		this.addDataSlot(DataSlot.shared(this.costs, 1));
		this.addDataSlot(DataSlot.shared(this.costs, 2));
		this.addDataSlot(this.enchantmentSeed).set(inventory.player.getEnchantmentSeed());
		this.addDataSlot(DataSlot.shared(this.enchantClue, 0));
		this.addDataSlot(DataSlot.shared(this.enchantClue, 1));
		this.addDataSlot(DataSlot.shared(this.enchantClue, 2));
		this.addDataSlot(DataSlot.shared(this.levelClue, 0));
		this.addDataSlot(DataSlot.shared(this.levelClue, 1));
		this.addDataSlot(DataSlot.shared(this.levelClue, 2));
	}

	public void slotsChanged(Container container) {
		if (container == this.enchantSlots) {
			ItemStack stack = container.getItem(0);
			if (!stack.isEmpty() && stack.isEnchantable()) {
				this.access.execute((level, pos) -> {
					float enchPower = 0;

					for (BlockPos offset : EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
						if (EnchantmentTableBlock.isValidBookShelf(level, pos, offset)) {
							enchPower += level.getBlockState(pos.offset(offset)).getEnchantPowerBonus(level, pos.offset(offset));
						}
					}

					this.random.setSeed(this.enchantmentSeed.get());

					for (int i = 0; i < 3; ++i) {
						this.costs[i] = EnchantmentHelper.getEnchantmentCost(this.random, 3 - i, (int) enchPower, stack);
						this.enchantClue[i] = -1;
						this.levelClue[i] = -1;
						if (this.costs[i] < i + 1) {
							this.costs[i] = 0;
						}
						this.costs[i] = ForgeEventFactory.onEnchantmentLevelSet(level, pos, 3 - i, (int) enchPower, stack, costs[i]);
					}

					for (int i = 0; i < 3; ++i) {
						if (this.costs[i] > 0) {
							List<EnchantmentInstance> list = this.getEnchantmentList(stack, i, this.costs[i]);
							if (list != null && !list.isEmpty()) {
								EnchantmentInstance enchantment = list.get(this.random.nextInt(list.size()));
								this.enchantClue[i] = BuiltInRegistries.ENCHANTMENT.getId(enchantment.enchantment);
								this.levelClue[i] = enchantment.level;
							}
						}
					}

					this.broadcastChanges();
				});
			} else {
				for (int i = 0; i < 3; ++i) {
					this.costs[i] = 0;
					this.enchantClue[i] = -1;
					this.levelClue[i] = -1;
				}
			}
		}

	}

	public boolean clickMenuButton(Player player, int slot) {
		if (slot >= 0 && slot < this.costs.length) {
			ItemStack input = this.enchantSlots.getItem(0);
			ItemStack fuel = this.enchantSlots.getItem(1);
			ItemStack spinel = this.enchantSlots.getItem(2);
			int i = slot + 1;
			if ((fuel.isEmpty() || fuel.getCount() < i || spinel.isEmpty() || spinel.getCount() < i) && !player.getAbilities().instabuild) {
				return false;
			} else if (this.costs[slot] <= 0 || input.isEmpty()) {
				return false;
			} else {
				this.access.execute((level, pos) -> {
					ItemStack output = input;
					List<EnchantmentInstance> list = this.getEnchantmentList(input, slot, this.costs[slot]);
					if (!list.isEmpty()) {
						player.onEnchantmentPerformed(input, 0);
						boolean flag = input.is(Items.BOOK);
						if (flag) {
							output = new ItemStack(Items.ENCHANTED_BOOK);
							CompoundTag tag = input.getTag();
							if (tag != null) {
								output.setTag(tag.copy());
							}

							this.enchantSlots.setItem(0, output);
						}

						for (EnchantmentInstance enchantment : list) {
							if (flag) {
								EnchantedBookItem.addEnchantment(output, enchantment);
							} else {
								output.enchant(enchantment.enchantment, enchantment.level);
								output.hurtAndBreak((4 - i) * this.random.nextInt(Math.max(1, (output.getMaxDamage() - output.getDamageValue()) / 2)), player, (entity) -> {
									entity.broadcastBreakEvent(player.getUsedItemHand());
								});
							}
						}

						if (!player.getAbilities().instabuild) {
							fuel.shrink(i);
							spinel.shrink(i);
							if (fuel.isEmpty()) {
								this.enchantSlots.setItem(1, ItemStack.EMPTY);
							}
							if (spinel.isEmpty()) {
								this.enchantSlots.setItem(2, ItemStack.EMPTY);
							}
						}

						player.awardStat(Stats.ENCHANT_ITEM);
						if (player instanceof ServerPlayer) {
							CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayer) player, output, i);
						}

						this.enchantSlots.setChanged();
						this.enchantmentSeed.set(player.getEnchantmentSeed());
						this.slotsChanged(this.enchantSlots);
						level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
					}

				});
				return true;
			}
		} else {
			Util.logAndPauseIfInIde(player.getName() + " pressed invalid button id: " + slot);
			return false;
		}
	}

	private List<EnchantmentInstance> getEnchantmentList(ItemStack stack, int p_39473_, int p_39474_) {
		this.random.setSeed(this.enchantmentSeed.get() + p_39473_);
		List<EnchantmentInstance> list = EnchantmentHelper.selectEnchantment(this.random, stack, p_39474_, false);
		if (stack.is(Items.BOOK) && list.size() > 1) {
			list.remove(this.random.nextInt(list.size()));
		}

		return list;
	}

	public int getLapisCount() {
		ItemStack stack = this.enchantSlots.getItem(1);
		return stack.isEmpty() ? 0 : stack.getCount();
	}

	public int getSpinelCount() {
		ItemStack stack = this.enchantSlots.getItem(2);
		return stack.isEmpty() ? 0 : stack.getCount();
	}

	public int getEnchantmentSeed() {
		return this.enchantmentSeed.get();
	}

	public void removed(Player p_39488_) {
		super.removed(p_39488_);
		this.access.execute((p_39469_, p_39470_) -> {
			this.clearContainer(p_39488_, this.enchantSlots);
		});
	}

	public boolean stillValid(Player p_39463_) {
		return stillValid(this.access, p_39463_, CCBlocks.ATONING_TABLE.get());
	}

	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack newStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack stack = slot.getItem();
			newStack = stack.copy();
			if (index == 0 || index == 1 || index == 2) {
				if (!this.moveItemStackTo(stack, 2, 38, true)) {
					return ItemStack.EMPTY;
				}
			} else if (stack.is(Tags.Items.ENCHANTING_FUELS)) {
				if (!this.moveItemStackTo(stack, 1, 2, true)) {
					return ItemStack.EMPTY;
				}
			} else if (stack.is(CCItemTags.ATONING_FUELS)) {
				if (!this. moveItemStackTo(stack, 2, 3, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (this.slots.get(0).hasItem() || !this.slots.get(0).mayPlace(stack)) {
					return ItemStack.EMPTY;
				}

				ItemStack itemstack2 = stack.copyWithCount(1);
				stack.shrink(1);
				this.slots.get(0).setByPlayer(itemstack2);
			}

			if (stack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (stack.getCount() == newStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, stack);
		}

		return newStack;
	}
}
