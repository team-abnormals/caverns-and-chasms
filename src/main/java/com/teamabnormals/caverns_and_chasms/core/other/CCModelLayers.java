package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.client.model.*;
import com.teamabnormals.caverns_and_chasms.client.renderer.block.AtoningTableRenderer;
import com.teamabnormals.caverns_and_chasms.client.renderer.block.DeeperSkullBlockRenderer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;


@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CCModelLayers {
	public static final ModelLayerLocation COPPER_GOLEM = register("copper_golem");
	public static final ModelLayerLocation COPPER_HORSE_ARMOR = register("copper_horse_armor");
	public static final ModelLayerLocation DEEPER = register("deeper");
	public static final ModelLayerLocation DEEPER_HEAD = register("deeper_head");
	public static final ModelLayerLocation DEEPER_ARMOR = register("deeper", "armor");
	public static final ModelLayerLocation PEEPER = register("peeper");
	public static final ModelLayerLocation PEEPER_HEAD = register("peeper_head");
	public static final ModelLayerLocation PEEPER_ARMOR = register("peeper", "armor");
	public static final ModelLayerLocation FLY = register("fly");
	public static final ModelLayerLocation GLARE = register("glare");
	public static final ModelLayerLocation LOST_GOAT = register("lost_goat");
	public static final ModelLayerLocation MIME = register("mime");
	public static final ModelLayerLocation MIME_HEAD = register("mime_head");
	public static final ModelLayerLocation GRAZER = register("grazer");
	public static final ModelLayerLocation RAT = register("rat");
	public static final ModelLayerLocation TMT_MINECART = register("tmt_minecart");
	public static final ModelLayerLocation TOOLBOX = register("toolbox");
	public static final ModelLayerLocation ROLLER_DOOR = register("roller_door");
	public static final ModelLayerLocation WINCH = register("winch");
	public static final ModelLayerLocation UNICORN_HORN = register("unicorn_horn");

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(DEEPER, () -> DeeperModel.createBodyLayer(CubeDeformation.NONE, true));
		event.registerLayerDefinition(DEEPER_HEAD, DeeperHeadModel::createHeadLayer);
		event.registerLayerDefinition(DEEPER_ARMOR, () -> DeeperModel.createBodyLayer(new CubeDeformation(2.0F), false));
		event.registerLayerDefinition(PEEPER, () -> PeeperModel.createBodyLayer(CubeDeformation.NONE));
		event.registerLayerDefinition(PEEPER_HEAD, PeeperHeadModel::createHeadLayer);
		event.registerLayerDefinition(PEEPER_ARMOR, () -> PeeperModel.createBodyLayer(new CubeDeformation(2.0F)));
		event.registerLayerDefinition(MIME, MimeModel::createBodyLayer);
		event.registerLayerDefinition(MIME_HEAD, MimeHeadModel::createHeadLayer);
		event.registerLayerDefinition(FLY, FlyModel::createBodyLayer);
		event.registerLayerDefinition(RAT, RatModel::createBodyLayer);
		event.registerLayerDefinition(COPPER_GOLEM, CopperGolemModel::createBodyLayer);
		event.registerLayerDefinition(GLARE, GlareModel::createBodyLayer);
		event.registerLayerDefinition(GRAZER, GrazerModel::createBodyLayer);
		event.registerLayerDefinition(TOOLBOX, ToolboxRenderer::createBodyLayer);
		event.registerLayerDefinition(ROLLER_DOOR, RollerDoorRenderer::createBodyLayer);
		event.registerLayerDefinition(WINCH, WinchRenderer::createBodyLayer);
		event.registerLayerDefinition(TMT_MINECART, MinecartModel::createBodyLayer);
		event.registerLayerDefinition(LOST_GOAT, LostGoatModel::createBodyLayer);
		event.registerLayerDefinition(COPPER_HORSE_ARMOR, () -> LayerDefinition.create(CopperHorseArmorModel.createBodyMesh(new CubeDeformation(0.1F)), 64, 64));
		event.registerLayerDefinition(UNICORN_HORN, () -> LayerDefinition.create(UnicornHornModel.createBodyMesh(new CubeDeformation(0.1F)), 64, 64));
	}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CCEntityTypes.DEEPER.get(), DeeperRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.PEEPER.get(), PeeperRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.KUNAI.get(), KunaiRenderer::new);
//		event.registerEntityRenderer(CCEntityTypes.FLY.get(), FlyRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.MIME.get(), MimeRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.RAT.get(), RatRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.OXIDIZED_COPPER_GOLEM.get(), OxidizedCopperGolemRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.GRAZER.get(), GrazerRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.SADDLED_GRAZER.get(), SaddledGrazerRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.BEJEWELED_PEARL.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.TMT.get(), TmtRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.TMT_MINECART.get(), TmtMinecartRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.BLUNT_ARROW.get(), BluntArrowRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.BLUNT_ARROW.get(), BluntArrowRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.LARGE_ARROW.get(), LargeArrowRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.GLARE.get(), GlareRenderer::new);
		event.registerEntityRenderer(CCEntityTypes.LOST_GOAT.get(), LostGoatRenderer::new);

		event.registerBlockEntityRenderer(CCBlockEntityTypes.CUPRIC_CAMPFIRE.get(), CampfireRenderer::new);
		event.registerBlockEntityRenderer(CCBlockEntityTypes.SKULL.get(), SkullBlockRenderer::new);
		event.registerBlockEntityRenderer(CCBlockEntityTypes.DEEPER_HEAD.get(), DeeperSkullBlockRenderer::new);
		event.registerBlockEntityRenderer(CCBlockEntityTypes.TOOLBOX.get(), ToolboxRenderer::new);
		event.registerBlockEntityRenderer(CCBlockEntityTypes.ROLLER_DOOR.get(), RollerDoorRenderer::new);
		event.registerBlockEntityRenderer(CCBlockEntityTypes.ROLLER_DOOR_HEADER.get(), RollerDoorRenderer::new);
		event.registerBlockEntityRenderer(CCBlockEntityTypes.WINCH.get(), WinchRenderer::new);
		event.registerBlockEntityRenderer(CCBlockEntityTypes.ATONING_TABLE.get(), AtoningTableRenderer::new);
	}

	public static ModelLayerLocation register(String name) {
		return register(name, "main");
	}

	public static ModelLayerLocation register(String name, String layer) {
		return new ModelLayerLocation(CavernsAndChasms.location(name), layer);
	}
}