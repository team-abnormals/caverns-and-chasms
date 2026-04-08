package com.teamabnormals.caverns_and_chasms.integration.quark;


import com.mojang.datafixers.util.Either;
import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.client.module.ImprovedTooltipsModule;
import org.violetmoon.quark.content.client.tooltip.ShulkerBoxTooltips;
import org.violetmoon.quark.content.client.tooltip.ShulkerBoxTooltips.ShulkerComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ToolboxTooltips {

	@OnlyIn(Dist.CLIENT)
	public static void makeTooltip(RenderTooltipEvent.GatherComponents event) {
		ItemStack stack = event.getItemStack();
		if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ToolboxBlock toolboxBlock && stack.has(DataComponents.CONTAINER)) {
			ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
			if (contents.nonEmptyStream().toList().isEmpty()) {
				return;
			}

			List<Either<FormattedText, TooltipComponent>> tooltip = event.getTooltipElements();
			List<Either<FormattedText, TooltipComponent>> tooltipCopy = new ArrayList<>(tooltip);

			for (int i = 1; i < tooltipCopy.size(); i++) {
				Either<FormattedText, TooltipComponent> either = tooltipCopy.get(i);
				if (either.left().isPresent() && either.left().get() instanceof MutableComponent component) {
					String s = either.left().get().getString();
					if (component.getContents() instanceof TranslatableContents translatableContents && translatableContents.getKey().contains("container.shulkerBox"))
						tooltip.remove(either);
				}
			}

			if (!ImprovedTooltipsModule.shulkerBoxRequireShift || Screen.hasShiftDown())
				tooltip.add(1, Either.right(new ShulkerComponent(stack)));
			if (ImprovedTooltipsModule.shulkerBoxRequireShift && !Screen.hasShiftDown())
				tooltip.add(1, Either.left(Component.translatable("quark.misc.shulker_box_shift")));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public record ToolboxComponent(ItemStack stack) implements ClientTooltipComponent, TooltipComponent {

		@Override
		public void renderImage(@Nonnull Font font, int tooltipX, int tooltipY, @NotNull GuiGraphics guiGraphics) {
			new ShulkerBoxTooltips.ShulkerComponent(this.stack).renderImage(font, tooltipX, tooltipY, guiGraphics);
		}

		public int getHeight() {
			return 29;
		}

		public int getWidth(@Nonnull Font font) {
			return 171;
		}
	}
}