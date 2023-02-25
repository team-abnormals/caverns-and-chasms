package com.teamabnormals.caverns_and_chasms.core.data.server.modifiers;

import com.teamabnormals.blueprint.common.advancement.modification.AdvancementModifierProvider;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.CriteriaModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.DisplayInfoModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.EffectsChangedModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.ParentModifier;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMobEffects;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collection;

public class CCAdvancementModifierProvider extends AdvancementModifierProvider {
	private static final EntityType<?>[] BREEDABLE_ANIMALS = new EntityType[]{CCEntityTypes.RAT.get()};
	private static final EntityType<?>[] MOBS_TO_KILL = new EntityType[]{CCEntityTypes.DEEPER.get(), CCEntityTypes.MIME.get()};

	public CCAdvancementModifierProvider(DataGenerator dataGenerator) {
		super(dataGenerator, CavernsAndChasms.MOD_ID);
	}

	@Override
	protected void registerEntries() {
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

		CriteriaModifier.Builder breedAllAnimals = CriteriaModifier.builder(this.modId);
		for (EntityType<?> entityType : BREEDABLE_ANIMALS) {
			breedAllAnimals.addCriterion(ForgeRegistries.ENTITY_TYPES.getKey(entityType).getPath(), BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(entityType)));
		}
		this.entry("husbandry/bred_all_animals").selects("husbandry/bred_all_animals").addModifier(breedAllAnimals.requirements(RequirementsStrategy.AND).build());

		this.entry("husbandry/wax_on").selects("husbandry/wax_on").addModifier(CriteriaModifier.builder(this.modId).addCriterion("wax_on", ItemInteractWithBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(CCBlockTags.WAXABLE_COPPER_BLOCKS).build()), ItemPredicate.Builder.item().of(Items.HONEYCOMB))).addIndexedRequirements(0, false, "wax_on").build());
		this.entry("husbandry/wax_off").selects("husbandry/wax_off").addModifier(CriteriaModifier.builder(this.modId).addCriterion("wax_off", ItemInteractWithBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(CCBlockTags.WAXED_COPPER_BLOCKS).build()), ItemPredicate.Builder.item().of(CCItemTags.TOOLS_AXES))).addIndexedRequirements(0, false, "wax_off").build());

		this.entry("husbandry/obtain_netherite_hoe").selects("husbandry/obtain_netherite_hoe")
				.addModifier(DisplayInfoModifier.builder().description(Component.translatable("advancements." + this.modId + ".husbandry.netherite_hoe.description")).build())
				.addModifier(CriteriaModifier.builder(this.modId).addCriterion("necromium_hoe", InventoryChangeTrigger.TriggerInstance.hasItems(CCItems.NECROMIUM_HOE.get())).addIndexedRequirements(0, false, "necromium_hoe").build());

		this.entry("adventure/smelt_copper_parent").selects("adventure/spyglass_at_parrot", "adventure/lightning_rod_with_villager_no_fire").addModifier(new ParentModifier(new ResourceLocation(this.modId, "adventure/smelt_copper")));

		CriteriaModifier.Builder killAMob = CriteriaModifier.builder(this.modId);
		CriteriaModifier.Builder killAllMobs = CriteriaModifier.builder(this.modId);
		ArrayList<String> names = org.apache.commons.compress.utils.Lists.newArrayList();
		for (EntityType<?> entityType : MOBS_TO_KILL) {
			String name = ForgeRegistries.ENTITY_TYPES.getKey(entityType).getPath();
			KilledTrigger.TriggerInstance triggerInstance = KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entityType));
			killAMob.addCriterion(name, triggerInstance);
			killAllMobs.addCriterion(name, triggerInstance);
			names.add(name);
		}

		this.entry("adventure/kill_a_mob").selects("adventure/kill_a_mob").addModifier(killAMob.addIndexedRequirements(0, false, names.toArray(new String[0])).build());
		this.entry("adventure/kill_all_mobs").selects("adventure/kill_all_mobs").addModifier(killAllMobs.requirements(RequirementsStrategy.AND).build());
	}
}