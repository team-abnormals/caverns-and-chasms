package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import it.crystalnest.soul_fire_d.api.Fire;
import it.crystalnest.soul_fire_d.api.FireManager;
import net.minecraft.resources.ResourceLocation;

public class CCFires {
  public static final ResourceLocation CUPRIC_FIRE = new ResourceLocation(CavernsAndChasms.MOD_ID, "cupric");
  public static final ResourceLocation ENDER_FIRE = new ResourceLocation("endergetic", "ender");

  public static void register() {
    FireManager.registerFire(
      FireManager.fireBuilder(CUPRIC_FIRE)
        .setDamage(0.5F)
        .setCanRainDouse(true)
        .setComponent(Fire.Component.FLAME_PARTICLE, new ResourceLocation(CavernsAndChasms.MOD_ID, "cupric_fire_flame"))
        .removeFireAspect()
        .removeFlame()
        .build()
    );
  }
}
