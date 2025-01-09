package com.teamabnormals.caverns_and_chasms.common.entity.monster.deeper;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

import java.util.function.IntFunction;

public enum DeeperHat implements StringRepresentable {
	NONE(0, "none", null),
	MOSCHATEL(1, "moschatel", CCBlocks.MOSCHATEL.get().asItem()),
	STANDARD(2, "standard", CCBlocks.CAVE_GROWTHS.get().asItem()),
	LURID(3, "lurid", CCBlocks.LURID_CAVE_GROWTHS.get().asItem()),
	WISPY(4, "wispy", CCBlocks.WISPY_CAVE_GROWTHS.get().asItem()),
	WEIRD(5, "weird", CCBlocks.WEIRD_CAVE_GROWTHS.get().asItem()),
	GRAINY(6, "grainy", CCBlocks.GRAINY_CAVE_GROWTHS.get().asItem()),
	ZESTY(7, "zesty", CCBlocks.ZESTY_CAVE_GROWTHS.get().asItem());

	public static final StringRepresentable.EnumCodec<DeeperHat> CODEC = StringRepresentable.fromEnum(DeeperHat::values);
	private static final IntFunction<DeeperHat> BY_ID = ByIdMap.continuous(DeeperHat::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
	private final int id;
	private final String name;
	private final Item item;
	private final LazyLoadedValue<ResourceLocation> texture = new LazyLoadedValue<>(() -> new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/deeper/hat/" + this.getSerializedName() + ".png"));

	private DeeperHat(int id, String name, Item item) {
		this.id = id;
		this.name = name;
		this.item = item;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}

	public int getId() {
		return this.id;
	}

	public Item getItem() {
		return this.item;
	}

	public ResourceLocation getTexture() {
		return this.texture.get();
	}

	public static DeeperHat byName(String name) {
		return CODEC.byName(name, NONE);
	}

	public static DeeperHat byId(int id) {
		return BY_ID.apply(id);
	}

	public static DeeperHat byItem(Item item) {
		for (DeeperHat hat : DeeperHat.values())
			if (hat.getItem() == item)
				return hat;
		return DeeperHat.NONE;
	}
}