package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.integration.quark.ToolboxTooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem;

import java.util.List;
import java.util.Locale;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, value = Dist.CLIENT)
public class CCClientEvents {

	@SubscribeEvent
	public static void makeTooltip(RenderTooltipEvent.GatherComponents event) {
		if (ModList.get().isLoaded("quark")) {
			ToolboxTooltips.makeTooltip(event);
		}
	}

	@SubscribeEvent
	public static void renderNameplate(RenderNameTagEvent event) {
		if (event.getEntity() instanceof LivingEntity entity) {
			if (entity.getItemBySlot(EquipmentSlot.HEAD).is(CCItems.COWL.get())) {
				event.setCanRender(TriState.FALSE);
			}
		}
	}

	@SubscribeEvent
	public static void livingRender(RenderLivingEvent.Pre<?, ?> event) {
		if (event.getEntity() instanceof IDataManager data && data.getValue(CCDataProcessors.OBSCURITY_INVISIBILITY)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		Player player = event.getEntity();
		List<Component> tooltip = event.getToolTip();

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

		if (stack.has(DataComponents.TRIM)) {
			ArmorTrim trim = stack.get(DataComponents.TRIM);
			Style style = trim.material().value().description().getStyle();

			int insertIndex = -1;
			for (int i = 0; i < tooltip.size(); i++) {
				Component line = tooltip.get(i);
				if (line.getString().equals(" " + trim.material().value().description().getString())) {
					insertIndex = i + 1;
					break;
				}
			}

			if (insertIndex != -1) {
				if (stack.has(CCDataComponents.FADED_TRIM)) {
					event.getToolTip().add(insertIndex++, CommonComponents.space().append(Component.translatable("tooltip." + CavernsAndChasms.MOD_ID + ".faded_modifier").withStyle(style)));
				}

				if (stack.has(CCDataComponents.EMISSIVE_TRIM)) {
					event.getToolTip().add(insertIndex++, CommonComponents.space().append(Component.translatable("tooltip." + CavernsAndChasms.MOD_ID + ".emissive_modifier").withStyle(style)));
				}

				if (stack.has(CCDataComponents.PULSE_TRIM)) {
					event.getToolTip().add(insertIndex++, CommonComponents.space().append(Component.translatable("tooltip." + CavernsAndChasms.MOD_ID + ".pulse_modifier").withStyle(style)));
				}
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