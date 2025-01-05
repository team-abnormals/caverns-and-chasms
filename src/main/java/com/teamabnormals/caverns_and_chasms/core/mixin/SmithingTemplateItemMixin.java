package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SmithingTemplateItem.class)
public abstract class SmithingTemplateItemMixin extends Item {
	@Shadow
	public abstract String getDescriptionId();

	@Unique
	private static final String EMISSIVE_DESCRIPTION_ID = Util.makeDescriptionId("item", new ResourceLocation(CavernsAndChasms.MOD_ID, "emissive_smithing_template"));

	@Unique
	private static final String FADED_DESCRIPTION_ID = Util.makeDescriptionId("item", new ResourceLocation(CavernsAndChasms.MOD_ID, "faded_smithing_template"));

	public SmithingTemplateItemMixin(Properties properties) {
		super(properties);
	}

	@Inject(at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, shift = Shift.AFTER), method = "appendHoverText")
	private void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag, CallbackInfo ci) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.getBoolean("EmissiveTrim")) {
			tooltip.add(Component.translatable("tooltip." + CavernsAndChasms.MOD_ID + ".emissive").withStyle(ChatFormatting.GRAY));
		} else if (tag.getBoolean("FadedTrim")) {
			tooltip.add(Component.translatable("tooltip." + CavernsAndChasms.MOD_ID + ".faded").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.getBoolean("EmissiveTrim") ? EMISSIVE_DESCRIPTION_ID : tag.getBoolean("FadedTrim") ? FADED_DESCRIPTION_ID : this.getDescriptionId();
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.getBoolean("EmissiveTrim") || tag.getBoolean("FadedTrim") || super.isFoil(stack);
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.getBoolean("EmissiveTrim") ? Rarity.RARE : tag.getBoolean("FadedTrim") ? Rarity.EPIC : super.getRarity(stack);

	}
}