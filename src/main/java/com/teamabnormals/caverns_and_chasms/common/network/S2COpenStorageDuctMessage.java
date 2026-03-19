package com.teamabnormals.caverns_and_chasms.common.network;

import com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory.StorageDuctScreen;
import com.teamabnormals.caverns_and_chasms.common.block.entity.StorageDuctBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.inventory.StorageDuctMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class S2COpenStorageDuctMessage {
	private final int windowId;
	private final int containerSize;
	private final BlockPos blockPos;

	public S2COpenStorageDuctMessage(int windowId, int containerSize, BlockPos blockPos) {
		this.windowId = windowId;
		this.containerSize = containerSize;
		this.blockPos = blockPos;
	}

	public static void serialize(S2COpenStorageDuctMessage message, FriendlyByteBuf buffer) {
		buffer.writeVarInt(message.windowId);
		buffer.writeVarInt(message.containerSize);
		buffer.writeBlockPos(message.blockPos);
	}

	public static S2COpenStorageDuctMessage deserialize(FriendlyByteBuf buffer) {
		return new S2COpenStorageDuctMessage(buffer.readVarInt(), buffer.readVarInt(), buffer.readBlockPos());
	}

	public static void handle(S2COpenStorageDuctMessage message, Supplier<Context> supplier) {
		supplier.get().enqueueWork(() -> {
			Player player = Minecraft.getInstance().player;
			if (player != null) {
				BlockEntity blockEntity = player.level().getBlockEntity(message.getBlockPos());
				if (blockEntity instanceof StorageDuctBlockEntity storageDuct) {
					StorageDuctMenu container = new StorageDuctMenu(message.getWindowId(), player.getInventory(), new SimpleContainer(message.getContainerSize()), null);
					player.containerMenu = container;
					Minecraft.getInstance().setScreen(new StorageDuctScreen(container, player.getInventory(), storageDuct.getDisplayName(), Math.min(message.getContainerSize(), 54)));
				}
			}
		});
		supplier.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	public int getWindowId() {
		return this.windowId;
	}

	@OnlyIn(Dist.CLIENT)
	public int getContainerSize() {
		return this.containerSize;
	}

	@OnlyIn(Dist.CLIENT)
	public BlockPos getBlockPos() {
		return this.blockPos;
	}
}