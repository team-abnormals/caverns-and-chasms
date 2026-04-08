package com.teamabnormals.caverns_and_chasms.core.mixin.item;

import com.teamabnormals.caverns_and_chasms.common.item.CCArmorTrim;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ArmorTrim.class)
public class ArmorTrimMixin implements CCArmorTrim {

	@Unique
	private boolean caverns_and_chasms$isFaded = false;

	@Unique
	private boolean caverns_and_chasms$isEmissive = false;

	@Unique
	private boolean caverns_and_chasms$isPulse = false;

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

	@Override
	public boolean isPulse() {
		return this.caverns_and_chasms$isPulse;
	}

	@Override
	public void setPulse(boolean pulse) {
		this.caverns_and_chasms$isPulse = pulse;
	}

	@ModifyVariable(method = "getTrim", at = @At("STORE"))
	private static ArmorTrim getTrim(ArmorTrim trim, RegistryAccess access, ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		CCArmorTrim armorTrim = (CCArmorTrim) trim;
		armorTrim.setFaded(tag.getBoolean("FadedTrim"));
		armorTrim.setEmissive(tag.getBoolean("EmissiveTrim"));
		armorTrim.setPulse(tag.getBoolean("PulseTrim"));
		return trim;
	}

	@Inject(at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 2, shift = Shift.AFTER), method = "addToTooltip")
	private void appendHoverText(TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag, CallbackInfo ci) {
		Style style = ArmorTrim.getTrim(access, stack).get().material().value().description().getStyle();

		if (stack.has(CCDataComponents.FADED_TRIM)) {
			tooltipAdder.accept(CommonComponents.space().append(Component.translatable("tooltip." + CavernsAndChasms.MOD_ID + ".faded_modifier").withStyle(style)));
		}

		if (stack.has(CCDataComponents.EMISSIVE_TRIM)) {
			tooltipAdder.accept(CommonComponents.space().append(Component.translatable("tooltip." + CavernsAndChasms.MOD_ID + ".emissive_modifier").withStyle(style)));
		}

		if (stack.has(CCDataComponents.PULSE_TRIM)) {
			tooltipAdder.accept(CommonComponents.space().append(Component.translatable("tooltip." + CavernsAndChasms.MOD_ID + ".pulse_modifier").withStyle(style)));
		}
	}
}
