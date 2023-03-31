package com.teamabnormals.caverns_and_chasms.integration.quark;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.content.client.module.ImprovedTooltipsModule;
import vazkii.quark.content.client.tooltip.ShulkerBoxTooltips.ShulkerComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ToolboxTooltips {

	@OnlyIn(Dist.CLIENT)
	public static void makeTooltip(RenderTooltipEvent.GatherComponents event) {
		if (ItemNBTHelper.getBoolean(event.getItemStack(), "quark:no_tooltip", false) || !ImprovedTooltipsModule.shulkerTooltips)
			return;

		ItemStack stack = event.getItemStack();
		if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ToolboxBlock toolboxBlock) {
			CompoundTag tag = ItemNBTHelper.getCompound(stack, "BlockEntityTag", false);

			if (tag.contains("LootTable") || !tag.contains("id")) return;

			BlockEntity te = BlockEntity.loadStatic(BlockPos.ZERO, toolboxBlock.defaultBlockState(), tag);
			if (te != null && te.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
				List<Either<FormattedText, TooltipComponent>> tooltip = event.getTooltipElements();
				List<Either<FormattedText, TooltipComponent>> tooltipCopy = new ArrayList<>(tooltip);

				for (int i = 1; i < tooltipCopy.size(); i++) {
					Either<FormattedText, TooltipComponent> either = tooltipCopy.get(i);
					if (either.left().isPresent()) {
						String s = either.left().get().getString();
						if (!s.startsWith("\u00a7") || s.startsWith("\u00a7o")) tooltip.remove(either);
					}
				}

				if (!ImprovedTooltipsModule.shulkerBoxRequireShift || Screen.hasShiftDown()) tooltip.add(1, Either.right(new ToolboxComponent(stack)));
				if (ImprovedTooltipsModule.shulkerBoxRequireShift && !Screen.hasShiftDown()) tooltip.add(1, Either.left(Component.translatable("quark.misc.shulker_box_shift")));
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public record ToolboxComponent(ItemStack stack) implements ClientTooltipComponent, TooltipComponent {

		@Override
		public void renderImage(@Nonnull Font font, int tooltipX, int tooltipY, @Nonnull PoseStack pose, @Nonnull ItemRenderer itemRenderer, int something) {
			new ShulkerComponent(this.stack).renderImage(font, tooltipX, tooltipY, pose, itemRenderer, something);
		}

		public int getHeight() {
			return 29;
		}

		public int getWidth(@Nonnull Font font) {
			return 171;
		}
	}
}