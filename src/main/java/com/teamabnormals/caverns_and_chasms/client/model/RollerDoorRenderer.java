package com.teamabnormals.caverns_and_chasms.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.common.block.entity.RollerDoorBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.roller_door.RollerDoorBlock;
import com.teamabnormals.caverns_and_chasms.common.block.roller_door.RollerDoorHeaderBlock;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
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
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RollerDoorRenderer<T extends RollerDoorBlockEntity> implements BlockEntityRenderer<T> {
	public static boolean renderAsItem;

	public static final Material ROLLER_DOOR_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, CavernsAndChasms.location("entity/roller_door/roller_door"));
	public static final Material ROLLER_DOOR_BOTTOM_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, CavernsAndChasms.location("entity/roller_door/roller_door_bottom"));

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
	public void render(T rollerDoor, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		BlockState blockstate = rollerDoor.getLevel() != null ? rollerDoor.getLevel().getBlockState(rollerDoor.getBlockPos()) : CCBlocks.ROLLER_DOOR_HEADER.get().defaultBlockState();
		Block block = blockstate.getBlock();
		if (block instanceof RollerDoorBlock) {
			Direction facing = blockstate.getValue(RollerDoorBlock.FACING);
			AttachFace face = blockstate.getValue(RollerDoorBlock.FACE);
			float openness = (float) rollerDoor.getOpenness(partialTick);
			boolean header = block instanceof RollerDoorHeaderBlock;
			boolean bottom = rollerDoor.isBottom() || renderAsItem;

			poseStack.pushPose();
			poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

			poseStack.translate(0.5F, -1.5F, -0.5F);

			poseStack.mulPose(Axis.YP.rotationDegrees(face == AttachFace.WALL ? facing.toYRot() : facing.getOpposite().toYRot()));
			if (face != AttachFace.WALL) {
				poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
				poseStack.translate(0.0D, -1.0D, face == AttachFace.CEILING ? 1.75D : 1.0D);
			}

			if (header)
				this.header.render(poseStack, ROLLER_DOOR_MATERIAL.buffer(buffer, RenderType::entitySolid), combinedLight, combinedOverlay);

			for (int i = 0; i < this.slats.length; i++) {
				ModelPart slat = this.slats[i];
				Material material = bottom ? ROLLER_DOOR_BOTTOM_MATERIAL : ROLLER_DOOR_MATERIAL;
				slat.y = 4.0F + (i + 1) * 4.0F - openness * 16.0F;
				if (slat.y < 8.0F) {
					if (bottom) {
						continue;
					} else {
						slat.y += 16.0F;
						if (rollerDoor.hasBottomBelow() && slat.y >= 12.0F)
							material = ROLLER_DOOR_BOTTOM_MATERIAL;
					}
				}
				slat.render(poseStack, material.buffer(buffer, RenderType::entitySolid), combinedLight, combinedOverlay);
			}

			poseStack.popPose();
		}
	}
}