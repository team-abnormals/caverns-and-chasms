package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.item.CCArmorTrim;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ArmorTrim.class)
public class ArmorTrimMixin implements CCArmorTrim {

	@Unique
	private boolean caverns_and_chasms$isFaded = false;

	@Unique
	private boolean caverns_and_chasms$isEmissive = false;

	@Override
	public boolean isFaded() {
		return this.caverns_and_chasms$isFaded;
	}

	@Override
	public void setFaded(boolean faded) {
		this.caverns_and_chasms$isFaded = faded;
	}

	@Override
	public boolean isEmissive() {
		return this.caverns_and_chasms$isEmissive;
	}

	@Override
	public void setEmissive(boolean emissive) {
		this.caverns_and_chasms$isEmissive = emissive;
	}

	@ModifyVariable(method = "getTrim", at = @At("STORE"))
	private static ArmorTrim getTrim(ArmorTrim trim, RegistryAccess access, ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		CCArmorTrim armorTrim = (CCArmorTrim) trim;
		if (tag.getBoolean("EmissiveTrim")) {
			armorTrim.setEmissive(true);
		} else if (tag.getBoolean("FadedTrim")) {
			armorTrim.setFaded(true);
		}
		return trim;
	}

	@Inject(at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2, shift = Shift.AFTER), method = "appendUpgradeHoverText")
	private static void appendHoverText(ItemStack stack, RegistryAccess access, List<Component> tooltip, CallbackInfo ci) {
		Style style = ArmorTrim.getTrim(access, stack).get().material().value().description().getStyle();

		if (stack.getOrCreateTag().getBoolean("EmissiveTrim")) {
			tooltip.add(CommonComponents.space().append(Component.translatable("tooltip." + CavernsAndChasms.MOD_ID + ".emissive").withStyle(style)));
		}

		if (stack.getOrCreateTag().getBoolean("FadedTrim")) {
			tooltip.add(CommonComponents.space().append(Component.translatable("tooltip." + CavernsAndChasms.MOD_ID + ".faded").withStyle(style)));
		}
	}
}
