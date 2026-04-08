package com.teamabnormals.caverns_and_chasms.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.caverns_and_chasms.common.network.*;
import com.teamabnormals.caverns_and_chasms.common.network.bone_flute.C2SBoneFluteAttackMessage;
import com.teamabnormals.caverns_and_chasms.common.network.bone_flute.C2SBoneFluteMoveMessage;
import com.teamabnormals.caverns_and_chasms.common.network.bone_flute.C2SBoneFluteRecallMessage;
import com.teamabnormals.caverns_and_chasms.common.network.bone_flute.C2SBoneFluteSitMessage;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCBlockStateProvider;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCItemModelProvider;
import com.teamabnormals.caverns_and_chasms.core.data.client.CCSpriteSourceProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.*;
import com.teamabnormals.caverns_and_chasms.core.data.server.modifiers.CCAdvancementModifierProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.modifiers.CCLootModifierProvider;
import com.teamabnormals.caverns_and_chasms.core.data.server.tags.*;
import com.teamabnormals.caverns_and_chasms.core.other.*;
import com.teamabnormals.caverns_and_chasms.core.registry.*;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeSerializers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRecipes.CCRecipeTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCStructureTypes.CCStructurePieceTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCStructureRepaletters;
import com.teamabnormals.caverns_and_chasms.core.registry.helper.CCBlockSubRegistryHelper;
import com.teamabnormals.gallery.core.data.client.GalleryItemModelProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.NetworkDirection;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Mod(CavernsAndChasms.MOD_ID)
public class CavernsAndChasms {
	public static final String MOD_ID = "caverns_and_chasms";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID.toUpperCase());
	public static final String NETWORK_PROTOCOL = "CC1";
	public static final RegistryHelper REGISTRY_HELPER = RegistryHelper.create(MOD_ID, helper -> helper.putSubHelper(BuiltInRegistries.BLOCK, new CCBlockSubRegistryHelper(helper)));

	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath(MOD_ID, "net")).networkProtocolVersion(() -> NETWORK_PROTOCOL).clientAcceptedVersions(NETWORK_PROTOCOL::equals).serverAcceptedVersions(NETWORK_PROTOCOL::equals).simpleChannel();

	public CavernsAndChasms(IEventBus bus, ModContainer container) {
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
		CCMenuTypes.MENUS.register(bus);
		CCInstruments.INSTRUMENTS.register(bus);
		CCGameEvents.GAME_EVENTS.register(bus);
		CCPoiTypes.POI_TYPES.register(bus);
		CCLootItemFunctions.LOOT_FUNCTION_TYPES.register(bus);
		CCDecoratedPotPatterns.DECORATED_POT_PATTERNS.register(bus);
		CCEnchantmentEffects.COMPONENTS.register(bus);
		CCCriteriaTriggers.TRIGGERS.register(bus);
		CCCriteriaTriggers.ENTITY_SUB_PREDICATE_TYPES.register(bus);
		CCDataComponents.DATA_COMPONENTS.register(bus);

		bus.addListener((ModConfigEvent event) -> {
			final ModConfig config = event.getConfig();
			if (config.getSpec() == CCConfig.COMMON_SPEC) {
				CCConfig.COMMON.load();
			}
		});

		bus.addListener(CCRegistries::registerRegistries);

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::dataSetup);

		CCStructureRepaletters.registerRepaletters();
		if (FMLEnvironment.dist == Dist.CLIENT) {
			CCItems.setupTabEditors();
			CCBlocks.setupTabEditors();
		}

		container.registerConfig(ModConfig.Type.COMMON, CCConfig.COMMON_SPEC);
		container.registerConfig(ModConfig.Type.CLIENT, CCConfig.CLIENT_SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(CCCompat::registerCompat);
	}

	private void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(CCClientCompat::registerClientCompat);
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
		generator.addProvider(server, new CCEnchantmentTagsProvider(output, provider, helper));
		generator.addProvider(server, new CCDataMapProvider(output, provider));
		generator.addProvider(server, new CCRecipeProvider(output));
		generator.addProvider(server, new CCLootTableProvider(output, provider));
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
	}

	private void setupMessages() {
		int id = -1;
		CHANNEL.registerMessage(id++, S2CSpinelBoomMessage.class, S2CSpinelBoomMessage::serialize, S2CSpinelBoomMessage::deserialize, S2CSpinelBoomMessage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		CHANNEL.registerMessage(id++, S2CCustomSoundExplosionMessage.class, S2CCustomSoundExplosionMessage::serialize, S2CCustomSoundExplosionMessage::deserialize, S2CCustomSoundExplosionMessage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		CHANNEL.registerMessage(id++, S2COpenStorageDuctMessage.class, S2COpenStorageDuctMessage::serialize, S2COpenStorageDuctMessage::deserialize, S2COpenStorageDuctMessage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		CHANNEL.registerMessage(id++, S2CUpdateAttachedRatsMessage.class, S2CUpdateAttachedRatsMessage::serialize, S2CUpdateAttachedRatsMessage::deserialize, S2CUpdateAttachedRatsMessage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		CHANNEL.registerMessage(id++, C2SGrazerJumpMessage.class, C2SGrazerJumpMessage::serialize, C2SGrazerJumpMessage::deserialize, C2SGrazerJumpMessage::handle);
		CHANNEL.registerMessage(id++, C2SBoneFluteSitMessage.class, C2SBoneFluteSitMessage::serialize, C2SBoneFluteSitMessage::deserialize, C2SBoneFluteSitMessage::handle);
		CHANNEL.registerMessage(id++, C2SBoneFluteRecallMessage.class, C2SBoneFluteRecallMessage::serialize, C2SBoneFluteRecallMessage::deserialize, C2SBoneFluteRecallMessage::handle);
		CHANNEL.registerMessage(id++, C2SBoneFluteMoveMessage.class, C2SBoneFluteMoveMessage::serialize, C2SBoneFluteMoveMessage::deserialize, C2SBoneFluteMoveMessage::handle);
		CHANNEL.registerMessage(id++, C2SBoneFluteAttackMessage.class, C2SBoneFluteAttackMessage::serialize, C2SBoneFluteAttackMessage::deserialize, C2SBoneFluteAttackMessage::handle);
	}

	public static ResourceLocation location(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
