package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CCPaintingVariants {
	public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<PaintingVariant> ISOLATION = PAINTING_VARIANTS.register("isolation", () -> new PaintingVariant(32, 32));
	public static final RegistryObject<PaintingVariant> EXSANGUINATED = PAINTING_VARIANTS.register("exsanguinated", () -> new PaintingVariant(64, 64));
	public static final RegistryObject<PaintingVariant> EMBEDDED = PAINTING_VARIANTS.register("embedded", () -> new PaintingVariant(32, 64));
	public static final RegistryObject<PaintingVariant> STARRY_NIGHT = PAINTING_VARIANTS.register("starry_night", () -> new PaintingVariant(16, 16));
	public static final RegistryObject<PaintingVariant> NOIR = PAINTING_VARIANTS.register("noir", () -> new PaintingVariant(16, 32));
}