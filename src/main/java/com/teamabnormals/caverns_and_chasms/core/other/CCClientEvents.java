package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.integration.quark.ToolboxTooltips;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, value = Dist.CLIENT)
public class CCClientEvents {

	@SubscribeEvent
	public static void makeTooltip(RenderTooltipEvent.GatherComponents event) {
		if (ModList.get().isLoaded("quark")) {
			ToolboxTooltips.makeTooltip(event);
		}
	}

	public static void customFoilAnimation(HumanoidModel<?> model, ModelPart armModel, boolean leftArm) {
		float f = model.attackTime;
		model.body.yRot = Mth.sin(Mth.sqrt(f) * ((float) Math.PI * 2F)) * 0.2F;
		if (leftArm) {
			model.body.yRot *= -1.0F;
		}

		model.rightArm.z = Mth.sin(model.body.yRot) * 5.0F;
		model.rightArm.x = -Mth.cos(model.body.yRot) * 5.0F;
		model.leftArm.z = -Mth.sin(model.body.yRot) * 5.0F;
		model.leftArm.x = Mth.cos(model.body.yRot) * 5.0F;
		model.rightArm.yRot += model.body.yRot;
		model.leftArm.yRot += model.body.yRot;
		model.leftArm.xRot += model.body.yRot;
		f = 1.0F - ((1.0F - model.attackTime) * f * f);
		float f1 = Mth.sin(f * (float) Math.PI);
		float f2 = Mth.sin(model.attackTime * (float) Math.PI) * -(model.head.xRot - 0.7F) * 0.75F;
		armModel.xRot -= f1 * 1.2F + f2;
		// armModel.yRot += model.body.yRot * 2.0F;
		armModel.zRot += Mth.sin(model.attackTime * (float) Math.PI) * -0.4F;
	}
}