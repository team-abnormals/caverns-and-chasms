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
import com.teamabnormals.caverns_and_chasms.core.data.server.modifiers.CCAdvancementModifiersProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.modifiers.CCLootModifiersProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.tags.CCBlockTagsProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.tags.CCEntityTypeTagsProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.tags.CCItemTagsProvider;
import com.teamabnormals.caverns_and_chasms.core.other.CCClientCompat;
import com.teamabnormals.caverns_and_chasms.core.other.CCCompat;
import com.teamabnormals.caverns_and_chasms.core.registry.*;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
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

	public CavernsAndChasms() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext context = ModLoadingContext.get();
		MinecraftForge.EVENT_BUS.register(this);

		REGISTRY_HELPER.register(bus);
		CCAttributes.ATTRIBUTES.register(bus);
		CCMobEffects.POTIONS.register(bus);
		CCMobEffects.MOB_EFFECTS.register(bus);
		CCFeatures.FEATURES.register(bus);
		CCParticleTypes.PARTICLE_TYPES.register(bus);
		CCRecipeSerializers.RECIPE_SERIALIZERS.register(bus);

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::dataSetup);

		bus.addListener(this::registerLayerDefinitions);
		bus.addListener(this::registerRenderers);
		bus.addListener(this::registerLayers);
		bus.addListener(this::registerItemColors);

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
		ExistingFileHelper fileHelper = event.getExistingFileHelper();

		if (event.includeServer()) {
			CCBlockTagsProvider blockTags = new CCBlockTagsProvider(generator, fileHelper);
			generator.addProvider(blockTags);
			generator.addProvider(new CCItemTagsProvider(generator, blockTags, fileHelper));
			generator.addProvider(new CCEntityTypeTagsProvider(generator, fileHelper));
			generator.addProvider(new CCRecipeProvider(generator));
			generator.addProvider(new CCLootTableProvider(generator));
			generator.addProvider(new CCAdvancementProvider(generator, fileHelper));
			generator.addProvider(CCLootModifiersProvider.createDataProvider(generator));
			generator.addProvider(CCAdvancementModifiersProvider.createDataProvider(generator));
		}

		if (event.includeClient()) {
			generator.addProvider(new CCItemModelProvider(generator, fileHelper));
			generator.addProvider(new CCBlockStateProvider(generator, fileHelper));
			//generator.addProvider(new CCLanguageProvider(generator));
		}
	}

	private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CavefishModel.LOCATION, CavefishModel::createLayerDefinition);
		event.registerLayerDefinition(DeeperModel.LOCATION, DeeperModel::createLayerDefinition);
		event.registerLayerDefinition(FlyModel.LOCATION, FlyModel::createLayerDefinition);
		event.registerLayerDefinition(MimeModel.LOCATION, MimeModel::createLayerDefinition);
		event.registerLayerDefinition(RatModel.LOCATION, RatModel::createLayerDefinition);
		event.registerLayerDefinition(CopperGolemModel.LOCATION, CopperGolemModel::createLayerDefinition);
		event.registerLayerDefinition(SanguineArmorModel.LOCATION, SanguineArmorModel::createLayerDefinition);
		event.registerLayerDefinition(MimeArmorModel.LOCATION, () -> MimeArmorModel.createLayerDefinition(0.0F));
		event.registerLayerDefinition(SpinelCrownModel.LOCATION, () -> SpinelCrownModel.createLayerDefinition(false));
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
		event.registerEntityRenderer(CCEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.SPINEL_PEARL.get(), ThrownItemRenderer::new);

		event.registerBlockEntityRenderer(CCBlockEntityTypes.CURSED_CAMPFIRE.get(), CampfireRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
	public void registerLayers(EntityRenderersEvent.AddLayers event) {
		event.getSkins().forEach(skin -> {
			PlayerRenderer renderer = event.getSkin(skin);
			renderer.addLayer(new RatOnShoulderLayer(renderer));
		});
	}

	@OnlyIn(Dist.CLIENT)
	public void registerItemColors(ColorHandlerEvent.Item event) {
		event.getItemColors().register((stack, color) -> color > 0 ? -1 : ((TuningForkItem) stack.getItem()).getColor(stack), CCItems.TUNING_FORK.get());
	}
}
