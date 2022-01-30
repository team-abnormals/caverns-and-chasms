package com.teamabnormals.caverns_and_chasms.core.data.server.modifiers;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.advancement.modification.AdvancementModifiers;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.CriteriaModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.DisplayInfoModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.EffectsChangedModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.IAdvancementModifier.Mode;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.IndexedRequirementsModifier;
import com.teamabnormals.blueprint.core.util.modification.ConfiguredModifier;
import com.teamabnormals.blueprint.core.util.modification.ModifierDataProvider;
import com.teamabnormals.blueprint.core.util.modification.TargetedModifier;
import com.teamabnormals.blueprint.core.util.modification.targeting.ConditionedModifierTargetSelector;
import com.teamabnormals.blueprint.core.util.modification.targeting.ModifierTargetSelectorRegistry;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMobEffects;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.advancements.critereon.ItemUsedOnBlockTrigger.TriggerInstance;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Items;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CCAdvancementModifiersProvider {
	public static ModifierDataProvider<Advancement.Builder, Void, DeserializationContext> createDataProvider(DataGenerator dataGenerator) {
		return AdvancementModifiers.createDataProvider(dataGenerator, "Advancement Modifiers", CavernsAndChasms.MOD_ID,
				createModifierEntry("nether/all_effects", Collections.singletonList(createEffectsChangedModifier(CCMobEffects.REWIND.get()))),
				createModifierEntry("nether/all_potions", Collections.singletonList(createEffectsChangedModifier(CCMobEffects.REWIND.get()))),

				createModifierEntry("husbandry/balanced_diet", Collections.singletonList(createCriteriaModifier(Mode.MODIFY, Arrays.asList(
						Pair.of("cavefish", new Criterion(ConsumeItemTrigger.TriggerInstance.usedItem(CCItems.CAVEFISH.get()))),
						Pair.of("cooked_cavefish", new Criterion(ConsumeItemTrigger.TriggerInstance.usedItem(CCItems.COOKED_CAVEFISH.get()))))))),

				createModifierEntry("husbandry/fishy_business", Collections.singletonList(createIndexedRequirementsModifier(0, Collections.singletonList(
						Pair.of("cavefish", new Criterion(FishingRodHookedTrigger.TriggerInstance.fishedItem(ItemPredicate.ANY, EntityPredicate.ANY, ItemPredicate.Builder.item().of(CCItems.CAVEFISH.get()).build()))))))),

				createModifierEntry("husbandry/tactical_fishing", Collections.singletonList(createIndexedRequirementsModifier(0, Collections.singletonList(
						Pair.of("cavefish_bucket", new Criterion(FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of(CCItems.CAVEFISH_BUCKET.get()).build()))))))),

				createModifierEntry("husbandry/obtain_netherite_hoe", Arrays.asList(
						createDescriptionModifier("husbandry", "netherite_hoe"),
						createIndexedRequirementsModifier(0, Collections.singletonList(Pair.of("necromium_hoe", new Criterion(InventoryChangeTrigger.TriggerInstance.hasItems(CCItems.NECROMIUM_HOE.get()))))))),

				createModifierEntry("husbandry/wax_on", Collections.singletonList(createCriteriaModifier(Mode.REPLACE, Collections.singletonList(
						Pair.of("wax_on", new Criterion(TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(CCBlockTags.WAXABLE_COPPER_BLOCKS).build()),
								ItemPredicate.Builder.item().of(Items.HONEYCOMB)))))))),

				createModifierEntry("husbandry/wax_off", Collections.singletonList(createCriteriaModifier(Mode.REPLACE, Collections.singletonList(
						Pair.of("wax_off", new Criterion(TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(CCBlockTags.WAXED_COPPER_BLOCKS).build()),
								ItemPredicate.Builder.item().of(CCItemTags.TOOLS_AXES)))))))),

				createModifierEntry(Arrays.asList(new ResourceLocation("adventure/spyglass_at_parrot"), new ResourceLocation("adventure/lightning_rod_with_villager_no_fire")), "adventure/smelt_copper_parent", Collections.singletonList(createParentModifier(new ResourceLocation(CavernsAndChasms.MOD_ID, "adventure/smelt_copper")))),

				createModifierEntry("adventure/kill_a_mob", Collections.singletonList(createIndexedRequirementsModifier(0, Arrays.asList(
						Pair.of("deeper", new Criterion(KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(CCEntityTypes.DEEPER.get())))),
						Pair.of("mime", new Criterion(KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(CCEntityTypes.MIME.get())))))))),

				createModifierEntry("adventure/kill_all_mobs", Collections.singletonList(createCriteriaModifier(Mode.MODIFY, Arrays.asList(
						Pair.of("deeper", new Criterion(KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(CCEntityTypes.DEEPER.get())))),
						Pair.of("mime", new Criterion(KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(CCEntityTypes.MIME.get()))))))))
		);
	}

	private static ModifierDataProvider.ProviderEntry<Advancement.Builder, Void, DeserializationContext> createModifierEntry(List<ResourceLocation> targets, String name, List<ConfiguredModifier<Advancement.Builder, ?, Void, DeserializationContext, ?>> modifiers) {
		return new ModifierDataProvider.ProviderEntry<>(
				new TargetedModifier<>(new ConditionedModifierTargetSelector<>(ModifierTargetSelectorRegistry.NAMES.withConfiguration(targets)), modifiers),
				new ResourceLocation(CavernsAndChasms.MOD_ID, name)
		);
	}

	private static ModifierDataProvider.ProviderEntry<Advancement.Builder, Void, DeserializationContext> createModifierEntry(String target, List<ConfiguredModifier<Advancement.Builder, ?, Void, DeserializationContext, ?>> modifiers) {
		return createModifierEntry(Collections.singletonList(new ResourceLocation(target)), target, modifiers);
	}

	private static ConfiguredModifier<Advancement.Builder, ?, Void, DeserializationContext, ?> createEffectsChangedModifier(MobEffect... entries) {
		MobEffectsPredicate predicate = MobEffectsPredicate.effects();
		for (MobEffect mobEffect : entries) {
			predicate.and(mobEffect);
		}
		return new ConfiguredModifier<>(AdvancementModifiers.EFFECTS_CHANGED_MODIFIER, new EffectsChangedModifier.Config("all_effects", false, predicate));
	}

	private static Pair<Optional<Map<String, Criterion>>, Optional<String[]>> collectCriterions(List<Pair<String, Criterion>> criterions) {
		Optional<Map<String, Criterion>> criterionMap = Optional.of(Maps.newHashMap());
		ArrayList<String> reqs = Lists.newArrayList();

		criterions.forEach(pair -> {
			criterionMap.get().put(CavernsAndChasms.MOD_ID + ":" + pair.getFirst(), pair.getSecond());
			reqs.add(CavernsAndChasms.MOD_ID + ":" + pair.getFirst());
		});

		return Pair.of(criterionMap, Optional.of(reqs.toArray(String[]::new)));
	}

	private static ConfiguredModifier<Advancement.Builder, ?, Void, DeserializationContext, ?> createDescriptionModifier(String category, String name) {
		return new ConfiguredModifier<>(AdvancementModifiers.DISPLAY_INFO_MODIFIER, new DisplayInfoModifier.Config(Mode.MODIFY, Optional.empty(), Optional.of(new TranslatableComponent("advancements." + CavernsAndChasms.MOD_ID + "." + category + "." + name + ".description")), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
	}

	private static ConfiguredModifier<Advancement.Builder, ?, Void, DeserializationContext, ?> createParentModifier(ResourceLocation parent) {
		return new ConfiguredModifier<>(AdvancementModifiers.PARENT_MODIFIER, parent);
	}

	private static ConfiguredModifier<Advancement.Builder, ?, Void, DeserializationContext, ?> createCriteriaModifier(Mode mode, List<Pair<String, Criterion>> criterions) {
		Pair<Optional<Map<String, Criterion>>, Optional<String[]>> requirements = collectCriterions(criterions);
		ArrayList<String[]> reqs = Lists.newArrayList();
		for (String string : requirements.getSecond().get()) {
			reqs.add(new String[]{string});
		}

		return new ConfiguredModifier<>(AdvancementModifiers.CRITERIA_MODIFIER, new CriteriaModifier.Config(mode, requirements.getFirst(), Optional.of(reqs.toArray(String[][]::new))));
	}

	private static ConfiguredModifier<Advancement.Builder, ?, Void, DeserializationContext, ?> createIndexedRequirementsModifier(int index, List<Pair<String, Criterion>> criterions) {
		Pair<Optional<Map<String, Criterion>>, Optional<String[]>> requirements = collectCriterions(criterions);
		return new ConfiguredModifier<>(AdvancementModifiers.INDEXED_REQUIREMENTS_MODIFIER, new IndexedRequirementsModifier.Config(Mode.MODIFY, index, requirements.getFirst(), requirements.getSecond()));
	}
}