package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.client.gui.MonocleGuiOverlay;
import com.teamabnormals.caverns_and_chasms.client.gui.MonocleGuiOverlay.MonocleHeadGuiOverlay;
import com.teamabnormals.caverns_and_chasms.client.model.DeeperHeadModel;
import com.teamabnormals.caverns_and_chasms.client.model.MimeHeadModel;
import com.teamabnormals.caverns_and_chasms.client.model.PeeperHeadModel;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatOnShoulderLayer;
import com.teamabnormals.caverns_and_chasms.common.item.BejeweledPearlItem;
import com.teamabnormals.caverns_and_chasms.common.item.GoldenBucketItem;
import com.teamabnormals.caverns_and_chasms.common.item.copper.TuningForkItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCSkullTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.datapack.CCTrimMaterials;
import com.teamabnormals.caverns_and_chasms.integration.quark.ToolboxTooltips.ToolboxComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CCClientCompat {

	public static void registerClientCompat() {
		registerRenderLayers();
		registerItemProperties();
		CCTrimMaterials.registerArmorMaterialOverrides();
		CCSkullTypes.registerSkullModels();
	}

	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.AddLayers event) {
		event.getSkins().forEach(skin -> {
			PlayerRenderer renderer = event.getSkin(skin);
			renderer.addLayer(new RatOnShoulderLayer(renderer, event.getEntityModels()));
		});
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, color) -> color > 0 ? -1 : TuningForkItem.getNoteColor(stack), CCItems.TUNING_FORK.get());
		event.register((stack, color) -> color > 0 ? -1 : PotionUtils.getColor(stack), CCItems.TETHER_POTION.get());
		event.register((stack, color) -> color > 0 ? -1 : PotionUtils.getColor(stack), CCItems.IMPACT_POTION.get());
		event.register((stack, color) -> color > 0 ? -1 : PotionUtils.getColor(stack), CCItems.TRAIL_POTION.get());
		event.register((stack, color) -> color > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack), Items.BUNDLE);
		event.register((stack, color) -> color > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack), CCItems.FOIL.get());
		event.register((stack, color) -> color > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack), CCItems.COWL.get());
	}

	@SubscribeEvent
	public static void createSkullModels(EntityRenderersEvent.CreateSkullModels event) {
		event.registerSkullModel(CCSkullTypes.DEEPER, new DeeperHeadModel(event.getEntityModelSet().bakeLayer(CCModelLayers.DEEPER_HEAD)));
		event.registerSkullModel(CCSkullTypes.MIME, new MimeHeadModel(event.getEntityModelSet().bakeLayer(CCModelLayers.MIME_HEAD)));
		event.registerSkullModel(CCSkullTypes.PEEPER, new PeeperHeadModel(event.getEntityModelSet().bakeLayer(CCModelLayers.PEEPER_HEAD)));
	}

	@SubscribeEvent
	public static void registerClientTooltips(RegisterClientTooltipComponentFactoriesEvent event) {
		if (ModList.get().isLoaded("quark")) {
			event.register(ToolboxComponent.class, Function.identity());
		}
	}

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
		event.registerAbove(new ResourceLocation("spyglass"), "monocle", new MonocleGuiOverlay());
		event.registerAbove(CavernsAndChasms.location("monocle"), "monocle_head", new MonocleHeadGuiOverlay());
	}

	public static void registerRenderLayers() {
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.COPPER_GRATE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.EXPOSED_COPPER_GRATE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WEATHERED_COPPER_GRATE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.OXIDIZED_COPPER_GRATE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_COPPER_GRATE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_EXPOSED_COPPER_GRATE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_WEATHERED_COPPER_GRATE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_OXIDIZED_COPPER_GRATE.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.COPPER_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.EXPOSED_COPPER_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WEATHERED_COPPER_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.OXIDIZED_COPPER_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_COPPER_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_EXPOSED_COPPER_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_WEATHERED_COPPER_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_OXIDIZED_COPPER_DOOR.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.COPPER_TRAPDOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.EXPOSED_COPPER_TRAPDOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WEATHERED_COPPER_TRAPDOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.OXIDIZED_COPPER_TRAPDOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_COPPER_TRAPDOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_EXPOSED_COPPER_TRAPDOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_WEATHERED_COPPER_TRAPDOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_OXIDIZED_COPPER_TRAPDOOR.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.COPPER_BARS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.EXPOSED_COPPER_BARS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WEATHERED_COPPER_BARS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.OXIDIZED_COPPER_BARS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_COPPER_BARS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_EXPOSED_COPPER_BARS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_WEATHERED_COPPER_BARS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_OXIDIZED_COPPER_BARS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.GOLDEN_BARS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.SILVER_BARS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.TIN_BARS.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.COPPER_RAIL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.EXPOSED_COPPER_RAIL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WEATHERED_COPPER_RAIL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.OXIDIZED_COPPER_RAIL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_COPPER_RAIL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_EXPOSED_COPPER_RAIL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_WEATHERED_COPPER_RAIL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WAXED_OXIDIZED_COPPER_RAIL.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.HALT_RAIL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.SPIKED_RAIL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.SLAUGHTER_RAIL.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.COAL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CHARCOAL.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.REFRACTOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.RESISTOR.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.BRAZIER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.SOUL_BRAZIER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.ENDER_BRAZIER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CUPRIC_BRAZIER.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CUPRIC_FIRE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CUPRIC_TORCH.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CUPRIC_WALL_TORCH.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CUPRIC_LANTERN.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CUPRIC_CAMPFIRE.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.AZALEA_LADDER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.AZALEA_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.AZALEA_TRAPDOOR.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.FALSE_HOPE.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.MOSCHATEL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.CAVE_GROWTHS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.LURID_CAVE_GROWTHS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WISPY_CAVE_GROWTHS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.GRAINY_CAVE_GROWTHS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.WEIRD_CAVE_GROWTHS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.ZESTY_CAVE_GROWTHS.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.POTTED_FALSE_HOPE.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.POTTED_MOSCHATEL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.POTTED_CAVE_GROWTHS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.POTTED_LURID_CAVE_GROWTHS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.POTTED_WISPY_CAVE_GROWTHS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.POTTED_GRAINY_CAVE_GROWTHS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.POTTED_WEIRD_CAVE_GROWTHS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.POTTED_ZESTY_CAVE_GROWTHS.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.FLOAT_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.FLOAT_GLASS_PANE.get(), RenderType.translucent());

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.SADDLED_EGG.get(), RenderType.cutout());
	}

	public static void registerItemProperties() {
		ItemProperties.register(Items.CROSSBOW, CavernsAndChasms.location("blunt_arrow"), (stack, level, entity, hash) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, CCItems.BLUNT_ARROW.get()) ? 1.0F : 0.0F);

		for (Item item : List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.TIPPED_ARROW, CCItems.TETHER_POTION.get(), CCItems.IMPACT_POTION.get(), CCItems.TRAIL_POTION.get())) {
			ItemProperties.register(item, CavernsAndChasms.location("subtle"), (stack, level, entity, hash) -> stack.getOrCreateTag().getBoolean("Subtle") ? 1.0F : 0.0F);
		}

		for (Item item : List.of(CCItems.GOLDEN_BUCKET.get(), CCItems.GOLDEN_WATER_BUCKET.get(), CCItems.GOLDEN_LAVA_BUCKET.get(), CCItems.GOLDEN_MILK_BUCKET.get(), CCItems.GOLDEN_POWDER_SNOW_BUCKET.get())) {
			ItemProperties.register(item, CavernsAndChasms.location("level"), (stack, level, entity, hash) -> GoldenBucketItem.getFluidLevel(stack));
		}

		for (Item item : List.of(CCItems.LOST_GOAT_HORN.get(), CCItems.COPPER_HORN.get())) {
			ItemProperties.register(item, new ResourceLocation("tooting"), (stack, level, entity, hash) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
		}

		for (Item item : List.of(Items.BUNDLE, CCItems.FOIL.get())) {
			ItemProperties.register(item, new ResourceLocation("dyed"), (stack, level, entity, hash) -> ((DyeableLeatherItem) stack.getItem()).getColor(stack) > 0 ? 1.0F : 0.0F);
		}

		ItemProperties.register(CCItems.TUNING_FORK.get(), CavernsAndChasms.location("holding"), (stack, level, entity, hash) -> stack.getOrCreateTag().contains("Note") ? 1.0F : 0.0F);
		ItemProperties.register(CCItems.DEPTH_GAUGE.get(), CavernsAndChasms.location("depth"), new ClampedItemPropertyFunction() {
			private double rotation;
			private double rota;
			private long lastUpdateTick;

			public float unclampedCall(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity livingEntity, int p_174668_) {
				Entity entity = livingEntity != null ? livingEntity : stack.getEntityRepresentation();
				if (entity == null) {
					return 0.33333F;
				} else {
					if (level == null && entity.level() instanceof ClientLevel clientLevel) {
						level = clientLevel;
					}

					if (level == null) {
						return 0.33333F;
					} else {
						double depth;
						if (level.dimensionType().natural()) {
							DecimalFormat format = new DecimalFormat("0.00000", DecimalFormatSymbols.getInstance(Locale.ROOT));
							int height = (Mth.clamp((int) entity.getY() - 1, -64, 320) + 64) / 8;
							depth = height / 48.0;
							depth = Float.parseFloat(format.format(depth));
						} else {
							depth = Math.random();
							depth = this.wobble(level, depth);
						}

						return (float) depth;
					}
				}
			}

			private double wobble(Level level, double depth) {
				if (level.getGameTime() != this.lastUpdateTick) {
					this.lastUpdateTick = level.getGameTime();
					double d0 = depth - this.rotation;
					this.rota += d0 * 0.05D;
					this.rota *= 0.8D;
					this.rotation = this.rotation + this.rota;
				}

				return this.rotation;
			}
		});
		ItemProperties.register(CCItems.BEJEWELED_PEARL.get(), CavernsAndChasms.location("charge"), (stack, level, entity, hash) -> {
			if (entity != null && entity.getUseItem() == stack)
				return (float) BejeweledPearlItem.getChargeStage(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / BejeweledPearlItem.getChargeStages();
			else if (stack.getOrCreateTag().contains("Life"))
				return (float) BejeweledPearlItem.getChargeStage(stack.getTag().getInt("Life")) / BejeweledPearlItem.getChargeStages();
			else
				return 0.0F;
		});
		ItemProperties.register(CCItems.BAROMETER.get(), CavernsAndChasms.location("weather"), (ClampedItemPropertyFunction) (stack, level, livingEntity, seed) -> {
			Entity entity = livingEntity != null ? livingEntity : stack.getEntityRepresentation();
			if (entity == null) {
				return 0.4F;
			} else {
				if (level == null && entity.level() instanceof ClientLevel clientLevel) {
					level = clientLevel;
				}

				if (level == null) {
					return 0.4F;
				} else {
					DecimalFormat format = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ROOT));
					Precipitation precipitation = level.getBiome(entity.blockPosition()).value().getPrecipitationAt(entity.blockPosition());
					float max = (!level.dimensionType().hasSkyLight() || level.dimensionType().hasCeiling()) ? 0.2F : precipitation == Precipitation.NONE ? 0.4F : level.isThundering() ? 0.8F : level.isRaining() ? (precipitation == Precipitation.SNOW ? 1.0F : 0.6F) : 0.4F;
					return Float.parseFloat(format.format(max));
				}
			}
		});
	}
}
