package com.teamabnormals.caverns_and_chasms.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.caverns_and_chasms.common.network.S2CCustomSoundExplosionMessage;
import com.teamabnormals.caverns_and_chasms.common.network.S2COpenStorageDuctMessage;
import com.teamabnormals.caverns_and_chasms.common.network.S2CSpinelBoomMessage;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCBlockStateProvider;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCItemModelProvider;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCSpriteSourceProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.*;
import com.teamabnormals.caverns_and_chasms.core.data.server.modifiers.CCAdvancementModifierProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.modifiers.CCLootModifierProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.tags.*;
import com.teamabnormals.caverns_and_chasms.core.other.CCClientCompat;
import com.teamabnormals.caverns_and_chasms.core.other.CCCompat;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.other.CCGameEvents;
import com.teamabnormals.caverns_and_chasms.core.registry.*;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCStructurePieceTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCStructureRepaletters;
import com.teamabnormals.caverns_and_chasms.core.registry.helper.CCBlockSubRegistryHelper;
import com.teamabnormals.gallery.core.data.client.GalleryAssetsRemolderProvider;
import com.teamabnormals.gallery.core.data.client.GalleryItemModelProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
		CCLootItemFunctions.LOOT_FUNCTION_TYPES.register(bus);
		CCDecoratedPotPatterns.DECORATED_POT_PATTERNS.register(bus);
		CCEnchantments.ENCHANTMENTS.register(bus);

		bus.addListener((ModConfigEvent event) -> {
			final ModConfig config = event.getConfig();
			if (config.getSpec() == CCConfig.COMMON_SPEC) {
				CCConfig.COMMON.load();
			}
		});

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::dataSetup);

		CCStructureRepaletters.registerRepaletters();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			CCItems.setupTabEditors();
			CCBlocks.setupTabEditors();
		});

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
		event.enqueueWork(() -> {
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
		generator.addProvider(server, new CCStructureTagsProvider(output, provider, helper));
		generator.addProvider(server, new CCRecipeProvider(output));
		generator.addProvider(server, new CCLootTableProvider(output));
		generator.addProvider(server, CCAdvancementProvider.create(output, provider, helper));
		generator.addProvider(server, new CCAdvancementModifierProvider(output, provider));
		generator.addProvider(server, new CCLootModifierProvider(output, provider));
		generator.addProvider(server, new CCDataRemolderProvider(output, provider));

		boolean client = event.includeClient();
		generator.addProvider(client, new CCItemModelProvider(output, helper));
		generator.addProvider(client, new CCBlockStateProvider(output, helper));
		generator.addProvider(client, new CCSpriteSourceProvider(output, helper));
		//generator.addProvider(client, new CCLanguageProvider(generator));

		generator.addProvider(client, new GalleryItemModelProvider(MOD_ID, output, helper));
		generator.addProvider(client, new GalleryAssetsRemolderProvider(MOD_ID, output, provider));
	}

	private void setupMessages() {
		CHANNEL.registerMessage(0, S2CSpinelBoomMessage.class, S2CSpinelBoomMessage::serialize, S2CSpinelBoomMessage::deserialize, S2CSpinelBoomMessage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		CHANNEL.registerMessage(1, S2CCustomSoundExplosionMessage.class, S2CCustomSoundExplosionMessage::serialize, S2CCustomSoundExplosionMessage::deserialize, S2CCustomSoundExplosionMessage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		CHANNEL.registerMessage(2, S2COpenStorageDuctMessage.class, S2COpenStorageDuctMessage::serialize, S2COpenStorageDuctMessage::deserialize, S2COpenStorageDuctMessage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}

	public static ResourceLocation location(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
