package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.common.dispenser.FishBucketDispenseItemBehavior;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.SilverArrow;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class CCCompat {

	public static void registerCompat() {
		registerDispenserBehaviors();
		changeLocalization();
		ObfuscationReflectionHelper.setPrivateValue(Item.class, CCBlocks.NECROMIUM_BLOCK.get().asItem(), true, "f_41372_");
	}

	public static void registerDispenserBehaviors() {
		DispenserBlock.registerBehavior(CCItems.CAVEFISH_BUCKET.get(), new FishBucketDispenseItemBehavior());

		DispenserBlock.registerBehavior(CCItems.SILVER_ARROW.get(), new AbstractProjectileDispenseBehavior() {
			protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
				SilverArrow entity = new SilverArrow(worldIn, position.x(), position.y(), position.z());
				entity.pickup = AbstractArrow.Pickup.ALLOWED;
				return entity;
			}
		});
	}

	public static void changeLocalization() {
		DataUtil.changeItemLocalization(Items.NETHERITE_SCRAP, CavernsAndChasms.MOD_ID, "ancient_scrap");
	}
}
