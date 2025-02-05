package com.teamabnormals.caverns_and_chasms.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class CopyRecordItem extends Item {

	public CopyRecordItem(Item.Properties builder) {
		super(builder);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		if (state.is(Blocks.JUKEBOX) && !state.getValue(JukeboxBlock.HAS_RECORD)) {
			ItemStack stack = context.getItemInHand();
			if (!level.isClientSide) {
				Player player = context.getPlayer();
				BlockEntity blockEntity = level.getBlockEntity(pos);
				if (blockEntity instanceof JukeboxBlockEntity jukebox) {
					ItemStack newStack = stack.copy();
					newStack.setCount(1);
					jukebox.setFirstItem(newStack);
					level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
				}

				stack.shrink(1);
				if (player != null) {
					player.awardStat(Stats.PLAY_RECORD);
				}
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		ItemStack disc = this.getDiscStack(stack);
		if (!disc.isEmpty()) {
			tooltip.add(Component.translatable(new ItemStack(this).getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
			tooltip.add(this.getDisplayName(stack).withStyle(ChatFormatting.GRAY));
		}
	}

	public MutableComponent getDisplayName(ItemStack stack) {
		return Component.translatable(this.getDiscStack(stack).getDescriptionId() + ".desc");
	}

	public ItemStack getDiscStack(ItemStack base) {
		String id = base.getOrCreateTag().getString("music_disc");
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
		return new ItemStack(item != null ? item : this);
	}
}