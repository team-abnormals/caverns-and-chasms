package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class CCModelLayers {
	public static final ModelLayerLocation COPPER_GOLEM = register("copper_golem");
	public static final ModelLayerLocation DEEPER = register("deeper");
	public static final ModelLayerLocation DEEPER_HEAD = register("deeper_head");
	public static final ModelLayerLocation FLY = register("fly");
	public static final ModelLayerLocation GLARE = register("glare");
	public static final ModelLayerLocation MIME = register("mime");
	public static final ModelLayerLocation MIME_ARMOR_INNER = register("mime", "inner_armor");
	public static final ModelLayerLocation MIME_ARMOR_OUTER = register("mime", "outer_armor");
	public static final ModelLayerLocation MIME_HEAD = register("mime_head");
	public static final ModelLayerLocation RAT = register("rat");
	public static final ModelLayerLocation SANGUINE_ARMOR = register("sanguine_armor");

	public static ModelLayerLocation register(String name) {
		return register(name, "main");
	}

	public static ModelLayerLocation register(String name, String layer) {
		return new ModelLayerLocation(new ResourceLocation(CavernsAndChasms.MOD_ID, name), layer);
	}
}