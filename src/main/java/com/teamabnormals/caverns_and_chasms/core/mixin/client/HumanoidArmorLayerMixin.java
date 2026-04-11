package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.client.CCRenderTypes;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Mime;
import com.teamabnormals.caverns_and_chasms.common.item.CCArmorTrim;
import com.teamabnormals.caverns_and_chasms.common.item.TetherPotionItem;
import com.teamabnormals.caverns_and_chasms.common.item.TrimModifierSmithingTemplateItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers.CCArmorMaterials;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCTrimPatterns;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
	@Final
	@Shadow
	private A outerModel;

	@Shadow
	protected abstract boolean usesInnerModel(EquipmentSlot p_117129_);

	@Shadow
	protected abstract Model getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlot slot, A model);

	@Shadow
	@Final
	public TextureAtlas armorTrimAtlas;
	@Shadow
	@Final
	private A innerModel;

	@Shadow
	protected abstract void renderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_, Model p_289658_, int p_350798_, ResourceLocation p_324344_);

	@Shadow
	protected abstract void renderGlint(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, A model);

	@Shadow
	protected abstract void renderTrim(Holder<ArmorMaterial> armorMaterial, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ArmorTrim trim, A model, boolean innerTexture);

	@Shadow
	protected abstract void renderTrim(Holder<ArmorMaterial> p_323506_, PoseStack p_289687_, MultiBufferSource p_289643_, int p_289683_, ArmorTrim p_289692_, Model p_289663_, boolean p_289651_);

	@Unique
	private static final ResourceLocation TETHER_POTION_LOCATION = CavernsAndChasms.location("textures/models/armor/tether_potion.png");
	@Unique
	private static final ResourceLocation TETHER_POTION_OVERLAY_LOCATION = CavernsAndChasms.location("textures/models/armor/tether_potion_overlay.png");
	@Unique
	private static final ResourceLocation SUBTLE_TETHER_POTION_OVERLAY_LOCATION = CavernsAndChasms.location("textures/models/armor/tether_potion_overlay_subtle.png");

	public HumanoidArmorLayerMixin(RenderLayerParent<T, M> entityRenderer) {
		super(entityRenderer);
	}

	@Inject(at = @At("TAIL"), method = "render")
	public void render(PoseStack stack, MultiBufferSource source, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		this.renderWornPotion(stack, source, packedLight, entity);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;setPartVisibility(Lnet/minecraft/client/model/HumanoidModel;Lnet/minecraft/world/entity/EquipmentSlot;)V", shift = At.Shift.BEFORE), method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V")
	public void renderArmorPiece(PoseStack poseStack, MultiBufferSource source, T entity, EquipmentSlot slot, int packedLight, A model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (entity instanceof Mime) {
			if (slot == EquipmentSlot.LEGS) {
				this.verticallyOffsetModelPart(this.getParentModel().body, model.body, 1.0F);
			} else if (slot == EquipmentSlot.FEET) {
				this.verticallyOffsetModelPart(this.getParentModel().rightLeg, model.rightLeg, 1.0F);
				this.verticallyOffsetModelPart(this.getParentModel().leftLeg, model.leftLeg, 1.0F);
			}
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;getArmorModel(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/client/model/HumanoidModel;", ordinal = 3), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V")
	public A renderArmorPieceForCowl(HumanoidArmorLayer layer, EquipmentSlot slot, Operation<A> original, PoseStack poseStack, MultiBufferSource source, int p_117098_, T entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
		if (stack.is(CCItems.COWL.get())) {
			if (entity.getItemBySlot(EquipmentSlot.CHEST).is(ItemTags.CHEST_ARMOR)) {
				return this.outerModel;
			}
			return this.innerModel;
		}
		return original.call(layer, slot);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;setPartVisibility(Lnet/minecraft/client/model/HumanoidModel;Lnet/minecraft/world/entity/EquipmentSlot;)V", shift = Shift.AFTER), method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V")
	public void renderArmorPieceForCowl(PoseStack poseStack, MultiBufferSource source, T entity, EquipmentSlot slot, int num, A model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		ItemStack stack = entity.getItemBySlot(slot);
		if (stack.is(CCItems.COWL.get())) {
			model.body.visible = true;
			model.rightArm.visible = true;
			model.leftArm.visible = true;
			if (entity.getItemBySlot(EquipmentSlot.CHEST).is(ItemTags.CHEST_ARMOR)) {
				setScale(model.head, 0.95F);
			} else {
				setScale(model.head, 1.02F);
			}
		}
	}

	@Unique
	private static void setScale(ModelPart part, float scale) {
		part.xScale = scale;
		part.yScale = scale;
		part.zScale = scale;
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hasFoil()Z", shift = At.Shift.BEFORE), method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V")
	public void renderSanguineTrim(PoseStack poseStack, MultiBufferSource source, T entity, EquipmentSlot slot, int num, A model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		ItemStack stack = entity.getItemBySlot(slot);
		if (stack.getItem() instanceof ArmorItem armorItem) {
			boolean copper = armorItem.getMaterial() == CCArmorMaterials.COPPER || armorItem.getMaterial() == CCArmorMaterials.EXPOSED_COPPER || armorItem.getMaterial() == CCArmorMaterials.WEATHERED_COPPER || armorItem.getMaterial() == CCArmorMaterials.OXIDIZED_COPPER;
			boolean sanguine = armorItem.getMaterial() == CCArmorMaterials.SANGUINE;
			if (copper || sanguine) {
				RegistryAccess access = entity.level().registryAccess();
				CCArmorTrim armorTrim = CCArmorTrim.create(stack);

				if (armorTrim != null) {
					ArmorTrim trim = new ArmorTrim(((ArmorTrim) armorTrim).material(), access.registryOrThrow(Registries.TRIM_PATTERN).getHolderOrThrow(copper ? CCTrimPatterns.COPPER : CCTrimPatterns.SANGUINE));
					((CCArmorTrim) trim).setFaded(armorTrim.isFaded());
					((CCArmorTrim) trim).setEmissive(armorTrim.isEmissive());
					((CCArmorTrim) trim).setPulse(armorTrim.isPulse());
					this.renderTrim(armorItem.getMaterial(), poseStack, source, num, trim, this.getArmorModelHook(entity, stack, slot, model), this.usesInnerModel(slot));
				}
			}
		}
	}

	@ModifyVariable(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V", at = @At("STORE"), ordinal = 0)
	private ArmorTrim modifyArmorTrim(ArmorTrim original, PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot) {
		if (original == null) return null;
		ItemStack stack = livingEntity.getItemBySlot(slot);
		return (ArmorTrim) CCArmorTrim.create(stack);
	}

	@Inject(at = @At("HEAD"), method = "renderTrim(Lnet/minecraft/core/Holder;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/armortrim/ArmorTrim;Lnet/minecraft/client/model/Model;Z)V", cancellable = true, remap = false)
	public void renderTrim(Holder<ArmorMaterial> material, PoseStack stack, MultiBufferSource source, int i, ArmorTrim trim, Model model, boolean inner, CallbackInfo ci) {
		CCArmorTrim armorTrim = (CCArmorTrim) trim;
		boolean faded = armorTrim.isFaded();
		boolean emissive = armorTrim.isEmissive();
		boolean pulse = armorTrim.isPulse();
		if (faded || emissive || pulse) {
			TextureAtlasSprite sprite = this.armorTrimAtlas.getSprite(inner ? trim.innerTexture(material) : trim.outerTexture(material));
			Function<ResourceLocation, RenderType> type = emissive ? CCRenderTypes.ARMOR_CUTOUT_NO_CULL_EMISSIVE : CCRenderTypes.ARMOR_TRANSLUCENT_NO_CULL;
			VertexConsumer vertexconsumer = sprite.wrap(source.getBuffer(type.apply(Sheets.ARMOR_TRIMS_SHEET)));
			model.renderToBuffer(stack, vertexconsumer, i, OverlayTexture.NO_OVERLAY, ARGB32.colorFromFloat(TrimModifierSmithingTemplateItem.getTrimAlpha(faded, emissive, pulse), 1.0F, 1.0F, 1.0F));
			ci.cancel();
		}
	}

	@Unique
	private void renderWornPotion(PoseStack poseStack, MultiBufferSource source, int packedLight, T entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
		if (stack.getItem() instanceof TetherPotionItem) {
			this.getParentModel().copyPropertiesTo(this.outerModel);

			this.outerModel.setAllVisible(false);
			this.outerModel.head.visible = true;
			this.outerModel.hat.visible = true;

			boolean flag = stack.hasFoil();
			int i = stack.get(DataComponents.POTION_CONTENTS).getColor();
			float r = (float) (i >> 16 & 255) / 255.0F;
			float g = (float) (i >> 8 & 255) / 255.0F;
			float b = (float) (i & 255) / 255.0F;

			this.renderModel(poseStack, source, packedLight, this.outerModel, ARGB32.colorFromFloat(1.0F, r, g, b), TETHER_POTION_LOCATION);
			this.renderModel(poseStack, source, packedLight, this.outerModel, ARGB32.colorFromFloat(1.0F, 1.0F, 1.0F, 1.0F), stack.has(CCDataComponents.SUBTLE) ? SUBTLE_TETHER_POTION_OVERLAY_LOCATION : TETHER_POTION_OVERLAY_LOCATION);
			if (flag) this.renderGlint(poseStack, source, packedLight, this.outerModel);
		}
	}

	@Unique
	private void verticallyOffsetModelPart(ModelPart parentModelPart, ModelPart modelPart, float offset) {
		float yaw = parentModelPart.xRot;
		float pitch = parentModelPart.yRot;
		float roll = parentModelPart.zRot;

		modelPart.x += (Mth.sin(yaw) * Mth.sin(pitch) * Mth.cos(roll) - Mth.cos(yaw) * Mth.sin(roll)) * offset;
		modelPart.y += (Mth.sin(yaw) * Mth.sin(pitch) * Mth.sin(roll) + Mth.cos(yaw) * Mth.cos(roll)) * offset;
		modelPart.z += Mth.sin(yaw) * Mth.cos(pitch) * offset;
	}
}
