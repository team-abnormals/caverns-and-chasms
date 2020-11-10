package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.common.item.GoldenBucketItem;
import com.minecraftabnormals.caverns_and_chasms.common.item.GoldenMilkBucketItem;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCItems {
	public static final RegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER;

	public static final RegistryObject<Item> CAVEFISH = HELPER.createItem("cavefish", () -> new Item(new Item.Properties().group(ItemGroup.FOOD).food(Foods.CAVEFISH)));
	public static final RegistryObject<Item> COOKED_CAVEFISH = HELPER.createItem("cooked_cavefish", () -> new Item(new Item.Properties().group(ItemGroup.FOOD).food(Foods.COOKED_CAVEFISH)));
	public static final RegistryObject<Item> CAVEFISH_BUCKET = HELPER.createItem("cavefish_bucket", () -> new FishBucketItem(() -> CCEntities.CAVEFISH.get(), () -> Fluids.WATER, new Item.Properties().maxStackSize(1).group(ItemGroup.MISC)));

	public static final RegistryObject<Item> GOLDEN_BUCKET = HELPER.createItem("golden_bucket", () -> new GoldenBucketItem(() -> Fluids.EMPTY, new Item.Properties().group(ItemGroup.MISC).maxStackSize(16)));
	public static final RegistryObject<Item> GOLDEN_WATER_BUCKET = HELPER.createItem("golden_water_bucket", () -> new GoldenBucketItem(() -> Fluids.WATER, new Item.Properties().containerItem(GOLDEN_BUCKET.get()).group(ItemGroup.MISC).maxStackSize(1)));
	public static final RegistryObject<Item> GOLDEN_LAVA_BUCKET = HELPER.createItem("golden_lava_bucket", () -> new GoldenBucketItem(() -> Fluids.LAVA, new Item.Properties().containerItem(GOLDEN_BUCKET.get()).group(ItemGroup.MISC).maxStackSize(1)));
	public static final RegistryObject<Item> GOLDEN_MILK_BUCKET = HELPER.createItem("golden_milk_bucket", () -> new GoldenMilkBucketItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1)));

	public static final RegistryObject<Item> CAVEFISH_SPAWN_EGG = HELPER.createSpawnEggItem("cavefish", () -> CCEntities.CAVEFISH.get(), 14145236, 11251356);
	public static final RegistryObject<Item> DEEPER_SPAWN_EGG = HELPER.createSpawnEggItem("deeper", () -> CCEntities.DEEPER.get(), 8355711, 13717260);

	static class Foods {
		public static final Food CAVEFISH = new Food.Builder().hunger(1).saturation(0.3F).build();
		public static final Food COOKED_CAVEFISH = new Food.Builder().hunger(4).saturation(0.25F).build();
	}

	public static void registerItemProperties() {
		ItemModelsProperties.func_239418_a_(GOLDEN_BUCKET.get(), new ResourceLocation("level"), (stack, world, entity) -> {
			return stack.getOrCreateTag().getInt("FluidLevel");
		});
		ItemModelsProperties.func_239418_a_(GOLDEN_WATER_BUCKET.get(), new ResourceLocation("level"), (stack, world, entity) -> {
			return stack.getOrCreateTag().getInt("FluidLevel");
		});
		ItemModelsProperties.func_239418_a_(GOLDEN_LAVA_BUCKET.get(), new ResourceLocation("level"), (stack, world, entity) -> {
			return stack.getOrCreateTag().getInt("FluidLevel");
		});
		ItemModelsProperties.func_239418_a_(GOLDEN_MILK_BUCKET.get(), new ResourceLocation("level"), (stack, world, entity) -> {
			return stack.getOrCreateTag().getInt("FluidLevel");
		});
	}
}
