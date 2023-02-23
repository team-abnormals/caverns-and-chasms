package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class NetheriteHorseArmorItem extends HorseArmorItem {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.DIAMOND_HORSE_ARMOR);
	private final float knockbackResistance;

	public NetheriteHorseArmorItem(int armorValue, float knockbackResistance, String tierArmor, Properties builder) {
		super(armorValue, new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/horse/armor/horse_armor_" + tierArmor + ".png"), builder);
		this.knockbackResistance = knockbackResistance;
	}

	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}