package com.teamabnormals.caverns_and_chasms.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.caverns_and_chasms.client.DeeperSpriteUploader;
import com.teamabnormals.caverns_and_chasms.client.model.*;
import com.teamabnormals.caverns_and_chasms.client.render.*;
import com.teamabnormals.caverns_and_chasms.client.render.layer.UndeadParrotLayer;
import com.teamabnormals.caverns_and_chasms.client.render.skeleton.SkeletonCatRenderer;
import com.teamabnormals.caverns_and_chasms.client.render.skeleton.SkeletonParrotRenderer;
import com.teamabnormals.caverns_and_chasms.client.render.skeleton.SkeletonWolfRenderer;
import com.teamabnormals.caverns_and_chasms.client.render.zombie.ZombieCatRenderer;
import com.teamabnormals.caverns_and_chasms.client.render.zombie.ZombieParrotRenderer;
import com.teamabnormals.caverns_and_chasms.client.render.zombie.ZombieWolfRenderer;
import com.teamabnormals.caverns_and_chasms.common.item.ForgottenCollarItem;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCBlockStateProvider;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCItemModelProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.CCLootTableProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.CCRecipeProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.modifiers.CCLootModifiersProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.tags.CCBlockTagsProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.tags.CCEntityTypeTagsProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.tags.CCItemTagsProvider;
import com.teamabnormals.caverns_and_chasms.core.other.CCClientCompat;
import com.teamabnormals.caverns_and_chasms.core.other.CCCompat;
import com.teamabnormals.caverns_and_chasms.core.registry.*;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.Serializers;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod(CavernsAndChasms.MOD_ID)
public class CavernsAndChasms {
	public static final String MOD_ID = "caverns_and_chasms";
	public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

	/*
	 * TODO:
	 *  Fix GoldenBucketItem
	 *  Fix SanguineArmorModel
	 */
	public CavernsAndChasms() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.register(this);

		REGISTRY_HELPER.register(bus);
		CCAttributes.ATTRIBUTES.register(bus);
		CCEffects.POTIONS.register(bus);
		CCEffects.EFFECTS.register(bus);
		CCEnchantments.ENCHANTMENTS.register(bus);
		CCParticleTypes.PARTICLES.register(bus);
		Serializers.RECIPE_SERIALIZERS.register(bus);

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::dataSetup);

		bus.addListener(this::registerLayerDefinitions);
		bus.addListener(this::registerRenderers);
		bus.addListener(this::registerLayers);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.addListener(this::registerItemColors);
			DeeperSpriteUploader.init(bus);
		});

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CCConfig.COMMON_SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			CCEntityTypes.registerEntitySpawns();
			CCFeatures.Configured.registerConfiguredFeatures();
			CCEffects.registerBrewingRecipes();
			CCCompat.registerCompat();
			CCEnchantments.registerEnchantmentTypes();
		});
	}

	private void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(CCClientCompat::registerClientCompat);
	}

	private void dataSetup(GatherDataEvent event) {
		DataGenerator dataGenerator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		if (event.includeServer()) {
			CCBlockTagsProvider blockTagGen = new CCBlockTagsProvider(dataGenerator, existingFileHelper);
			dataGenerator.addProvider(blockTagGen);
			dataGenerator.addProvider(new CCItemTagsProvider(dataGenerator, blockTagGen, existingFileHelper));
			dataGenerator.addProvider(new CCEntityTypeTagsProvider(dataGenerator, existingFileHelper));
			dataGenerator.addProvider(new CCRecipeProvider(dataGenerator));
			dataGenerator.addProvider(new CCLootTableProvider(dataGenerator));
			dataGenerator.addProvider(CCLootModifiersProvider.createLootModifierDataProvider(dataGenerator));
		}

		if (event.includeClient()) {
			dataGenerator.addProvider(new CCItemModelProvider(dataGenerator, existingFileHelper));
			dataGenerator.addProvider(new CCBlockStateProvider(dataGenerator, existingFileHelper));
			//dataGenerator.addProvider(new CCLanguageProvider(dataGenerator));
		}
	}

	private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CavefishModel.LOCATION, CavefishModel::createLayerDefinition);
		event.registerLayerDefinition(DeeperModel.LOCATION, DeeperModel::createLayerDefinition);
		event.registerLayerDefinition(FlyModel.LOCATION, FlyModel::createLayerDefinition);
		event.registerLayerDefinition(MimeArmorModel.LOCATION, () -> MimeArmorModel.createLayerDefinition(0.0F));
		event.registerLayerDefinition(MimeModel.LOCATION, MimeModel::createLayerDefinition);
		event.registerLayerDefinition(RatModel.LOCATION, RatModel::createLayerDefinition);
		event.registerLayerDefinition(SanguineArmorModel.LOCATION, SanguineArmorModel::createLayerDefinition);
	}

	private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CCEntityTypes.CAVEFISH.get(), CavefishRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.DEEPER.get(), DeeperRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.SPIDERLING.get(), SpiderlingRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.SILVER_ARROW.get(), SilverArrowRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.KUNAI.get(), KunaiRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.FLY.get(), FlyRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.MIME.get(), MimeRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.RAT.get(), RatRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.ZOMBIE_WOLF.get(), ZombieWolfRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.ZOMBIE_CAT.get(), ZombieCatRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.ZOMBIE_PARROT.get(), ZombieParrotRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.SKELETON_WOLF.get(), SkeletonWolfRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.SKELETON_CAT.get(), SkeletonCatRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.SKELETON_PARROT.get(), SkeletonParrotRenderer::new);

		event.registerBlockEntityRenderer(CCBlockEntityTypes.CURSED_CAMPFIRE.get(), CampfireRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
	private void registerItemColors(ColorHandlerEvent.Item event) {
		event.getItemColors().register((stack, color) -> color > 0 ? -1 : ((ForgottenCollarItem) stack.getItem()).getColor(stack), CCItems.FORGOTTEN_COLLAR.get());
	}

	private void registerLayers(EntityRenderersEvent.AddLayers event) {
		event.getSkins().forEach(skin -> {
			PlayerRenderer renderer = event.getSkin(skin);
			renderer.addLayer(new UndeadParrotLayer<>(renderer, event.getEntityModels()));
		});
	}
}
