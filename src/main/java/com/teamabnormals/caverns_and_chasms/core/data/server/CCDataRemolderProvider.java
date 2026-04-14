package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.Lists;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.CriteriaModifier;
import com.teamabnormals.blueprint.common.remolder.Remolder;
import com.teamabnormals.blueprint.common.remolder.data.RemolderProvider;
import com.teamabnormals.blueprint.common.remolder.util.AdvancementRemolders;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMobEffects;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCStructures;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.advancements.critereon.EntityPredicate.Builder;
import net.minecraft.advancements.critereon.KilledTrigger.TriggerInstance;
import net.minecraft.advancements.critereon.MinMaxBounds.Doubles;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.Target;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSet.StructureSelectionEntry;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import static com.teamabnormals.blueprint.common.remolder.RemolderTypes.add;
import static com.teamabnormals.blueprint.common.remolder.RemolderTypes.sequence;
import static com.teamabnormals.blueprint.common.remolder.data.DynamicReference.target;
import static com.teamabnormals.blueprint.common.remolder.data.DynamicReference.value;
import static com.teamabnormals.blueprint.common.remolder.util.LootRemolders.*;
import static com.teamabnormals.caverns_and_chasms.core.registry.CCItems.*;

public class CCDataRemolderProvider extends RemolderProvider {

	public CCDataRemolderProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(CavernsAndChasms.MOD_ID, Target.DATA_PACK, output, provider);
	}

	@Override
	protected void registerEntries(Provider provider) {
		this.registerLootRemolders(provider);
		this.registerAdvancementRemolders(provider);

		HolderGetter<Structure> structures = provider.lookupOrThrow(Registries.STRUCTURE);
		this.entry("worldgen/structure_set/mineshafts")
				.path("worldgen/structure_set/mineshafts")
				.remolder(add(target("structures[]"), value(
						StructureSet.entry(structures.getOrThrow(CCStructures.MINESHAFT_LUSH), 1), StructureSelectionEntry.CODEC)
				));
	}

	private static final EntityType<?>[] BREEDABLE_ANIMALS = new EntityType[]{CCEntityTypes.RAT.get()};
	private static final EntityType<?>[] MOBS_TO_KILL = new EntityType[]{CCEntityTypes.DEEPER.get(), CCEntityTypes.EVENDEEPER.get(), CCEntityTypes.MIME.get(), CCEntityTypes.PEEPER.get(), CCEntityTypes.GRAZER.get()};
	private static final Item[] SMITHING_TEMPLATES = new Item[]{CCItems.EXILE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), CCItems.FORGER_ARMOR_TRIM_SMITHING_TEMPLATE.get(), CCItems.IMMOLATE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), CCItems.RIM_ARMOR_TRIM_SMITHING_TEMPLATE.get(), CCItems.PLATE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), CCItems.CORE_ARMOR_TRIM_SMITHING_TEMPLATE.get()};

	public void registerAdvancementRemolders(Provider provider) {
		this.allEffects(CCMobEffects.MOB_EFFECTS);
		this.allPotions(CCMobEffects.POTIONS);
		this.balancedDiet(CCItems.HELPER.getDeferredRegister());
		this.killMobs(MOBS_TO_KILL);
		this.breedAllAnimals(BREEDABLE_ANIMALS);
		this.trimWithAnyArmorPattern(SMITHING_TEMPLATES);

		this.advancementRemolder("story/lava_bucket").remolder(AdvancementRemolders.criteria(CriteriaModifier.builder(this.modId)
				.addCriterion("golden_lava_bucket", InventoryChangeTrigger.TriggerInstance.hasItems(CCItems.GOLDEN_LAVA_BUCKET))
				.addIndexedRequirements(0, false, "golden_lava_bucket").build())
		);

		Remolder replaceCopperParent = AdvancementRemolders.replaceParent(CavernsAndChasms.location("adventure/smelt_copper"));
		this.advancementRemolder("adventure/spyglass_at_parrot").remolder(replaceCopperParent);
		this.advancementRemolder("adventure/lightning_rod_with_villager_no_fire").remolder(replaceCopperParent);

		this.advancementRemolder("husbandry/obtain_netherite_hoe").remolder(sequence(
				AdvancementRemolders.remoldDisplayInfo().description(Component.translatable("advancements." + this.modId + ".husbandry.netherite_hoe.description")).build()),
				AdvancementRemolders.criteria(CriteriaModifier.builder(this.modId).addCriterion("necromium_hoe", InventoryChangeTrigger.TriggerInstance.hasItems(CCItems.NECROMIUM_HOE.get())).addIndexedRequirements(0, false, "necromium_hoe").build()));

		this.advancementRemolder("husbandry/wax_on").remolder(sequence(
				AdvancementRemolders.remoldDisplayInfo().description(Component.translatable("advancements." + this.modId + ".husbandry.wax_on.description")).build(),
				AdvancementRemolders.criteria(CriteriaModifier.builder(this.modId)
						.addCriterion("wax_on_blocks", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(CCBlockTags.WAXABLE_COPPER_BLOCKS)), ItemPredicate.Builder.item().of(CCItemTags.WAX)))
						.addCriterion("wax_on_golem", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(CCItemTags.WAX), Optional.of(EntityPredicate.wrap(Builder.entity().of(CCEntityTypes.COPPER_GOLEM.get())))))
						.addCriterion("wax_on_oxidized_golem", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(CCItemTags.WAX), Optional.of(EntityPredicate.wrap(Builder.entity().of(CCEntityTypes.OXIDIZED_COPPER_GOLEM.get())))))
						.addIndexedRequirements(0, false, "wax_on_blocks", "wax_on_golem", "wax_on_oxidized_golem").build())));

		this.advancementRemolder("husbandry/wax_off").remolder(sequence(
				AdvancementRemolders.remoldDisplayInfo().description(Component.translatable("advancements." + this.modId + ".husbandry.wax_off.description")).build(),
				AdvancementRemolders.criteria(CriteriaModifier.builder(this.modId)
						.addCriterion("wax_off_blocks", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(CCBlockTags.WAXED_COPPER_BLOCKS)), ItemPredicate.Builder.item().of(ItemTags.AXES)))
						.addCriterion("wax_off_golem", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(ItemTags.AXES), Optional.of(EntityPredicate.wrap(Builder.entity().of(CCEntityTypes.COPPER_GOLEM.get())))))
						.addCriterion("wax_off_oxidized_golem", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(ItemTags.AXES), Optional.of(EntityPredicate.wrap(Builder.entity().of(CCEntityTypes.OXIDIZED_COPPER_GOLEM.get())))))
						.addIndexedRequirements(0, false, "wax_off_blocks", "wax_off_golem", "wax_off_oxidized_golem").build())));

	}

	public Entry advancementRemolder(String key) {
		return this.advancementRemolder(ResourceLocation.parse(key));
	}

	public Entry advancementRemolder(ResourceLocation location) {
		String name = "advancement/" + location.getPath();
		return this.entry(name).path(name);
	}

	public Entry breedAllAnimals(EntityType<?>... entityTypes) {
		CriteriaModifier.Builder breedAllAnimals = CriteriaModifier.builder(this.modId);
		for (EntityType<?> entityType : entityTypes) {
			breedAllAnimals.addCriterion(BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath(), BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(entityType)));
		}
		return this.advancementRemolder("husbandry/bred_all_animals").remolder(AdvancementRemolders.criteria(breedAllAnimals.requirements(Strategy.AND).build()));
	}

	public void killMobs(EntityType<?>... entityTypes) {
		CriteriaModifier.Builder killAMob = CriteriaModifier.builder(this.modId);
		CriteriaModifier.Builder killAllMobs = CriteriaModifier.builder(this.modId);
		ArrayList<String> names = Lists.newArrayList();
		for (EntityType<?> entityType : entityTypes) {
			String name = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath();
			Criterion<TriggerInstance> triggerInstance = KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entityType));
			killAMob.addCriterion(name, triggerInstance);
			killAllMobs.addCriterion(name, triggerInstance);
			names.add(name);
		}

		this.advancementRemolder("adventure/kill_a_mob").remolder(AdvancementRemolders.criteria(killAMob.addIndexedRequirements(0, false, names.toArray(new String[0])).build()));
		this.advancementRemolder("adventure/kill_all_mobs").remolder(AdvancementRemolders.criteria(killAllMobs.requirements(Strategy.AND).build()));
	}

	public Entry trimWithAnyArmorPattern(Item... smithingTemplates) {
		CriteriaModifier.Builder trimWithAnyPattern = CriteriaModifier.builder(this.modId);
		ArrayList<String> smithingModifiers = Lists.newArrayList();
		for (Item item : smithingTemplates) {
			ResourceLocation trimName = BuiltInRegistries.ITEM.getKey(item).withSuffix("_smithing_trim");
			trimWithAnyPattern.addCriterion("armor_trimmed_" + trimName, RecipeCraftedTrigger.TriggerInstance.craftedItem(trimName));
			smithingModifiers.add("armor_trimmed_" + trimName);
		}

		return this.advancementRemolder("adventure/trim_with_any_armor_pattern").remolder(AdvancementRemolders.criteria(trimWithAnyPattern.addIndexedRequirements(0, false, smithingModifiers.toArray(new String[0])).build()));
	}

	public Entry allEffects(DeferredRegister<MobEffect> register) {
		MobEffectsPredicate.Builder builder = MobEffectsPredicate.Builder.effects();
		register.getEntries().forEach(builder::and);
		return this.advancementRemolder("nether/all_effects").remolder(AdvancementRemolders.addToEffectsChanged("all_effects", builder.build().get()));
	}

	public Entry allPotions(DeferredRegister<Potion> register) {
		MobEffectsPredicate.Builder builder = MobEffectsPredicate.Builder.effects();
		List<Holder<MobEffect>> dupes = new ArrayList<>();
		register.getEntries().forEach(potion -> {
			potion.get().effects.stream().filter(e -> !dupes.contains(e.getEffect())).forEach(instance -> {
				builder.and(instance.getEffect());
				dupes.add(instance.getEffect());
			});
		});
		return this.advancementRemolder("nether/all_potions").remolder(AdvancementRemolders.addToEffectsChanged("all_effects", builder.build().get()));
	}

	public Entry balancedDiet(DeferredRegister<Item> register) {
		return this.advancementRemolder("husbandry/balanced_diet").remolder(AdvancementRemolders.criteria(this.buildBalancedDiet(register)));
	}

	public CriteriaModifier buildBalancedDiet(DeferredRegister<Item> register) {
		return this.buildBalancedDiet(register, item -> true);
	}

	public CriteriaModifier buildBalancedDiet(DeferredRegister<Item> register, Predicate<DeferredHolder<Item, ? extends Item>> predicate) {
		CriteriaModifier.Builder balancedDiet = CriteriaModifier.builder(this.modId);
		Collection<DeferredHolder<Item, ? extends Item>> items = register.getEntries().stream()
				.filter(i -> i.get().getDefaultInstance().getFoodProperties(null) != null)
				.filter(predicate).toList();
		items.forEach(item -> {
			balancedDiet.addCriterion(BuiltInRegistries.ITEM.getKey(item.get()).getPath(), ConsumeItemTrigger.TriggerInstance.usedItem(item.get()));
		});
		return balancedDiet.requirements(Strategy.AND).build();
	}

	public void registerLootRemolders(Provider provider) {
		LootItemCondition.Builder hasSilkTouch = hasSilkTouch(provider);
		this.lootRemolder(BuiltInLootTables.SIMPLE_DUNGEON).remolder(sequence(
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 15)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 10)),
				addEntry(0, entry(BEJEWELED_APPLE.get(), 10)),
				addEntry(1, entry(SILVER_INGOT.get(), 5, 1, 4)),
				addEntry(1, entry(ZIRCONIA.get(), 10, 1, 2))));

		this.lootRemolder(BuiltInLootTables.ABANDONED_MINESHAFT).remolder(sequence(
				addEntry(0, entry(Items.BUNDLE, 5)),
				addEntry(0, entry(DEPTH_GAUGE.get(), 5)),
				addEntry(0, entry(TOOLBELT.get(), 5)),
				addEntry(1, entry(SILVER_INGOT.get(), 5, 1, 3)),
				addEntry(1, entry(SPINEL.get(), 5, 6, 11)),
				addEntry(1, entry(TIN_INGOT.get(), 2, 1, 3)),
				addEntry(2, entry(CCBlocks.SPARKLER.getFirst().get(), 15, 4, 12)),
				addEntry(2, entry(CCBlocks.SPIKED_RAIL.get(), 5, 1, 4))));

		this.lootRemolder(BuiltInLootTables.STRONGHOLD_CORRIDOR).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 5, 1, 3)),
				addEntry(0, entry(TIN_INGOT.get(), 8, 1, 3)),
				addEntry(0, entry(ZIRCONIA.get(), 5, 1, 2)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 1)),
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 1)),
				addEntry(0, entry(BEJEWELED_APPLE.get(), 10, 1, 3))));

		this.lootRemolder(BuiltInLootTables.STRONGHOLD_CROSSING).remolder(sequence(
				addEntry(0, LootItem.lootTableItem(COWL.get()).setWeight(2).apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)).build()),
				addEntry(0, entry(SILVER_INGOT.get(), 5, 1, 3)),
				addEntry(0, entry(BEJEWELED_APPLE.get(), 10, 1, 3))));

		this.lootRemolder(BuiltInLootTables.STRONGHOLD_LIBRARY).remolder(sequence(
				addEntry(0, entry(ZIRCONIA.get(), 1, 1, 3))));

		this.lootRemolder(BuiltInLootTables.SHIPWRECK_MAP).remolder(sequence(
				addEntry(1, entry(BAROMETER.get(), 1)),
				addEntry(1, entry(DEPTH_GAUGE.get(), 1))));

		this.lootRemolder(BuiltInLootTables.SHIPWRECK_TREASURE).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 10, 1, 5)),
				addEntry(1, entry(SILVER_NUGGET.get(), 10, 1, 10)),
				addEntry(1, entry(SPINEL.get(), 20, 1, 8))));

		this.lootRemolder(BuiltInLootTables.BURIED_TREASURE).remolder(sequence(
				addEntry(1, entry(SILVER_INGOT.get(), 10, 1, 4)),
				addEntry(2, entry(ZIRCONIA.get(), 5, 1, 2)),
				addEntry(2, entry(TURQUOISE.get(), 1))));

		this.lootRemolder(BuiltInLootTables.DESERT_PYRAMID).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 15, 1, 5)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 10)),
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 15))));

		this.lootRemolder(BuiltInLootTables.JUNGLE_TEMPLE).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 15, 2, 7)),
				addEntry(0, entry(SPINEL.get(), 15, 2, 5)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 1)),
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 1))));

		this.lootRemolder(BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER).remolder(sequence(
				addEntry(0, entry(RICOCHET_ARROW.get(), 10, 1, 4))));

		this.lootRemolder(BuiltInLootTables.WOODLAND_MANSION).remolder(sequence(
				addPool(pool("turquoise").setRolls(UniformGenerator.between(0.0F, 1.0F)).add(LootItem.lootTableItem(TURQUOISE.get())).build()),
				addEntry(0, entry(COWL.get(), 10)),
				addEntry(1, entry(SILVER_INGOT.get(), 5, 1, 4)),
				addEntry(1, entry(TIN_INGOT.get(), 8, 1, 4)),
				addEntry(1, entry(ZIRCONIA.get(), 5, 1, 2))));

		this.lootRemolder(BuiltInLootTables.VILLAGE_FISHER).remolder(sequence(
				addPool(pool("barometer").setRolls(UniformGenerator.between(0.0F, 1.0F)).add(LootItem.lootTableItem(BAROMETER.get())).build()),
				addEntry(0, entry(CAVEFISH.get(), 1, 1, 3))));

		this.lootRemolder(BuiltInLootTables.VILLAGE_WEAPONSMITH).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 5, 1, 3)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 1)),
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 1))));

		this.lootRemolder(BuiltInLootTables.VILLAGE_TOOLSMITH).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 1, 1, 3))));

		this.lootRemolder(BuiltInLootTables.VILLAGE_TEMPLE).remolder(sequence(
				addEntry(0, entry(SPINEL.get(), 1, 1, 4)),
				addEntry(0, entry(SILVER_INGOT.get(), 1, 1, 4)),
				addEntry(0, entry(ZIRCONIA.get(), 1))));

		this.lootRemolder(BuiltInLootTables.VILLAGE_FLETCHER).remolder(sequence(
				addEntry(0, entry(CCBlocks.HOOP.get(), 1)),
				addEntry(0, entry(BLUNT_ARROW.get(), 1, 1, 8))));

		this.lootRemolder(BuiltInLootTables.VILLAGE_SNOWY_HOUSE).remolder(sequence(
				addEntry(0, entry(SILVER_NUGGET.get(), 1, 1, 4))));

		this.lootRemolder(BuiltInLootTables.FISHING_FISH).remolder(sequence(
				addEntry(0, LootItem.lootTableItem(CAVEFISH.get()).setWeight(70).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setY(Doubles.atMost(30.0D)))).build())));

		this.lootRemolder(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE).remolder(sequence(
				addEntry(0, entry(TOOLBELT.get(), 1))));

		this.lootRemolder(BuiltInLootTables.IGLOO_CHEST).remolder(sequence(
				addEntry(0, entry(TOOLBELT.get(), 2))));

		this.lootRemolder(BuiltInLootTables.PILLAGER_OUTPOST).remolder(sequence(
				addEntry(1, entry(COWL.get(), 1)),
				addEntry(3, entry(RICOCHET_ARROW.get(), 2, 1, 4)),
				addEntry(3, entry(LARGE_ARROW.get(), 4, 1, 2))));

		this.lootRemolder(BuiltInLootTables.ANCIENT_CITY).remolder(sequence(
				addEntry(0, entry(TUNING_FORK.get(), 2)),
				addEntry(0, entry(CCBlocks.SPARKLER.getFirst().get(), 5, 1, 15)),
				addEntry(0, entry(BEJEWELED_APPLE.get(), 3, 1, 3)),
				addEntry(0, entry(ZIRCONIA.get(), 3, 1, 2)),
				addEntry(0, LootItem.lootTableItem(COWL.get()).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(provider, UniformGenerator.between(20.0F, 39.0F))).build())));

		this.lootRemolder(BuiltInLootTables.RUINED_PORTAL).remolder(sequence(
				addPool(pool("lodestone").add(EmptyLootItem.emptyItem()).add(LootItem.lootTableItem(Blocks.LODESTONE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).build()),
				addEntry(0, entry(CCBlocks.GOLDEN_BARS.get(), 5, 8, 16)),
				addEntry(0, entry(CCBlocks.LAVA_LAMP.get(), 5)),
				addEntry(0, entry(GOLDEN_BUCKET.get(), 1))));

		this.lootRemolder(BuiltInLootTables.NETHER_BRIDGE).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 5, 1, 5)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 6)),
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 5)),
				addEntry(0, entry(TURQUOISE.get(), 1)),
				addEntry(1, entry(EXILE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), 1))));

		this.lootRemolder(BuiltInLootTables.BASTION_BRIDGE).remolder(sequence(
				addPool(pool("golden_bucket").add(LootItem.lootTableItem(GOLDEN_BUCKET.get())).build()),
				addEntry(1, LootItem.lootTableItem(TOOLBELT.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)).build()),
				addEntry(1, entry(SILVER_INGOT.get(), 1, 4, 9)), addEntry(1, entry(LARGE_ARROW.get(), 1, 4, 12)),
				addEntry(2, entry(SILVER_NUGGET.get(), 1, 2, 6))));

		this.lootRemolder(BuiltInLootTables.BASTION_HOGLIN_STABLE).remolder(sequence(
				addEntry(0, entry(TURQUOISE.get(), 6))));

		this.lootRemolder(BuiltInLootTables.BASTION_TREASURE).remolder(sequence(
				addEntry(0, entry(TURQUOISE.get(), 6)),
				addEntry(1, entry(SILVER_INGOT.get(), 1, 3, 9)),
				addEntry(1, entry(CCBlocks.SILVER_BLOCK.get(), 1, 2, 5)),
				addEntry(1, entry(LARGE_ARROW.get(), 1, 6, 10))));

		this.lootRemolder(BuiltInLootTables.BASTION_OTHER).remolder(sequence(
				addEntry(0, entry(LARGE_ARROW.get(), 1, 4, 8)),
				addEntry(0, entry(TURQUOISE.get(), 3)),
				addEntry(1, entry(SILVER_INGOT.get(), 2, 1, 6)),
				addEntry(1, entry(CCBlocks.SILVER_BLOCK.get(), 2)),
				addEntry(2, entry(SILVER_NUGGET.get(), 1, 2, 8))));

		this.lootRemolder(BuiltInLootTables.END_CITY_TREASURE).remolder(sequence(
				addEntry(0, entry(BEJEWELED_APPLE.get(), 5, 3, 9)),
				addEntry(0, entry(SILVER_INGOT.get(), 15, 2, 7)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 1)),
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 1)),
				addEntry(0, entry(TURQUOISE.get(), 1))));

		this.lootRemolder(ResourceLocation.fromNamespaceAndPath("atmospheric", "chests/kousa_sanctum")).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 15, 2, 7)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 1)), addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 1))));

		this.lootRemolder(ResourceLocation.withDefaultNamespace("entities/elder_guardian")).remolder(sequence(addPool(
				pool("turquoise").setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TURQUOISE.get()).setWeight(1)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
								.when(LootItemRandomChanceCondition.randomChance(0.1F))).build())));

		this.lootRemolder(ResourceLocation.withDefaultNamespace("blocks/copper_ore")).remolder(sequence(addPool(
				pool("turquoise").setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TURQUOISE.get())
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
								.when(hasSilkTouch.invert())
								.when(LootItemRandomChanceCondition.randomChance(0.005F))).build())));

		this.lootRemolder(ResourceLocation.withDefaultNamespace("blocks/deepslate_copper_ore")).remolder(sequence(addPool(
				pool("turquoise").setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TURQUOISE.get())
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
								.when(hasSilkTouch.invert())
								.when(LootItemRandomChanceCondition.randomChance(0.01F))).build())));
	}

	protected LootItemCondition.Builder hasSilkTouch(Provider provider) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
		return MatchTool.toolMatches(ItemPredicate.Builder.item().withSubPredicate(ItemSubPredicates.ENCHANTMENTS, ItemEnchantmentsPredicate.enchantments(
				List.of(new EnchantmentPredicate(registrylookup.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))));
	}

	public Entry lootRemolder(ResourceKey<LootTable> key) {
		return this.lootRemolder(key.location());
	}

	public Entry lootRemolder(ResourceLocation location) {
		String name = "loot_table/" + location.getPath();
		return this.entry(name).path(name);
	}

	private static LootPool.Builder pool(String name) {
		return LootPool.lootPool().name(CavernsAndChasms.MOD_ID + ":" + name);
	}

	private static LootPoolEntryContainer entry(ItemLike item, int weight) {
		return LootItem.lootTableItem(item).setWeight(weight).build();
	}

	private static LootPoolEntryContainer entry(ItemLike item, int weight, int min, int max) {
		return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).build();
	}
}