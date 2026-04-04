package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
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
				ABNORMALS_BANNER_PATTERN, MUSIC_DISC_ANALOGUE, MUSIC_DISC_EPILOGUE, MUSIC_DISC_COPY,
				COPPER_NUGGET, OXIDIZED_COPPER_GOLEM,
				EXPOSED_COPPER_INGOT, WEATHERED_COPPER_INGOT, OXIDIZED_COPPER_INGOT,
				RAW_SILVER, LARGE_ARROW,
				SILVER_INGOT, SILVER_NUGGET, SILVER_HORSE_ARMOR,
				RAW_TIN, TIN_INGOT, TIN_NUGGET, TINPLATE, RICOCHET_ARROW,
				NECROMIUM_INGOT, NECROMIUM_NUGGET, NECROMIUM_HORSE_ARMOR,
				NETHERITE_NUGGET, NETHERITE_HORSE_ARMOR,
				BEJEWELED_APPLE, BLUNT_ARROW, SPINEL, TMT_MINECART, ZIRCONIA,
				TURQUOISE, UNICORN_HORN,
				CAVEFISH, CAVEFISH_BUCKET,
				COWL,
				TRIM_MODIFIER_SMITHING_TEMPLATE,
				LIVING_FLESH, EXILE_ARMOR_TRIM_SMITHING_TEMPLATE, FORGER_ARMOR_TRIM_SMITHING_TEMPLATE, IMMOLATE_ARMOR_TRIM_SMITHING_TEMPLATE, RIM_ARMOR_TRIM_SMITHING_TEMPLATE, PLATE_ARMOR_TRIM_SMITHING_TEMPLATE, CORE_ARMOR_TRIM_SMITHING_TEMPLATE,
				BOOM_POTTERY_SHERD, CAST_POTTERY_SHERD, RIDE_POTTERY_SHERD, STALKER_POTTERY_SHERD,
				AZALEA_BOAT.getFirst(), AZALEA_BOAT.getSecond(), AZALEA_FURNACE_BOAT, LARGE_AZALEA_BOAT,
				WAXED_EXPOSED_COPPER_INGOT, WAXED_WEATHERED_COPPER_INGOT, WAXED_OXIDIZED_COPPER_INGOT,
				COPPER_HORSE_ARMOR, EXPOSED_COPPER_HORSE_ARMOR, WEATHERED_COPPER_HORSE_ARMOR, OXIDIZED_COPPER_HORSE_ARMOR,
				WAXED_COPPER_HORSE_ARMOR, WAXED_EXPOSED_COPPER_HORSE_ARMOR, WAXED_WEATHERED_COPPER_HORSE_ARMOR, WAXED_OXIDIZED_COPPER_HORSE_ARMOR
		);

		this.overlayItem(TOOLBELT, "generated");
		this.packingContainerItem(PACKING_CONTAINER, "generated");

		this.withExistingParent(name(WAXED_COPPER_INGOT.get()), "item/generated").texture("layer0", new ResourceLocation("item/copper_ingot"));

		this.handheldItem(
				COPPER_SWORD, COPPER_PICKAXE, COPPER_AXE, COPPER_SHOVEL, COPPER_HOE,
				EXPOSED_COPPER_SWORD, EXPOSED_COPPER_PICKAXE, EXPOSED_COPPER_AXE, EXPOSED_COPPER_SHOVEL, EXPOSED_COPPER_HOE,
				WEATHERED_COPPER_SWORD, WEATHERED_COPPER_PICKAXE, WEATHERED_COPPER_AXE, WEATHERED_COPPER_SHOVEL, WEATHERED_COPPER_HOE,
				OXIDIZED_COPPER_SWORD, OXIDIZED_COPPER_PICKAXE, OXIDIZED_COPPER_AXE, OXIDIZED_COPPER_SHOVEL, OXIDIZED_COPPER_HOE,
				WAXED_COPPER_SWORD, WAXED_COPPER_PICKAXE, WAXED_COPPER_AXE, WAXED_COPPER_SHOVEL, WAXED_COPPER_HOE,
				WAXED_EXPOSED_COPPER_SWORD, WAXED_EXPOSED_COPPER_PICKAXE, WAXED_EXPOSED_COPPER_AXE, WAXED_EXPOSED_COPPER_SHOVEL, WAXED_EXPOSED_COPPER_HOE,
				WAXED_WEATHERED_COPPER_SWORD, WAXED_WEATHERED_COPPER_PICKAXE, WAXED_WEATHERED_COPPER_AXE, WAXED_WEATHERED_COPPER_SHOVEL, WAXED_WEATHERED_COPPER_HOE,
				WAXED_OXIDIZED_COPPER_SWORD, WAXED_OXIDIZED_COPPER_PICKAXE, WAXED_OXIDIZED_COPPER_AXE, WAXED_OXIDIZED_COPPER_SHOVEL, WAXED_OXIDIZED_COPPER_HOE,
				SILVER_SWORD, SILVER_PICKAXE, SILVER_AXE, SILVER_SHOVEL, SILVER_HOE,
				NECROMIUM_SWORD, NECROMIUM_PICKAXE, NECROMIUM_AXE, NECROMIUM_SHOVEL, NECROMIUM_HOE
		);

		this.item(WAXED_OXIDIZED_COPPER_GOLEM, "oxidized_copper_golem", "generated");
		this.handheldItem(KUNAI);
		this.spawnEggItem(PEEPER_SPAWN_EGG, COPPER_GOLEM_SPAWN_EGG, DEEPER_SPAWN_EGG, EVENDEEPER_SPAWN_EGG, MIME_SPAWN_EGG, GLARE_SPAWN_EGG, RAT_SPAWN_EGG, CAVEFISH_SPAWN_EGG, GRAZER_SPAWN_EGG, SADDLED_GRAZER_SPAWN_EGG);

		this.trimmableCopperArmorItem(true, COPPER_HELMET, COPPER_CHESTPLATE, COPPER_LEGGINGS, COPPER_BOOTS);
		this.trimmableCopperArmorItem(EXPOSED_COPPER_HELMET, EXPOSED_COPPER_CHESTPLATE, EXPOSED_COPPER_LEGGINGS, EXPOSED_COPPER_BOOTS);
		this.trimmableCopperArmorItem(WEATHERED_COPPER_HELMET, WEATHERED_COPPER_CHESTPLATE, WEATHERED_COPPER_LEGGINGS, WEATHERED_COPPER_BOOTS);
		this.trimmableCopperArmorItem(OXIDIZED_COPPER_HELMET, OXIDIZED_COPPER_CHESTPLATE, OXIDIZED_COPPER_LEGGINGS, OXIDIZED_COPPER_BOOTS);
		this.trimmableCopperArmorItem(true, WAXED_COPPER_HELMET, WAXED_COPPER_CHESTPLATE, WAXED_COPPER_LEGGINGS, WAXED_COPPER_BOOTS);
		this.trimmableCopperArmorItem(WAXED_EXPOSED_COPPER_HELMET, WAXED_EXPOSED_COPPER_CHESTPLATE, WAXED_EXPOSED_COPPER_LEGGINGS, WAXED_EXPOSED_COPPER_BOOTS);
		this.trimmableCopperArmorItem(WAXED_WEATHERED_COPPER_HELMET, WAXED_WEATHERED_COPPER_CHESTPLATE, WAXED_WEATHERED_COPPER_LEGGINGS, WAXED_WEATHERED_COPPER_BOOTS);
		this.trimmableCopperArmorItem(WAXED_OXIDIZED_COPPER_HELMET, WAXED_OXIDIZED_COPPER_CHESTPLATE, WAXED_OXIDIZED_COPPER_LEGGINGS, WAXED_OXIDIZED_COPPER_BOOTS);

		this.trimmableArmorItem(SILVER_HELMET, SILVER_CHESTPLATE, SILVER_LEGGINGS, SILVER_BOOTS);
		this.trimmableArmorItem(NECROMIUM_HELMET, NECROMIUM_CHESTPLATE, NECROMIUM_LEGGINGS, NECROMIUM_BOOTS);
		this.trimmableArmorItem(SANGUINE_HELMET, SANGUINE_CHESTPLATE, SANGUINE_LEGGINGS, SANGUINE_BOOTS);
	}

	public ItemModelBuilder item(RegistryObject<? extends ItemLike> item, String type) {
		return this.withExistingParent(name(item.get()), "item/" + type).texture("layer0", itemTexture(item.get()).toString().replace("waxed_", ""));
	}

	public ItemModelBuilder overlayItem(RegistryObject<? extends ItemLike> item, String type) {
		return this.withExistingParent(name(item.get()), "item/" + type)
				.texture("layer0", itemTexture(item.get()))
				.texture("layer1", itemTexture(item.get()).withSuffix("_overlay"));
	}

	public ItemModelBuilder packingContainerItem(RegistryObject<? extends ItemLike> item, String type) {
		ModelFile dyed = this.withExistingParent(name(item.get()) + "_dyed", "item/" + type)
				.texture("layer0", itemTexture(item.get()))
				.texture("layer1", itemTexture(item.get()).withSuffix("_overlay"));

		ModelFile filled = this.withExistingParent(name(item.get()) + "_filled", "item/" + type)
				.texture("layer0", itemTexture(item.get()).withSuffix("_filled"));

		ModelFile dyedFilled = this.withExistingParent(name(item.get()) + "_dyed_filled", "item/" + type)
				.texture("layer0", itemTexture(item.get()).withSuffix("_filled"))
				.texture("layer1", itemTexture(item.get()).withSuffix("_overlay"));

		return this.withExistingParent(name(item.get()), "item/" + type).texture("layer0", itemTexture(item.get()))
				.override().model(filled).predicate(new ResourceLocation("dyed"), 0).predicate(new ResourceLocation("filled"), 0.0000001F).end()
				.override().model(dyed).predicate(new ResourceLocation("dyed"), 1).end()
				.override().model(dyedFilled).predicate(new ResourceLocation("dyed"), 1).predicate(new ResourceLocation("filled"), 0.0000001F).end();
	}

	@SafeVarargs
	public final void trimmableCopperArmorItem(RegistryObject<? extends ItemLike>... items) {
		this.trimmableCopperArmorItem(false, items);
	}

	@SafeVarargs
	public final void trimmableCopperArmorItem(boolean darker, RegistryObject<? extends ItemLike>... items) {
		for (RegistryObject<? extends ItemLike> item : items) {
			if (item.get().asItem() instanceof ArmorItem armor) {
				ResourceLocation location = ForgeRegistries.ITEMS.getKey(armor);
				ItemModelBuilder itemModel = this.item(item, "generated");
				int trimType = 1;
				for (String trim : new String[]{"quartz", "iron", "netherite", "redstone", darker ? "caverns_and_chasms_copper_darker" : "copper", "gold", "emerald", "diamond", "lapis", "amethyst"}) {
					ResourceLocation name = new ResourceLocation(location.getNamespace(), "item/" + location.getPath() + "_" + trim + "_trim");
					itemModel.override().model(new UncheckedModelFile(name)).predicate(new ResourceLocation("trim_type"), (float) (trimType / 10.0));
					ResourceLocation texture = new ResourceLocation("trims/items/" + armor.getType().getName() + "_trim_" + trim);
					this.existingFileHelper.trackGenerated(texture, PackType.CLIENT_RESOURCES, ".png", "textures");
					withExistingParent(name.getPath(), "item/generated").texture("layer0", new ResourceLocation(this.modid, "item/" + location.getPath().replace("waxed_", ""))).texture("layer1", texture);
					trimType++;
				}
			}
		}
	}
}