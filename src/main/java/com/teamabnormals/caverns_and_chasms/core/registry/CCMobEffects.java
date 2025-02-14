package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.common.effect.BlueprintMobEffect;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCMobEffects {
	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CavernsAndChasms.MOD_ID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<MobEffect> REWIND = MOB_EFFECTS.register("rewind", () -> new BlueprintMobEffect(MobEffectCategory.BENEFICIAL, 0xCE65B4));

	public static final RegistryObject<Potion> REWIND_NORMAL = POTIONS.register("rewind", () -> new Potion("rewind", new MobEffectInstance(REWIND.get(), 400)));
	public static final RegistryObject<Potion> REWIND_LONG = POTIONS.register("long_rewind", () -> new Potion("rewind", new MobEffectInstance(REWIND.get(), 800)));

	public static void registerBrewingRecipes() {
		PotionBrewing.addContainer(CCItems.TETHER_POTION.get());
		PotionBrewing.addContainerRecipe(Items.POTION, CCItems.SPINEL.get(), CCItems.TETHER_POTION.get());

		PotionBrewing.addContainer(CCItems.IMPACT_POTION.get());
		PotionBrewing.addContainerRecipe(Items.SPLASH_POTION, CCItems.SPINEL.get(), CCItems.IMPACT_POTION.get());

		PotionBrewing.addContainer(CCItems.TRAIL_POTION.get());
		PotionBrewing.addContainerRecipe(Items.LINGERING_POTION, CCItems.SPINEL.get(), CCItems.TRAIL_POTION.get());

		PotionBrewing.addContainerRecipe(Items.POTION, CCItems.TURQUOISE.get(), Items.POTION);
		PotionBrewing.addContainerRecipe(Items.SPLASH_POTION, CCItems.TURQUOISE.get(), Items.SPLASH_POTION);
		PotionBrewing.addContainerRecipe(Items.LINGERING_POTION, CCItems.TURQUOISE.get(), Items.LINGERING_POTION);
		PotionBrewing.addContainerRecipe(CCItems.TETHER_POTION.get(), CCItems.TURQUOISE.get(), CCItems.TETHER_POTION.get());
		PotionBrewing.addContainerRecipe(CCItems.IMPACT_POTION.get(), CCItems.TURQUOISE.get(), CCItems.IMPACT_POTION.get());
		PotionBrewing.addContainerRecipe(CCItems.TRAIL_POTION.get(), CCItems.TURQUOISE.get(), CCItems.TRAIL_POTION.get());

		DataUtil.addMix(Potions.AWKWARD, CCItems.BEJEWELED_PEARL.get(), REWIND_NORMAL.get());
		DataUtil.addMix(REWIND_NORMAL.get(), Items.REDSTONE, REWIND_LONG.get());
	}
}