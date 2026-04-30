package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.HoldButtonBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.HoldPlateBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.*;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;

public class CCBlockEntityTypes {
	public static final BlockEntitySubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getBlockEntitySubHelper();

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CupricCampfireBlockEntity>> CUPRIC_CAMPFIRE = HELPER.createBlockEntity("cupric_campfire", CupricCampfireBlockEntity::new, () -> Set.of(CCBlocks.CUPRIC_CAMPFIRE.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CCSkullBlockEntity>> SKULL = HELPER.createBlockEntity("skull", CCSkullBlockEntity::new, () -> Set.of(CCBlocks.PEEPER_HEAD.get(), CCBlocks.PEEPER_WALL_HEAD.get(), CCBlocks.MIME_HEAD.get(), CCBlocks.MIME_WALL_HEAD.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DeeperSkullBlockEntity>> DEEPER_HEAD = HELPER.createBlockEntity("deeper_head", DeeperSkullBlockEntity::new, () -> Set.of(CCBlocks.DEEPER_HEAD.get(), CCBlocks.DEEPER_WALL_HEAD.get(), CCBlocks.EVENDEEPER_HEAD.get(), CCBlocks.EVENDEEPER_WALL_HEAD.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ToolboxBlockEntity>> TOOLBOX = HELPER.createBlockEntity("toolbox", ToolboxBlockEntity::new, () -> Set.of(BlockEntitySubRegistryHelper.collectBlocks(ToolboxBlock.class)));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AtoningTableBlockEntity>> ATONING_TABLE = HELPER.createBlockEntity("atoning_table", AtoningTableBlockEntity::new, () -> Set.of(CCBlocks.ATONING_TABLE.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LiftPlateBlockEntity>> LIFT_PLATE = HELPER.createBlockEntity("lift_plate", LiftPlateBlockEntity::new, () -> Set.of(CCBlocks.COPPER_PRESSURE_PLATE.get(), CCBlocks.EXPOSED_COPPER_PRESSURE_PLATE.get(), CCBlocks.WEATHERED_COPPER_PRESSURE_PLATE.get(), CCBlocks.OXIDIZED_COPPER_PRESSURE_PLATE.get(), CCBlocks.WAXED_COPPER_PRESSURE_PLATE.get(), CCBlocks.WAXED_EXPOSED_COPPER_PRESSURE_PLATE.get(), CCBlocks.WAXED_WEATHERED_COPPER_PRESSURE_PLATE.get(), CCBlocks.WAXED_OXIDIZED_COPPER_PRESSURE_PLATE.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LiftButtonBlockEntity>> LIFT_BUTTON = HELPER.createBlockEntity("lift_button", LiftButtonBlockEntity::new, () -> Set.of(CCBlocks.COPPER_BUTTON.get(), CCBlocks.EXPOSED_COPPER_BUTTON.get(), CCBlocks.WEATHERED_COPPER_BUTTON.get(), CCBlocks.OXIDIZED_COPPER_BUTTON.get(), CCBlocks.WAXED_COPPER_BUTTON.get(), CCBlocks.WAXED_EXPOSED_COPPER_BUTTON.get(), CCBlocks.WAXED_WEATHERED_COPPER_BUTTON.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HoldPlateBlockEntity>> HOLD_PRESSURE_PLATE = HELPER.createBlockEntity("hold_pressure_plate", HoldPlateBlockEntity::new, () -> Set.of(CCBlocks.HOLD_PRESSURE_PLATE.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HoldButtonBlockEntity>> HOLD_BUTTON = HELPER.createBlockEntity("hold_button", HoldButtonBlockEntity::new, () -> Set.of(CCBlocks.HOLD_BUTTON.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WinchBlockEntity>> WINCH = HELPER.createBlockEntity("winch", WinchBlockEntity::new, () -> Set.of(CCBlocks.WINCH.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DimmerBlockEntity>> DIMMER = HELPER.createBlockEntity("dimmer", DimmerBlockEntity::new, () -> Set.of(CCBlocks.DIMMER.get(), CCBlocks.WALL_DIMMER.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HoopBlockEntity>> HOOP = HELPER.createBlockEntity("hoop", HoopBlockEntity::new, () -> Set.of(CCBlocks.HOOP.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SplurterBlockEntity>> SPLURTER = HELPER.createBlockEntity("splurter", SplurterBlockEntity::new, () -> Set.of(CCBlocks.SPLURTER.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ScattererBlockEntity>> SCATTERER = HELPER.createBlockEntity("scatterer", ScattererBlockEntity::new, () -> Set.of(CCBlocks.SCATTERER.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StorageDuctBlockEntity>> STORAGE_DUCT = HELPER.createBlockEntity("storage_duct", StorageDuctBlockEntity::new, () -> Set.of(CCBlocks.STORAGE_DUCT.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StorageDuctHatchBlockEntity>> STORAGE_DUCT_HATCH = HELPER.createBlockEntity("storage_duct_hatch", StorageDuctHatchBlockEntity::new, () -> Set.of(CCBlocks.STORAGE_DUCT_HATCH.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MovingDoorBlockEntity>> MOVING_DOOR = HELPER.createBlockEntity("roller_door", (BlockEntityType.BlockEntitySupplier<? extends MovingDoorBlockEntity>) MovingDoorBlockEntity::new, () -> Set.of(CCBlocks.ROLLER_DOOR.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MovingDoorHeaderBlockEntity>> MOVING_DOOR_HEADER = HELPER.createBlockEntity("roller_door_header", MovingDoorHeaderBlockEntity::new, () -> Set.of(CCBlocks.ROLLER_DOOR_HEADER.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ResistorBlockEntity>> RESISTOR = HELPER.createBlockEntity("resistor", ResistorBlockEntity::new, () -> Set.of(CCBlocks.RESISTOR.get()));
}