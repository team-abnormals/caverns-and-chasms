package com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory;

import com.teamabnormals.caverns_and_chasms.common.inventory.DismantlingMenu;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class DismantlingScreen extends CCItemCombinerScreen<DismantlingMenu> {
	private static final ResourceLocation SMITHING_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/gui/container/dismantling.png");
	private static final ResourceLocation EMPTY_SLOT_SMITHING_TEMPLATE_ARMOR_TRIM = new ResourceLocation("item/empty_slot_smithing_template_armor_trim");
	private static final ResourceLocation EMPTY_SLOT_SMITHING_TEMPLATE_NETHERITE_UPGRADE = new ResourceLocation("item/empty_slot_smithing_template_netherite_upgrade");
	private static final ResourceLocation EMPTY_SLOT_SPINEL = new ResourceLocation(CavernsAndChasms.MOD_ID, "item/empty_slot_spinel");

	private static final Component MISSING_SMITHED_ITEM = Component.translatable("container.caverns_and_chasms.dismantle.missing_smithed_item");
	private static final Component MISSING_SPINEL = Component.translatable("container.caverns_and_chasms.dismantle.missing_spinel");
	private static final Component RESULT_ERROR_TOOLTIP = Component.translatable("container.caverns_and_chasms.dismantle.result_error_tooltip");
	private static final Component DURABILITY_ERROR_TOOLTIP = Component.translatable("container.caverns_and_chasms.dismantle.durability_error_tooltip");
	private static final List<ResourceLocation> EMPTY_SLOT_SMITHING_TEMPLATES = List.of(EMPTY_SLOT_SMITHING_TEMPLATE_ARMOR_TRIM, EMPTY_SLOT_SMITHING_TEMPLATE_NETHERITE_UPGRADE);
	public static final Quaternionf ARMOR_STAND_ANGLE = (new Quaternionf()).rotationXYZ(0.43633232F, 0.0F, (float) Math.PI);
	private final CyclingSlotBackground smithingIcon = new CyclingSlotBackground(0);
	private final CyclingSlotBackground spinelIcon = new CyclingSlotBackground(1);
	private final CyclingSlotBackground templateIcon = new CyclingSlotBackground(2);
	@Nullable
	private ArmorStand armorStandPreview;

	public DismantlingScreen(DismantlingMenu p_99290_, Inventory p_99291_, Component p_99292_) {
		super(p_99290_, p_99291_, p_99292_, SMITHING_LOCATION);
		this.titleLabelX = 44;
		this.titleLabelY = 15;
	}

	protected void subInit() {
		this.armorStandPreview = new ArmorStand(this.minecraft.level, 0.0D, 0.0D, 0.0D);
		this.armorStandPreview.setNoBasePlate(true);
		this.armorStandPreview.setShowArms(true);
		this.armorStandPreview.yBodyRot = 210.0F;
		this.armorStandPreview.setXRot(25.0F);
		this.armorStandPreview.yHeadRot = this.armorStandPreview.getYRot();
		this.armorStandPreview.yHeadRotO = this.armorStandPreview.getYRot();
		this.updateArmorStandPreview(this.menu.getSlot(3).getItem());
	}

	public void containerTick() {
		super.containerTick();
		this.smithingIcon.tick(((SmithingTemplateItem) Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).getBaseSlotEmptyIcons());
		this.spinelIcon.tick(List.of(EMPTY_SLOT_SPINEL));
		this.templateIcon.tick(EMPTY_SLOT_SMITHING_TEMPLATES);
	}

	public void render(GuiGraphics p_281961_, int p_282410_, int p_283013_, float p_282408_) {
		super.render(p_281961_, p_282410_, p_283013_, p_282408_);
		this.renderOnboardingTooltips(p_281961_, p_282410_, p_283013_);
	}

	protected void renderBg(GuiGraphics p_283264_, float p_267158_, int p_267266_, int p_266722_) {
		super.renderBg(p_283264_, p_267158_, p_267266_, p_266722_);
		this.smithingIcon.render(this.menu, p_283264_, p_267158_, this.leftPos, this.topPos);
		this.spinelIcon.render(this.menu, p_283264_, p_267158_, this.leftPos, this.topPos);
		InventoryScreen.renderEntityInInventory(p_283264_, this.leftPos + 153, this.topPos + 75, 25, ARMOR_STAND_ANGLE, null, this.armorStandPreview);
	}

	public void slotChanged(AbstractContainerMenu p_267217_, int p_266842_, ItemStack p_267208_) {
		if (p_266842_ == 3) {
			this.updateArmorStandPreview(p_267208_);
		}
	}

	private void updateArmorStandPreview(ItemStack p_268225_) {
		if (this.armorStandPreview != null) {
			for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
				this.armorStandPreview.setItemSlot(equipmentslot, ItemStack.EMPTY);
			}

			if (!p_268225_.isEmpty()) {
				ItemStack itemstack = p_268225_.copy();
				Item item = p_268225_.getItem();
				if (item instanceof ArmorItem) {
					ArmorItem armoritem = (ArmorItem) item;
					this.armorStandPreview.setItemSlot(armoritem.getEquipmentSlot(), itemstack);
				} else {
					this.armorStandPreview.setItemSlot(EquipmentSlot.OFFHAND, itemstack);
				}
			}

		}
	}

	protected void renderErrorIcon(GuiGraphics p_281835_, int p_283389_, int p_282634_) {
		if (this.hasRecipeError()) {
			p_281835_.blit(SMITHING_LOCATION, p_283389_ + 47, p_282634_ + 46, this.imageWidth, 0, 28, 21);
		}
	}

	private void renderOnboardingTooltips(GuiGraphics p_281668_, int p_267192_, int p_266859_) {
		Optional<Component> optional = Optional.empty();
		if (this.hasRecipeError() && this.isHovering(47, 46, 28, 21, p_267192_, p_266859_)) {
			optional = Optional.of(this.stillHasResults() ? RESULT_ERROR_TOOLTIP : DURABILITY_ERROR_TOOLTIP);
		}

		if (this.hoveredSlot != null) {
			ItemStack itemstack = this.menu.getSlot(0).getItem();
			ItemStack itemstack1 = this.menu.getSlot(1).getItem();
			if (itemstack.isEmpty() && this.hoveredSlot.index == 0) {
				optional = Optional.of(MISSING_SMITHED_ITEM);
			} else if (itemstack1.isEmpty() && this.hoveredSlot.index == 1) {
				optional = Optional.of(MISSING_SPINEL);
			}
		}

		optional.ifPresent((p_280863_) -> {
			p_281668_.renderTooltip(this.font, this.font.split(p_280863_, 115), p_267192_, p_266859_);
		});
	}

	private boolean hasRecipeError() {
		return this.stillHasResults() || this.menu.durabilityError();
	}

	private boolean stillHasResults() {
		return !this.menu.areResultsFull() && !this.menu.areResultsEmpty();
	}


}