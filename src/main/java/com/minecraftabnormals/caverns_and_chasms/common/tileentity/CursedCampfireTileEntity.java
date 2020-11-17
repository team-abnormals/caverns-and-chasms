package com.minecraftabnormals.caverns_and_chasms.common.tileentity;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCTileEntities;
import net.minecraft.tileentity.CampfireTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class CursedCampfireTileEntity extends CampfireTileEntity {

	public CursedCampfireTileEntity() {
		super();
	}

	public TileEntityType<?> getType() {
		return CCTileEntities.CURSED_CAMPFIRE.get();
	}
}
