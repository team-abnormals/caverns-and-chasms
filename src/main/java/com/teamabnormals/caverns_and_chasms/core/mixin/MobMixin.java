package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

	protected MobMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
	private void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty, CallbackInfo info) {
		if (random.nextFloat() < 0.5F) {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.getType() == EquipmentSlot.Type.ARMOR) {
					ItemStack stack = this.getItemBySlot(slot);
					Item silverItem = replaceSilverWithGold(stack);
					if (silverItem != null) {
						this.setItemSlot(slot, new ItemStack(silverItem));
					}
				}
			}
		}
	}

	private Item replaceSilverWithGold(ItemStack goldItem) {
		if (goldItem.is(Items.GOLDEN_HELMET))
			return CCItems.SILVER_HELMET.get();
		if (goldItem.is(Items.GOLDEN_CHESTPLATE))
			return CCItems.SILVER_CHESTPLATE.get();
		if (goldItem.is(Items.GOLDEN_LEGGINGS))
			return CCItems.SILVER_LEGGINGS.get();
		if (goldItem.is(Items.GOLDEN_BOOTS))
			return CCItems.SILVER_BOOTS.get();

		if (goldItem.is(Items.GOLDEN_SWORD))
			return CCItems.SILVER_SWORD.get();
		if (goldItem.is(Items.GOLDEN_PICKAXE))
			return CCItems.SILVER_PICKAXE.get();
		if (goldItem.is(Items.GOLDEN_HOE))
			return CCItems.SILVER_HOE.get();
		if (goldItem.is(Items.GOLDEN_AXE))
			return CCItems.SILVER_AXE.get();
		if (goldItem.is(Items.GOLDEN_SHOVEL))
			return CCItems.SILVER_SHOVEL.get();

		return null;
	}
}
