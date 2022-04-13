package com.teamabnormals.caverns_and_chasms.common.item;

import java.util.List;

import javax.annotation.Nullable;

import com.teamabnormals.caverns_and_chasms.core.other.CCPotionUtil;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TetherPotionItem extends PotionItem implements Wearable {

	public TetherPotionItem(Properties properties) {
		super(properties);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public void onArmorTick(ItemStack stack, Level world, Player player) {
		if (!world.isClientSide()) {
			for(MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(stack)) {
				if (!mobeffectinstance.getEffect().isInstantenous()) {
					MobEffectInstance mobeffectinstance1 = new MobEffectInstance(mobeffectinstance.getEffect(), 36000, mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible(), mobeffectinstance.showIcon());
					player.addEffect(mobeffectinstance1);
				}
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(itemstack);
		ItemStack itemstack1 = player.getItemBySlot(equipmentslot);
		if (itemstack1.isEmpty()) {
			player.setItemSlot(equipmentslot, itemstack.copy());
			if (!level.isClientSide()) {
				player.awardStat(Stats.ITEM_USED.get(this));
			}

			itemstack.setCount(0);
			return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
		} else {
			return InteractionResultHolder.fail(itemstack);
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return super.isFoil(stack) && !CCPotionUtil.isElegantPotion(stack);
	}

	@Nullable
	@Override
	public SoundEvent getEquipSound() {
		return CCSoundEvents.ARMOR_EQUIP_TETHER_POTION.get();
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		if (CCPotionUtil.isElegantPotion(stack)) {
			return this.getDescriptionId() + ".effect.elegant";
		} else {
			return super.getDescriptionId(stack);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		CCPotionUtil.addTetherPotionTooltip(stack, tooltip);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			for(Potion potion : Registry.POTION) {
				ItemStack itemstack = PotionUtils.setPotion(new ItemStack(this), potion);
				if (potion != Potions.EMPTY && !CCPotionUtil.isElegantPotion(itemstack)) {
					items.add(itemstack);
					if (potion == Potions.AWKWARD) {
						items.add(PotionUtils.setPotion(new ItemStack(this), Potions.HEALING));
					}
				}
			}
		}
	}
}