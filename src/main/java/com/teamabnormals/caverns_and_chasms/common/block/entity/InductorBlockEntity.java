package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InductorBlockEntity extends BlockEntity {
    public InductorBlockEntity(BlockPos pos, BlockState state) {
        super(CCBlockEntityTypes.INDUCTOR.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, InductorBlockEntity blockEntity) {
    }
}