package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.abnormals_core.common.dispenser.FishBucketDispenseBehavior;
import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.minecraftabnormals.caverns_and_chasms.common.entity.SilverArrowEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class CCCompat {

	public static void registerCompat() {
		registerDispenserBehaviors();
		changeLocalization();
		ObfuscationReflectionHelper.setPrivateValue(Item.class, CCBlocks.NECROMIUM_BLOCK.get().asItem(), true, "field_234684_d_");
	}

	public static void registerDispenserBehaviors() {
		DispenserBlock.registerBehavior(CCItems.CAVEFISH_BUCKET.get(), new FishBucketDispenseBehavior());

		DispenserBlock.registerBehavior(CCItems.SILVER_ARROW.get(), new ProjectileDispenseBehavior() {
			protected ProjectileEntity getProjectile(World worldIn, IPosition position, ItemStack stackIn) {
				SilverArrowEntity entity = new SilverArrowEntity(worldIn, position.x(), position.y(), position.z());
				entity.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
				return entity;
			}
		});
	}

	public static void changeLocalization() {
		DataUtil.changeItemLocalization(Items.NETHERITE_SCRAP, CavernsAndChasms.MOD_ID, "ancient_scrap");
	}

	public static void registerRenderLayers() {
		RenderTypeLookup.setRenderLayer(CCBlocks.GOLDEN_BARS.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.SILVER_BARS.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.SPIKED_RAIL.get(), RenderType.cutout());

		RenderTypeLookup.setRenderLayer(CCBlocks.BRAZIER.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.SOUL_BRAZIER.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.ENDER_BRAZIER.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.CURSED_BRAZIER.get(), RenderType.cutout());

		RenderTypeLookup.setRenderLayer(CCBlocks.CURSED_FIRE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.CURSED_TORCH.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.CURSED_WALL_TORCH.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.CURSED_LANTERN.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CCBlocks.CURSED_CAMPFIRE.get(), RenderType.cutout());
	}
}
