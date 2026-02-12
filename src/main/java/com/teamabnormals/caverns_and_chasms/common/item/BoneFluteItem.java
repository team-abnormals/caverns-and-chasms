package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class BoneFluteItem extends Item {

	public BoneFluteItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Command command = this.getCommand(player);
		player.startUsingItem(hand);
		// player.pick()
		level.playSound(player, player, command.sound, SoundSource.RECORDS, 3.0F, 1.0F);
		level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
		for (Rat rat : level.getEntitiesOfClass(Rat.class, player.getBoundingBox().inflate(48D), (entity) -> entity.getOwner() == player && entity.distanceToSqr(player) <= 2304D)) {
			rat.setOrderedToSit(command == Command.SIT);
		}
		// player.getCooldowns().addCooldown(this, 20);
		player.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.TOOT_HORN;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	private Command getCommand(Player player) {
		if (player.isCrouching())
			return Command.SIT;
		return Command.STAND;
	}

	private enum Command {
		SIT(CCSoundEvents.TUNING_FORK_VIBRATE.get()),
		STAND(CCSoundEvents.TUNING_FORK_VIBRATE.get());

		private final SoundEvent sound;

		Command(SoundEvent sound) {
			this.sound = sound;
		}
	}
}