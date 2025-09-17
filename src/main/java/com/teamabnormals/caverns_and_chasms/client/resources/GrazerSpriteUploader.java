package com.teamabnormals.caverns_and_chasms.client.resources;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class GrazerSpriteUploader extends TextureAtlasHolder {
	public static final ResourceLocation ATLAS_LOCATION = CavernsAndChasms.location("textures/atlas/grazer.png");
	public static final ResourceLocation GRAZER_DROOL_SPRITE = CavernsAndChasms.location("grazer_drool");

	private static GrazerSpriteUploader uploader;

	public GrazerSpriteUploader(TextureManager textureManager) {
		super(textureManager, ATLAS_LOCATION, CavernsAndChasms.location("grazer"));
	}

	public static void init(IEventBus bus) {
		bus.addListener(EventPriority.NORMAL, false, RegisterColorHandlersEvent.Block.class, event -> {
			Minecraft minecraft = Minecraft.getInstance();
			ResourceManager resourceManager = minecraft.getResourceManager();
			if (resourceManager instanceof ReloadableResourceManager) {
				((ReloadableResourceManager) resourceManager).registerReloadListener(uploader = new GrazerSpriteUploader(minecraft.textureManager));
			}
		});
	}

	public static TextureAtlasSprite getDroolSprite() {
		return uploader.getSprite(GRAZER_DROOL_SPRITE);
	}
}