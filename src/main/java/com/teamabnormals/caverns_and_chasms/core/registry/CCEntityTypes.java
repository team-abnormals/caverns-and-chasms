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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent.Operation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCEntityTypes {
	public static final EntitySubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getEntitySubHelper();
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CavernsAndChasms.MOD_ID);

	public static final MobCategory LOST_GOAT_CATEGORY = MobCategory.create(CavernsAndChasms.MOD_ID + ":lost_goat", "lost_goat", 1, false, false, 128);
	public static final MobCategory UNDERGROUND_WATER_AMBIENT = MobCategory.create(CavernsAndChasms.MOD_ID + ":underground_water_ambient", "underground_water_ambient", 20, true, false, 128);
	public static final MobCategory UNDERGROUND_AMBIENT = MobCategory.create(CavernsAndChasms.MOD_ID + ":underground_ambient", "underground_ambient", 16, true, false, 128);

	public static final RegistryObject<EntityType<Deeper>> DEEPER = HELPER.createLivingEntity("deeper", Deeper::new, MobCategory.MONSTER, 0.6F, 1.7F);
	public static final RegistryObject<EntityType<Evendeeper>> EVENDEEPER = HELPER.createLivingEntity("evendeeper", Evendeeper::new, MobCategory.MONSTER, 0.6F, 1.7F);
	public static final RegistryObject<EntityType<Peeper>> PEEPER = HELPER.createLivingEntity("peeper", Peeper::new, MobCategory.MONSTER, 0.6F, 2.2F);
	public static final RegistryObject<EntityType<Mime>> MIME = HELPER.createLivingEntity("mime", Mime::new, MobCategory.MONSTER, 0.6F, 2.1F);
	//	public static final RegistryObject<EntityType<Fly>> FLY = HELPER.createLivingEntity("fly", Fly::new, MobCategory.CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<Rat>> RAT = HELPER.createLivingEntity("rat", Rat::new, UNDERGROUND_AMBIENT, 0.5F, 0.45F);
	public static final RegistryObject<EntityType<Glare>> GLARE = HELPER.createLivingEntity("glare", Glare::new, MobCategory.AMBIENT, 0.6F, 0.95F);
	public static final RegistryObject<EntityType<CopperGolem>> COPPER_GOLEM = HELPER.createLivingEntity("copper_golem", CopperGolem::new, MobCategory.MISC, 0.6F, 0.9F);
	public static final RegistryObject<EntityType<OxidizedCopperGolem>> OXIDIZED_COPPER_GOLEM = ENTITY_TYPES.register("oxidized_copper_golem", () -> EntityType.Builder.<OxidizedCopperGolem>of(OxidizedCopperGolem::new, MobCategory.MISC).fireImmune().sized(0.6F, 0.9F).clientTrackingRange(10).build(CavernsAndChasms.location("oxidized_copper_golem").toString()));
	public static final RegistryObject<EntityType<Grazer>> GRAZER = HELPER.createLivingEntity("grazer", Grazer::new, MobCategory.MONSTER, 0.9F, 1.98F);
	public static final RegistryObject<EntityType<SaddledGrazer>> SADDLED_GRAZER = HELPER.createLivingEntity("saddled_grazer", SaddledGrazer::new, MobCategory.CREATURE, 0.9F, 1.98F);
	public static final RegistryObject<EntityType<Cavefish>> CAVEFISH = HELPER.createEntity("cavefish", Cavefish::new, Cavefish::new, UNDERGROUND_WATER_AMBIENT, 0.4F, 0.3F);
	public static final RegistryObject<EntityType<Kunai>> KUNAI = HELPER.createEntity("kunai", Kunai::new, Kunai::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<PrimedTmt>> TMT = ENTITY_TYPES.register("tmt", () -> EntityType.Builder.<PrimedTmt>of(PrimedTmt::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10).build(CavernsAndChasms.location("tmt").toString()));
	public static final RegistryObject<EntityType<ThrownBejeweledPearl>> BEJEWELED_PEARL = HELPER.createEntity("spinel_pearl", ThrownBejeweledPearl::new, ThrownBejeweledPearl::new, MobCategory.MISC, 0.25F, 0.25F);
	public static final RegistryObject<EntityType<BluntArrow>> BLUNT_ARROW = HELPER.createEntity("blunt_arrow", BluntArrow::new, BluntArrow::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<RicochetArrow>> RICOCHET_ARROW = HELPER.createEntity("ricochet_arrow", RicochetArrow::new, RicochetArrow::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<LargeArrow>> LARGE_ARROW = HELPER.createEntity("large_arrow", LargeArrow::new, LargeArrow::new, MobCategory.MISC, 0.75F, 0.75F);
	public static final RegistryObject<EntityType<LostGoat>> LOST_GOAT = HELPER.createLivingEntity("lost_goat", LostGoat::new, LOST_GOAT_CATEGORY, 0.9F, 1.3F);
	public static final RegistryObject<EntityType<MinecartTMT>> TMT_MINECART = HELPER.createEntity("tmt_minecart", MinecartTMT::new, MinecartTMT::new, MobCategory.MISC, 0.98F, 0.7F);

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(DEEPER.get(), Deeper.createAttributes().build());
		event.put(EVENDEEPER.get(), Evendeeper.createAttributes().build());
		event.put(PEEPER.get(), Peeper.createAttributes().build());
		event.put(MIME.get(), Mime.createAttributes().build());
//		event.put(FLY.get(), Fly.createAttributes().build());
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
	public static void registerAttributes(EntityAttributeModificationEvent event) {
//		event.add(EntityType.SKELETON, Attributes.MAX_HEALTH, 10.0D);
	}

	@SubscribeEvent
	public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
		event.register(DEEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, Operation.AND);
		event.register(EVENDEEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, Operation.AND);
		event.register(PEEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Peeper::checkPeeperSpawnRules, Operation.AND);
		event.register(MIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mime::checkMimeSpawnRules, Operation.AND);
		event.register(GLARE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Glare::checkGlareSpawnRules, Operation.AND);
		event.register(GRAZER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Grazer::checkGrazerSpawnRules, Operation.AND);
		event.register(RAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Rat::checkRatSpawnRules, Operation.AND);
		event.register(CAVEFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Cavefish::checkCavefishSpawnRules, Operation.AND);
		event.register(LOST_GOAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LostGoat::checkLostGoatSpawnRules, Operation.AND);
	}
}
