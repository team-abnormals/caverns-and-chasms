package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.common.entity.LostGoat;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Cavefish;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Glare;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.Grazer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.SaddledGrazer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.common.entity.decoration.OxidizedCopperGolem;
import com.teamabnormals.caverns_and_chasms.common.entity.item.PrimedTmt;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Mime;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.Deeper;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.Evendeeper;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.Peeper;
import com.teamabnormals.caverns_and_chasms.common.entity.projectile.*;
import com.teamabnormals.caverns_and_chasms.common.entity.vehicle.MinecartTMT;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCEnums;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation;
import net.neoforged.neoforge.registries.DeferredHolder;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCEntityTypes {
	public static final EntitySubRegistryHelper ENTITY_TYPES = CavernsAndChasms.REGISTRY_HELPER.getEntitySubHelper();

	public static final DeferredHolder<EntityType<?>, EntityType<Deeper>> DEEPER = ENTITY_TYPES.createEntity("deeper", Deeper::new, MobCategory.MONSTER, builder -> builder.sized(0.6F, 1.7F).clientTrackingRange(8));
	public static final DeferredHolder<EntityType<?>, EntityType<Evendeeper>> EVENDEEPER = ENTITY_TYPES.createEntity("evendeeper", Evendeeper::new, MobCategory.MONSTER, builder -> builder.sized(0.6F, 1.7F).clientTrackingRange(8));
	public static final DeferredHolder<EntityType<?>, EntityType<Peeper>> PEEPER = ENTITY_TYPES.createEntity("peeper", Peeper::new, MobCategory.MONSTER, builder -> builder.sized(0.6F, 2.2F).clientTrackingRange(8));
	public static final DeferredHolder<EntityType<?>, EntityType<Mime>> MIME = ENTITY_TYPES.createEntity("mime", Mime::new, MobCategory.MONSTER, builder -> builder.sized(0.6F, 2.1F).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8));
	public static final DeferredHolder<EntityType<?>, EntityType<Rat>> RAT = ENTITY_TYPES.createEntity("rat", Rat::new, CCEnums.UNDERGROUND_AMBIENT.getValue(), builder -> builder.sized(0.5F, 0.45F).eyeHeight(0.22F).passengerAttachments(0.3F).clientTrackingRange(8));
	public static final DeferredHolder<EntityType<?>, EntityType<Glare>> GLARE = ENTITY_TYPES.createEntity("glare", Glare::new, MobCategory.AMBIENT, builder -> builder.sized(0.6F, 0.95F).eyeHeight(0.6F).passengerAttachments(0.7F).clientTrackingRange(8));
	public static final DeferredHolder<EntityType<?>, EntityType<CopperGolem>> COPPER_GOLEM = ENTITY_TYPES.createEntity("copper_golem", CopperGolem::new, MobCategory.MISC, builder -> builder.sized(0.6F, 0.9F).clientTrackingRange(10));
	public static final DeferredHolder<EntityType<?>, EntityType<OxidizedCopperGolem>> OXIDIZED_COPPER_GOLEM = ENTITY_TYPES.createEntity("oxidized_copper_golem", OxidizedCopperGolem::new, MobCategory.MISC, builder -> builder.fireImmune().sized(0.6F, 0.9F).clientTrackingRange(10));
	public static final DeferredHolder<EntityType<?>, EntityType<Grazer>> GRAZER = ENTITY_TYPES.createEntity("grazer", Grazer::new, MobCategory.MONSTER, builder -> builder.sized(0.9F, 1.98F).eyeHeight(1.0625F).passengerAttachments(2.1F).clientTrackingRange(8));
	public static final DeferredHolder<EntityType<?>, EntityType<SaddledGrazer>> SADDLED_GRAZER = ENTITY_TYPES.createEntity("saddled_grazer", SaddledGrazer::new, MobCategory.CREATURE, builder -> builder.sized(0.9F, 1.98F).eyeHeight(1.0625F).passengerAttachments(2.1F).clientTrackingRange(8));
	public static final DeferredHolder<EntityType<?>, EntityType<Cavefish>> CAVEFISH = ENTITY_TYPES.createEntity("cavefish", Cavefish::new, CCEnums.UNDERGROUND_WATER_AMBIENT.getValue(), builder -> builder.sized(0.4F, 0.3F).eyeHeight(0.26F).clientTrackingRange(4));
	public static final DeferredHolder<EntityType<?>, EntityType<Kunai>> KUNAI = ENTITY_TYPES.createEntity("kunai", Kunai::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));
	public static final DeferredHolder<EntityType<?>, EntityType<PrimedTmt>> TMT = ENTITY_TYPES.createEntity("tmt", PrimedTmt::new, MobCategory.MISC, builder -> builder.fireImmune().sized(0.98F, 0.98F).eyeHeight(0.15F).clientTrackingRange(10).updateInterval(10));
	public static final DeferredHolder<EntityType<?>, EntityType<ThrownBejeweledPearl>> BEJEWELED_PEARL = ENTITY_TYPES.createEntity("bejeweled_pearl", ThrownBejeweledPearl::new, MobCategory.MISC, builder -> builder.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
	public static final DeferredHolder<EntityType<?>, EntityType<LostGoat>> LOST_GOAT = ENTITY_TYPES.createEntity("lost_goat", LostGoat::new, CCEnums.LOST_GOAT.getValue(), builder -> builder.sized(0.9F, 1.3F).passengerAttachments(1.1125F).clientTrackingRange(10));
	public static final DeferredHolder<EntityType<?>, EntityType<MinecartTMT>> TMT_MINECART = ENTITY_TYPES.createEntity("tmt_minecart", MinecartTMT::new, MobCategory.MISC, builder -> builder.sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));

	public static final DeferredHolder<EntityType<?>, EntityType<BluntArrow>> BLUNT_ARROW = ENTITY_TYPES.createEntity("blunt_arrow", BluntArrow::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));
	public static final DeferredHolder<EntityType<?>, EntityType<RicochetArrow>> RICOCHET_ARROW = ENTITY_TYPES.createEntity("ricochet_arrow", RicochetArrow::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));
	public static final DeferredHolder<EntityType<?>, EntityType<LargeArrow>> LARGE_ARROW = ENTITY_TYPES.createEntity("large_arrow", LargeArrow::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(DEEPER.get(), Deeper.createAttributes().build());
		event.put(EVENDEEPER.get(), Evendeeper.createAttributes().build());
		event.put(PEEPER.get(), Peeper.createAttributes().build());
		event.put(MIME.get(), Mime.createAttributes().build());
		event.put(RAT.get(), Rat.createAttributes().build());
		event.put(CAVEFISH.get(), Cavefish.createAttributes().build());
		event.put(GLARE.get(), Glare.createAttributes().build());
		event.put(COPPER_GOLEM.get(), CopperGolem.createAttributes().build());
		event.put(OXIDIZED_COPPER_GOLEM.get(), OxidizedCopperGolem.createAttributes().build());
		event.put(GRAZER.get(), Grazer.createAttributes().build());
		event.put(SADDLED_GRAZER.get(), SaddledGrazer.createAttributes().build());
		event.put(LOST_GOAT.get(), LostGoat.createAttributes().build());
	}

	@SubscribeEvent
	public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
		event.register(DEEPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, Operation.AND);
		event.register(EVENDEEPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, Operation.AND);
		event.register(PEEPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Peeper::checkPeeperSpawnRules, Operation.AND);
		event.register(MIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mime::checkMimeSpawnRules, Operation.AND);
		event.register(GLARE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Glare::checkGlareSpawnRules, Operation.AND);
		event.register(GRAZER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Grazer::checkGrazerSpawnRules, Operation.AND);
		event.register(RAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Rat::checkRatSpawnRules, Operation.AND);
		event.register(CAVEFISH.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Cavefish::checkCavefishSpawnRules, Operation.AND);
		event.register(LOST_GOAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LostGoat::checkLostGoatSpawnRules, Operation.AND);
	}
}
