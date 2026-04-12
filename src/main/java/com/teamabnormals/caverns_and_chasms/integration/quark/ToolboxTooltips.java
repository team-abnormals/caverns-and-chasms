package com.teamabnormals.caverns_and_chasms.integration.quark;


import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import net.minecraft.client.Minecraft;
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
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.client.module.ChestSearchingModule;
import org.violetmoon.quark.content.client.module.ImprovedTooltipsModule;
import org.violetmoon.quark.content.client.tooltip.ShulkerBoxTooltips.ShulkerComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
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
		private static final int[][] TARGET_RATIOS = new int[][]{{1, 1}, {9, 3}, {9, 5}, {9, 6}, {9, 8}, {9, 9}, {12, 9}};

		@Override
		public void renderImage(@NotNull Font font, int tooltipX, int tooltipY, @NotNull GuiGraphics guiGraphics) {
			Minecraft mc = Minecraft.getInstance();
			PoseStack pose = guiGraphics.pose();
			if (this.stack.has(DataComponents.CONTAINER)) {
				ItemContainerContents contents = this.stack.get(DataComponents.CONTAINER);
				int currentX = tooltipX;
				int currentY = tooltipY - 1;
				int size = Math.toIntExact(contents.nonEmptyStream().count());
				int[] dims = new int[]{Math.min(size, 7), 1 + (size - 1) / 7};

				for (int[] testAgainst : TARGET_RATIOS) {
					if (testAgainst[0] * testAgainst[1] == size) {
						dims = testAgainst;
						break;
					}
				}

				int texWidth = 10 + 18 * dims[0];
				int right = tooltipX + texWidth;
				Window window = mc.getWindow();
				if (right > window.getGuiScaledWidth()) {
					currentX = tooltipX - (right - window.getGuiScaledWidth());
				}

				pose.pushPose();
				pose.translate(0.0F, 0.0F, 700.0F);
				int color = -1;

				ShulkerComponent.renderTooltipBackground(guiGraphics, mc, pose, currentX, currentY, dims[0], dims[1], color);
				Iterator<ItemStack> stackIterator = contents.nonEmptyItems().iterator();

				for (int i = 0; i < size; ++i) {
					ItemStack itemstack = stackIterator.next();
					int xp = currentX + 6 + i % 9 * 18;
					int yp = currentY + 6 + i / 9 * 18;
					guiGraphics.renderItem(itemstack, xp, yp);
					guiGraphics.renderItemDecorations(mc.font, itemstack, xp, yp);
					if (!Quark.ZETA.modules.get(ChestSearchingModule.class).namesMatch(itemstack)) {
						RenderSystem.disableDepthTest();
						guiGraphics.fill(xp, yp, xp + 16, yp + 16, -1442840576);
						RenderSystem.enableDepthTest();
					}
				}

				pose.popPose();
			}

		}

		public int getHeight() {
			return 47;
		}

		public int getWidth(@Nonnull Font font) {
			return 135;
		}
	}
}