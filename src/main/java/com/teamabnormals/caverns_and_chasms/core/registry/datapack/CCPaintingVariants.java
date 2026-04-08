package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class CCPaintingVariants {
	public static final ResourceKey<PaintingVariant> ISOLATION = create("isolation");
	public static final ResourceKey<PaintingVariant> EXSANGUINATED = create("exsanguinated");
	public static final ResourceKey<PaintingVariant> EMBEDDED = create("embedded");
	public static final ResourceKey<PaintingVariant> STARRY_NIGHT = create("starry_night");
	public static final ResourceKey<PaintingVariant> NOIR = create("noir");
	public static final ResourceKey<PaintingVariant> KNIGHT = create("knight");
	public static final ResourceKey<PaintingVariant> PROTOTYPE_701 = create("prototype_701");
	public static final ResourceKey<PaintingVariant> SQUIRMY = create("squirmy");
	public static final ResourceKey<PaintingVariant> THE_ENIGMA = create("the_enigma");
	public static final ResourceKey<PaintingVariant> CHEF = create("chef");

	public static void bootstrap(BootstrapContext<PaintingVariant> context) {
		register(context, ISOLATION, 2, 2);
		register(context, EXSANGUINATED, 4, 4);
		register(context, EMBEDDED, 2, 4);
		register(context, STARRY_NIGHT, 1, 1);
		register(context, NOIR, 1, 2);
		register(context, KNIGHT, 3, 2);
		register(context, PROTOTYPE_701, 3, 2);
		register(context, SQUIRMY, 3, 1);
		register(context, THE_ENIGMA, 4, 4);
		register(context, CHEF, 3, 4);
	}

	private static ResourceKey<PaintingVariant> create(String name) {
		return ResourceKey.create(Registries.PAINTING_VARIANT, CavernsAndChasms.location(name));
	}

	private static void register(BootstrapContext<PaintingVariant> context, ResourceKey<PaintingVariant> key, int width, int height) {
		context.register(key, new PaintingVariant(width, height, key.location()));
	}
}