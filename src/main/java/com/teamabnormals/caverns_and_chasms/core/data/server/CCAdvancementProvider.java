package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.teamabnormals.blueprint.core.other.tags.BlueprintEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.common.advancement.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCDamageTypeTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMobEffects;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCStructures;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.advancements.critereon.DamageSourcePredicate.Builder;
import net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.Slots;
import net.minecraft.advancements.critereon.MinMaxBounds.Doubles;
import net.minecraft.advancements.critereon.MinMaxBounds.Ints;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.AdvancementProvider.AdvancementGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CCAdvancementProvider implements AdvancementGenerator {

	public static AdvancementProvider create(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		return new AdvancementProvider(output, provider, helper, List.of(new CCAdvancementProvider()));
	}

	@Override
	public void generate(Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper helper) {
		createAdvancement("obtain_ancient_hoes", "husbandry", ResourceLocation.withDefaultNamespace("husbandry/obtain_netherite_hoe"), CCItems.NECROMIUM_HOE.get(), AdvancementType.CHALLENGE, true, true, false)
				.rewards(AdvancementRewards.Builder.experience(100))
				.addCriterion("ancient_hoes", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HOE, CCItems.NECROMIUM_HOE.get()))
				.save(consumer, CavernsAndChasms.MOD_ID + ":husbandry/obtain_ancient_hoes");

		AdvancementHolder boneFlute = createAdvancement("obtain_bone_flute", "husbandry", ResourceLocation.withDefaultNamespace("husbandry/tame_an_animal"), CCItems.BONE_FLUTE.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("bone_flute", InventoryChangeTrigger.TriggerInstance.hasItems(CCItems.BONE_FLUTE.get()))
				.save(consumer, CavernsAndChasms.MOD_ID + ":husbandry/obtain_bone_flute");

		AdvancementHolder killWithRat = createAdvancement("kill_with_rat", "husbandry", boneFlute, CCItems.BONE_FLUTE.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("kill_with_rat", RatKilledEntityTrigger.TriggerInstance.ratKilledEntity())
				.save(consumer, CavernsAndChasms.MOD_ID + ":husbandry/kill_with_rat");

		createAdvancement("kill_guardian_with_rat", "husbandry", killWithRat, CCItems.BONE_FLUTE.get(), AdvancementType.CHALLENGE, true, true, true)
				.addCriterion("kill_guardian_with_rat", RatKilledEntityTrigger.TriggerInstance.ratKilledEntity(EntityPredicate.Builder.entity().of(EntityType.GUARDIAN)))
				.save(consumer, CavernsAndChasms.MOD_ID + ":husbandry/kill_guardian_with_rat");

		createAdvancement("necromium_armor", "nether", ResourceLocation.withDefaultNamespace("nether/obtain_ancient_debris"), CCItems.NECROMIUM_CHESTPLATE.get(), AdvancementType.CHALLENGE, true, true, false)
				.rewards(AdvancementRewards.Builder.experience(100))
				.addCriterion("necromium_armor", InventoryChangeTrigger.TriggerInstance.hasItems(CCItems.NECROMIUM_HELMET.get(), CCItems.NECROMIUM_CHESTPLATE.get(), CCItems.NECROMIUM_LEGGINGS.get(), CCItems.NECROMIUM_BOOTS.get()))
				.save(consumer, CavernsAndChasms.MOD_ID + ":nether/necromium_armor");

		AdvancementHolder smeltCopper = createAdvancement("smelt_copper", "adventure", ResourceLocation.withDefaultNamespace("adventure/root"), Items.COPPER_INGOT, AdvancementType.TASK, true, true, false)
				.addCriterion("copper", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COPPER_INGOT))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/smelt_copper");

		AdvancementHolder useTuningFork = createAdvancement("use_tuning_fork", "adventure", smeltCopper, CCItems.TUNING_FORK.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("use_tuning_fork", CCCriteriaTriggers.useTuningFork())
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/use_tuning_fork");

		createAdvancement("tune_a_fish", "adventure", useTuningFork, CCItems.TUNING_FORK.get(), AdvancementType.TASK, true, true, true)
				.addCriterion("attack_fish", CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new PlayerHurtEntityTrigger.TriggerInstance(Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of(CCItems.TUNING_FORK.get())).build()).build())), Optional.empty(), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(BlueprintEntityTypeTags.FISHES).build())))))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/tune_a_fish");

		createAdvancement("summon_copper_golem", "adventure", smeltCopper, Items.CARVED_PUMPKIN, AdvancementType.GOAL, true, true, false)
				.addCriterion("summoned_golem", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(CCEntityTypes.COPPER_GOLEM.get())))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/summon_copper_golem");

		createAdvancement("thunderstruck", "adventure", smeltCopper, CCItems.COPPER_HELMET.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("thunderstruck", CriteriaTriggers.ENTITY_HURT_PLAYER.createCriterion(new EntityHurtPlayerTrigger.TriggerInstance(Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().head(ItemPredicate.Builder.item().of(CCItemTags.COPPER_HELMETS)).build()).build())), Optional.of(DamagePredicate.Builder.damageInstance().type(Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_LIGHTNING))).build()))))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/thunderstruck");

		createAdvancement("ride_boat_with_deeper", "nether", ResourceLocation.withDefaultNamespace("nether/root"), CCItems.DEEPER_HEAD.get(), AdvancementType.TASK, true, true, true)
				.addCriterion("ride_boat_with_deeper", StartRidingTrigger.TriggerInstance.playerStartsRiding(EntityPredicate.Builder.entity().located(LocationPredicate.Builder.inDimension(Level.NETHER)).vehicle(EntityPredicate.Builder.entity().of(EntityType.BOAT).passenger(EntityPredicate.Builder.entity().of(CCEntityTypes.DEEPER.get())))))
				.save(consumer, CavernsAndChasms.MOD_ID + ":nether/ride_boat_with_deeper");

		createAdvancement("dont_move", "adventure", ResourceLocation.withDefaultNamespace("adventure/kill_a_mob"), CCBlocks.PEEPER_HEAD.get(), AdvancementType.TASK, true, true, true)
				.addCriterion("spotted_by_peeper", CCCriteriaTriggers.spottedByPeeper())
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/dont_move");

		ItemStack stack = PotionContents.createItemStack(Items.POTION, CCMobEffects.REVENANT);
		createAdvancement("kill_bat_with_vampirism", "adventure", ResourceLocation.withDefaultNamespace("adventure/kill_a_mob"), stack, AdvancementType.TASK, true, true, true)
				.addCriterion("kill_bat", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityType.BAT), Optional.of(Builder.damageType().tag(TagPredicate.is(CCDamageTypeTags.DRAINS_ENEMIES)).build())))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/kill_bat_with_vampirism");

		createAdvancement("dismantle_item", "adventure", ResourceLocation.withDefaultNamespace("adventure/trim_with_any_armor_pattern"), CCBlocks.DISMANTLING_TABLE.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("dismantled_item", CCCriteriaTriggers.dismantledItem())
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/dismantle_item");

		AdvancementHolder atoneItem = createAdvancement("atone_item", "adventure", ResourceLocation.withDefaultNamespace("adventure/root"), CCBlocks.ATONING_TABLE.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("atoned_item", AtonedItemTrigger.TriggerInstance.atonedItem())
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/atone_item");

		createAdvancement("break_item_atoning", "adventure", atoneItem, CCBlocks.ATONING_TABLE.get(), AdvancementType.TASK, true, true, true)
				.addCriterion("broken_atonement", AtonedItemTrigger.TriggerInstance.brokenAtonement())
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/break_item_atoning");

		AdvancementHolder bejeweledAnvilRepair = createAdvancement("bejeweled_anvil_repair", "adventure", ResourceLocation.withDefaultNamespace("adventure/root"), CCBlocks.BEJEWELED_ANVIL.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("bejeweled_anvil_repair", RepairedItemTrigger.TriggerInstance.repairedItem())
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/bejeweled_anvil_repair");

		AdvancementHolder zirconia = createAdvancement("repair_with_zirconia", "adventure", bejeweledAnvilRepair, CCItems.ZIRCONIA.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("repair_with_zirconia", RepairedItemTrigger.TriggerInstance.repairedItemWith(ItemPredicate.Builder.item().of(CCItems.ZIRCONIA.get()).build()))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/repair_with_zirconia");

		createAdvancement("copy_music_disc", "adventure", zirconia, CCItems.MUSIC_DISC_COPY.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("copy_music_disc", InventoryChangeTrigger.TriggerInstance.hasItems(CCItems.MUSIC_DISC_COPY.get()))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/copy_music_disc");

		stack = new ItemStack(Items.DIAMOND_LEGGINGS);
		stack.enchant(provider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING), 3);
		createAdvancement("repair_pants_with_zirconia", "adventure", zirconia, stack, AdvancementType.TASK, true, true, true)
				.addCriterion("repair_pants_with_zirconia", RepairedItemTrigger.TriggerInstance.repairedItemWith(ItemPredicate.Builder.item().of(ItemTags.LEG_ARMOR).build(), ItemPredicate.Builder.item().of(CCItems.ZIRCONIA.get()).build()))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/repair_pants_with_zirconia");

		AdvancementHolder monolith = createAdvancement("find_monolith", "adventure", ResourceLocation.withDefaultNamespace("adventure/root"), CCItems.RAW_TIN.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("find_monolith", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(provider.lookupOrThrow(Registries.STRUCTURE).getOrThrow(CCStructures.TIN_MONOLITH))))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/find_monolith");

		createAdvancement("open_large_storage_duct", "adventure", monolith, CCBlocks.STORAGE_DUCT.get(), AdvancementType.CHALLENGE, true, true, false)
				.addCriterion("open_large_storage_duct", OpenStorageDuctTrigger.TriggerInstance.openStorageDuct(Ints.atLeast(100)))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/open_large_storage_duct");

		createAdvancement("half_court", "adventure", monolith, CCBlocks.HOOP.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("half_court", HoopTrigger.TriggerInstance.hoopEntered(Ints.ANY, Ints.ANY, Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().distance(
						new DistancePredicate(MinMaxBounds.Doubles.ANY, Doubles.atLeast(3.048D), MinMaxBounds.Doubles.ANY, Doubles.atLeast(14.326D), MinMaxBounds.Doubles.ANY)
				).build()))))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/half_court");

		createAdvancement("hatch_saddled_grazer", "adventure", monolith, CCBlocks.SADDLED_EGG.get(), AdvancementType.TASK, true, true, true)
				.addCriterion("hatch_saddled_grazer", CCCriteriaTriggers.hatchSaddledGrazer())
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/hatch_saddled_grazer");

		createAdvancement("ricochet_bullseye", "adventure", monolith, CCBlocks.BOUNCER.get(), AdvancementType.CHALLENGE, true, true, false)
				.rewards(AdvancementRewards.Builder.experience(50))
				.addCriterion("ricochet_bullseye", TargetBlockTrigger.TriggerInstance.targetHit(MinMaxBounds.Ints.exactly(15), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().subPredicate(RicochetPredicate.ricochets(Ints.atLeast(3))).build()))))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/ricochet_bullseye");

		AdvancementHolder ricochetHit = createAdvancement("ricochet_hit", "adventure", monolith, CCItems.RICOCHET_ARROW.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("ricochet_hit", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().direct(EntityPredicate.Builder.entity().subPredicate(RicochetPredicate.ricochets(Ints.atLeast(1))))), Optional.empty()))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/ricochet_hit");

		createAdvancement("ricochet_hit_yourself", "adventure", ricochetHit, CCItems.RICOCHET_ARROW.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("ricochet_hit_yourself", PlayerHurtSelfTrigger.TriggerInstance.playerHurtSelfWithDamage(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().direct(EntityPredicate.Builder.entity().subPredicate(RicochetPredicate.ricochets(Ints.atLeast(1)))))))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/ricochet_hit_yourself");

		AdvancementHolder turq = createAdvancement("obtain_turquoise", "adventure", ResourceLocation.withDefaultNamespace("adventure/root"), CCItems.TURQUOISE.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("turquoise", InventoryChangeTrigger.TriggerInstance.hasItems(CCItems.TURQUOISE.get()))
				.save(consumer, CavernsAndChasms.MOD_ID + ":husbandry/obtain_turquoise");

		createAdvancement("use_unicorn_horn", "adventure", turq, CCItems.UNICORN_HORN.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("unicorn_horn", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(CCItems.UNICORN_HORN.get()), Optional.empty()))
				.save(consumer, CavernsAndChasms.MOD_ID + ":husbandry/use_unicorn_horn");

		createAdvancement("equip_monocle", "adventure", turq, CCItems.MONOCLE.get(), AdvancementType.TASK, true, true, false)
				.addCriterion("monocle", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(
						Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().head(ItemPredicate.Builder.item().of(CCItems.MONOCLE.get())).build()).build())), Slots.ANY, List.of(ItemPredicate.Builder.item().of(CCItems.MONOCLE.get()).build())))
				).save(consumer, CavernsAndChasms.MOD_ID + ":nether/equip_monocle");
	}

	private static Advancement.Builder createAdvancement(String name, String category, ResourceLocation parent, ItemLike icon, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
		return createAdvancement(name, category, Advancement.Builder.advancement().build(parent), icon, frame, showToast, announceToChat, hidden);
	}

	private static Advancement.Builder createAdvancement(String name, String category, ResourceLocation parent, ItemStack icon, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
		return createAdvancement(name, category, Advancement.Builder.advancement().build(parent), icon, frame, showToast, announceToChat, hidden);
	}

	private static Advancement.Builder createAdvancement(String name, String category, AdvancementHolder parent, ItemStack icon, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
		return Advancement.Builder.advancement().parent(parent).display(icon,
				Component.translatable("advancements." + CavernsAndChasms.MOD_ID + "." + category + "." + name + ".title"),
				Component.translatable("advancements." + CavernsAndChasms.MOD_ID + "." + category + "." + name + ".description"),
				null, frame, showToast, announceToChat, hidden);
	}

	private static Advancement.Builder createAdvancement(String name, String category, AdvancementHolder parent, ItemLike icon, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
		return createAdvancement(name, category, parent, new ItemStack(icon), frame, showToast, announceToChat, hidden);
	}
}