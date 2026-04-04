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
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.client.module.ChestSearchingModule;
import org.violetmoon.quark.content.client.module.ImprovedTooltipsModule;
import org.violetmoon.quark.content.client.tooltip.ShulkerBoxTooltips.ShulkerComponent;
import org.violetmoon.zeta.util.ItemNBTHelper;

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
		private static final int[][] TARGET_RATIOS = new int[][]{{1, 1}, {9, 3}, {9, 5}, {9, 6}, {9, 8}, {9, 9}, {12, 9}};

		@Override
		public void renderImage(@NotNull Font font, int tooltipX, int tooltipY, @NotNull GuiGraphics guiGraphics) {
			Minecraft mc = Minecraft.getInstance();
			PoseStack pose = guiGraphics.pose();
			CompoundTag cmp = ItemNBTHelper.getCompound(this.stack, "BlockEntityTag", true);
			if (cmp != null) {
				if (cmp.contains("LootTable")) {
					return;
				}

				if (!cmp.contains("id")) {
					cmp = cmp.copy();
					cmp.putString("id", "caverns_and_chasms:toolbox");
				}

				BlockEntity te = BlockEntity.loadStatic(BlockPos.ZERO, ((BlockItem) this.stack.getItem()).getBlock().defaultBlockState(), cmp);
				if (te != null) {
					if (te instanceof RandomizableContainerBlockEntity randomizable) {
						randomizable.setLootTable(null, 0L);
					}

					LazyOptional<IItemHandler> handler = te.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
					handler.ifPresent((capability) -> {
						int currentX = tooltipX;
						int currentY = tooltipY - 1;
						int size = capability.getSlots();
						int[] dims = new int[]{Math.min(size, 7), Math.max(size / 7, 1)};

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

						for (int i = 0; i < size; ++i) {
							ItemStack itemstack = capability.getStackInSlot(i);
							int xp = currentX + 6 + i % 7 * 18;
							int yp = currentY + 6 + i / 7 * 18;
							if (!itemstack.isEmpty()) {
								guiGraphics.renderItem(itemstack, xp, yp);
								guiGraphics.renderItemDecorations(mc.font, itemstack, xp, yp);
							}

							if (!Quark.ZETA.modules.get(ChestSearchingModule.class).namesMatch(itemstack)) {
								RenderSystem.disableDepthTest();
								guiGraphics.fill(xp, yp, xp + 16, yp + 16, -1442840576);
							}
						}

						pose.popPose();
					});
				}
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