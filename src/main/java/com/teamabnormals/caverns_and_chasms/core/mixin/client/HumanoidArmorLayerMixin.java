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
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
	@Final
	@Shadow
	private A outerModel;

	@Shadow
	protected abstract void renderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_, ArmorItem p_289650_, Model p_289658_, boolean p_289668_, float p_289678_, float p_289674_, float p_289693_, ResourceLocation armorResource);

	@Shadow
	protected abstract boolean usesInnerModel(EquipmentSlot p_117129_);

	@Shadow
	protected abstract void renderTrim(ArmorMaterial p_289690_, PoseStack p_289687_, MultiBufferSource p_289643_, int p_289683_, ArmorTrim p_289692_, Model p_289663_, boolean p_289651_);

	@Shadow
	protected abstract Model getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlot slot, A model);

	@Shadow
	@Final
	public TextureAtlas armorTrimAtlas;
	@Shadow
	@Final
	private A innerModel;

	@Shadow
	protected abstract void renderArmorPiece(PoseStack p_117119_, MultiBufferSource p_117120_, T p_117121_, EquipmentSlot p_117122_, int p_117123_, A p_117124_);

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

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;setPartVisibility(Lnet/minecraft/client/model/HumanoidModel;Lnet/minecraft/world/entity/EquipmentSlot;)V", shift = At.Shift.BEFORE), method = "renderArmorPiece")
	public void renderArmorPiece(PoseStack poseStack, MultiBufferSource source, T entity, EquipmentSlot slot, int num, A model, CallbackInfo ci) {
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
			if (entity.getItemBySlot(EquipmentSlot.CHEST).is(Tags.Items.ARMORS_CHESTPLATES)) {
				return this.outerModel;
			}
			return this.innerModel;
		}
		return original.call(layer, slot);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;setPartVisibility(Lnet/minecraft/client/model/HumanoidModel;Lnet/minecraft/world/entity/EquipmentSlot;)V", shift = Shift.AFTER), method = "renderArmorPiece")
	public void renderArmorPieceForCowl(PoseStack poseStack, MultiBufferSource source, T entity, EquipmentSlot slot, int num, A model, CallbackInfo ci) {
		ItemStack stack = entity.getItemBySlot(slot);
		if (stack.is(CCItems.COWL.get())) {
			model.body.visible = true;
			model.rightArm.visible = true;
			model.leftArm.visible = true;
			if (entity.getItemBySlot(EquipmentSlot.CHEST).is(Tags.Items.ARMORS_CHESTPLATES)) {
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

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hasFoil()Z", shift = At.Shift.BEFORE), method = "renderArmorPiece")
	public void renderSanguineTrim(PoseStack poseStack, MultiBufferSource source, T entity, EquipmentSlot slot, int num, A model, CallbackInfo ci) {
		ItemStack stack = entity.getItemBySlot(slot);
		if (stack.getItem() instanceof ArmorItem armorItem) {
			boolean copper = armorItem.getMaterial() == CCArmorMaterials.COPPER || armorItem.getMaterial() == CCArmorMaterials.EXPOSED_COPPER
					|| armorItem.getMaterial() == CCArmorMaterials.WEATHERED_COPPER || armorItem.getMaterial() == CCArmorMaterials.OXIDIZED_COPPER;
			boolean sanguine = armorItem.getMaterial() == CCArmorMaterials.SANGUINE;
			if (copper || sanguine) {
				RegistryAccess access = entity.level().registryAccess();
				ArmorTrim.getTrim(access, stack).ifPresent(armorTrim -> {
					ArmorTrim trim = new ArmorTrim(armorTrim.material(), access.registryOrThrow(Registries.TRIM_PATTERN).getHolderOrThrow(copper ? CCTrimPatterns.COPPER : CCTrimPatterns.SANGUINE));
					((CCArmorTrim) trim).setFaded(((CCArmorTrim) armorTrim).isFaded());
					((CCArmorTrim) trim).setEmissive(((CCArmorTrim) armorTrim).isEmissive());
					((CCArmorTrim) trim).setPulse(((CCArmorTrim) armorTrim).isPulse());
					this.renderTrim(armorItem.getMaterial(), poseStack, source, num, trim, this.getArmorModelHook(entity, stack, slot, model), this.usesInnerModel(slot));
				});
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "renderTrim(Lnet/minecraft/world/item/ArmorMaterial;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/armortrim/ArmorTrim;Lnet/minecraft/client/model/Model;Z)V", cancellable = true, remap = false)
	public void renderTrim(ArmorMaterial material, PoseStack stack, MultiBufferSource source, int i, ArmorTrim trim, Model model, boolean inner, CallbackInfo ci) {
		CCArmorTrim armorTrim = (CCArmorTrim) trim;
		boolean faded = armorTrim.isFaded();
		boolean emissive = armorTrim.isEmissive();
		boolean pulse = armorTrim.isPulse();
		if (faded || emissive || pulse) {
			TextureAtlasSprite sprite = this.armorTrimAtlas.getSprite(inner ? trim.innerTexture(material) : trim.outerTexture(material));
			Function<ResourceLocation, RenderType> type = emissive ? CCRenderTypes.ARMOR_CUTOUT_NO_CULL_EMISSIVE : CCRenderTypes.ARMOR_TRANSLUCENT_NO_CULL;
			VertexConsumer vertexconsumer = sprite.wrap(source.getBuffer(type.apply(Sheets.ARMOR_TRIMS_SHEET)));
			model.renderToBuffer(stack, vertexconsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, TrimModifierSmithingTemplateItem.getTrimAlpha(faded, emissive, pulse));
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
			int i = PotionUtils.getColor(stack);
			float r = (float) (i >> 16 & 255) / 255.0F;
			float g = (float) (i >> 8 & 255) / 255.0F;
			float b = (float) (i & 255) / 255.0F;

			this.renderModel(poseStack, source, packedLight, null, this.outerModel, flag, r, g, b, TETHER_POTION_LOCATION);
			this.renderModel(poseStack, source, packedLight, null, this.outerModel, flag, 1.0F, 1.0F, 1.0F, stack.getOrCreateTag().getBoolean("Subtle") ? SUBTLE_TETHER_POTION_OVERLAY_LOCATION : TETHER_POTION_OVERLAY_LOCATION);
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
