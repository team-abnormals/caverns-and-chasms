package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.client.render.*;
import com.minecraftabnormals.caverns_and_chasms.client.render.layer.UndeadParrotLayer;
import com.minecraftabnormals.caverns_and_chasms.client.render.skeleton.SkeletonCatRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.skeleton.SkeletonParrotRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.skeleton.SkeletonWolfRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.zombie.ZombieCatRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.zombie.ZombieParrotRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.zombie.ZombieWolfRenderer;
import com.minecraftabnormals.caverns_and_chasms.common.item.OreDetectorItem;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.blueprint.client.renderer.SlabfishHatRenderLayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, value = Dist.CLIENT)
public class CCClientCompat {

	public static void registerClientCompat() {
		registerRenderLayers();
		registerItemProperties();
	}

	public static void registerRenderLayers() {
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.GOLDEN_BARS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.SILVER_BARS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.SPIKED_RAIL.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.BRAZIER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.SOUL_BRAZIER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.ENDER_BRAZIER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CURSED_BRAZIER.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CURSED_FIRE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CURSED_TORCH.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CURSED_WALL_TORCH.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CURSED_LANTERN.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CURSED_CAMPFIRE.get(), RenderType.cutout());
	}

	public static void registerItemProperties() {
		ItemProperties.register(Items.CROSSBOW, new ResourceLocation(CavernsAndChasms.MOD_ID, "silver_arrow"), (stack, world, entity, hash) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, CCItems.SILVER_ARROW.get()) ? 1.0F : 0.0F);

		ItemProperties.register(CCItems.GOLDEN_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, world, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemProperties.register(CCItems.GOLDEN_WATER_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, world, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemProperties.register(CCItems.GOLDEN_LAVA_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, world, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemProperties.register(CCItems.GOLDEN_MILK_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, world, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));

		ItemProperties.register(CCItems.ORE_DETECTOR.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "detect"), (stack, world, entity, hash) -> {
			if (stack.hasTag() && stack.getTag().contains("Detecting") && entity instanceof Player && OreDetectorItem.getDetectionData(stack)) {
				return 1;
			} else {
				return 0;
			}
		});
	}

	@SubscribeEvent
	public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
//		event.getSkins().forEach(skin -> {
//			PlayerRenderer renderer = event.getSkin(skin);
//			renderer.addLayer(new UndeadParrotLayer<>(renderer, event.getEntityModels()));
//		});
	}


}
