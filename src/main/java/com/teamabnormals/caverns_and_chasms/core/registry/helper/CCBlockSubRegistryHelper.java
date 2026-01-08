package com.teamabnormals.caverns_and_chasms.core.registry.helper;

import com.teamabnormals.blueprint.client.renderer.block.TypedBlockEntityWithoutLevelRenderer;
import com.teamabnormals.blueprint.common.item.BEWLRBlockItem;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.caverns_and_chasms.client.renderer.block.RollerDoorBlockEntityWithoutLevelRenderer;
import com.teamabnormals.caverns_and_chasms.client.renderer.block.ToolboxBlockEntityWithoutLevelRenderer;
import com.teamabnormals.caverns_and_chasms.common.block.entity.RollerDoorBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.ToolboxBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.WinchBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.item.RollerDoorBlockItem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
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

	public <B extends Block> RegistryObject<B> createRollerDoorBlock(String name, Supplier<? extends B> supplier) {
		RegistryObject<B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name, () -> new RollerDoorBlockItem(block.get(), new Item.Properties().stacksTo(64), () -> () -> rollerDoorBEWLR()));
		return block;
	}

	public <B extends Block> RegistryObject<B> createWinchBlock(String name, Supplier<? extends B> supplier) {
		RegistryObject<B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name, () -> new BEWLRBlockItem(block.get(), new Item.Properties().stacksTo(64), () -> () -> winchBEWLR()));
		return block;
	}

	public <B extends Block> RegistryObject<B> createPlacedItem(String name, Supplier<? extends B> supplier) {
		RegistryObject<B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name + "_placed", () -> new BlockItem(block.get(), new Item.Properties()));
		return block;
	}

	@OnlyIn(Dist.CLIENT)
	private static BEWLRBlockItem.LazyBEWLR toolboxBEWLR() {
		return new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> new ToolboxBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new ToolboxBlockEntity(BlockPos.ZERO, CCBlocks.TOOLBOX.get().defaultBlockState())));
	}

	@OnlyIn(Dist.CLIENT)
	private static BEWLRBlockItem.LazyBEWLR rollerDoorBEWLR() {
		return new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> new RollerDoorBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new RollerDoorBlockEntity(BlockPos.ZERO, CCBlocks.ROLLER_DOOR_HEADER.get().defaultBlockState())));
	}

	@OnlyIn(Dist.CLIENT)
	private static BEWLRBlockItem.LazyBEWLR winchBEWLR() {
		return new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> new TypedBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new WinchBlockEntity(BlockPos.ZERO, CCBlocks.WINCH.get().defaultBlockState())));
	}
}
