package com.teamabnormals.caverns_and_chasms.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.WinchBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.WinchBlock;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WinchRenderer<T extends WinchBlockEntity> implements BlockEntityRenderer<T> {
	public static final Material WINCH_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, CavernsAndChasms.location("entity/winch/winch"));
	public static final Material GLOW_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, CavernsAndChasms.location("entity/winch/winch_glow"));

	private final ModelPart base;
	private final ModelPart crank;

	public WinchRenderer(BlockEntityRendererProvider.Context context) {
		ModelPart root = context.bakeLayer(CCModelLayers.WINCH);
		this.base = root.getChild("base");
		this.crank = root.getChild("crank");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -11.0F, -1.0F, 10.0F, 10.0F, 3.0F), PartPose.offset(4.0F, 22.0F, 6.0F));
		root.addOrReplaceChild("crank", CubeListBuilder.create().texOffs(0, 13).addBox(-1.0F, -8.0F, -2.0F, 2.0F, 9.0F, 2.0F)
				.texOffs(8, 13).addBox(-1.0F, -8.0F, -5.0F, 2.0F, 2.0F, 3.0F), PartPose.offset(0.0F, 16.0F, 5.0F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void render(T winch, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		BlockState blockstate = winch.getLevel() != null ? winch.getLevel().getBlockState(winch.getBlockPos()) : CCBlocks.WINCH.get().defaultBlockState();
		Block block = blockstate.getBlock();
		if (block instanceof WinchBlock) {
			Direction facing = blockstate.getValue(WinchBlock.FACING);
			AttachFace face = blockstate.getValue(WinchBlock.FACE);

			poseStack.pushPose();
			poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

			poseStack.translate(0.5F, -1.5F, -0.5F);

			poseStack.mulPose(Axis.YP.rotationDegrees(face == AttachFace.WALL ? facing.toYRot() : facing.getOpposite().toYRot()));
			if (face != AttachFace.WALL) {
				poseStack.mulPose(Axis.XP.rotationDegrees(face == AttachFace.CEILING ? 90.0F : -90.0F));
				poseStack.translate(0.0D, -1.0D, face == AttachFace.CEILING ? -1.0D : 1.0D);
			}

			this.crank.zRot = Mth.HALF_PI + winch.getVisualRotation(partialTick) * Mth.DEG_TO_RAD;

			VertexConsumer vertexConsumer = WINCH_MATERIAL.buffer(buffer, RenderType::entitySolid);
			this.base.render(poseStack, vertexConsumer, combinedLight, combinedOverlay);
			this.crank.render(poseStack, vertexConsumer, combinedLight, combinedOverlay);
			this.base.render(poseStack, GLOW_MATERIAL.buffer(buffer, RenderType::entityTranslucentCull), combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, winch.getPower() / 15F);

			// System.out.println("G: " + winch.time + ", P: " + partialTick + ", R0: " + winch.getRotation(0) + ", R1: " + winch.getRotation(1) + ", R: " + winch.getRotation(partialTick));

			poseStack.popPose();
		}
	}
}