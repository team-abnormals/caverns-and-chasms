package com.minecraftabnormals.caverns_and_chasms.common.block;

import com.minecraftabnormals.caverns_and_chasms.core.other.CCDamageSources;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpikedRailBlock extends PoweredRailBlock {
	public SpikedRailBlock(AbstractBlock.Properties properties) {
		super(properties, true);
	}

	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		cart.getPassengers().forEach((entity) -> {
			if (state.get(POWERED) && entity instanceof LivingEntity)
				entity.attackEntityFrom(CCDamageSources.SPIKED_RAIL, 4.0F);
		});
	}
}
