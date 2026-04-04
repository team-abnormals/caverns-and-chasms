package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import com.teamabnormals.caverns_and_chasms.common.block.entity.*;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCBlockEntityTypes {
	public static final BlockEntitySubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getBlockEntitySubHelper();

	public static final RegistryObject<BlockEntityType<CupricCampfireBlockEntity>> CUPRIC_CAMPFIRE = HELPER.createBlockEntity("cupric_campfire", CupricCampfireBlockEntity::new, () -> Set.of(CCBlocks.CUPRIC_CAMPFIRE.get()));
	public static final RegistryObject<BlockEntityType<CCSkullBlockEntity>> SKULL = HELPER.createBlockEntity("skull", CCSkullBlockEntity::new, () -> Set.of(CCBlocks.PEEPER_HEAD.get(), CCBlocks.PEEPER_WALL_HEAD.get(), CCBlocks.MIME_HEAD.get(), CCBlocks.MIME_WALL_HEAD.get()));
	public static final RegistryObject<BlockEntityType<DeeperSkullBlockEntity>> DEEPER_HEAD = HELPER.createBlockEntity("deeper_head", DeeperSkullBlockEntity::new, () -> Set.of(CCBlocks.DEEPER_HEAD.get(), CCBlocks.DEEPER_WALL_HEAD.get(), CCBlocks.EVENDEEPER_HEAD.get(), CCBlocks.EVENDEEPER_WALL_HEAD.get()));
	public static final RegistryObject<BlockEntityType<ToolboxBlockEntity>> TOOLBOX = HELPER.createBlockEntity("toolbox", ToolboxBlockEntity::new, () -> Set.of(BlockEntitySubRegistryHelper.collectBlocks(ToolboxBlock.class)));
	public static final RegistryObject<BlockEntityType<AtoningTableBlockEntity>> ATONING_TABLE = HELPER.createBlockEntity("atoning_table", AtoningTableBlockEntity::new, () -> Set.of(CCBlocks.ATONING_TABLE.get()));
	public static final RegistryObject<BlockEntityType<HoldPlateBlockEntity>> HOLD_PLATE = HELPER.createBlockEntity("hold_plate", HoldPlateBlockEntity::new, () -> Set.of(CCBlocks.HOLD_PLATE.get()));
	public static final RegistryObject<BlockEntityType<HoldButtonBlockEntity>> HOLD_BUTTON = HELPER.createBlockEntity("hold_button", HoldButtonBlockEntity::new, () -> Set.of(CCBlocks.HOLD_BUTTON.get()));
	public static final RegistryObject<BlockEntityType<WinchBlockEntity>> WINCH = HELPER.createBlockEntity("winch", WinchBlockEntity::new, () -> Set.of(CCBlocks.WINCH.get()));
	public static final RegistryObject<BlockEntityType<DimmerBlockEntity>> DIMMER = HELPER.createBlockEntity("dimmer", DimmerBlockEntity::new, () -> Set.of(CCBlocks.DIMMER.get(), CCBlocks.WALL_DIMMER.get()));
	public static final RegistryObject<BlockEntityType<HoopBlockEntity>> HOOP = HELPER.createBlockEntity("hoop", HoopBlockEntity::new, () -> Set.of(CCBlocks.HOOP.get()));
	public static final RegistryObject<BlockEntityType<SplurterBlockEntity>> SPLURTER = HELPER.createBlockEntity("splurter", SplurterBlockEntity::new, () -> Set.of(CCBlocks.SPLURTER.get()));
	public static final RegistryObject<BlockEntityType<ScattererBlockEntity>> SCATTERER = HELPER.createBlockEntity("scatterer", ScattererBlockEntity::new, () -> Set.of(CCBlocks.SCATTERER.get()));
	public static final RegistryObject<BlockEntityType<StorageDuctBlockEntity>> STORAGE_DUCT = HELPER.createBlockEntity("storage_duct", StorageDuctBlockEntity::new, () -> Set.of(CCBlocks.STORAGE_DUCT.get()));
	public static final RegistryObject<BlockEntityType<StorageDuctHatchBlockEntity>> STORAGE_DUCT_HATCH = HELPER.createBlockEntity("storage_duct_hatch", StorageDuctHatchBlockEntity::new, () -> Set.of(CCBlocks.STORAGE_DUCT_HATCH.get()));
	public static final RegistryObject<BlockEntityType<MovingDoorBlockEntity>> MOVING_DOOR = HELPER.createBlockEntity("roller_door", (BlockEntityType.BlockEntitySupplier<? extends MovingDoorBlockEntity>) MovingDoorBlockEntity::new, () -> Set.of(CCBlocks.ROLLER_DOOR.get()));
	public static final RegistryObject<BlockEntityType<MovingDoorHeaderBlockEntity>> MOVING_DOOR_HEADER = HELPER.createBlockEntity("roller_door_header", MovingDoorHeaderBlockEntity::new, () -> Set.of(CCBlocks.ROLLER_DOOR_HEADER.get()));
	public static final RegistryObject<BlockEntityType<ResistorBlockEntity>> RESISTOR = HELPER.createBlockEntity("resistor", ResistorBlockEntity::new, () -> Set.of(CCBlocks.RESISTOR.get()));
}