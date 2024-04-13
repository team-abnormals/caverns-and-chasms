package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

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
				ABNORMALS_BANNER_PATTERN,
				COPPER_NUGGET, OXIDIZED_COPPER_GOLEM,
				RAW_SILVER, LARGE_ARROW,
				BEJEWELED_APPLE, BLUNT_ARROW,
				LIVING_FLESH,
				AZALEA_BOAT.getFirst(), AZALEA_BOAT.getSecond(), AZALEA_FURNACE_BOAT, LARGE_AZALEA_BOAT
		);
		this.item(WAXED_OXIDIZED_COPPER_GOLEM, "oxidized_copper_golem", "generated");
		this.handheldItem(KUNAI);
		this.spawnEggItem(PEEPER_SPAWN_EGG, COPPER_GOLEM_SPAWN_EGG);

		this.trimmableArmorItem(SILVER_HELMET, SILVER_CHESTPLATE, SILVER_LEGGINGS, SILVER_BOOTS);
		this.trimmableArmorItem(NECROMIUM_HELMET, NECROMIUM_CHESTPLATE, NECROMIUM_LEGGINGS, NECROMIUM_BOOTS);
		this.trimmableArmorItem(SANGUINE_HELMET, SANGUINE_CHESTPLATE, SANGUINE_LEGGINGS, SANGUINE_BOOTS);
	}
}