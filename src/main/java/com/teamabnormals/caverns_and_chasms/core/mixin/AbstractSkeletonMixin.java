package com.teamabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonMixin extends Monster {

	protected AbstractSkeletonMixin(EntityType<? extends Monster> type, Level worldIn) {
		super(type, worldIn);
	}

	@Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
	private void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty, CallbackInfo info) {
		int difficultyChance = difficulty.getDifficulty().getId() + 1;

		if (this.level.dimension() != Level.NETHER && random.nextInt(difficultyChance) == 0) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(random.nextInt(difficultyChance) == 0 ? Items.WOODEN_SWORD : Items.WOODEN_AXE));
		}
	}
}
