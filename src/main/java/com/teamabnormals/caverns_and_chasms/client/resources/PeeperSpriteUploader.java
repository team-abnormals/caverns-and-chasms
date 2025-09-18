package com.teamabnormals.caverns_and_chasms.client.resources;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PeeperSpriteUploader extends TextureAtlasHolder {
	public static final ResourceLocation ATLAS_LOCATION = CavernsAndChasms.location("textures/atlas/peeper.png");

	public static final ResourceLocation CHARGED_PEEPER_SPRITE = CavernsAndChasms.location("charged_peeper");
	public static final ResourceLocation CHARGED_PEEPER_GLOW_SPRITE = CavernsAndChasms.location("charged_peeper_glow");

	private static PeeperSpriteUploader uploader;

	public PeeperSpriteUploader(TextureManager textureManagerIn) {
		super(textureManagerIn, ATLAS_LOCATION, CavernsAndChasms.location("peeper"));
	}

	@SubscribeEvent
	public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(uploader = new PeeperSpriteUploader(Minecraft.getInstance().textureManager));
	}

	public static TextureAtlasSprite getChargedPeeperSprite() {
		return uploader.getSprite(CHARGED_PEEPER_SPRITE);
	}

	public static TextureAtlasSprite getChargedPeeperGlowSprite() {
		return uploader.getSprite(CHARGED_PEEPER_GLOW_SPRITE);
	}
}
