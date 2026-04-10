package com.teamabnormals.caverns_and_chasms.core.registry.helper;

import com.teamabnormals.blueprint.client.MemoizedBEWLR;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.caverns_and_chasms.client.renderer.block.RollerDoorBlockEntityWithoutLevelRenderer;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.MovingDoorType;
import com.teamabnormals.caverns_and_chasms.common.item.MovingDoorBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashMap;
import java.util.function.Supplier;

public class CCItemSubRegistryHelper extends ItemSubRegistryHelper {
	protected final HashMap<DeferredHolder<? extends Item, ?>, IClientItemExtensions> clientItemExtensions = new HashMap<>();

	public CCItemSubRegistryHelper(RegistryHelper parent) {
		super(parent);
	}

	@Override
	public void register(IEventBus eventBus) {
		super.register(eventBus);
		if (FMLEnvironment.dist == Dist.CLIENT) {
			eventBus.addListener((RegisterClientExtensionsEvent event) -> {
				this.clientItemExtensions.forEach((holder, extensions) -> {
					event.registerItem(extensions, holder.get());
				});
				this.clientItemExtensions.clear();
			});
		}
	}

	public DeferredItem<Item> createMovingDoorItem(String name, MovingDoorType doorType, Supplier<? extends Block> block) {
		DeferredItem<Item> item = this.deferredRegister.register(name, () -> new MovingDoorBlockItem(block.get(), MovingDoorType.ROLLER_DOOR, new Item.Properties()));
		if (FMLEnvironment.dist == Dist.CLIENT) {
			this.clientItemExtensions.put(item, movingDoorBEWLR(doorType, block));
		}
		return item;
	}

	@OnlyIn(Dist.CLIENT)
	private static IClientItemExtensions movingDoorBEWLR(MovingDoorType doorType, Supplier<? extends Block> block) {
		return MemoizedBEWLR.asCustomItemRenderer((dispatcher, entityModelSet) -> {
			MovingDoorHeaderBlockEntity blockEntity = new MovingDoorHeaderBlockEntity(BlockPos.ZERO, block.get().defaultBlockState());
			blockEntity.setDoorType(doorType);
			blockEntity.syncVisuals();
			return new RollerDoorBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, blockEntity);
		});
	}
}
