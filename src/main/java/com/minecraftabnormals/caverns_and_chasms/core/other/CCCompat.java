package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.abnormals_core.common.dispenser.FishBucketDispenseBehavior;
import com.minecraftabnormals.caverns_and_chasms.common.entity.RottenEggEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.SilverArrowEntity;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.world.World;

public class CCCompat {

	public static void registerDispenserBehaviors() {
		DispenserBlock.registerDispenseBehavior(CCItems.CAVEFISH_BUCKET.get(), new FishBucketDispenseBehavior());

		DispenserBlock.registerDispenseBehavior(CCItems.SILVER_ARROW.get(), new ProjectileDispenseBehavior() {
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				SilverArrowEntity entity = new SilverArrowEntity(worldIn, position.getX(), position.getY(), position.getZ());
				entity.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
				return entity;
			}
		});

		DispenserBlock.registerDispenseBehavior(CCItems.ROTTEN_EGG.get(), new ProjectileDispenseBehavior() {
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				return Util.make(new RottenEggEntity(worldIn, position.getX(), position.getY(), position.getZ()), (entity) -> {
					entity.setItem(stackIn);
				});
			}
		});
	}

	public static void registerRenderLayers() {
		RenderTypeLookup.setRenderLayer(CCBlocks.GOLDEN_BARS.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.SILVER_BARS.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.SPIKED_RAIL.get(), RenderType.getCutout());

		RenderTypeLookup.setRenderLayer(CCBlocks.BRAZIER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.SOUL_BRAZIER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.ENDER_BRAZIER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.CURSED_BRAZIER.get(), RenderType.getCutout());

		RenderTypeLookup.setRenderLayer(CCBlocks.CURSED_FIRE.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.CURSED_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.CURSED_WALL_TORCH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.CURSED_LANTERN.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.CURSED_CAMPFIRE.get(), RenderType.getCutout());
	}
}
