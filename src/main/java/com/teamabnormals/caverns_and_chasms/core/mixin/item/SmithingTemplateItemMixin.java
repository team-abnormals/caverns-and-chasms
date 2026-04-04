package com.teamabnormals.caverns_and_chasms.core.mixin.item;

import com.teamabnormals.caverns_and_chasms.common.item.TrimModifierSmithingTemplateItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SmithingTemplateItem.class)
public class SmithingTemplateItemMixin {

	@Inject(method = "createTrimmableMaterialIconList", at = @At(value = "RETURN", target = "Lnet/minecraft/world/item/armortrim/ArmorTrim;setTrim(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/armortrim/ArmorTrim;)Z"), cancellable = true)
	private static void createTrimmableMaterialIconList(CallbackInfoReturnable<List<ResourceLocation>> cir) {
		List<ResourceLocation> list = new ArrayList<>(cir.getReturnValue());
		list.addAll(List.of(
				TrimModifierSmithingTemplateItem.EMPTY_SLOT_SPINEL,
				TrimModifierSmithingTemplateItem.EMPTY_SLOT_TURQUOISE,
				TrimModifierSmithingTemplateItem.EMPTY_SLOT_ZIRCONIA,
				TrimModifierSmithingTemplateItem.EMPTY_SLOT_LIVING_FLESH
		));
		cir.setReturnValue(list);
	}
}