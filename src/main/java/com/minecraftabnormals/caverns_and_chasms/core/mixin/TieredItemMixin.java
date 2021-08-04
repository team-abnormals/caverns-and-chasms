package com.minecraftabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(TieredItem.class)
public abstract class TieredItemMixin extends Item {

	@Shadow
	IItemTier getTier() {
		return null;
	}

	public TieredItemMixin(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (this.getTier() == ItemTier.GOLD)
			tooltip.add(new TranslationTextComponent("tooltip.caverns_and_chasms.boosting").withStyle(TextFormatting.GRAY));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
}
