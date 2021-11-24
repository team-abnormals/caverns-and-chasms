package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;

import java.util.stream.Stream;

public class OreDetectorItem extends Item implements Vanishable {

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
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
		if (!world.isClientSide) {
			CompoundTag tag = stack.getOrCreateTag();
			AABB aabb = entity.getBoundingBox().inflate(3, 3, 3);
			Stream<BlockPos> blocks = BlockPos.betweenClosedStream(new BlockPos(aabb.minX, aabb.minY, aabb.minZ), new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ));

			tag.putBoolean("Detecting", false);

			blocks.forEach(blockPos -> {

				BlockState state = world.getBlockState(blockPos);

				if (EnchantmentHelper.getItemEnchantmentLevel(CCEnchantments.PROSPECTING.get(), stack) > 0) {
					if (state.is(CCBlockTags.PROSPECTING_METALS)) {
						tag.putBoolean("Detecting", true);
					}
				} else if (EnchantmentHelper.getItemEnchantmentLevel(CCEnchantments.TREASURING.get(), stack) > 0) {
					if (state.is(CCBlockTags.TREASURING_GEMS)) {
						tag.putBoolean("Detecting", true);
					}
				} else if (state.is(Tags.Blocks.ORES)) {
					tag.putBoolean("Detecting", true);
				}
			});
		}
	}
}
