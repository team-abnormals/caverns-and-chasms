package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.integration.quark.ToolboxTooltips;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, value = Dist.CLIENT)
public class CCClientEvents {

	@SubscribeEvent
	public static void makeTooltip(RenderTooltipEvent.GatherComponents event) {
		if (ModList.get().isLoaded("quark")) {
			ToolboxTooltips.makeTooltip(event);
		}
	}
}