package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.teamabnormals.blueprint.core.other.tags.BlueprintEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.common.data.ForgeAdvancementProvider.AdvancementGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CCAdvancementProvider implements AdvancementGenerator {

	public static ForgeAdvancementProvider create(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		return new ForgeAdvancementProvider(output, provider, helper, List.of(new CCAdvancementProvider()));
	}

	@Override
	public void generate(Provider provider, Consumer<Advancement> consumer, ExistingFileHelper helper) {
		createAdvancement("obtain_ancient_hoes", "husbandry", new ResourceLocation("husbandry/obtain_netherite_hoe"), CCItems.NECROMIUM_HOE.get(), FrameType.CHALLENGE, true, true, false)
				.rewards(AdvancementRewards.Builder.experience(100))
				.addCriterion("ancient_hoes", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HOE, CCItems.NECROMIUM_HOE.get()))
				.save(consumer, CavernsAndChasms.MOD_ID + ":husbandry/obtain_ancient_hoes");

		createAdvancement("necromium_armor", "nether", new ResourceLocation("nether/obtain_ancient_debris"), CCItems.NECROMIUM_CHESTPLATE.get(), FrameType.CHALLENGE, true, true, false)
				.rewards(AdvancementRewards.Builder.experience(100))
				.addCriterion("necromium_armor", InventoryChangeTrigger.TriggerInstance.hasItems(CCItems.NECROMIUM_HELMET.get(), CCItems.NECROMIUM_CHESTPLATE.get(), CCItems.NECROMIUM_LEGGINGS.get(), CCItems.NECROMIUM_BOOTS.get()))
				.save(consumer, CavernsAndChasms.MOD_ID + ":nether/necromium_armor");

		createAdvancement("smelt_copper", "adventure", new ResourceLocation("adventure/root"), Items.COPPER_INGOT, FrameType.TASK, true, true, false)
				.addCriterion("copper", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COPPER_INGOT))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/smelt_copper");

		createAdvancement("use_tuning_fork", "adventure", new ResourceLocation(CavernsAndChasms.MOD_ID, "adventure/smelt_copper"), CCItems.TUNING_FORK.get(), FrameType.TASK, true, true, false)
				.addCriterion("use_tuning_fork", CCCriteriaTriggers.USE_TUNING_FORK.createInstance())
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/use_tuning_fork");

		createAdvancement("tune_a_fish", "adventure", new ResourceLocation(CavernsAndChasms.MOD_ID, "adventure/use_tuning_fork"), CCItems.TUNING_FORK.get(), FrameType.TASK, true, true, true)
				.addCriterion("attack_fish", new PlayerHurtEntityTrigger.TriggerInstance(EntityPredicate.wrap(EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of(CCItems.TUNING_FORK.get()).build()).build()).build()), DamagePredicate.ANY, EntityPredicate.wrap(EntityPredicate.Builder.entity().of(BlueprintEntityTypeTags.FISHES).build())))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/tune_a_fish");

		createAdvancement("summon_copper_golem", "adventure", new ResourceLocation(CavernsAndChasms.MOD_ID, "adventure/smelt_copper"), Items.CARVED_PUMPKIN, FrameType.GOAL, true, true, false)
				.addCriterion("summoned_golem", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(CCEntityTypes.COPPER_GOLEM.get())))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/summon_copper_golem");

		createAdvancement("ride_boat_with_deeper", "nether", new ResourceLocation("nether/root"), CCItems.DEEPER_HEAD.get(), FrameType.TASK, true, true, true)
				.addCriterion("ride_boat_with_deeper", StartRidingTrigger.TriggerInstance.playerStartsRiding(EntityPredicate.Builder.entity().located(LocationPredicate.inDimension(Level.NETHER)).vehicle(EntityPredicate.Builder.entity().of(EntityType.BOAT).passenger(EntityPredicate.Builder.entity().of(CCEntityTypes.DEEPER.get()).build()).build())))
				.save(consumer, CavernsAndChasms.MOD_ID + ":nether/ride_boat_with_deeper");

		createAdvancement("dont_move", "adventure", new ResourceLocation("adventure/kill_a_mob"), CCBlocks.PEEPER_HEAD.get(), FrameType.TASK, true, true, true)
				.addCriterion("spotted_by_peeper", CCCriteriaTriggers.SPOTTED_BY_PEEPER.createInstance())
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/dont_move");

		createAdvancement("dismantle_item", "adventure", new ResourceLocation("adventure/trim_with_any_armor_pattern"), CCBlocks.DISMANTLING_TABLE.get(), FrameType.TASK, true, true, false)
				.addCriterion("dismantled_item", CCCriteriaTriggers.DISMANTLED_ITEM.createInstance())
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/dismantle_item");
	}

	private static Advancement.Builder createAdvancement(String name, String category, ResourceLocation parent, ItemLike icon, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
		return Advancement.Builder.advancement().parent(Advancement.Builder.advancement().build(parent)).display(icon,
				Component.translatable("advancements." + CavernsAndChasms.MOD_ID + "." + category + "." + name + ".title"),
				Component.translatable("advancements." + CavernsAndChasms.MOD_ID + "." + category + "." + name + ".description"),
				null, frame, showToast, announceToChat, hidden);
	}
}