package com.teamabnormals.caverns_and_chasms.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.caverns_and_chasms.client.model.*;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.*;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatOnShoulderLayer;
import com.teamabnormals.caverns_and_chasms.client.resources.DeeperSpriteUploader;
import com.teamabnormals.caverns_and_chasms.common.item.TuningForkItem;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCBlockStateProvider;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCItemModelProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.CCAdvancementProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.CCLootTableProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.CCRecipeProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.modifiers.CCAdvancementModifierProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.modifiers.CCBiomeModifierProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.modifiers.CCLootModifierProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.tags.*;
import com.teamabnormals.caverns_and_chasms.core.other.CCClientCompat;
import com.teamabnormals.caverns_and_chasms.core.other.CCCompat;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.registry.*;
import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures.CCConfiguredFeatures;
import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures.CCPlacedFeatures;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeTypes;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CavernsAndChasms.MOD_ID)
public class CavernsAndChasms {
	public static final String MOD_ID = "caverns_and_chasms";
	public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

	public CavernsAndChasms() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext context = ModLoadingContext.get();
		MinecraftForge.EVENT_BUS.register(this);

		CCDataProcessors.registerTrackedData();

		REGISTRY_HELPER.register(bus);
		CCEntityTypes.ENTITIES.register(bus);
		CCAttributes.ATTRIBUTES.register(bus);
		CCMobEffects.POTIONS.register(bus);
		CCMobEffects.MOB_EFFECTS.register(bus);
		CCFeatures.FEATURES.register(bus);
		CCParticleTypes.PARTICLE_TYPES.register(bus);
		CCRecipeSerializers.RECIPE_SERIALIZERS.register(bus);
		CCConfiguredFeatures.CONFIGURED_FEATURES.register(bus);
		CCPlacedFeatures.PLACED_FEATURES.register(bus);
		CCRecipeTypes.RECIPE_TYPES.register(bus);
		CCBiomeModifierTypes.BIOME_MODIFIER_SERIALIZERS.register(bus);
		CCMemoryModuleTypes.MEMORY_MODULE_TYPES.register(bus);
		CCPaintingVariants.PAINTING_VARIANTS.register(bus);

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::dataSetup);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.addListener(this::registerLayerDefinitions);
			bus.addListener(this::registerRenderers);
			bus.addListener(this::registerLayers);
			bus.addListener(this::registerItemColors);
		});

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> DeeperSpriteUploader.init(bus));

		context.registerConfig(ModConfig.Type.COMMON, CCConfig.COMMON_SPEC);
		context.registerConfig(ModConfig.Type.CLIENT, CCConfig.CLIENT_SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			CCCompat.registerCompat();
			CCEntityTypes.registerEntitySpawns();
			CCMobEffects.registerBrewingRecipes();
		});
	}

	private void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(CCClientCompat::registerClientCompat);
	}

	private void dataSetup(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		boolean server = event.includeServer();
		CCBlockTagsProvider blockTags = new CCBlockTagsProvider(generator, helper);
		generator.addProvider(server, blockTags);
		generator.addProvider(server, new CCItemTagsProvider(generator, blockTags, helper));
		generator.addProvider(server, new CCEntityTypeTagsProvider(generator, helper));
		generator.addProvider(server, new CCMobEffectTagsProvider(generator, helper));
		generator.addProvider(server, new CCBiomeTagsProvider(generator, helper));
		generator.addProvider(server, new CCPaintingVariantTagsProvider(generator, helper));
		generator.addProvider(server, new CCRecipeProvider(generator));
		generator.addProvider(server, new CCLootTableProvider(generator));
		generator.addProvider(server, new CCAdvancementProvider(generator, helper));
		generator.addProvider(server, new CCAdvancementModifierProvider(generator));
		generator.addProvider(server, new CCLootModifierProvider(generator));
		generator.addProvider(server, CCBiomeModifierProvider.create(generator, helper));

		boolean client = event.includeClient();
		generator.addProvider(client, new CCItemModelProvider(generator, helper));
		generator.addProvider(client, new CCBlockStateProvider(generator, helper));
		//generator.addProvider(client, new CCLanguageProvider(generator));
	}

	@OnlyIn(Dist.CLIENT)
	private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CavefishModel.LOCATION, CavefishModel::createLayerDefinition);
		event.registerLayerDefinition(DeeperModel.LOCATION, DeeperModel::createLayerDefinition);
		event.registerLayerDefinition(FlyModel.LOCATION, FlyModel::createLayerDefinition);
		event.registerLayerDefinition(MimeModel.LOCATION, MimeModel::createLayerDefinition);
		event.registerLayerDefinition(RatModel.LOCATION, RatModel::createLayerDefinition);
		event.registerLayerDefinition(CopperGolemModel.LOCATION, CopperGolemModel::createLayerDefinition);
		event.registerLayerDefinition(SanguineArmorModel.LOCATION, SanguineArmorModel::createLayerDefinition);
		event.registerLayerDefinition(MimeArmorModel.LOCATION, () -> MimeArmorModel.createLayerDefinition(0.0F));
		event.registerLayerDefinition(GlareModel.LOCATION, GlareModel::createBodyLayer);
	}

	@OnlyIn(Dist.CLIENT)
	private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CCEntityTypes.CAVEFISH.get(), CavefishRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.DEEPER.get(), DeeperRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.SPIDERLING.get(), SpiderlingRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.KUNAI.get(), KunaiRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.FLY.get(), FlyRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.MIME.get(), MimeRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.RAT.get(), RatRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.OXIDIZED_COPPER_GOLEM.get(), OxidizedCopperGolemRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.BEJEWELED_PEARL.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.TMT.get(), TmtRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.BLUNT_ARROW.get(), BluntArrowRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.GLARE.get(), GlareRenderer::new);

		event.registerBlockEntityRenderer(CCBlockEntityTypes.CUPRIC_CAMPFIRE.get(), CampfireRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
	public void registerLayers(EntityRenderersEvent.AddLayers event) {
		event.getSkins().forEach(skin -> {
			PlayerRenderer renderer = event.getSkin(skin);
			renderer.addLayer(new RatOnShoulderLayer(renderer));
		});
	}

	@OnlyIn(Dist.CLIENT)
	public void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, color) -> color > 0 ? -1 : TuningForkItem.getNoteColor(stack), CCItems.TUNING_FORK.get());
		event.register((stack, color) -> color > 0 ? -1 : PotionUtils.getColor(stack), CCItems.TETHER_POTION.get());
	}
}
