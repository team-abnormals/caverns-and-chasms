package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.teamabnormals.caverns_and_chasms.client.model.DeeperModel;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.Deeper;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DeeperPowerLayer<T extends Deeper, M extends DeeperModel<T>> extends EnergySwirlLayer<T, M> {
	private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private final M model;

	public DeeperPowerLayer(RenderLayerParent<T, M> parent, M model) {
		super(parent);
		this.model = model;
	}

	@Override
	protected float xOffset(float p_116683_) {
		return p_116683_ * 0.01F;
	}

	@Override
	protected ResourceLocation getTextureLocation() {
		return POWER_LOCATION;
	}

	@Override
	protected M model() {
		return this.model;
	}
}