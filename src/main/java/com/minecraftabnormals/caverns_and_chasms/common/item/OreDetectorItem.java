package com.minecraftabnormals.caverns_and_chasms.common.item;

import com.minecraftabnormals.caverns_and_chasms.core.other.CCTags;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEnchantments;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import java.util.stream.Stream;

public class OreDetectorItem extends Item {

	public OreDetectorItem(Properties properties) {
		super(properties);
	}

	public static boolean getDetectionData(ItemStack stack) {
		return stack.getTag().getBoolean("Detecting");
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!world.isRemote) {
			CompoundNBT tag = stack.getOrCreateTag();
			AxisAlignedBB aabb = entity.getBoundingBox().grow(3, 3, 3);
			Stream<BlockPos> blocks = BlockPos.getAllInBox(new BlockPos(aabb.minX, aabb.minY, aabb.minZ), new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ));

			tag.putBoolean("Detecting", false);

			blocks.forEach(blockPos -> {

				Block block = world.getBlockState(blockPos).getBlock();

				if (EnchantmentHelper.getEnchantmentLevel(CCEnchantments.PROSPECTING.get(), stack) > 0) {
					if (block.isIn(CCTags.Blocks.PROSPECTING_METALS)) {
						tag.putBoolean("Detecting", true);
					}
				} else if (EnchantmentHelper.getEnchantmentLevel(CCEnchantments.TREASURING.get(), stack) > 0) {
					if (block.isIn(CCTags.Blocks.TREASURING_GEMS)) {
						tag.putBoolean("Detecting", true);
					}
				} else if (block.isIn(Tags.Blocks.ORES)) {
					tag.putBoolean("Detecting", true);
				}
			});
		}
	}
}
