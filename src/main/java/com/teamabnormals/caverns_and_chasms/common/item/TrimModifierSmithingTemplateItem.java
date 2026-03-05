package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class TrimModifierSmithingTemplateItem extends SmithingTemplateItem {
	private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
	private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;

	private static final Component TRIM_MODIFIER = Component.translatable(Util.makeDescriptionId("upgrade", CavernsAndChasms.location("trim_modifier"))).withStyle(TITLE_FORMAT);
	private static final Component TRIM_MODIFIER_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", CavernsAndChasms.location("smithing_template.trim_modifier.applies_to"))).withStyle(DESCRIPTION_FORMAT);
	private static final Component TRIM_MODIFIER_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", CavernsAndChasms.location("smithing_template.trim_modifier.ingredients"))).withStyle(DESCRIPTION_FORMAT);
	private static final Component TRIM_MODIFIER_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", CavernsAndChasms.location("smithing_template.trim_modifier.base_slot_description")));
	private static final Component TRIM_MODIFIER_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", CavernsAndChasms.location("smithing_template.trim_modifier.additions_slot_description")));

	public static final ResourceLocation EMPTY_SLOT_SMITHING_TEMPLATE_TRIM_MODIFIER = CavernsAndChasms.location("item/empty_slot_smithing_template_trim_modifier");

	private static final ResourceLocation EMPTY_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
	private static final ResourceLocation EMPTY_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
	private static final ResourceLocation EMPTY_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
	private static final ResourceLocation EMPTY_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");

	public static final ResourceLocation EMPTY_SLOT_SPINEL = CavernsAndChasms.location("item/empty_slot_spinel");
	public static final ResourceLocation EMPTY_SLOT_TURQUOISE = CavernsAndChasms.location("item/empty_slot_turquoise");
	public static final ResourceLocation EMPTY_SLOT_LIVING_FLESH = CavernsAndChasms.location("item/empty_slot_living_flesh");
	public static final ResourceLocation EMPTY_SLOT_ZIRCONIA = CavernsAndChasms.location("item/empty_slot_zirconia");

	private static final ResourceLocation EMPTY_SLOT_GLOW_INK_SAC = CavernsAndChasms.location("item/empty_slot_glow_ink_sac");

	public TrimModifierSmithingTemplateItem(Component p_266834_, Component p_267043_, Component p_267048_, Component p_267278_, Component p_267090_, List<ResourceLocation> p_266755_, List<ResourceLocation> p_267060_) {
		super(p_266834_, p_267043_, p_267048_, p_267278_, p_267090_, p_266755_, p_267060_);
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return CCItems.FANCY;
	}

	public static TrimModifierSmithingTemplateItem createTrimModifierTemplate() {
		return new TrimModifierSmithingTemplateItem(TRIM_MODIFIER_APPLIES_TO, TRIM_MODIFIER_INGREDIENTS, TRIM_MODIFIER, TRIM_MODIFIER_BASE_SLOT_DESCRIPTION, TRIM_MODIFIER_ADDITIONS_SLOT_DESCRIPTION, createTrimModifierIconList(), createTrimModifierMaterialList());
	}

	private static List<ResourceLocation> createTrimModifierIconList() {
		return List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_BOOTS);
	}

	private static List<ResourceLocation> createTrimModifierMaterialList() {
		return List.of(EMPTY_SLOT_SPINEL, EMPTY_SLOT_GLOW_INK_SAC);
	}

	public static float getBothAlpha() {
		return 0.2F;
	}

	public static float getFadedAlpha() {
		return 0.4F;
	}

	public static float getEmissiveAlpha() {
		return 0.6F;
	}
}
