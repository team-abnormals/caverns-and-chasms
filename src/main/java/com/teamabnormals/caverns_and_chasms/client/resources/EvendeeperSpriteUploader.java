package com.teamabnormals.caverns_and_chasms.client.resources;

import com.teamabnormals.caverns_and_chasms.client.model.DeeperModel;
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
public class EvendeeperSpriteUploader extends TextureAtlasHolder {
	public static final ResourceLocation ATLAS_LOCATION = CavernsAndChasms.location("textures/atlas/evendeeper.png");

	public static final ResourceLocation EVENDEEPER_SPRITE = CavernsAndChasms.location("evendeeper");
	public static final ResourceLocation PRIMED_SPRITE = CavernsAndChasms.location("evendeeper_primed");
	public static final ResourceLocation EMISSIVE_SPRITE = CavernsAndChasms.location("evendeeper_emissive");
	public static final ResourceLocation CHARGED_SPRITE = CavernsAndChasms.location("charged_evendeeper");
	public static final ResourceLocation CHARGED_EMISSIVE_SPRITE = CavernsAndChasms.location("charged_evendeeper_emissive");

	private static EvendeeperSpriteUploader uploader;

	public EvendeeperSpriteUploader(TextureManager textureManagerIn) {
		super(textureManagerIn, ATLAS_LOCATION, EVENDEEPER_SPRITE);
	}

	@SubscribeEvent
	public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(uploader = new EvendeeperSpriteUploader(Minecraft.getInstance().textureManager));
	}

	public static TextureAtlasSprite getSprite(DeeperModel.DeeperSprite sprite) {
		return switch (sprite) {
			case PRIMED -> uploader.getSprite(PRIMED_SPRITE);
			case EMISSIVE -> uploader.getSprite(EMISSIVE_SPRITE);
			case CHARGED -> uploader.getSprite(CHARGED_SPRITE);
			case CHARGED_EMISSIVE -> uploader.getSprite(CHARGED_EMISSIVE_SPRITE);
		};
	}
}