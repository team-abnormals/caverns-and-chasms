package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	private static final Material CUPRIC_FIRE_0 = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(CavernsAndChasms.MOD_ID, "block/cupric_fire_0"));
	private static final Material CUPRIC_FIRE_1 = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(CavernsAndChasms.MOD_ID, "block/cupric_fire_1"));

	@ModifyVariable(method = "renderFlame", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0), ordinal = 0)
	private TextureAtlasSprite renderFlame0(TextureAtlasSprite sprite, PoseStack poseStack, MultiBufferSource source, Entity entity) {
		return entity.getType() == CCEntityTypes.COPPER_GOLEM.get() ? CUPRIC_FIRE_0.sprite() : sprite;
	}

	@ModifyVariable(method = "renderFlame", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 1), ordinal = 1)
	private TextureAtlasSprite renderFlame1(TextureAtlasSprite sprite, PoseStack poseStack, MultiBufferSource source, Entity entity) {
		return entity.getType() == CCEntityTypes.COPPER_GOLEM.get() ? CUPRIC_FIRE_1.sprite() : sprite;
	}
}