package com.teamabnormals.caverns_and_chasms.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.client.event.RegisterShadersEvent;

import javax.annotation.Nullable;
import java.io.IOException;

public final class CCShaders {
	@Nullable
	private static ShaderInstance rendertypeArmorTranslucentNoCullShader;
	@Nullable
	private static ShaderInstance rendertypeArmorCutoutNoCullEmissiveShader;

	public static void registerShaders(RegisterShadersEvent event) {
		try {
			ResourceProvider resourceProvider = event.getResourceProvider();
			event.registerShader(new ShaderInstance(resourceProvider, new ResourceLocation(CavernsAndChasms.MOD_ID, "rendertype_armor_translucent_no_cull"), DefaultVertexFormat.NEW_ENTITY), shaderInstance -> {
				rendertypeArmorTranslucentNoCullShader = shaderInstance;
			});

			event.registerShader(new ShaderInstance(resourceProvider, new ResourceLocation(CavernsAndChasms.MOD_ID, "rendertype_armor_cutout_no_cull_emissive"), DefaultVertexFormat.NEW_ENTITY), shaderInstance -> {
				rendertypeArmorCutoutNoCullEmissiveShader = shaderInstance;
			});
		} catch (IOException e) {
			throw new RuntimeException("Could not reload Caverns & Chasms's shaders!", e);
		}
	}

	@Nullable
	public static ShaderInstance getRendertypeArmorTranslucentNoCullShader() {
		return rendertypeArmorTranslucentNoCullShader;
	}

	@Nullable
	public static ShaderInstance getRrendertypeArmorCutoutNoCullEmissiveShader() {
		return rendertypeArmorCutoutNoCullEmissiveShader;
	}
}