package com.teamabnormals.caverns_and_chasms.client.resources;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;


@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, value = Dist.CLIENT)
public class GrazerSpriteUploader extends TextureAtlasHolder {
	public static final ResourceLocation ATLAS_LOCATION = CavernsAndChasms.location("textures/atlas/grazer.png");
	public static final ResourceLocation GRAZER_DROOL_SPRITE = CavernsAndChasms.location("grazer_drool");

	private static GrazerSpriteUploader uploader;

	public GrazerSpriteUploader(TextureManager textureManager) {
		super(textureManager, ATLAS_LOCATION, CavernsAndChasms.location("grazer"));
	}

	@SubscribeEvent
	public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(uploader = new GrazerSpriteUploader(Minecraft.getInstance().getTextureManager()));
	}

	public static TextureAtlasSprite getDroolSprite() {
		return uploader.getSprite(GRAZER_DROOL_SPRITE);
	}
}