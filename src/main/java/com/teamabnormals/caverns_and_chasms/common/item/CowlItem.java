package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraftforge.event.entity.living.LivingEvent.LivingVisibilityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CowlItem extends DyeableArmorItem {

	public CowlItem(ArmorMaterial material, ArmorItem.Type slot, Properties properties) {
		super(material, slot, properties);
	}

	@SubscribeEvent
	public static void onLivingVisiblity(LivingVisibilityEvent event) {
		if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof CowlItem) {
			event.modifyVisibility(0.5F);
		}
	}
}