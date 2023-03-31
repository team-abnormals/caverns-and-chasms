package com.teamabnormals.caverns_and_chasms.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import com.teamabnormals.caverns_and_chasms.common.block.entity.ToolboxBlockEntity;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ToolboxRenderer<T extends ToolboxBlockEntity> implements BlockEntityRenderer<T> {
	public static Block itemBlock = null;

	public static final ResourceLocation TOOLBOX_SHEET = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/atlas/toolboxes.png");
	public static final Map<WeatherState, Material> TOOLBOX_MATERIALS = Map.of(
			WeatherState.UNAFFECTED, new Material(TOOLBOX_SHEET, new ResourceLocation(CavernsAndChasms.MOD_ID, "entity/toolbox/toolbox")),
			WeatherState.EXPOSED, new Material(TOOLBOX_SHEET, new ResourceLocation(CavernsAndChasms.MOD_ID, "entity/toolbox/exposed_toolbox")),
			WeatherState.WEATHERED, new Material(TOOLBOX_SHEET, new ResourceLocation(CavernsAndChasms.MOD_ID, "entity/toolbox/weathered_toolbox")),
			WeatherState.OXIDIZED, new Material(TOOLBOX_SHEET, new ResourceLocation(CavernsAndChasms.MOD_ID, "entity/toolbox/oxidized_toolbox")));

	private final ModelPart base;
	private final ModelPart lid;

	public ToolboxRenderer(BlockEntityRendererProvider.Context context) {
		ModelPart root = context.bakeLayer(CCModelLayers.TOOLBOX);
		this.lid = root.getChild("lid");
		this.base = root.getChild("base");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -6.0F, -4.0F, 16.0F, 6.0F, 8.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
		root.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 14).addBox(-8.0F, -2.0F, -8.0F, 16.0F, 2.0F, 8.0F).texOffs(0, 29).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 3.0F, 0.0F), PartPose.offset(0.0F, 18.0F, 4.0F));
		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void render(T toolbox, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		Block inventoryBlock = itemBlock;
		BlockState state = toolbox.getLevel() != null ? toolbox.getBlockState() : CCBlocks.TOOLBOX.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
		Block block = inventoryBlock == null ? state.getBlock() : inventoryBlock;
		if (block instanceof ToolboxBlock toolboxBlock) {
			poseStack.pushPose();

			poseStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
			poseStack.translate(0.5F, -1.5F, -0.5F);
			poseStack.mulPose(Vector3f.YP.rotationDegrees(state.getValue(ToolboxBlock.FACING).toYRot()));

			float openness = 1.0F - toolbox.getProgress(partialTicks);
			lid.xRot = -((1.0F - openness * openness * openness) * ((float) Math.PI / 2F));

			VertexConsumer vertexConsumer = TOOLBOX_MATERIALS.get(toolboxBlock.getWeatherState()).buffer(buffer, RenderType::entityCutout);
			lid.render(poseStack, vertexConsumer, combinedLight, combinedOverlay);
			base.render(poseStack, vertexConsumer, combinedLight, combinedOverlay);

			poseStack.popPose();
		}
	}
}