package com.teamabnormals.caverns_and_chasms.core.registry.helper;

import com.teamabnormals.blueprint.common.item.BEWLRBlockItem;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.caverns_and_chasms.client.renderer.block.ToolboxBlockEntityWithoutLevelRenderer;
import com.teamabnormals.caverns_and_chasms.common.block.entity.ToolboxBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CCBlockSubRegistryHelper extends BlockSubRegistryHelper {

	public CCBlockSubRegistryHelper(RegistryHelper parent) {
		super(parent, parent.getItemSubHelper().getDeferredRegister(), parent.getBlockSubHelper().getDeferredRegister());
	}

	public <B extends Block> RegistryObject<B> createToolboxBlock(String name, Supplier<? extends B> supplier) {
		RegistryObject<B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name, () -> new BEWLRBlockItem(block.get(), new Item.Properties().stacksTo(1), () -> () -> toolboxBEWLR()));
		return block;
	}

	@OnlyIn(Dist.CLIENT)
	private static BEWLRBlockItem.LazyBEWLR toolboxBEWLR() {
		return new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> new ToolboxBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new ToolboxBlockEntity(BlockPos.ZERO, CCBlocks.TOOLBOX.get().defaultBlockState())));
	}
}
