package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.abnormals_core.core.util.registry.TileEntitySubRegistryHelper;
import com.minecraftabnormals.caverns_and_chasms.common.tileentity.CursedCampfireTileEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.CampfireTileEntityRenderer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCTileEntities {
	public static final TileEntitySubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getTileEntitySubHelper();

	public static final RegistryObject<TileEntityType<CursedCampfireTileEntity>> CURSED_CAMPFIRE = HELPER.createTileEntity("cursed_campfire", CursedCampfireTileEntity::new, () -> new Block[]{CCBlocks.CURSED_CAMPFIRE.get()});

	public static void registerRenderers() {
		ClientRegistry.bindTileEntityRenderer(CURSED_CAMPFIRE.get(), CampfireTileEntityRenderer::new);
	}
}
