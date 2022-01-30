package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class CCAdvancementProvider extends AdvancementProvider {

	public CCAdvancementProvider(DataGenerator generator, ExistingFileHelper fileHelper) {
		super(generator, fileHelper);
	}

	@Override
	protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
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
				.addCriterion("use_tuning_fork", ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(Blocks.NOTE_BLOCK).build()), ItemPredicate.Builder.item().of(CCItems.TUNING_FORK.get())))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/use_tuning_fork");

		createAdvancement("tune_a_fish", "adventure", new ResourceLocation(CavernsAndChasms.MOD_ID, "adventure/use_tuning_fork"), CCItems.TUNING_FORK.get(), FrameType.TASK, true, true, true)
				.addCriterion("attack_fish", new PlayerHurtEntityTrigger.TriggerInstance(EntityPredicate.Composite.wrap(EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of(CCItems.TUNING_FORK.get()).build()).build()).build()), DamagePredicate.ANY, EntityPredicate.Composite.wrap(EntityPredicate.Builder.entity().of(CCEntityTypeTags.FISHES).build())))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/tune_a_fish");

		createAdvancement("summon_copper_golem", "adventure", new ResourceLocation(CavernsAndChasms.MOD_ID, "adventure/smelt_copper"), Items.CARVED_PUMPKIN, FrameType.GOAL, true, true, false)
				.addCriterion("summoned_golem", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(CCEntityTypes.COPPER_GOLEM.get())))
				.save(consumer, CavernsAndChasms.MOD_ID + ":adventure/summon_copper_golem");
	}

	private static Advancement.Builder createAdvancement(String name, String category, ResourceLocation parent, ItemLike icon, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
		return Advancement.Builder.advancement().parent(Advancement.Builder.advancement().build(parent)).display(icon,
				new TranslatableComponent("advancements." + CavernsAndChasms.MOD_ID + "." + category + "." + name + ".title"),
				new TranslatableComponent("advancements." + CavernsAndChasms.MOD_ID + "." + category + "." + name + ".description"),
				null, frame, showToast, announceToChat, hidden);
	}
}