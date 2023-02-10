package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Cavefish;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Fly;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.common.entity.item.PrimedTmt;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Deeper;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Mime;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Spiderling;
import com.teamabnormals.caverns_and_chasms.common.entity.projectile.Kunai;
import com.teamabnormals.caverns_and_chasms.common.entity.projectile.ThrownBejeweledPearl;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCEntityTypes {
	public static final EntitySubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getEntitySubHelper();
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<EntityType<Cavefish>> CAVEFISH = HELPER.createLivingEntity("cavefish", Cavefish::new, MobCategory.WATER_CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<Deeper>> DEEPER = HELPER.createLivingEntity("deeper", Deeper::new, MobCategory.MONSTER, 0.6F, 1.7F);
	public static final RegistryObject<EntityType<Spiderling>> SPIDERLING = HELPER.createLivingEntity("spiderling", Spiderling::new, MobCategory.MONSTER, 0.35F, 0.25F);
	public static final RegistryObject<EntityType<Kunai>> KUNAI = HELPER.createEntity("kunai", Kunai::new, Kunai::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<Fly>> FLY = HELPER.createLivingEntity("fly", Fly::new, MobCategory.CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<Mime>> MIME = HELPER.createLivingEntity("mime", Mime::new, MobCategory.MONSTER, 0.6F, 2.1F);
	public static final RegistryObject<EntityType<Rat>> RAT = HELPER.createLivingEntity("rat", Rat::new, MobCategory.CREATURE, 0.5F, 0.45F);
	public static final RegistryObject<EntityType<CopperGolem>> COPPER_GOLEM = HELPER.createLivingEntity("copper_golem", CopperGolem::new, MobCategory.MISC, 0.6F, 0.9F);
	public static final RegistryObject<EntityType<ThrownBejeweledPearl>> BEJEWELED_PEARL = HELPER.createEntity("spinel_pearl", ThrownBejeweledPearl::new, ThrownBejeweledPearl::new, MobCategory.MISC, 0.25F, 0.25F);
	public static final RegistryObject<EntityType<PrimedTmt>> TMT = ENTITIES.register("tmt", () -> EntityType.Builder.<PrimedTmt>of(PrimedTmt::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).build(new ResourceLocation(CavernsAndChasms.MOD_ID, "tmt").toString()));

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(CAVEFISH.get(), Cavefish.registerAttributes().build());
		event.put(DEEPER.get(), Deeper.createAttributes().build());
		event.put(SPIDERLING.get(), Spiderling.registerAttributes().build());
		event.put(FLY.get(), Fly.registerAttributes().build());
		event.put(MIME.get(), Mime.registerAttributes().build());
		event.put(RAT.get(), Rat.registerAttributes().build());
		event.put(COPPER_GOLEM.get(), CopperGolem.registerAttributes().build());
	}

	public static void registerEntitySpawns() {
		SpawnPlacements.register(CAVEFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Cavefish::canCavefishSpawn);
		SpawnPlacements.register(DEEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(MIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mime::canMimeSpawn);
	}
}
