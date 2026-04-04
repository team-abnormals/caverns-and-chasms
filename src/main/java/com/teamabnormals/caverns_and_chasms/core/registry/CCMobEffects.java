package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.common.effect.BlueprintMobEffect;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.common.effect.VampirismMobEffect;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
	public static final RegistryObject<MobEffect> VAMPIRISM = MOB_EFFECTS.register("vampirism", VampirismMobEffect::new);

	public static final RegistryObject<Potion> REWIND_NORMAL = POTIONS.register("rewind", () -> new Potion("rewind", new MobEffectInstance(REWIND.get(), 400)));
	public static final RegistryObject<Potion> REWIND_LONG = POTIONS.register("long_rewind", () -> new Potion("rewind", new MobEffectInstance(REWIND.get(), 800)));

	public static final RegistryObject<Potion> BLINDNESS = POTIONS.register("blindness", () -> new Potion("blindness", new MobEffectInstance(MobEffects.BLINDNESS, 3600)));
	public static final RegistryObject<Potion> BLINDNESS_LONG = POTIONS.register("long_blindness", () -> new Potion("blindness", new MobEffectInstance(MobEffects.BLINDNESS, 9600)));

	public static final RegistryObject<Potion> REVENANT = POTIONS.register("revenant", () -> new Potion("revenant", new MobEffectInstance(VAMPIRISM.get(), 1800)));
	public static final RegistryObject<Potion> LONG_REVENANT = POTIONS.register("long_revenant", () -> new Potion("revenant", new MobEffectInstance(VAMPIRISM.get(), 4800)));
	public static final RegistryObject<Potion> STRONG_REVENANT = POTIONS.register("strong_revenant", () -> new Potion("revenant", new MobEffectInstance(VAMPIRISM.get(), 400, 1)));

	public static void registerBrewingRecipes() {
		PotionBrewing.addContainer(CCItems.TETHER_POTION.get());
		PotionBrewing.addContainerRecipe(Items.POTION, CCItems.SPINEL.get(), CCItems.TETHER_POTION.get());

		PotionBrewing.addContainer(CCItems.IMPACT_POTION.get());
		PotionBrewing.addContainerRecipe(Items.SPLASH_POTION, CCItems.SPINEL.get(), CCItems.IMPACT_POTION.get());
		PotionBrewing.addContainerRecipe(CCItems.TETHER_POTION.get(), Items.GUNPOWDER, CCItems.IMPACT_POTION.get());

		PotionBrewing.addContainer(CCItems.TRAIL_POTION.get());
		PotionBrewing.addContainerRecipe(Items.LINGERING_POTION, CCItems.SPINEL.get(), CCItems.TRAIL_POTION.get());
		PotionBrewing.addContainerRecipe(CCItems.IMPACT_POTION.get(), Items.DRAGON_BREATH, CCItems.TRAIL_POTION.get());

		PotionBrewing.addContainerRecipe(Items.POTION, CCItems.TURQUOISE.get(), Items.POTION);
		PotionBrewing.addContainerRecipe(Items.SPLASH_POTION, CCItems.TURQUOISE.get(), Items.SPLASH_POTION);
		PotionBrewing.addContainerRecipe(Items.LINGERING_POTION, CCItems.TURQUOISE.get(), Items.LINGERING_POTION);
		PotionBrewing.addContainerRecipe(CCItems.TETHER_POTION.get(), CCItems.TURQUOISE.get(), CCItems.TETHER_POTION.get());
		PotionBrewing.addContainerRecipe(CCItems.IMPACT_POTION.get(), CCItems.TURQUOISE.get(), CCItems.IMPACT_POTION.get());
		PotionBrewing.addContainerRecipe(CCItems.TRAIL_POTION.get(), CCItems.TURQUOISE.get(), CCItems.TRAIL_POTION.get());

		DataUtil.addMix(Potions.AWKWARD, CCItems.BEJEWELED_PEARL.get(), REWIND_NORMAL.get());
		DataUtil.addMix(REWIND_NORMAL.get(), Items.REDSTONE, REWIND_LONG.get());

		DataUtil.addMix(Potions.AWKWARD, CCItems.CAVEFISH.get(), BLINDNESS.get());
		DataUtil.addMix(BLINDNESS.get(), Items.REDSTONE, BLINDNESS_LONG.get());

		DataUtil.addMix(Potions.AWKWARD, CCItems.LIVING_FLESH.get(), REVENANT.get());
		DataUtil.addMix(REVENANT.get(), Items.REDSTONE, LONG_REVENANT.get());
		DataUtil.addMix(REVENANT.get(), Items.GLOWSTONE_DUST, STRONG_REVENANT.get());
	}
}