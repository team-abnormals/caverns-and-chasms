package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.UnaryOperator;

public class CCEnums {
	public static final EnumProxy<MobCategory> UNDERGROUND_AMBIENT = new EnumProxy<>(MobCategory.class, CavernsAndChasms.MOD_ID + ":underground_ambient", 16, true, false, 128);
	public static final EnumProxy<MobCategory> UNDERGROUND_WATER_AMBIENT = new EnumProxy<>(MobCategory.class, CavernsAndChasms.MOD_ID + ":underground_water_ambient", 20, true, false, 128);
	public static final EnumProxy<MobCategory> LOST_GOAT = new EnumProxy<>(MobCategory.class, CavernsAndChasms.MOD_ID + ":lost_goat", 1, false, false, 128);

	public static final EnumProxy<Rarity> FANCY = new EnumProxy<>(Rarity.class, -1, CavernsAndChasms.MOD_ID + ":fancy", (UnaryOperator<Style>) style -> style.withColor(0x2BFF75));
}