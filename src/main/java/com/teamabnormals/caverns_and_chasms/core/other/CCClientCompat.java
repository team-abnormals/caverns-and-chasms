package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.common.item.BejeweledPearlItem;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, value = Dist.CLIENT)
public class CCClientCompat {

	public static void registerClientCompat() {
		registerRenderLayers();
		registerItemProperties();
	}

	public static void registerRenderLayers() {
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
	}

	public static void registerItemProperties() {
		ItemProperties.register(CCItems.GOLDEN_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "faded"), (stack, level, entity, hash) -> stack.getOrCreateTag().getBoolean("FadedTrim") ? 1.0F : 0.0F);


		ItemProperties.register(CCItems.GOLDEN_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, level, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemProperties.register(CCItems.GOLDEN_WATER_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, level, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemProperties.register(CCItems.GOLDEN_LAVA_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, level, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemProperties.register(CCItems.GOLDEN_MILK_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, level, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemProperties.register(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, level, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));

		ItemProperties.register(Items.CROSSBOW, new ResourceLocation(CavernsAndChasms.MOD_ID, "blunt_arrow"), (stack, level, entity, hash) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, CCItems.BLUNT_ARROW.get()) ? 1.0F : 0.0F);

		ItemProperties.register(CCItems.LOST_GOAT_HORN.get(), new ResourceLocation("tooting"), (stack, level, entity, hash) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
		ItemProperties.register(CCItems.COPPER_HORN.get(), new ResourceLocation("tooting"), (stack, level, entity, hash) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(Items.BUNDLE, new ResourceLocation("dyed"), (stack, level, entity, hash) -> {
			return ((DyeableLeatherItem) stack.getItem()).getColor(stack) > 0 ? 1.0F : 0.0F;
		});

		ItemProperties.register(CCItems.TUNING_FORK.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "holding"), (stack, level, entity, hash) -> stack.getOrCreateTag().contains("Note") ? 1.0F : 0.0F);
		ItemProperties.register(CCItems.DEPTH_GAUGE.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "depth"), new ClampedItemPropertyFunction() {
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
		ItemProperties.register(CCItems.BEJEWELED_PEARL.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "charge"), (stack, level, entity, hash) -> {
			if (entity != null && entity.getUseItem() == stack)
				return (float) BejeweledPearlItem.getChargeStage(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / BejeweledPearlItem.getChargeStages();
			else if (stack.getOrCreateTag().contains("Life"))
				return (float) BejeweledPearlItem.getChargeStage(stack.getTag().getInt("Life")) / BejeweledPearlItem.getChargeStages();
			else
				return 0.0F;
		});
		ItemProperties.register(CCItems.BAROMETER.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "weather"), (ClampedItemPropertyFunction) (stack, level, livingEntity, seed) -> {
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

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		Player player = event.getEntity();

		if (player != null && player.getInventory().contains(stack)) {
			Level level = player.level();

			if (item == Items.COMPASS && CCConfig.CLIENT.compassesDisplayPosition.get()) {
				event.getToolTip().add(createTooltip("latitude").withStyle(ChatFormatting.GRAY).append(Component.literal(String.format(Locale.ROOT, ": %.3f", player.getX())).withStyle(ChatFormatting.GRAY)));
				event.getToolTip().add(createTooltip("longitude").withStyle(ChatFormatting.GRAY).append(Component.literal(String.format(Locale.ROOT, ": %.3f", player.getZ())).withStyle(ChatFormatting.GRAY)));
			}

			if (item == Items.CLOCK) {
				if (CCConfig.CLIENT.clocksDisplayTime.get()) {
					event.getToolTip().add(Component.literal(calculateTime(level)).withStyle(ChatFormatting.GRAY));
				}

				if (CCConfig.CLIENT.clocksDisplayDay.get()) {
					event.getToolTip().add(createTooltip("day").withStyle(ChatFormatting.GRAY).append(Component.literal(" " + level.getDayTime() / 24000L).withStyle(ChatFormatting.GRAY)));
				}
			}

			if (item == CCItems.DEPTH_GAUGE.get() && CCConfig.CLIENT.depthGaugesDisplayPosition.get()) {
				event.getToolTip().add(createTooltip("altitude").withStyle(ChatFormatting.GRAY).append(Component.literal(String.format(Locale.ROOT, ": %.3f", player.getY())).withStyle(ChatFormatting.GRAY)));
			}

			if (item == CCItems.BAROMETER.get() && CCConfig.CLIENT.barometersDisplayWeather.get()) {
				event.getToolTip().add(createTooltip("weather").withStyle(ChatFormatting.GRAY).append(": ").append(createTooltip(getWeather(player, level)).withStyle(ChatFormatting.GRAY)));
			}
		}
	}

	private static String getWeather(Player player, Level level) {
		Precipitation precipitation = level.getBiome(player.blockPosition()).value().getPrecipitationAt(player.blockPosition());

		if (precipitation != Precipitation.NONE) {
			if (level.isThundering())
				return "stormy";
			else if (level.isRaining()) {
				if (precipitation == Precipitation.SNOW)
					return "snowy";
				return "rainy";
			}
		}

		return level.dimensionType().hasSkyLight() && !level.dimensionType().hasCeiling() ? "clear" : "null";
	}

	private static MutableComponent createTooltip(String identifier) {
		return Component.translatable("tooltip." + CavernsAndChasms.MOD_ID + "." + identifier);
	}

	private static String calculateTime(Level level) {
		String addition = "";

		int totalMinutes = (int) (level.dayTime() * 3 / 50);
		int hour = 6 + (totalMinutes / 60);
		if (hour >= 24) hour %= 24;
		int minute = totalMinutes % 60;

		if (!CCConfig.CLIENT.clocksUse24hrTime.get()) {
			addition = " " + (hour > 11 ? createTooltip("pm").getString() : createTooltip("am").getString());
			hour = hour > 12 ? hour - 12 : hour == 0 ? 12 : hour;
		}

		String stringMinute = (minute < 10 ? "0" : "") + minute;
		return hour + ":" + stringMinute + addition;
	}

	@SubscribeEvent
	public static void onItemUse(RightClickItem event) {
		Player player = event.getEntity();
		Item item = event.getItemStack().getItem();
		Level level = player.level();
		boolean displayTime = CCConfig.CLIENT.clocksDisplayTime.get();
		boolean displayDay = CCConfig.CLIENT.clocksDisplayDay.get();
		if (item == Items.COMPASS && CCConfig.CLIENT.compassesDisplayPosition.get()) {
			player.displayClientMessage(createTooltip("latitude").append(Component.literal(String.format(Locale.ROOT, ": %.3f, ", player.getX())).append(createTooltip("longitude").append(Component.literal(String.format(Locale.ROOT, ": %.3f", player.getZ()))))), true);
			event.setCanceled(true);
			event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
		} else if (item == Items.CLOCK && (displayDay || displayTime)) {
			MutableComponent time = Component.literal(calculateTime(level));
			MutableComponent day = createTooltip("day").append(Component.literal(" " + (level.getDayTime() + 6000) / 24000L));
			MutableComponent message = Component.literal("");
			if (CCConfig.CLIENT.clocksDisplayTime.get()) {
				message.append(time);
				if (displayDay) message.append(Component.literal(", "));
			}
			if (displayDay) message.append(day);
			player.displayClientMessage(message, true);
			event.setCanceled(true);
			event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
		} else if (item == CCItems.DEPTH_GAUGE.get() && CCConfig.CLIENT.depthGaugesDisplayPosition.get()) {
			player.displayClientMessage(createTooltip("altitude").append(Component.literal(String.format(Locale.ROOT, ": %.3f", player.getY()))), true);
			event.setCanceled(true);
			event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
		} else if (item == CCItems.BAROMETER.get() && CCConfig.CLIENT.barometersDisplayWeather.get()) {
			player.displayClientMessage(createTooltip("weather").append(": ").append(createTooltip(getWeather(player, level))), true);
			event.setCanceled(true);
			event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
		}
	}
}
