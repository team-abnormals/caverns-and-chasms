package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.common.entity.vehicle.MinecartTMT;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TmtMinecartRenderer extends MinecartRenderer<MinecartTMT> {
	private final BlockRenderDispatcher blockRenderer;

	public TmtMinecartRenderer(EntityRendererProvider.Context p_174424_) {
		super(p_174424_, CCModelLayers.TMT_MINECART);
		this.blockRenderer = p_174424_.getBlockRenderDispatcher();
	}

	@Override
	protected void renderMinecartContents(MinecartTMT cart, float p_116152_, BlockState p_116153_, PoseStack p_116154_, MultiBufferSource p_116155_, int p_116156_) {
		int i = cart.getFuse();
		if (i > -1 && (float) i - p_116152_ + 1.0F < 10.0F) {
			float f = 1.0F - ((float) i - p_116152_ + 1.0F) / 10.0F;
			f = Mth.clamp(f, 0.0F, 1.0F);
			f *= f;
			f *= f;
			float f1 = 1.0F + f * 0.3F;
			p_116154_.scale(f1, f1, f1);
		}

		TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, p_116153_, p_116154_, p_116155_, p_116156_, i > -1 && i / 5 % 2 == 0);
	}
}