package com.teamabnormals.caverns_and_chasms.core.data.server.modifiers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.AdvancementModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.AdvancementModifier.Mode;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.CriteriaModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.DisplayInfoModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.EffectsChangedModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.IndexedRequirementsModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.ParentModifier;
import com.teamabnormals.blueprint.core.util.modification.AdvancementModifierProvider;
import com.teamabnormals.blueprint.core.util.modification.ObjectModifierGroup;
import com.teamabnormals.blueprint.core.util.modification.selection.selectors.NamesResourceSelector;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMobEffects;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.advancements.critereon.ItemUsedOnBlockTrigger.TriggerInstance;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CCAdvancementModifiersProvider extends AdvancementModifierProvider {

	public CCAdvancementModifiersProvider(DataGenerator dataGenerator) {
		super(dataGenerator, CavernsAndChasms.MOD_ID);
	}

	@Override
	protected void registerEntries() {
		this.registerEntry("nether/all_effects", new EffectsChangedModifier("all_effects", false, MobEffectsPredicate.effects().and(CCMobEffects.REWIND.get())), new ResourceLocation(("nether/all_effects")));
		this.registerEntry("nether/all_potions", new EffectsChangedModifier("all_effects", false, MobEffectsPredicate.effects().and(CCMobEffects.REWIND.get())), new ResourceLocation(("nether/all_potions")));

		this.registerEntry("husbandry/balanced_diet", createCriteriaModifier(Mode.MODIFY, Pair.of("cavefish", new Criterion(ConsumeItemTrigger.TriggerInstance.usedItem(CCItems.CAVEFISH.get()))), Pair.of("cooked_cavefish", new Criterion(ConsumeItemTrigger.TriggerInstance.usedItem(CCItems.COOKED_CAVEFISH.get())))), new ResourceLocation("husbandry/balanced_diet"));
		this.registerEntry("husbandry/fishy_business", createCriteriaModifier(Mode.MODIFY, Pair.of("cavefish", new Criterion(FishingRodHookedTrigger.TriggerInstance.fishedItem(ItemPredicate.ANY, EntityPredicate.ANY, ItemPredicate.Builder.item().of(CCItems.CAVEFISH.get()).build())))), new ResourceLocation("husbandry/fishy_business"));
		this.registerEntry("husbandry/tactical_fishing", createIndexedRequirementsModifier(0, Collections.singletonList(Pair.of("cavefish_bucket", new Criterion(FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of(CCItems.CAVEFISH_BUCKET.get()).build()))))), new ResourceLocation("husbandry/tactical_fishing"));
		this.registerEntry("husbandry/wax_on", createCriteriaModifier(Mode.REPLACE, Pair.of("wax_on", new Criterion(TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(CCBlockTags.WAXABLE_COPPER_BLOCKS).build()), ItemPredicate.Builder.item().of(Items.HONEYCOMB))))), new ResourceLocation("husbandry/wax_on"));
		this.registerEntry("husbandry/wax_off", createCriteriaModifier(Mode.REPLACE, Pair.of("wax_off", new Criterion(TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(CCBlockTags.WAXED_COPPER_BLOCKS).build()), ItemPredicate.Builder.item().of(CCItemTags.TOOLS_AXES))))), new ResourceLocation("husbandry/wax_off"));

		this.registerEntry("husbandry/obtain_netherite_hoe",
				new ObjectModifierGroup<>(new NamesResourceSelector(new ResourceLocation("husbandry/obtain_netherite_hoe")), List.of(
						new DisplayInfoModifier(Mode.MODIFY, Optional.empty(), Optional.of(new TranslatableComponent("advancements." + this.getModId() + ".husbandry.obtain_netherite_hoe.description")), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()),
						createIndexedRequirementsModifier(0, Collections.singletonList(Pair.of("necromium_hoe", new Criterion(InventoryChangeTrigger.TriggerInstance.hasItems(CCItems.NECROMIUM_HOE.get())))))
				)));

		this.registerEntry("adventure/smelt_copper_parent", new ParentModifier(new ResourceLocation(this.getModId(), "adventure/smelt_copper")), new ResourceLocation("adventure/spyglass_at_parrot"), new ResourceLocation("adventure/lightning_rod_with_villager_no_fire"));
		this.registerEntry("adventure/kill_a_mob", createIndexedRequirementsModifier(0, Arrays.asList(
				Pair.of("deeper", new Criterion(KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(CCEntityTypes.DEEPER.get())))),
				Pair.of("mime", new Criterion(KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(CCEntityTypes.MIME.get())))))), new ResourceLocation("adventure/kill_a_mob"));
		this.registerEntry("adventure/kill_all_mobs", createCriteriaModifier(Mode.MODIFY,
				Pair.of("deeper", new Criterion(KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(CCEntityTypes.DEEPER.get())))),
				Pair.of("mime", new Criterion(KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(CCEntityTypes.MIME.get()))))), new ResourceLocation("adventure/kill_all_mobs"));

	}

	private Pair<Optional<Map<String, Criterion>>, Optional<String[]>> collectCriterions(List<Pair<String, Criterion>> criterions) {
		Optional<Map<String, Criterion>> criterionMap = Optional.of(Maps.newHashMap());
		ArrayList<String> reqs = Lists.newArrayList();

		criterions.forEach(pair -> {
			criterionMap.get().put(this.getModId() + ":" + pair.getFirst(), pair.getSecond());
			reqs.add(this.getModId() + ":" + pair.getFirst());
		});

		return Pair.of(criterionMap, Optional.of(reqs.toArray(String[]::new)));
	}

	private CriteriaModifier createCriteriaModifier(Mode mode, Pair<String, Criterion>... criterions) {
		Pair<Optional<Map<String, Criterion>>, Optional<String[]>> requirements = collectCriterions(Arrays.asList(criterions));
		ArrayList<String[]> reqs = Lists.newArrayList();
		for (String string : requirements.getSecond().get()) {
			reqs.add(new String[]{string});
		}

		return new CriteriaModifier(mode, requirements.getFirst(), Optional.of(reqs.toArray(String[][]::new)));
	}

	private IndexedRequirementsModifier createIndexedRequirementsModifier(int index, List<Pair<String, Criterion>> criterions) {
		Pair<Optional<Map<String, Criterion>>, Optional<String[]>> requirements = collectCriterions(criterions);
		return new IndexedRequirementsModifier(AdvancementModifier.Mode.MODIFY, index, requirements.getFirst(), requirements.getSecond());
	}
}