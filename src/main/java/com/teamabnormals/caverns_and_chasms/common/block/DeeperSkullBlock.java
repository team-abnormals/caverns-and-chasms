package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.DeeperSkullBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.DeeperHat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolActions;

import java.util.List;

public class DeeperSkullBlock extends SkullBlock {

	public DeeperSkullBlock(Type type, Properties properties) {
		super(type, properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DeeperSkullBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return null;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		return tryToAddHat(level, pos, player, hand);
	}

	public static InteractionResult tryToAddHat(Level level, BlockPos pos, Player player, InteractionHand hand) {
		if (player.getAbilities().mayBuild && level.getBlockEntity(pos) instanceof DeeperSkullBlockEntity blockentity) {
			ItemStack itemstack = player.getItemInHand(hand);
			if (itemstack.canPerformAction(ToolActions.SHEARS_CARVE)) {
				DeeperHat hat = blockentity.getHat();
				if (hat != DeeperHat.NONE) {
					if (!level.isClientSide) {
						level.playSound(null, pos, SoundEvents.SNOW_GOLEM_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
						blockentity.setHat(DeeperHat.NONE);
						popResource(level, pos, new ItemStack(hat.getItem()));
						itemstack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(hand));
						level.gameEvent(player, GameEvent.SHEAR, pos);
						player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
					}
					return InteractionResult.sidedSuccess(level.isClientSide);
				}
			} else {
				DeeperHat hat = DeeperHat.byItem(itemstack.getItem());
				if (hat != DeeperHat.NONE && hat != blockentity.getHat()) {
					if (!level.isClientSide) {
						level.playSound(null, pos, CCSoundEvents.CAVE_GROWTHS_PLACE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
						blockentity.setHat(hat);
					}
					if (!player.getAbilities().instabuild)
						itemstack.shrink(1);
					return InteractionResult.sidedSuccess(level.isClientSide);
				}
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof DeeperSkullBlockEntity blockentity) {
			Item item = blockentity.getHat().getItem();
			if (item != null)
				drops.add(new ItemStack(item));
		}
		return drops;
	}
}