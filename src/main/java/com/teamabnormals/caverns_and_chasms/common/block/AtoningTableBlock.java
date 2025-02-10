package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.AtoningTableBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.inventory.AtoningMenu;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class AtoningTableBlock extends EnchantmentTableBlock {

	public AtoningTableBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AtoningTableBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? createTickerHelper(type, CCBlockEntityTypes.ATONING_TABLE.get(), AtoningTableBlockEntity::bookAnimationTick) : null;
	}

	@Nullable
	@Override
	public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		BlockEntity entity = level.getBlockEntity(pos);
		if (entity instanceof AtoningTableBlockEntity table) {
			return new SimpleMenuProvider((i, inventory, player) -> new AtoningMenu(i, inventory, ContainerLevelAccess.create(level, pos)), table.getDisplayName());
		} else {
			return null;
		}
	}

	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		float enchPower = 0;

		for (BlockPos blockpos : BOOKSHELF_OFFSETS) {
			if (isValidBookShelf(level, pos, blockpos)) {
				enchPower += level.getBlockState(blockpos).getEnchantPowerBonus(level, blockpos);
				if (random.nextInt(16) == 0)
					level.addParticle(ParticleTypes.ENCHANT, pos.getX() + 0.5D, pos.getY() + 2.0D, pos.getZ() + 0.5D, blockpos.getX() + random.nextFloat() - 0.5D, blockpos.getY() - random.nextFloat() - 1.0F, blockpos.getZ() + random.nextFloat() - 0.5D);
			}
		}

		if (random.nextInt(100) == 0)
			level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, CCSoundEvents.ATONING_TABLE_WHISPERS.get(), SoundSource.BLOCKS, Math.min(enchPower, 15) / 15.0F + 1.0F, 1.0F, false);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			if (level.getBlockEntity(pos) instanceof AtoningTableBlockEntity table) {
				table.setCustomName(stack.getHoverName());
			}
		}
	}
}