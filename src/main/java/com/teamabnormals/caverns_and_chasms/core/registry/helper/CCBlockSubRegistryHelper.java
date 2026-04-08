package com.teamabnormals.caverns_and_chasms.core.registry.helper;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.client.MemoizedBEWLR;
import com.teamabnormals.blueprint.client.renderer.block.TypedBlockEntityWithoutLevelRenderer;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.caverns_and_chasms.client.renderer.block.ToolboxBlockEntityWithoutLevelRenderer;
import com.teamabnormals.caverns_and_chasms.common.block.SparklerBlock;
import com.teamabnormals.caverns_and_chasms.common.block.WallSparklerBlock;
import com.teamabnormals.caverns_and_chasms.common.block.entity.ToolboxBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.WinchBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents.CCSoundTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class CCBlockSubRegistryHelper extends BlockSubRegistryHelper {

	public CCBlockSubRegistryHelper(RegistryHelper parent) {
		super(parent);
	}

	public <B extends Block> DeferredBlock<B> createToolboxBlock(String name, Supplier<? extends B> supplier) {
		DeferredBlock<B> block = this.deferredRegister.register(name, supplier);
		DeferredHolder<Item, BlockItem> item = this.itemRegister.register(name, () -> new BlockItem(block.get(), new Item.Properties().stacksTo(1)));
		if (FMLEnvironment.dist == Dist.CLIENT) {
			this.clientItemExtensions.put(item, toolboxBEWLR(block));
		}
		return block;
	}

	public <B extends Block> DeferredBlock<B> createWinchBlock(String name, Supplier<? extends B> supplier) {
		DeferredBlock<B> block = this.deferredRegister.register(name, supplier);
		DeferredHolder<Item, BlockItem> item = this.itemRegister.register(name, () -> new BlockItem(block.get(), new Item.Properties().stacksTo(64)));
		if (FMLEnvironment.dist == Dist.CLIENT) {
			this.clientItemExtensions.put(item, winchBEWLR(block));
		}
		return block;
	}

	public <B extends Block> DeferredBlock<B> createPlacedItem(String name, Supplier<? extends B> supplier) {
		DeferredBlock<B> block = this.deferredRegister.register(name, supplier);
		this.itemRegister.register(name + "_placed", () -> new BlockItem(block.get(), new Item.Properties()));
		return block;
	}

	public Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> createSparklerBlock(String name, String wallName, Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> particle) {
		DeferredBlock<SparklerBlock> block = this.deferredRegister.register(name, () -> new SparklerBlock(BlockBehaviour.Properties.of().noCollission().instabreak().lightLevel(CCProperties.litBlockEmission(12)).sound(CCSoundTypes.SPARKLER).pushReaction(PushReaction.DESTROY), particle));
		DeferredBlock<WallSparklerBlock> wallBlock = this.deferredRegister.register(wallName, () -> new WallSparklerBlock(BlockBehaviour.Properties.of().noCollission().instabreak().lightLevel(CCProperties.litBlockEmission(12)).sound(CCSoundTypes.SPARKLER).pushReaction(PushReaction.DESTROY).lootFrom(block), particle));
		this.itemRegister.register(name, () -> new StandingAndWallBlockItem(block.get(), wallBlock.get(), new Item.Properties(), Direction.DOWN));
		return Pair.of(block, wallBlock);
	}

	@OnlyIn(Dist.CLIENT)
	private static IClientItemExtensions toolboxBEWLR(Supplier<? extends Block> block) {
		return MemoizedBEWLR.asCustomItemRenderer((dispatcher, entityModelSet) -> new ToolboxBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new ToolboxBlockEntity(BlockPos.ZERO, block.get().defaultBlockState())));
	}

	@OnlyIn(Dist.CLIENT)
	private static IClientItemExtensions winchBEWLR(Supplier<? extends Block> block) {
		return MemoizedBEWLR.asCustomItemRenderer((dispatcher, entityModelSet) -> new TypedBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new WinchBlockEntity(BlockPos.ZERO, block.get().defaultBlockState())));
	}
}
