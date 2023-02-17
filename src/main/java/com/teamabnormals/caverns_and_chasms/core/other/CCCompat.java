package com.teamabnormals.caverns_and_chasms.core.other;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableBiMap;
import com.teamabnormals.blueprint.common.dispenser.FishBucketDispenseItemBehavior;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.item.PrimedTmt;
import com.teamabnormals.caverns_and_chasms.common.entity.projectile.Kunai;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class CCCompat {

	public static void registerCompat() {
		registerFlammables();
		registerDispenserBehaviors();
		changeLocalization();
		setFireproof();
		addWaxables();
	}

	private static void registerFlammables() {
		DataUtil.registerFlammable(CCBlocks.AZALEA_LOG.get(), 5, 5);
		DataUtil.registerFlammable(CCBlocks.AZALEA_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(CCBlocks.STRIPPED_AZALEA_LOG.get(), 5, 5);
		DataUtil.registerFlammable(CCBlocks.STRIPPED_AZALEA_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(CCBlocks.AZALEA_PLANKS.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_STAIRS.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_FENCE.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_FENCE_GATE.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_BOARDS.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_VERTICAL_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_BOOKSHELF.get(), 30, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_BEEHIVE.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_POST.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.STRIPPED_AZALEA_POST.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_HEDGE.get(), 5, 20);
	}

	private static void registerDispenserBehaviors() {
		DispenserBlock.registerBehavior(CCItems.CAVEFISH_BUCKET.get(), new FishBucketDispenseItemBehavior());
		DispenserBlock.registerBehavior(CCItems.KUNAI.get(), new AbstractProjectileDispenseBehavior() {
			protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
				Kunai entity = new Kunai(worldIn, position.x(), position.y(), position.z());
				entity.pickup = AbstractArrow.Pickup.ALLOWED;
				return entity;
			}
		});

		DispenserBlock.registerBehavior(CCBlocks.TMT.get(), new DefaultDispenseItemBehavior() {
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				Level level = source.getLevel();
				BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
				PrimedTmt primedtmt = new PrimedTmt(level, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, null);
				level.addFreshEntity(primedtmt);
				level.playSound(null, primedtmt.getX(), primedtmt.getY(), primedtmt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.gameEvent(null, GameEvent.ENTITY_PLACE, pos);
				stack.shrink(1);
				return stack;
			}
		});
	}

	private static void changeLocalization() {
		DataUtil.changeItemLocalization(Items.NETHERITE_SCRAP, CavernsAndChasms.MOD_ID, "ancient_scrap");
	}

	private static void setFireproof() {
		ObfuscationReflectionHelper.setPrivateValue(Item.class, CCBlocks.NECROMIUM_BLOCK.get().asItem(), true, "f_41372_");
	}

	private static void addWaxables() {
		ImmutableBiMap.Builder<Block, Block> builder = ImmutableBiMap.builder();
		HoneycombItem.WAXABLES.get().forEach(builder::put);
		builder.put(CCBlocks.COPPER_BARS.get(), CCBlocks.WAXED_COPPER_BARS.get());
		builder.put(CCBlocks.EXPOSED_COPPER_BARS.get(), CCBlocks.WAXED_EXPOSED_COPPER_BARS.get());
		builder.put(CCBlocks.WEATHERED_COPPER_BARS.get(), CCBlocks.WAXED_WEATHERED_COPPER_BARS.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_BARS.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BARS.get());
		builder.put(CCBlocks.COPPER_BUTTON.get(), CCBlocks.WAXED_COPPER_BUTTON.get());
		builder.put(CCBlocks.EXPOSED_COPPER_BUTTON.get(), CCBlocks.WAXED_EXPOSED_COPPER_BUTTON.get());
		builder.put(CCBlocks.WEATHERED_COPPER_BUTTON.get(), CCBlocks.WAXED_WEATHERED_COPPER_BUTTON.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_BUTTON.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get());
		HoneycombItem.WAXABLES = Suppliers.memoize(builder::build);
	}
}
