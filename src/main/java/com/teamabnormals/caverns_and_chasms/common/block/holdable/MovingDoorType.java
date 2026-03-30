package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MovingDoorType {
	private static final Map<String, MovingDoorType> TYPES = new HashMap<>();

	public static final MovingDoorType ROLLER_DOOR = new MovingDoorType(CavernsAndChasms.MOD_ID, "roller_door", "roller_door", CCItems.ROLLER_DOOR);
	public static final MovingDoorType ROLLER_WINDOW = new MovingDoorType(CavernsAndChasms.MOD_ID, "roller_window", "roller_door", CCItems.ROLLER_WINDOW);

	private final String registryName;
	private Material material;
	private Material bottomMaterial;
	private final Supplier<Item> item;

	public MovingDoorType(String modId, String name, String group, Supplier<Item> item) {
		this.registryName = modId + ":" + name;
		if (FMLEnvironment.dist == Dist.CLIENT) {
			this.material = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(modId, "entity/" + group + "/" + name));
			this.bottomMaterial = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(modId, "entity/" + group + "/" + name + "_bottom"));
		}
		this.item = item;
		TYPES.put(registryName, this);
	}

	public String getRegistryName() {
		return this.registryName;
	}

	@OnlyIn(Dist.CLIENT)
	public Material getNormalMaterial() {
		return this.material;
	}

	@OnlyIn(Dist.CLIENT)
	public Material getBottomMaterial() {
		return this.bottomMaterial;
	}

	public Item getItem() {
		return this.item.get();
	}

	public static MovingDoorType byName(String name) {
		return TYPES.get(name);
	}
}