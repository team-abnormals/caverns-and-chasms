package com.teamabnormals.caverns_and_chasms.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class CCRenderTypes extends RenderType {
	public static final ShaderStateShard RENDERTYPE_ARMOR_TRANSLUCENT_NO_CULL_SHADER = new RenderStateShard.ShaderStateShard(CCShaders::getRendertypeArmorTranslucentNoCullShader);
	public static final ShaderStateShard RENDERTYPE_ARMOR_CUTOUT_NO_CULL_EMISSIVE = new RenderStateShard.ShaderStateShard(CCShaders::getRrendertypeArmorCutoutNoCullEmissiveShader);

	public CCRenderTypes(String p_173178_, VertexFormat p_173179_, Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
		super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
	}

	public static final Function<ResourceLocation, RenderType> ARMOR_TRANSLUCENT_NO_CULL = Util.memoize((p_286149_) -> {
		RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ARMOR_TRANSLUCENT_NO_CULL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(p_286149_, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(true);
		return create(CavernsAndChasms.MOD_ID + ":armor_translucent_no_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
	});

	public static final Function<ResourceLocation, RenderType> ARMOR_CUTOUT_NO_CULL_EMISSIVE = Util.memoize((p_286149_) -> {
		RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ARMOR_CUTOUT_NO_CULL_EMISSIVE).setTextureState(new RenderStateShard.TextureStateShard(p_286149_, false, false)).setTransparencyState(NO_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(true);
		return create(CavernsAndChasms.MOD_ID + ":armor_cutout_no_cull_emissive", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
	});
}
