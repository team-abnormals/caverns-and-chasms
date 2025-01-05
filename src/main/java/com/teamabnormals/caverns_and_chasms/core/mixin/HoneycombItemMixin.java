package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import com.teamabnormals.caverns_and_chasms.common.block.entity.ToolboxBlockEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoneycombItem.class)
public abstract class HoneycombItemMixin {

	@Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
	private void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		if (context.getLevel().getBlockState(context.getClickedPos()).getBlock() instanceof ToolboxBlock) {
			BlockEntity toolbox = level.getBlockEntity(pos);
			if (toolbox instanceof ToolboxBlockEntity) {
				if (HoneycombItem.getWaxed(state).isPresent()) {
					BlockState newState = HoneycombItem.getWaxed(state).get();
					Player player = context.getPlayer();
					ItemStack stack = context.getItemInHand();
					if (player instanceof ServerPlayer) {
						CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, stack);
					}

					stack.shrink(1);
					CompoundTag tag = toolbox.serializeNBT();
					level.setBlock(pos, newState, 11);
					level.getBlockEntity(pos).deserializeNBT(tag);

					level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
					level.levelEvent(player, 3003, pos, 0);
					cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
				}
			}
		}
	}
}