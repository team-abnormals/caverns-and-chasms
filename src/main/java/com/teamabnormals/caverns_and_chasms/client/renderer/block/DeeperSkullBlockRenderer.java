package com.teamabnormals.caverns_and_chasms.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.client.model.DeeperHeadModel;
import com.teamabnormals.caverns_and_chasms.client.model.EvendeeperHeadModel;
import com.teamabnormals.caverns_and_chasms.common.block.entity.DeeperSkullBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.DeeperHat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCSkullTypes;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class DeeperSkullBlockRenderer extends SkullBlockRenderer {
	private static final ResourceLocation DEEPER_TEXTURE = CavernsAndChasms.location("textures/entity/deeper/deeper.png");
	private static final ResourceLocation EVENDEEPER_TEXTURE = CavernsAndChasms.location("textures/entity/evendeeper/evendeeper.png");
	private final SkullModelBase deeperModel;
	private final SkullModelBase evendeeperModel;

	public DeeperSkullBlockRenderer(Context context) {
		super(context);
		this.deeperModel = new DeeperHeadModel(context.bakeLayer(CCModelLayers.DEEPER_HEAD));
		this.evendeeperModel = new EvendeeperHeadModel(context.bakeLayer(CCModelLayers.EVENDEEPER_HEAD));
	}

	@Override
	public void render(SkullBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int p_112539_) {
		float f = blockEntity.getAnimation(partialTick);
		BlockState blockstate = blockEntity.getBlockState();
		boolean flag = blockstate.getBlock() instanceof WallSkullBlock;
		Direction direction = flag ? blockstate.getValue(WallSkullBlock.FACING) : null;
		int i = flag ? RotationSegment.convertToSegment(direction.getOpposite()) : blockstate.getValue(SkullBlock.ROTATION);
		float f1 = RotationSegment.convertToDegrees(i);
		SkullBlock.Type skullblocktype = ((AbstractSkullBlock) blockstate.getBlock()).getType();
		renderDeeperSkull(direction, f1, f, poseStack, buffer, packedLight, skullblocktype, ((DeeperSkullBlockEntity) blockEntity).getHat());
	}

	private void renderDeeperSkull(@Nullable Direction direction, float rot, float anim, PoseStack poseStack, MultiBufferSource buffer, int packedLight, SkullBlock.Type skullblocktype, DeeperHat hat) {
		poseStack.pushPose();
		if (direction == null) {
			poseStack.translate(0.5F, 0.0F, 0.5F);
		} else {
			poseStack.translate(0.5F - (float) direction.getStepX() * 0.25F, 0.25F, 0.5F - (float) direction.getStepZ() * 0.25F);
		}

		poseStack.scale(-1.0F, -1.0F, 1.0F);
		VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(skullblocktype == CCSkullTypes.EVENDEEPER ? EVENDEEPER_TEXTURE : DEEPER_TEXTURE));
		SkullModelBase model = skullblocktype == CCSkullTypes.EVENDEEPER ? this.evendeeperModel : this.deeperModel;
		model.setupAnim(anim, rot, 0.0F);
		model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		if (hat != DeeperHat.NONE) {
			vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(skullblocktype == CCSkullTypes.EVENDEEPER ? hat.getEvendeeperTexture() : hat.getDeeperTexture()));
			model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
		poseStack.popPose();
	}
}