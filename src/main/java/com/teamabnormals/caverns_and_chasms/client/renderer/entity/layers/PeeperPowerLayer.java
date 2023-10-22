package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.teamabnormals.caverns_and_chasms.client.model.PeeperModel;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Peeper;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PeeperPowerLayer extends EnergySwirlLayer<Peeper, PeeperModel<Peeper>> {
	private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private final PeeperModel<Peeper> model;

	public PeeperPowerLayer(RenderLayerParent<Peeper, PeeperModel<Peeper>> parent, EntityModelSet modelSet) {
		super(parent);
		this.model = new PeeperModel<>(modelSet.bakeLayer(CCModelLayers.PEEPER_ARMOR));
	}

	protected float xOffset(float p_116683_) {
		return p_116683_ * 0.01F;
	}

	protected ResourceLocation getTextureLocation() {
		return POWER_LOCATION;
	}

	protected EntityModel<Peeper> model() {
		return this.model;
	}
}