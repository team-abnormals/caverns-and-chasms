package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.common.block.entity.CursedCampfireBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCBlockEntityTypes {
	public static final BlockEntitySubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getBlockEntitySubHelper();

	public static final RegistryObject<BlockEntityType<CursedCampfireBlockEntity>> CURSED_CAMPFIRE = HELPER.createBlockEntity("cursed_campfire", CursedCampfireBlockEntity::new, () -> new Block[]{CCBlocks.CURSED_CAMPFIRE.get()});
}
