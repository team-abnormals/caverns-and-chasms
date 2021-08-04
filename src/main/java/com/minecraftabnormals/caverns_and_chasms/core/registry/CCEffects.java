package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.minecraftabnormals.caverns_and_chasms.common.potion.AfflictionEffect;
import com.minecraftabnormals.caverns_and_chasms.common.potion.RewindEffect;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCEffects {
	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, CavernsAndChasms.MOD_ID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<Effect> REWIND = EFFECTS.register("rewind", RewindEffect::new);
	public static final RegistryObject<Effect> AFFLICTION = EFFECTS.register("affliction", AfflictionEffect::new);

	public static final RegistryObject<Potion> REWIND_NORMAL = POTIONS.register("rewind", () -> new Potion(new EffectInstance(REWIND.get(), 400)));
	public static final RegistryObject<Potion> REWIND_LONG = POTIONS.register("rewind_long", () -> new Potion(new EffectInstance(REWIND.get(), 800)));

	public static void registerBrewingRecipes() {
		DataUtil.addMix(Potions.AWKWARD, CCItems.SUGILITE.get(), REWIND_NORMAL.get());
		DataUtil.addMix(REWIND_NORMAL.get(), Items.REDSTONE, REWIND_LONG.get());
	}
}