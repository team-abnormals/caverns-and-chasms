package com.teamabnormals.caverns_and_chasms.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.MovingDoorType;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.RollerDoorBlock;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RollerDoorRenderer<T extends MovingDoorBlockEntity> implements BlockEntityRenderer<T> {
	public static boolean renderAsItem;

	private final ModelPart header;
	private final ModelPart[] slats;

	public RollerDoorRenderer(BlockEntityRendererProvider.Context context) {
		ModelPart root = context.bakeLayer(CCModelLayers.ROLLER_DOOR);
		this.header = root.getChild("header");
		this.slats = new ModelPart[]{root.getChild("slat1"), root.getChild("slat2"), root.getChild("slat3"), root.getChild("slat4")};
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild("header", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 0.0F, -2.0F, 16.0F, 4.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 8.0F, -6.0F));
		root.addOrReplaceChild("slat1", CubeListBuilder.create().texOffs(0, 8).addBox(-8.0F, 0.0F, -1.0F, 16.0F, 4.0F, 2.0F), PartPose.offset(0.0F, 8.0F, -6.0F));
		root.addOrReplaceChild("slat2", CubeListBuilder.create().texOffs(0, 14).addBox(-8.0F, 0.0F, -1.0F, 16.0F, 4.0F, 2.0F), PartPose.offset(0.0F, 12.0F, -6.0F));
		root.addOrReplaceChild("slat3", CubeListBuilder.create().texOffs(0, 20).addBox(-8.0F, 0.0F, -1.0F, 16.0F, 4.0F, 2.0F), PartPose.offset(0.0F, 16.0F, -6.0F));
		root.addOrReplaceChild("slat4", CubeListBuilder.create().texOffs(0, 26).addBox(-8.0F, 0.0F, -1.0F, 16.0F, 4.0F, 2.0F), PartPose.offset(0.0F, 20.0F, -6.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Override
	public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		BlockState blockState = blockEntity.getLevel() != null ? blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()) : CCBlocks.ROLLER_DOOR_HEADER.get().defaultBlockState();
		if (blockState.getBlock() instanceof RollerDoorBlock) {
			Direction facing = blockState.getValue(RollerDoorBlock.FACING);
			AttachFace face = blockState.getValue(RollerDoorBlock.FACE);

			boolean showOldVisuals = blockEntity.shouldShowOldVisuals(partialTick);
			float openness = (float) blockEntity.getVisualOpenness(partialTick);
			MovingDoorType thisType = blockEntity.getVisualDoorType(showOldVisuals);
			MovingDoorType belowType = blockEntity.getVisualBelowDoorType(showOldVisuals);
			boolean isBottom = blockEntity.isVisuallyBottom(showOldVisuals) || renderAsItem;
			boolean isBelowBottom = blockEntity.visualIsBelowBottom(showOldVisuals);

			Material thisMaterial = isBottom ? thisType.getBottomMaterial() : thisType.getNormalMaterial();
			Material belowMaterial = belowType == null ? thisMaterial : isBelowBottom ? belowType.getBottomMaterial() : belowType.getNormalMaterial();

			poseStack.pushPose();
			poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

			poseStack.translate(0.5F, -1.5F, -0.5F);

			poseStack.mulPose(Axis.YP.rotationDegrees(face == AttachFace.WALL ? facing.toYRot() : facing.getOpposite().toYRot()));
			if (face != AttachFace.WALL) {
				poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
				poseStack.translate(0.0D, -1.0D, face == AttachFace.CEILING ? 1.75D : 1.0D);
			}

			if (blockEntity instanceof MovingDoorHeaderBlockEntity) {
				this.header.render(poseStack, thisMaterial.buffer(buffer, RenderType::entityTranslucentCull), combinedLight, combinedOverlay);
			}

			for (int i = 0; i < this.slats.length; i++) {
				ModelPart slat = this.slats[i];
				Material material = thisMaterial;

				slat.y = 4.0F + (i + 1) * 4.0F - openness * 16.0F;
				if (slat.y < 8.0F) {
					if (isBottom) {
						continue;
					} else {
						slat.y += 16.0F;
						if (slat.y >= 12.0F) {
							material = belowMaterial;
						}
					}
				}
				slat.render(poseStack, material.buffer(buffer, RenderType::entityTranslucentCull), combinedLight, combinedOverlay);
			}

			poseStack.popPose();
		}
	}
}