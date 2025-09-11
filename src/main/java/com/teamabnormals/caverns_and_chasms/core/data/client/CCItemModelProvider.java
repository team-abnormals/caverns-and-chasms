package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static com.teamabnormals.caverns_and_chasms.core.registry.CCItems.*;


public class CCItemModelProvider extends BlueprintItemModelProvider {

	public CCItemModelProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		this.animatedItem(DEPTH_GAUGE, 48);
		this.animatedItem(BAROMETER, 21);
		this.generatedItem(
				ABNORMALS_BANNER_PATTERN, MUSIC_DISC_EPILOGUE, MUSIC_DISC_COPY,
				COPPER_NUGGET, OXIDIZED_COPPER_GOLEM,
				EXPOSED_COPPER_INGOT, WEATHERED_COPPER_INGOT, OXIDIZED_COPPER_INGOT,
				RAW_SILVER, LARGE_ARROW,
				SILVER_INGOT, SILVER_NUGGET, SILVER_HORSE_ARMOR,
				RAW_TIN, TIN_INGOT, TIN_NUGGET,
				NECROMIUM_INGOT, NECROMIUM_NUGGET, NECROMIUM_HORSE_ARMOR,
				NETHERITE_NUGGET, NETHERITE_HORSE_ARMOR,
				BEJEWELED_APPLE, BLUNT_ARROW, SPINEL, TMT_MINECART, ZIRCONIA,
				TURQUOISE,
				COWL,
				LIVING_FLESH, EXILE_ARMOR_TRIM_SMITHING_TEMPLATE, FORGER_ARMOR_TRIM_SMITHING_TEMPLATE, IMMOLATE_ARMOR_TRIM_SMITHING_TEMPLATE, RIM_ARMOR_TRIM_SMITHING_TEMPLATE, PLATE_ARMOR_TRIM_SMITHING_TEMPLATE, CORE_ARMOR_TRIM_SMITHING_TEMPLATE,
				BOOM_POTTERY_SHERD, CAST_POTTERY_SHERD, RIDE_POTTERY_SHERD, STALKER_POTTERY_SHERD,
				AZALEA_BOAT.getFirst(), AZALEA_BOAT.getSecond(), AZALEA_FURNACE_BOAT, LARGE_AZALEA_BOAT
		);

		this.withExistingParent(name(WAXED_COPPER_INGOT.get()), "item/generated").texture("layer0", new ResourceLocation("item/copper_ingot"));
		this.item(WAXED_EXPOSED_COPPER_INGOT, "exposed_copper_ingot", "generated");
		this.item(WAXED_WEATHERED_COPPER_INGOT, "weathered_copper_ingot", "generated");
		this.item(WAXED_OXIDIZED_COPPER_INGOT, "oxidized_copper_ingot", "generated");

		this.handheldItem(
				COPPER_SWORD, COPPER_PICKAXE, COPPER_AXE, COPPER_SHOVEL, COPPER_HOE,
				EXPOSED_COPPER_SWORD, EXPOSED_COPPER_PICKAXE, EXPOSED_COPPER_AXE, EXPOSED_COPPER_SHOVEL, EXPOSED_COPPER_HOE,
				WEATHERED_COPPER_SWORD, WEATHERED_COPPER_PICKAXE, WEATHERED_COPPER_AXE, WEATHERED_COPPER_SHOVEL, WEATHERED_COPPER_HOE,
				OXIDIZED_COPPER_SWORD, OXIDIZED_COPPER_PICKAXE, OXIDIZED_COPPER_AXE, OXIDIZED_COPPER_SHOVEL, OXIDIZED_COPPER_HOE,
				SILVER_SWORD, SILVER_PICKAXE, SILVER_AXE, SILVER_SHOVEL, SILVER_HOE,
				NECROMIUM_SWORD, NECROMIUM_PICKAXE, NECROMIUM_AXE, NECROMIUM_SHOVEL, NECROMIUM_HOE
		);

		this.item(WAXED_OXIDIZED_COPPER_GOLEM, "oxidized_copper_golem", "generated");
		this.handheldItem(KUNAI);
		this.spawnEggItem(PEEPER_SPAWN_EGG, COPPER_GOLEM_SPAWN_EGG, DEEPER_SPAWN_EGG, MIME_SPAWN_EGG, GLARE_SPAWN_EGG, RAT_SPAWN_EGG, GRAZER_SPAWN_EGG);

		this.trimmableArmorItem(COPPER_HELMET, COPPER_CHESTPLATE, COPPER_LEGGINGS, COPPER_BOOTS);
		this.trimmableArmorItem(EXPOSED_COPPER_HELMET, EXPOSED_COPPER_CHESTPLATE, EXPOSED_COPPER_LEGGINGS, EXPOSED_COPPER_BOOTS);
		this.trimmableArmorItem(WEATHERED_COPPER_HELMET, WEATHERED_COPPER_CHESTPLATE, WEATHERED_COPPER_LEGGINGS, WEATHERED_COPPER_BOOTS);
		this.trimmableArmorItem(OXIDIZED_COPPER_HELMET, OXIDIZED_COPPER_CHESTPLATE, OXIDIZED_COPPER_LEGGINGS, OXIDIZED_COPPER_BOOTS);
		
		this.trimmableArmorItem(SILVER_HELMET, SILVER_CHESTPLATE, SILVER_LEGGINGS, SILVER_BOOTS);
		this.trimmableArmorItem(NECROMIUM_HELMET, NECROMIUM_CHESTPLATE, NECROMIUM_LEGGINGS, NECROMIUM_BOOTS);
		this.trimmableArmorItem(SANGUINE_HELMET, SANGUINE_CHESTPLATE, SANGUINE_LEGGINGS, SANGUINE_BOOTS);
	}
}