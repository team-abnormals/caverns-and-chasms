package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class MovingDoorType {
	public static final MovingDoorType ROLLER_DOOR = new MovingDoorType("roller_door", "roller_door", CCBlocks.ROLLER_DOOR, CCBlocks.ROLLER_DOOR_HEADER);
	public static final MovingDoorType ROLLER_WINDOW = new MovingDoorType("roller_window", "roller_door", CCBlocks.ROLLER_WINDOW, CCBlocks.ROLLER_WINDOW_HEADER);

	private final Supplier<Block> normalBlock;
	private final Supplier<Block> headerBlock;
	private final Material material;
	private final Material bottomMaterial;

	public MovingDoorType(String name, String group, Supplier<Block> normalBlock, Supplier<Block> headerBlock) {
		this.normalBlock = normalBlock;
		this.headerBlock = headerBlock;
		this.material = new Material(InventoryMenu.BLOCK_ATLAS, CavernsAndChasms.location("entity/" + group + "/" + name));
		this.bottomMaterial = new Material(InventoryMenu.BLOCK_ATLAS, CavernsAndChasms.location("entity/" + group + "/" + name + "_bottom"));
	}

	public AbstractMovingDoorBlock getNormalBlock() {
		return (AbstractMovingDoorBlock) this.normalBlock.get();
	}

	public AbstractMovingDoorBlock getHeaderBlock() {
		return (AbstractMovingDoorBlock) this.headerBlock.get();
	}

	public Material getNormalMaterial() {
		return this.material;
	}

	public Material getBottomMaterial() {
		return this.bottomMaterial;
	}
}