package com.teamabnormals.caverns_and_chasms.core;

import com.teamabnormals.blueprint.core.api.BlueprintTrims;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.caverns_and_chasms.client.CCShaders;
import com.teamabnormals.caverns_and_chasms.client.model.*;
import com.teamabnormals.caverns_and_chasms.client.renderer.block.AtoningTableRenderer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.*;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatOnShoulderLayer;
import com.teamabnormals.caverns_and_chasms.client.resources.DeeperSpriteUploader;
import com.teamabnormals.caverns_and_chasms.common.item.TuningForkItem;
import com.teamabnormals.caverns_and_chasms.common.network.S2CSpinelBoomMessage;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCBlockStateProvider;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCItemModelProvider;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCSpriteSourceProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.CCAdvancementProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.CCDatapackBuiltinEntriesProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.CCLootTableProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.CCRecipeProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.modifiers.CCAdvancementModifierProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.modifiers.CCLootModifierProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.tags.*;
import com.teamabnormals.caverns_and_chasms.core.other.*;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers.CCArmorMaterials;
import com.teamabnormals.caverns_and_chasms.core.registry.*;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCSkullTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCStructurePieceTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.helper.CCBlockSubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.integration.quark.ToolboxTooltips.ToolboxComponent;
import com.teamabnormals.gallery.core.data.client.GalleryAssetsRemolderProvider;
import com.teamabnormals.gallery.core.data.client.GalleryItemModelProvider;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Mod(CavernsAndChasms.MOD_ID)
public class CavernsAndChasms {
	public static final String MOD_ID = "caverns_and_chasms";
	public static final String NETWORK_PROTOCOL = "CC1";
	public static final RegistryHelper REGISTRY_HELPER = RegistryHelper.create(MOD_ID, helper -> helper.putSubHelper(ForgeRegistries.BLOCKS, new CCBlockSubRegistryHelper(helper)));

	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "net")).networkProtocolVersion(() -> NETWORK_PROTOCOL).clientAcceptedVersions(NETWORK_PROTOCOL::equals).serverAcceptedVersions(NETWORK_PROTOCOL::equals).simpleChannel();

	public CavernsAndChasms() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext context = ModLoadingContext.get();
		MinecraftForge.EVENT_BUS.register(this);

		this.setupMessages();
		CCDataProcessors.registerTrackedData();

		REGISTRY_HELPER.register(bus);
		CCEntityTypes.ENTITY_TYPES.register(bus);
		CCAttributes.ATTRIBUTES.register(bus);
		CCMobEffects.POTIONS.register(bus);
		CCMobEffects.MOB_EFFECTS.register(bus);
		CCFeatures.FEATURES.register(bus);
		CCStructureTypes.STRUCTURE_TYPES.register(bus);
		CCStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(bus);
		CCParticleTypes.PARTICLE_TYPES.register(bus);
		CCRecipeSerializers.RECIPE_SERIALIZERS.register(bus);
		CCRecipeTypes.RECIPE_TYPES.register(bus);
		CCPlacementModifierTypes.PLACEMENT_MODIFIER_TYPES.register(bus);
		CCBiomeModifierTypes.BIOME_MODIFIER_SERIALIZERS.register(bus);
		CCPaintingVariants.PAINTING_VARIANTS.register(bus);
		CCMenuTypes.MENU_TYPES.register(bus);
		CCInstruments.INSTRUMENTS.register(bus);
		CCGameEvents.GAME_EVENTS.register(bus);
		CCPoiTypes.POI_TYPES.register(bus);
		CCBannerPatterns.BANNER_PATTERNS.register(bus);

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::dataSetup);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			CCItems.setupTabEditors();
			CCBlocks.setupTabEditors();
			bus.addListener(this::registerLayerDefinitions);
			bus.addListener(this::registerRenderers);
			bus.addListener(this::registerLayers);
			bus.addListener(this::registerItemColors);
			bus.addListener(this::createSkullModels);
			bus.addListener(this::registerClientTooltips);
			bus.addListener(CCShaders::registerShaders);
		});

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> DeeperSpriteUploader.init(bus));

		context.registerConfig(ModConfig.Type.COMMON, CCConfig.COMMON_SPEC);
		context.registerConfig(ModConfig.Type.CLIENT, CCConfig.CLIENT_SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			CCCompat.registerCompat();
			CCMobEffects.registerBrewingRecipes();
		});
	}

	private void clientSetup(FMLClientSetupEvent event) {
		BlueprintTrims.registerArmorMaterialOverrides(CCTrimMaterials.SILVER, Map.of(CCArmorMaterials.SILVER, CavernsAndChasms.MOD_ID + "_silver_darker"));
		BlueprintTrims.registerArmorMaterialOverrides(CCTrimMaterials.NECROMIUM, Map.of(CCArmorMaterials.NECROMIUM, CavernsAndChasms.MOD_ID + "_necromium_darker"));
		BlueprintTrims.registerArmorMaterialOverrides(CCTrimMaterials.SANGUINE, Map.of(CCArmorMaterials.SANGUINE, CavernsAndChasms.MOD_ID + "_sanguine_darker"));

		event.enqueueWork(() -> {
			SkullBlockRenderer.SKIN_BY_TYPE.put(CCSkullTypes.DEEPER, new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/deeper/deeper.png"));
			SkullBlockRenderer.SKIN_BY_TYPE.put(CCSkullTypes.PEEPER, new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/peeper/peeper.png"));
			SkullBlockRenderer.SKIN_BY_TYPE.put(CCSkullTypes.MIME, MimeRenderer.MIME_TEXTURE);
			CCMenuTypes.registerScreenFactories();
			CCClientCompat.registerClientCompat();
		});
	}

	private void dataSetup(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<Provider> provider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		boolean server = event.includeServer();

		CCDatapackBuiltinEntriesProvider datapackEntries = new CCDatapackBuiltinEntriesProvider(output, provider);
		generator.addProvider(server, datapackEntries);
		provider = datapackEntries.getRegistryProvider();

		CCBlockTagsProvider blockTags = new CCBlockTagsProvider(output, provider, helper);
		generator.addProvider(server, blockTags);
		generator.addProvider(server, new CCItemTagsProvider(output, provider, blockTags.contentsGetter(), helper));
		generator.addProvider(server, new CCEntityTypeTagsProvider(output, provider, helper));
		generator.addProvider(server, new CCMobEffectTagsProvider(output, provider, helper));
		generator.addProvider(server, new CCBiomeTagsProvider(output, provider, helper));
		generator.addProvider(server, new CCPaintingVariantTagsProvider(output, provider, helper));
		generator.addProvider(server, new CCBannerPatternTagsProvider(output, provider, helper));
		generator.addProvider(server, new CCInstrumentTagsProvider(output, provider, helper));
		generator.addProvider(server, new CCGameEventTagsProvider(output, provider, helper));
		generator.addProvider(server, new CCDamageTypeTagsProvider(output, provider, helper));
		generator.addProvider(server, new CCTrimMaterialTagsProvider(output, provider, helper));
		generator.addProvider(server, new CCRecipeProvider(output));
		generator.addProvider(server, new CCLootTableProvider(output));
		generator.addProvider(server, CCAdvancementProvider.create(output, provider, helper));
		generator.addProvider(server, new CCAdvancementModifierProvider(output, provider));
		generator.addProvider(server, new CCLootModifierProvider(output, provider));

		boolean client = event.includeClient();
		generator.addProvider(client, new CCItemModelProvider(output, helper));
		generator.addProvider(client, new CCBlockStateProvider(output, helper));
		generator.addProvider(client, new CCSpriteSourceProvider(output, helper));
		//generator.addProvider(client, new CCLanguageProvider(generator));

		generator.addProvider(client, new GalleryItemModelProvider(MOD_ID, output, helper));
		generator.addProvider(client, new GalleryAssetsRemolderProvider(MOD_ID, output, provider));
	}

	@OnlyIn(Dist.CLIENT)
	private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CCModelLayers.DEEPER, () -> DeeperModel.createBodyLayer(CubeDeformation.NONE));
		event.registerLayerDefinition(CCModelLayers.DEEPER_HEAD, SkullModel::createHumanoidHeadLayer);
		event.registerLayerDefinition(CCModelLayers.DEEPER_ARMOR, () -> DeeperModel.createBodyLayer(new CubeDeformation(2.0F)));
		event.registerLayerDefinition(CCModelLayers.PEEPER, () -> PeeperModel.createBodyLayer(CubeDeformation.NONE));
		event.registerLayerDefinition(CCModelLayers.PEEPER_HEAD, PeeperHeadModel::createHeadLayer);
		event.registerLayerDefinition(CCModelLayers.PEEPER_ARMOR, () -> PeeperModel.createBodyLayer(new CubeDeformation(2.0F)));
		event.registerLayerDefinition(CCModelLayers.MIME, MimeModel::createBodyLayer);
		event.registerLayerDefinition(CCModelLayers.MIME_HEAD, MimeHeadModel::createHeadLayer);
		event.registerLayerDefinition(CCModelLayers.FLY, FlyModel::createBodyLayer);
		event.registerLayerDefinition(CCModelLayers.RAT, RatModel::createBodyLayer);
		event.registerLayerDefinition(CCModelLayers.COPPER_GOLEM, CopperGolemModel::createBodyLayer);
		event.registerLayerDefinition(CCModelLayers.GLARE, GlareModel::createBodyLayer);
		event.registerLayerDefinition(CCModelLayers.TOOLBOX, ToolboxRenderer::createBodyLayer);
		event.registerLayerDefinition(CCModelLayers.LOST_GOAT, LostGoatModel::createBodyLayer);
	}

	@OnlyIn(Dist.CLIENT)
	private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CCEntityTypes.DEEPER.get(), DeeperRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.PEEPER.get(), PeeperRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.KUNAI.get(), KunaiRenderer::new);
//		event.registerEntityRenderer(CCEntityTypes.FLY.get(), FlyRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.MIME.get(), MimeRenderer::new);
		// event.registerEntityRenderer(CCEntityTypes.RAT.get(), RatRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.OXIDIZED_COPPER_GOLEM.get(), OxidizedCopperGolemRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.BEJEWELED_PEARL.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.TMT.get(), TmtRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.BLUNT_ARROW.get(), BluntArrowRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.BLUNT_ARROW.get(), BluntArrowRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.LARGE_ARROW.get(), LargeArrowRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.GLARE.get(), GlareRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.LOST_GOAT.get(), LostGoatRenderer::new);

		event.registerBlockEntityRenderer(CCBlockEntityTypes.CUPRIC_CAMPFIRE.get(), CampfireRenderer::new);
		event.registerBlockEntityRenderer(CCBlockEntityTypes.SKULL.get(), SkullBlockRenderer::new);
		event.registerBlockEntityRenderer(CCBlockEntityTypes.TOOLBOX.get(), ToolboxRenderer::new);
		event.registerBlockEntityRenderer(CCBlockEntityTypes.ATONING_TABLE.get(), AtoningTableRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
	public void registerLayers(EntityRenderersEvent.AddLayers event) {
		event.getSkins().forEach(skin -> {
			PlayerRenderer renderer = event.getSkin(skin);
			renderer.addLayer(new RatOnShoulderLayer(renderer, event.getEntityModels()));
		});
	}

	@OnlyIn(Dist.CLIENT)
	public void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, color) -> color > 0 ? -1 : TuningForkItem.getNoteColor(stack), CCItems.TUNING_FORK.get());
		event.register((stack, color) -> color > 0 ? -1 : PotionUtils.getColor(stack), CCItems.TETHER_POTION.get());
		event.register((stack, color) -> color > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack), Items.BUNDLE);

	}

	@OnlyIn(Dist.CLIENT)
	private void createSkullModels(EntityRenderersEvent.CreateSkullModels event) {
		event.registerSkullModel(CCSkullTypes.DEEPER, new SkullModel(event.getEntityModelSet().bakeLayer(CCModelLayers.DEEPER_HEAD)));
		event.registerSkullModel(CCSkullTypes.MIME, new MimeHeadModel(event.getEntityModelSet().bakeLayer(CCModelLayers.MIME_HEAD)));
		event.registerSkullModel(CCSkullTypes.PEEPER, new PeeperHeadModel(event.getEntityModelSet().bakeLayer(CCModelLayers.PEEPER_HEAD)));
	}

	@OnlyIn(Dist.CLIENT)
	private void registerClientTooltips(RegisterClientTooltipComponentFactoriesEvent event) {
		if (ModList.get().isLoaded("quark")) {
			event.register(ToolboxComponent.class, Function.identity());
		}
	}

	private void setupMessages() {
		CHANNEL.registerMessage(0, S2CSpinelBoomMessage.class, S2CSpinelBoomMessage::serialize, S2CSpinelBoomMessage::deserialize, S2CSpinelBoomMessage::handle);
	}
}
