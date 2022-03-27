package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.CupricCampfireBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class CupricCampfireBlock extends CampfireBlock {

	public CupricCampfireBlock(Properties properties) {
		super(false, 2, properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CupricCampfireBlockEntity(pos, state);
	}

	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
		if (!entityIn.fireImmune() && state.getValue(LIT) && entityIn instanceof LivingEntity && ((LivingEntity) entityIn).isInvertedHealAndHarm() && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
			entityIn.hurt(DamageSource.IN_FIRE, 4.0F);
		}
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (level.isClientSide) {
			return state.getValue(LIT) ? createTickerHelper(blockEntityType, CCBlockEntityTypes.CUPRIC_CAMPFIRE.get(), CampfireBlockEntity::particleTick) : null;
		} else {
			return state.getValue(LIT) ? createTickerHelper(blockEntityType, CCBlockEntityTypes.CUPRIC_CAMPFIRE.get(), CampfireBlockEntity::cookTick) : createTickerHelper(blockEntityType, CCBlockEntityTypes.CUPRIC_CAMPFIRE.get(), CampfireBlockEntity::cooldownTick);
		}
	}
}
