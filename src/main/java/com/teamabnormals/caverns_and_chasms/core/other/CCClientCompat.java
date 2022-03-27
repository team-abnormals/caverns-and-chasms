package com.teamabnormals.caverns_and_chasms.core.other;

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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Locale;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, value = Dist.CLIENT)
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

		ItemBlockRenderTypes.setRenderLayer(CCBlocks.AZALEA_LADDER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.AZALEA_DOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.AZALEA_TRAPDOOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.AZALEA_POST.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.STRIPPED_AZALEA_POST.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.AZALEA_LEAF_CARPET.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.FLOWERING_AZALEA_LEAF_CARPET.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.AZALEA_HEDGE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(CCBlocks.FLOWERING_AZALEA_HEDGE.get(), RenderType.cutoutMipped());
	}

	public static void registerItemProperties() {
		ItemProperties.register(Items.CROSSBOW, new ResourceLocation(CavernsAndChasms.MOD_ID, "silver_arrow"), (stack, world, entity, hash) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, CCItems.SILVER_ARROW.get()) ? 1.0F : 0.0F);

		ItemProperties.register(CCItems.GOLDEN_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, world, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemProperties.register(CCItems.GOLDEN_WATER_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, world, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemProperties.register(CCItems.GOLDEN_LAVA_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, world, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemProperties.register(CCItems.GOLDEN_MILK_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, world, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));
		ItemProperties.register(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "level"), (stack, world, entity, hash) -> stack.getOrCreateTag().getInt("FluidLevel"));

		ItemProperties.register(CCItems.TUNING_FORK.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "holding"), (stack, world, entity, hash) -> stack.getOrCreateTag().contains("Note") ? 1.0F : 0.0F);
		ItemProperties.register(CCItems.DEPTH_GAUGE.get(), new ResourceLocation(CavernsAndChasms.MOD_ID, "depth"), new ClampedItemPropertyFunction() {
			private double rotation;
			private double rota;
			private long lastUpdateTick;

			public float unclampedCall(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity livingEntity, int p_174668_) {
				Entity entity = livingEntity != null ? livingEntity : stack.getEntityRepresentation();
				if (entity == null) {
					return 0.33333F;
				} else {
					if (level == null && entity.level instanceof ClientLevel) {
						level = (ClientLevel) entity.level;
					}

					if (level == null) {
						return 0.33333F;
					} else {
						double depth;
						if (level.dimensionType().natural()) {
							DecimalFormat format = new DecimalFormat("0.00000");
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
	}

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		Player player = event.getPlayer();

		if (player != null && player.getInventory().contains(stack)) {
			Level level = player.level;

			if (item == Items.COMPASS && CCConfig.CLIENT.compassesDisplayPosition.get()) {
				event.getToolTip().add(createTooltip("latitude").withStyle(ChatFormatting.GRAY).append(new TextComponent(String.format(Locale.ROOT, ": %.3f", player.getX())).withStyle(ChatFormatting.GRAY)));
				event.getToolTip().add(createTooltip("longitude").withStyle(ChatFormatting.GRAY).append(new TextComponent(String.format(Locale.ROOT, ": %.3f", player.getZ())).withStyle(ChatFormatting.GRAY)));
			}

			if (item == Items.CLOCK) {
				if (CCConfig.CLIENT.clocksDisplayTime.get()) {
					event.getToolTip().add(new TextComponent(calculateTime(level)).withStyle(ChatFormatting.GRAY));
				}

				if (CCConfig.CLIENT.clocksDisplayDay.get()) {
					event.getToolTip().add(createTooltip("day").withStyle(ChatFormatting.GRAY).append(new TextComponent(" " + level.getDayTime() / 24000L).withStyle(ChatFormatting.GRAY)));
				}
			}

			if (item == CCItems.DEPTH_GAUGE.get() && CCConfig.CLIENT.depthGaugesDisplayPosition.get()) {
				event.getToolTip().add(createTooltip("altitude").withStyle(ChatFormatting.GRAY).append(new TextComponent(String.format(Locale.ROOT, ": %.3f", player.getY())).withStyle(ChatFormatting.GRAY)));
			}

			if (item == CCItems.BAROMETER.get() && CCConfig.CLIENT.barometersDisplayWeather.get()) {
				event.getToolTip().add(createTooltip("weather").withStyle(ChatFormatting.GRAY).append(": ").append(createTooltip(getWeather(player, level)).withStyle(ChatFormatting.GRAY)));
			}
		}
	}

	private static String getWeather(Player player, Level level) {
		Precipitation precipitation = level.getBiome(player.blockPosition()).value().getPrecipitation();

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

	private static TranslatableComponent createTooltip(String identifier) {
		return new TranslatableComponent("tooltip." + CavernsAndChasms.MOD_ID + "." + identifier);
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
		if (event.getEntityLiving() instanceof Player player) {
			Item item = event.getItemStack().getItem();
			Level level = player.level;
			boolean displayTime = CCConfig.CLIENT.clocksDisplayTime.get();
			boolean displayDay = CCConfig.CLIENT.clocksDisplayDay.get();
			if (item == Items.COMPASS && CCConfig.CLIENT.compassesDisplayPosition.get()) {
				player.displayClientMessage(createTooltip("latitude").append(new TextComponent(String.format(Locale.ROOT, ": %.3f, ", player.getX())).append(createTooltip("longitude").append(new TextComponent(String.format(Locale.ROOT, ": %.3f", player.getZ()))))), true);
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
			} else if (item == Items.CLOCK && (displayDay || displayTime)) {
				TextComponent time = new TextComponent(calculateTime(level));
				MutableComponent day = createTooltip("day").append(new TextComponent(" " + (level.getDayTime() + 6000) / 24000L));
				TextComponent message = new TextComponent("");
				if (CCConfig.CLIENT.clocksDisplayTime.get()) {
					message.append(time);
					if (displayDay) message.append(new TextComponent(", "));
				}
				if (displayDay) message.append(day);
				player.displayClientMessage(message, true);
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
			} else if (item == CCItems.DEPTH_GAUGE.get() && CCConfig.CLIENT.depthGaugesDisplayPosition.get()) {
				player.displayClientMessage(createTooltip("altitude").append(new TextComponent(String.format(Locale.ROOT, ": %.3f", player.getY()))), true);
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
			} else if (item == CCItems.BAROMETER.get() && CCConfig.CLIENT.barometersDisplayWeather.get()) {
				player.displayClientMessage(createTooltip("weather").append(": ").append(createTooltip(getWeather(player, level))), true);
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
			}
		}
	}
}
