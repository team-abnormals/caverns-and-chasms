package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.common.effect.BlueprintMobEffect;
import com.teamabnormals.caverns_and_chasms.common.effect.VampirismMobEffect;
import com.teamabnormals.caverns_and_chasms.common.recipe.SubtleBrewingRecipe;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCMobEffects {
	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, CavernsAndChasms.MOD_ID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<MobEffect, MobEffect> SUBTLE = MOB_EFFECTS.register("subtle", () -> new BlueprintMobEffect(MobEffectCategory.BENEFICIAL, 0xCE65B4));

	public static final DeferredHolder<MobEffect, MobEffect> REWIND = MOB_EFFECTS.register("rewind", () -> new BlueprintMobEffect(MobEffectCategory.BENEFICIAL, 0xCE65B4));
	public static final DeferredHolder<MobEffect, MobEffect> VAMPIRISM = MOB_EFFECTS.register("vampirism", VampirismMobEffect::new);

	public static final DeferredHolder<Potion, Potion> REWIND_NORMAL = POTIONS.register("rewind", () -> new Potion("rewind", new MobEffectInstance(REWIND, 400)));
	public static final DeferredHolder<Potion, Potion> REWIND_LONG = POTIONS.register("long_rewind", () -> new Potion("rewind", new MobEffectInstance(REWIND, 800)));

	public static final DeferredHolder<Potion, Potion> BLINDNESS = POTIONS.register("blindness", () -> new Potion("blindness", new MobEffectInstance(MobEffects.BLINDNESS, 3600)));
	public static final DeferredHolder<Potion, Potion> BLINDNESS_LONG = POTIONS.register("long_blindness", () -> new Potion("blindness", new MobEffectInstance(MobEffects.BLINDNESS, 9600)));

	public static final DeferredHolder<Potion, Potion> REVENANT = POTIONS.register("revenant", () -> new Potion("revenant", new MobEffectInstance(VAMPIRISM, 1800)));
	public static final DeferredHolder<Potion, Potion> LONG_REVENANT = POTIONS.register("long_revenant", () -> new Potion("revenant", new MobEffectInstance(VAMPIRISM, 4800)));
	public static final DeferredHolder<Potion, Potion> STRONG_REVENANT = POTIONS.register("strong_revenant", () -> new Potion("revenant", new MobEffectInstance(VAMPIRISM, 400, 1)));

	@SubscribeEvent
	public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
		PotionBrewing.Builder builder = event.getBuilder();

		builder.addContainer(CCItems.TETHER_POTION.get());
		builder.addContainerRecipe(Items.POTION, CCItems.SPINEL.get(), CCItems.TETHER_POTION.get());

		builder.addContainer(CCItems.IMPACT_POTION.get());
		builder.addContainerRecipe(Items.SPLASH_POTION, CCItems.SPINEL.get(), CCItems.IMPACT_POTION.get());
		builder.addContainerRecipe(CCItems.TETHER_POTION.get(), Items.GUNPOWDER, CCItems.IMPACT_POTION.get());

		builder.addContainer(CCItems.TRAIL_POTION.get());
		builder.addContainerRecipe(Items.LINGERING_POTION, CCItems.SPINEL.get(), CCItems.TRAIL_POTION.get());
		builder.addContainerRecipe(CCItems.IMPACT_POTION.get(), Items.DRAGON_BREATH, CCItems.TRAIL_POTION.get());

		builder.addMix(Potions.AWKWARD, CCItems.BEJEWELED_PEARL.get(), REWIND_NORMAL);
		builder.addMix(REWIND_NORMAL, Items.REDSTONE, REWIND_LONG);

		builder.addMix(Potions.AWKWARD, CCItems.CAVEFISH.get(), BLINDNESS);
		builder.addMix(BLINDNESS, Items.REDSTONE, BLINDNESS_LONG);

		builder.addMix(Potions.AWKWARD, CCItems.LIVING_FLESH.get(), REVENANT);
		builder.addMix(REVENANT, Items.REDSTONE, LONG_REVENANT);
		builder.addMix(REVENANT, Items.GLOWSTONE_DUST, STRONG_REVENANT);

		builder.addRecipe(new SubtleBrewingRecipe());
	}
}