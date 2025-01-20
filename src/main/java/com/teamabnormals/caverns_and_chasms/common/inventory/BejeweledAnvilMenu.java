package com.teamabnormals.caverns_and_chasms.common.inventory;

import com.teamabnormals.caverns_and_chasms.common.level.SpinelBoom;
import com.teamabnormals.caverns_and_chasms.common.network.S2CSpinelBoomMessage;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMenuTypes;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Map;

public class BejeweledAnvilMenu extends ItemCombinerMenu {
	public int repairItemCountCost;
	@Nullable
	private String itemName;

	public BejeweledAnvilMenu(int p_39005_, Inventory p_39006_) {
		this(p_39005_, p_39006_, ContainerLevelAccess.NULL);
	}

	public BejeweledAnvilMenu(int p_39008_, Inventory p_39009_, ContainerLevelAccess p_39010_) {
		super(CCMenuTypes.BEJEWELED_ANVIL.get(), p_39008_, p_39009_, p_39010_);
	}

	protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
		return ItemCombinerMenuSlotDefinition.create().withSlot(0, 27, 47, (p_266635_) -> true).withSlot(1, 76, 47, (p_266634_) -> true).withResultSlot(2, 134, 47).build();
	}

	@Override
	protected boolean isValidBlock(BlockState p_39019_) {
		return p_39019_.is(CCBlocks.BEJEWELED_ANVIL.get());
	}

	@Override
	protected boolean mayPickup(Player player, boolean p_39024_) {
		return true;
	}

	protected void onTake(Player player, ItemStack p_150475_) {
		this.inputSlots.setItem(0, ItemStack.EMPTY);
		if (this.repairItemCountCost > 0) {
			ItemStack itemstack = this.inputSlots.getItem(1);
			if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCountCost) {
				itemstack.shrink(this.repairItemCountCost);
				this.inputSlots.setItem(1, itemstack);
			} else {
				this.inputSlots.getItem(1).shrink(1);
			}
		} else {
			this.inputSlots.getItem(1).shrink(1);
		}

		this.access.execute((level, pos) -> {
			BlockState state = level.getBlockState(pos);
			if (state.is(CCBlocks.BEJEWELED_ANVIL.get())) {
				level.removeBlock(pos, false);
				level.levelEvent(1029, pos, 0);

				if (!level.isClientSide()) {
					SpinelBoom boom = new SpinelBoom(level, null, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 2.0F);
					if (!ForgeEventFactory.onExplosionStart(level, boom)) {
						boom.explode();
						boom.finalizeExplosion(true);
						CavernsAndChasms.CHANNEL.send(PacketDistributor.DIMENSION.with(level::dimension), new S2CSpinelBoomMessage(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, 2.0F, boom.getToBlow()));
					}
				}
			} else {
				level.levelEvent(1030, pos, 0);
			}
		});
	}

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
			Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(item1Copy);
			this.repairItemCountCost = 0;
			boolean flag = false;

			//TODO: if (!ForgeHooks.onAnvilChange(this, item1, item2, resultSlots, itemName, j, this.player)) return;
			if (!item2.isEmpty()) {
				flag = item2.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(item2).isEmpty();
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

					Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(item2);
					boolean flag2 = false;
					boolean flag3 = false;

					for (Enchantment enchantment1 : map1.keySet()) {
						if (enchantment1 != null) {
							int i2 = map.getOrDefault(enchantment1, 0);
							int j2 = map1.get(enchantment1);
							j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
							boolean flag1 = enchantment1.canEnchant(item1);
							if (this.player.getAbilities().instabuild || item1.is(Items.ENCHANTED_BOOK)) {
								flag1 = true;
							}

							for (Enchantment enchantment : map.keySet()) {
								if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
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

								map.put(enchantment1, j2);
								int k3 = switch (enchantment1.getRarity()) {
									case COMMON -> 1;
									case UNCOMMON -> 2;
									case RARE -> 4;
									case VERY_RARE -> 8;
								};

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

			if (this.itemName != null && !Util.isBlank(this.itemName)) {
				if (!this.itemName.equals(item1.getHoverName().getString())) {
					k = 1;
					i += k;
					item1Copy.setHoverName(Component.literal(this.itemName));
				}
			} else if (item1.hasCustomHoverName()) {
				k = 1;
				i += k;
				item1Copy.resetHoverName();
			}
			if (flag && !item1Copy.isBookEnchantable(item2)) item1Copy = ItemStack.EMPTY;

			if (i <= 0) {
				item1Copy = ItemStack.EMPTY;
			}

			if (!item1Copy.isEmpty()) {
				int k2 = item1Copy.getBaseRepairCost();
				if (!item2.isEmpty() && k2 < item2.getBaseRepairCost()) {
					k2 = item2.getBaseRepairCost();
				}

				if (k != i || k == 0) {
					k2 = calculateIncreasedRepairCost(k2);
				}

				item1Copy.setRepairCost(k2);
				EnchantmentHelper.setEnchantments(map, item1Copy);
			}

			this.resultSlots.setItem(0, item1Copy);
			this.broadcastChanges();
		}
	}

	public static int calculateIncreasedRepairCost(int p_39026_) {
		return p_39026_ * 2 + 1;
	}

	public boolean setItemName(String p_288970_) {
		String s = validateName(p_288970_);
		if (s != null && !s.equals(this.itemName)) {
			this.itemName = s;
			if (this.getSlot(2).hasItem()) {
				ItemStack itemstack = this.getSlot(2).getItem();
				if (Util.isBlank(s)) {
					itemstack.resetHoverName();
				} else {
					itemstack.setHoverName(Component.literal(s));
				}
			}

			this.createResult();
			return true;
		} else {
			return false;
		}
	}

	@Nullable
	private static String validateName(String p_288995_) {
		String s = SharedConstants.filterText(p_288995_);
		return s.length() <= 50 ? s : null;
	}
}
