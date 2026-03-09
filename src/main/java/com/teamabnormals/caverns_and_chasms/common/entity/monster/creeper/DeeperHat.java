package com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.IntFunction;

public enum DeeperHat implements StringRepresentable {
	NONE(0, "none", null),
	MOSCHATEL(1, "moschatel", CCBlocks.MOSCHATEL.get()),
	STANDARD(2, "standard", CCBlocks.CAVE_GROWTHS.get()),
	LURID(3, "lurid", CCBlocks.LURID_CAVE_GROWTHS.get()),
	WISPY(4, "wispy", CCBlocks.WISPY_CAVE_GROWTHS.get()),
	WEIRD(5, "weird", CCBlocks.WEIRD_CAVE_GROWTHS.get()),
	GRAINY(6, "grainy", CCBlocks.GRAINY_CAVE_GROWTHS.get()),
	ZESTY(7, "zesty", CCBlocks.ZESTY_CAVE_GROWTHS.get());

	public static final StringRepresentable.EnumCodec<DeeperHat> CODEC = StringRepresentable.fromEnum(DeeperHat::values);
	private static final IntFunction<DeeperHat> BY_ID = ByIdMap.continuous(DeeperHat::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
	private final int id;
	private final String name;
	private final Block block;
	private final LazyLoadedValue<ResourceLocation> deeperTexture = new LazyLoadedValue<>(() -> CavernsAndChasms.location("textures/entity/deeper/hat/" + this.getSerializedName() + ".png"));
	private final LazyLoadedValue<ResourceLocation> evendeeperTexture = new LazyLoadedValue<>(() -> CavernsAndChasms.location("textures/entity/evendeeper/hat/" + this.getSerializedName() + ".png"));

	DeeperHat(int id, String name, Block block) {
		this.id = id;
		this.name = name;
		this.block = block;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}

	public int getId() {
		return this.id;
	}

	public Block getBlock() {
		return this.block;
	}

	public Item getItem() {
		return this.block == null ? null : this.block.asItem();
	}

	public ResourceLocation getDeeperTexture() {
		return this.deeperTexture.get();
	}

	public ResourceLocation getEvendeeperTexture() {
		return this.evendeeperTexture.get();
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