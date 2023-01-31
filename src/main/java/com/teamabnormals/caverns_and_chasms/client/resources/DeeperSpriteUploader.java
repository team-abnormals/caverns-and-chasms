package com.teamabnormals.caverns_and_chasms.client.resources;

import com.teamabnormals.caverns_and_chasms.client.model.DeeperModel;
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

import java.util.stream.Stream;

/**
 * @author Ocelot
 */
public class DeeperSpriteUploader extends TextureAtlasHolder {
	public static final ResourceLocation ATLAS_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/atlas/deeper.png");
	public static final ResourceLocation DEEPER_SPRITE = new ResourceLocation(CavernsAndChasms.MOD_ID, "deeper");
	public static final ResourceLocation PRIMED_SPRITE = new ResourceLocation(CavernsAndChasms.MOD_ID, "deeper_primed");
	public static final ResourceLocation EMISSIVE_SPRITE = new ResourceLocation(CavernsAndChasms.MOD_ID, "deeper_emissive");

	private static DeeperSpriteUploader uploader;

	public DeeperSpriteUploader(TextureManager textureManagerIn, ResourceLocation atlasTextureLocation, String prefixIn) {
		super(textureManagerIn, atlasTextureLocation, prefixIn);
	}

	@Override
	protected Stream<ResourceLocation> getResourcesToLoad() {
		return Stream.of(DEEPER_SPRITE, PRIMED_SPRITE, EMISSIVE_SPRITE);
	}

	/**
	 * Initializes this uploader under the mod bus.
	 *
	 * @param bus The bus to register to
	 */
	public static void init(IEventBus bus) {
		bus.addListener(EventPriority.NORMAL, false, RegisterColorHandlersEvent.Block.class, event -> {
			Minecraft minecraft = Minecraft.getInstance();
			ResourceManager resourceManager = minecraft.getResourceManager();
			if (resourceManager instanceof ReloadableResourceManager) {
				((ReloadableResourceManager) resourceManager).registerReloadListener(uploader = new DeeperSpriteUploader(minecraft.textureManager, ATLAS_LOCATION, "entity/deeper"));
			}
		});
	}

	/**
	 * @return The sprite for the deeper
	 */
	public static TextureAtlasSprite getSprite(DeeperModel.DeeperSprite sprite) {
		switch (sprite) {
			case BASE:
			default:
				return uploader.getSprite(DEEPER_SPRITE);
			case PRIMED:
				return uploader.getSprite(PRIMED_SPRITE);
			case EMISSIVE:
				return uploader.getSprite(EMISSIVE_SPRITE);
		}
	}
}
