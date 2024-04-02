package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.item.silver.SilverItem;
import com.teamabnormals.caverns_and_chasms.core.other.CCDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SpikedRailBlock extends PoweredRailBlock {

	public SpikedRailBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
		cart.getPassengers().forEach((entity) -> {
			if (state.getValue(POWERED) && entity instanceof LivingEntity target) {
				if (target.hurt(CCDamageTypes.spikedRail(level), 4.0F)) {
					SilverItem.causeMagicDamageParticles(target);
				}
			}
		});
	}
}
