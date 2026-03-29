package com.teamabnormals.caverns_and_chasms.core.registry.helper;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.client.renderer.block.TypedBlockEntityWithoutLevelRenderer;
import com.teamabnormals.blueprint.common.item.BEWLRBlockItem;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.caverns_and_chasms.client.renderer.block.ToolboxBlockEntityWithoutLevelRenderer;
import com.teamabnormals.caverns_and_chasms.common.block.SparklerBlock;
import com.teamabnormals.caverns_and_chasms.common.block.WallSparklerBlock;
import com.teamabnormals.caverns_and_chasms.common.block.entity.ToolboxBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.WinchBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents.CCSoundTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CCBlockSubRegistryHelper extends BlockSubRegistryHelper {

	public CCBlockSubRegistryHelper(RegistryHelper parent) {
		super(parent);
	}

	public <B extends Block> RegistryObject<B> createToolboxBlock(String name, Supplier<? extends B> supplier) {
		RegistryObject<B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name, () -> new BEWLRBlockItem(block.get(), new Item.Properties().stacksTo(1), () -> () -> toolboxBEWLR()));
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

	public Pair<RegistryObject<SparklerBlock>, RegistryObject<WallSparklerBlock>> createSparklerBlock(String name, String wallName, Pair<RegistryObject<SimpleParticleType>, RegistryObject<SimpleParticleType>> particle) {
		RegistryObject<SparklerBlock> block = this.deferredRegister.register(name, () -> new SparklerBlock(BlockBehaviour.Properties.of().noCollission().instabreak().lightLevel(CCProperties.litBlockEmission(12)).sound(CCSoundTypes.SPARKLER).pushReaction(PushReaction.DESTROY), particle));
		RegistryObject<WallSparklerBlock> wallBlock = this.deferredRegister.register(wallName, () -> new WallSparklerBlock(BlockBehaviour.Properties.of().noCollission().instabreak().lightLevel(CCProperties.litBlockEmission(12)).sound(CCSoundTypes.SPARKLER).pushReaction(PushReaction.DESTROY).lootFrom(block), particle));
		this.itemRegister.register(name, () -> new StandingAndWallBlockItem(block.get(), wallBlock.get(), new Item.Properties(), Direction.DOWN));
		return Pair.of(block, wallBlock);
	}

	@OnlyIn(Dist.CLIENT)
	private static BEWLRBlockItem.LazyBEWLR toolboxBEWLR() {
		return new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> new ToolboxBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new ToolboxBlockEntity(BlockPos.ZERO, CCBlocks.TOOLBOX.get().defaultBlockState())));
	}

	@OnlyIn(Dist.CLIENT)
	private static BEWLRBlockItem.LazyBEWLR winchBEWLR() {
		return new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> new TypedBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new WinchBlockEntity(BlockPos.ZERO, CCBlocks.WINCH.get().defaultBlockState())));
	}
}
