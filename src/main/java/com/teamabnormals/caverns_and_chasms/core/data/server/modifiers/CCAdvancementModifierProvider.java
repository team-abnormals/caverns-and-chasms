package com.teamabnormals.caverns_and_chasms.core.data.server.modifiers;

import com.google.common.collect.Lists;
import com.teamabnormals.blueprint.common.advancement.modification.AdvancementModifierProvider;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.CriteriaModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.DisplayInfoModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.EffectsChangedModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.ParentModifier;
import com.teamabnormals.blueprint.core.data.server.BlueprintRecipeProvider;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMobEffects;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class CCAdvancementModifierProvider extends AdvancementModifierProvider {
	private static final EntityType<?>[] BREEDABLE_ANIMALS = new EntityType[]{}; //CCEntityTypes.RAT.get()};
	private static final EntityType<?>[] MOBS_TO_KILL = new EntityType[]{CCEntityTypes.DEEPER.get(), CCEntityTypes.MIME.get(), CCEntityTypes.PEEPER.get()};
	private static final Item[] SMITHING_TEMPLATES = new Item[]{CCItems.EXILE_ARMOR_TRIM_SMITHING_TEMPLATE.get()};

	public CCAdvancementModifierProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(CavernsAndChasms.MOD_ID, output, provider);
	}

	@Override
	protected void registerEntries(Provider provider) {
		this.entry("story/lava_bucket").selects("story/lava_bucket").addModifier(CriteriaModifier.builder(this.modId).addCriterion("golden_lava_bucket", InventoryChangeTrigger.TriggerInstance.hasItems(CCItems.GOLDEN_LAVA_BUCKET.get())).addIndexedRequirements(0, false, "golden_lava_bucket").build());

		this.entry("nether/all_effects").selects("nether/all_effects").addModifier(new EffectsChangedModifier("all_effects", false, MobEffectsPredicate.effects().and(CCMobEffects.REWIND.get())));
		this.entry("nether/all_potions").selects("nether/all_potions").addModifier(new EffectsChangedModifier("all_effects", false, MobEffectsPredicate.effects().and(CCMobEffects.REWIND.get())));

		CriteriaModifier.Builder balancedDiet = CriteriaModifier.builder(this.modId);
		Collection<RegistryObject<Item>> items = CCItems.HELPER.getDeferredRegister().getEntries();
		items.forEach(item -> {
			if (item.get().isEdible()) {
				balancedDiet.addCriterion(ForgeRegistries.ITEMS.getKey(item.get()).getPath(), ConsumeItemTrigger.TriggerInstance.usedItem(item.get()));
			}
		});
		this.entry("husbandry/balanced_diet").selects("husbandry/balanced_diet").addModifier(balancedDiet.requirements(RequirementsStrategy.AND).build());

//		CriteriaModifier.Builder breedAllAnimals = CriteriaModifier.builder(this.modId);
//		for (EntityType<?> entityType : BREEDABLE_ANIMALS) {
//			breedAllAnimals.addCriterion(ForgeRegistries.ENTITY_TYPES.getKey(entityType).getPath(), BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(entityType)));
//		}
//		this.entry("husbandry/bred_all_animals").selects("husbandry/bred_all_animals").addModifier(breedAllAnimals.requirements(RequirementsStrategy.AND).build());

		this.entry("husbandry/wax_on").selects("husbandry/wax_on")
				.addModifier(DisplayInfoModifier.builder().description(Component.translatable("advancements." + this.modId + ".husbandry.wax_on.description")).build())
				.addModifier(CriteriaModifier.builder(this.modId)
						.addCriterion("wax_on_blocks", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(CCBlockTags.WAXABLE_COPPER_BLOCKS).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ButtonBlock.POWERED, false).build()).build()), ItemPredicate.Builder.item().of(Items.HONEYCOMB)))
						.addCriterion("wax_on_golem", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(Items.HONEYCOMB), EntityPredicate.wrap(EntityPredicate.Builder.entity().of(CCEntityTypes.COPPER_GOLEM.get()).build())))
						.addCriterion("wax_on_oxidized_golem", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(Items.HONEYCOMB), EntityPredicate.wrap(EntityPredicate.Builder.entity().of(CCEntityTypes.OXIDIZED_COPPER_GOLEM.get()).build())))
						.addIndexedRequirements(0, false, "wax_on_blocks", "wax_on_golem", "wax_on_oxidized_golem").build());
		this.entry("husbandry/wax_off").selects("husbandry/wax_off")
				.addModifier(DisplayInfoModifier.builder().description(Component.translatable("advancements." + this.modId + ".husbandry.wax_off.description")).build())
				.addModifier(CriteriaModifier.builder(this.modId)
						.addCriterion("wax_off_blocks", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(CCBlockTags.WAXED_COPPER_BLOCKS).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ButtonBlock.POWERED, false).build()).build()), ItemPredicate.Builder.item().of(ItemTags.AXES)))
						.addCriterion("wax_off_golem", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(ItemTags.AXES), EntityPredicate.wrap(EntityPredicate.Builder.entity().of(CCEntityTypes.COPPER_GOLEM.get()).build())))
						.addCriterion("wax_off_oxidized_golem", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(ItemTags.AXES), EntityPredicate.wrap(EntityPredicate.Builder.entity().of(CCEntityTypes.OXIDIZED_COPPER_GOLEM.get()).build())))
						.addIndexedRequirements(0, false, "wax_off_blocks", "wax_off_golem", "wax_off_oxidized_golem").build());

		this.entry("husbandry/obtain_netherite_hoe").selects("husbandry/obtain_netherite_hoe")
				.addModifier(DisplayInfoModifier.builder().description(Component.translatable("advancements." + this.modId + ".husbandry.netherite_hoe.description")).build())
				.addModifier(CriteriaModifier.builder(this.modId).addCriterion("necromium_hoe", InventoryChangeTrigger.TriggerInstance.hasItems(CCItems.NECROMIUM_HOE.get())).addIndexedRequirements(0, false, "necromium_hoe").build());

		this.entry("adventure/smelt_copper_parent").selects("adventure/spyglass_at_parrot", "adventure/lightning_rod_with_villager_no_fire").addModifier(new ParentModifier(new ResourceLocation(this.modId, "adventure/smelt_copper")));

		CriteriaModifier.Builder killAMob = CriteriaModifier.builder(this.modId);
		CriteriaModifier.Builder killAllMobs = CriteriaModifier.builder(this.modId);
		ArrayList<String> names = Lists.newArrayList();
		for (EntityType<?> entityType : MOBS_TO_KILL) {
			String name = ForgeRegistries.ENTITY_TYPES.getKey(entityType).getPath();
			KilledTrigger.TriggerInstance triggerInstance = KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entityType));
			killAMob.addCriterion(name, triggerInstance);
			killAllMobs.addCriterion(name, triggerInstance);
			names.add(name);
		}

		this.entry("adventure/kill_a_mob").selects("adventure/kill_a_mob").addModifier(killAMob.addIndexedRequirements(0, false, names.toArray(new String[0])).build());
		this.entry("adventure/kill_all_mobs").selects("adventure/kill_all_mobs").addModifier(killAllMobs.requirements(RequirementsStrategy.AND).build());

		CriteriaModifier.Builder trimWithAnyPattern = CriteriaModifier.builder(this.modId);
		ArrayList<String> smithingModifiers = Lists.newArrayList();
		for (Item item : SMITHING_TEMPLATES) {
			ResourceLocation trimName = BlueprintRecipeProvider.suffix(ForgeRegistries.ITEMS.getKey(item), "_smithing_trim");
			trimWithAnyPattern.addCriterion("armor_trimmed_" + trimName, RecipeCraftedTrigger.TriggerInstance.craftedItem(trimName));
			smithingModifiers.add("armor_trimmed_" + trimName);
		}

		this.entry("adventure/trim_with_any_armor_pattern").selects("adventure/trim_with_any_armor_pattern").addModifier(trimWithAnyPattern.addIndexedRequirements(0, false, smithingModifiers.toArray(new String[0])).build());

	}
}