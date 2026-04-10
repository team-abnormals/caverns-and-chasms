package com.teamabnormals.caverns_and_chasms.common.inventory;

import com.teamabnormals.caverns_and_chasms.common.level.SpinelBoom;
import com.teamabnormals.caverns_and_chasms.common.network.SpinelBoomPayload;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMenuTypes;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;

public class BejeweledAnvilMenu extends AnvilMenu {

	public BejeweledAnvilMenu(int containerId, Inventory playerInventory) {
		this(containerId, playerInventory, ContainerLevelAccess.NULL);
	}

	public BejeweledAnvilMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
		super(containerId, playerInventory, access);
		this.menuType = CCMenuTypes.BEJEWELED_ANVIL.get();
	}

	@Override
	protected boolean isValidBlock(BlockState p_39019_) {
		return p_39019_.is(CCBlocks.BEJEWELED_ANVIL.get());
	}

	@Override
	protected boolean mayPickup(Player player, boolean hasStack) {
		return true;
	}

	@Override
	protected void onTake(Player player, ItemStack stack) {
		ItemStack input = this.inputSlots.getItem(0);
		ItemStack ingredient = this.inputSlots.getItem(1);
		ItemStack output = this.resultSlots.getItem(0);

		CommonHooks.onAnvilRepair(player, stack, this.inputSlots.getItem(0), this.inputSlots.getItem(1));

		this.inputSlots.setItem(0, ItemStack.EMPTY);
		ItemStack itemstack = this.inputSlots.getItem(1);
		if (this.repairItemCountCost > 0) {
			if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCountCost) {
				itemstack.shrink(this.repairItemCountCost);
				this.inputSlots.setItem(1, itemstack);
			} else {
				this.inputSlots.setItem(1, ItemStack.EMPTY);
			}
		} else {
			itemstack.shrink(1);
			this.inputSlots.setItem(1, itemstack);
		}

		this.access.execute((level, pos) -> {
			BlockState state = level.getBlockState(pos);
			if (player instanceof ServerPlayer serverPlayer) {
				CCCriteriaTriggers.REPAIRED_ITEM.get().trigger(serverPlayer, input, ingredient, output);
			}

			if (state.is(CCBlocks.BEJEWELED_ANVIL.get())) {
				level.removeBlock(pos, false);
				level.levelEvent(1029, pos, 0);

				if (level instanceof ServerLevel serverLevel) {
					SpinelBoom boom = new SpinelBoom(level, null, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 2.0F);
					if (!EventHooks.onExplosionStart(level, boom)) {
						boom.explode();
						boom.finalizeExplosion(true);
						PacketDistributor.sendToPlayersInDimension(serverLevel, new SpinelBoomPayload(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, 2.0F, boom.getToBlow()));
					}
				}
			} else {
				level.levelEvent(1030, pos, 0);
			}
		});
	}

	@Override
	public void createResult() {
		ItemStack item1 = this.inputSlots.getItem(0);
		int i = 0;
		int j = 0;
		int k = 0;
		if (item1.isEmpty()) {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
		} else {
			ItemStack item1Copy = item1.copy();
			ItemStack item2 = this.inputSlots.getItem(1);
			ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(item1Copy));
			this.repairItemCountCost = 0;
			boolean flag = false;

			if (!CommonHooks.onAnvilChange(this, item1, item2, resultSlots, itemName, j, this.player)) return;
			if (!item2.isEmpty()) {
				flag = item2.has(DataComponents.STORED_ENCHANTMENTS);
				if (item1Copy.isDamageableItem() && item1Copy.getItem().isValidRepairItem(item1, item2)) {
					int l2 = Math.min(item1Copy.getDamageValue(), item1Copy.getMaxDamage() / 4);
					if (l2 <= 0) {
						this.resultSlots.setItem(0, ItemStack.EMPTY);
						return;
					}

					int i3;
					for (i3 = 0; l2 > 0 && i3 < item2.getCount(); ++i3) {
						int j3 = item1Copy.getDamageValue() - l2;
						item1Copy.setDamageValue(j3);
						++i;
						l2 = Math.min(item1Copy.getDamageValue(), item1Copy.getMaxDamage() / 4);
					}

					this.repairItemCountCost = i3;
				} else {
					if (!flag && (!item1Copy.is(item2.getItem()) || !item1Copy.isDamageableItem())) {
						this.resultSlots.setItem(0, ItemStack.EMPTY);
						return;
					}

					if (item1Copy.isDamageableItem() && !flag) {
						int l = item1.getMaxDamage() - item1.getDamageValue();
						int i1 = item2.getMaxDamage() - item2.getDamageValue();
						int j1 = i1 + item1Copy.getMaxDamage() * 12 / 100;
						int k1 = l + j1;
						int l1 = item1Copy.getMaxDamage() - k1;
						if (l1 < 0) {
							l1 = 0;
						}

						if (l1 < item1Copy.getDamageValue()) {
							item1Copy.setDamageValue(l1);
							i += 2;
						}
					}

					ItemEnchantments map1 = EnchantmentHelper.getEnchantmentsForCrafting(item2);
					boolean flag2 = false;
					boolean flag3 = false;

					for (Entry<Holder<Enchantment>> entry : map1.entrySet()) {
						Holder<Enchantment> holder1 = entry.getKey();
						if (holder1 != null) {
							int i2 = mutable.getLevel(holder1);
							int j2 = entry.getIntValue();
							j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
							Enchantment enchantment1 = holder1.value();
							boolean flag1 = item1.supportsEnchantment(holder1);
							if (this.player.getAbilities().instabuild) {
								flag1 = true;
							}

							for (Holder<Enchantment> holder2 : mutable.keySet()) {
								if (!holder2.equals(holder1) && !Enchantment.areCompatible(holder1, holder2)) {
									flag1 = false;
									++i;
								}
							}

							if (!flag1) {
								flag3 = true;
							} else {
								flag2 = true;
								if (j2 > enchantment1.getMaxLevel()) {
									j2 = enchantment1.getMaxLevel();
								}

								mutable.set(holder1, j2);
								int k3 = enchantment1.getAnvilCost();
								if (flag) {
									k3 = Math.max(1, k3 / 2);
								}

								i += k3 * j2;
								if (item1.getCount() > 1) {
									i = 40;
								}
							}
						}
					}

					if (flag3 && !flag2) {
						this.resultSlots.setItem(0, ItemStack.EMPTY);
						return;
					}
				}
			}

			if (this.itemName != null && !StringUtil.isBlank(this.itemName)) {
				if (!this.itemName.equals(item1.getHoverName().getString())) {
					k = 1;
					i += k;
					item1Copy.set(DataComponents.CUSTOM_NAME, Component.literal(this.itemName));
				}
			} else if (item1.has(DataComponents.CUSTOM_NAME)) {
				k = 1;
				i += k;
				item1Copy.remove(DataComponents.CUSTOM_NAME);
			}
			if (flag && !item1Copy.isBookEnchantable(item2)) item1Copy = ItemStack.EMPTY;

			if (i <= 0) {
				item1Copy = ItemStack.EMPTY;
			}

			if (!item1Copy.isEmpty()) {
				int k2 = item1Copy.getOrDefault(DataComponents.REPAIR_COST, 0);
				if (!item2.isEmpty() && k2 < item2.getOrDefault(DataComponents.REPAIR_COST, 0)) {
					k2 = item2.getOrDefault(DataComponents.REPAIR_COST, 0);
				}

				if (k != i || k == 0) {
					k2 = calculateIncreasedRepairCost(k2);
				}

				item1Copy.set(DataComponents.REPAIR_COST, k2);
				EnchantmentHelper.setEnchantments(item1Copy, mutable.toImmutable());
			}

			this.resultSlots.setItem(0, item1Copy);
			this.broadcastChanges();
		}
	}
}
