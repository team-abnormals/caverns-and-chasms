package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class TuningForkItem extends Item {

	public TuningForkItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
		return !player.isCreative();
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() instanceof NoteBlock && player != null && player.isCrouching()) {
			ItemStack stack = context.getItemInHand();
			CompoundTag tag = stack.getOrCreateTag();
			int note = state.getValue(NoteBlock.NOTE);
			if (!tag.contains("Note") || tag.getInt("Note") != note) {
				tag.putInt("Note", state.getValue(NoteBlock.NOTE));
				player.displayClientMessage(new TranslatableComponent(this.getDescriptionId() + ".capture_note", new TranslatableComponent(this.getDescriptionId() + ".note." + note)).append(" (" + note + ")"), true);
				this.playNote(level, player.getX(), player.getY(), player.getZ(), CCSoundEvents.TUNING_FORK_VIBRATE.get(), note);
			}
			return InteractionResult.sidedSuccess(level.isClientSide());
		}

		return super.useOn(context);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		CompoundTag tag = stack.getOrCreateTag();

		if (tag.contains("Note")) {
			int note = tag.getInt("Note");
			player.displayClientMessage(new TranslatableComponent(this.getDescriptionId() + ".note").append(": ").append(new TranslatableComponent(this.getDescriptionId() + ".note." + note)).append(" (" + note + ")"), true);
			this.playNote(level, player.getX(), player.getY(), player.getZ(), CCSoundEvents.TUNING_FORK_VIBRATE.get(), note);
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
		}

		return super.use(level, player, hand);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		CompoundTag tag = stack.getOrCreateTag();

		if (tag.contains("Note")) {
			int note = tag.getInt("Note");
			this.playNote(target.getLevel(), target.getX(), target.getY(), target.getZ(), CCSoundEvents.TUNING_FORK_VIBRATE.get(), note);
			NetworkUtil.spawnParticle("minecraft:note", target.getX(), target.getY() + target.getEyeHeight(), target.getZ(), (double) note / 24.0D, 0.0D, 0.0D);
		}

		return super.hurtEnemy(stack, target, attacker);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		CompoundTag tag = stack.getTag();
		if (tag != null) {
			if (tag.contains("Note")) {
				int note = tag.getInt("Note");
				tooltip.add(new TranslatableComponent(this.getDescriptionId() + ".note." + note).append(" (" + note + ")").withStyle(ChatFormatting.GRAY));
			}
		}
	}

	public void playNote(Level level, double x, double y, double z, SoundEvent soundEvent, int note) {
		float pitch = (float) Math.pow(2.0D, (double) (note - 12) / 12.0D);
		level.playSound(null, x, y, z, soundEvent, SoundSource.NEUTRAL, 1.0F, pitch);
	}

	public int getColor(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("Note")) {
			int note = tag.getInt("Note");
			float f = note / 24.0F;
			float rcol = Math.max(0.0F, Mth.sin((f + 0.0F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F);
			float gcol = Math.max(0.0F, Mth.sin((f + 0.33333334F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F);
			float bcol = Math.max(0.0F, Mth.sin((f + 0.6666667F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F);
			return ((int) (rcol * 255.0F) << 16) + ((int) (gcol * 255.0F) << 8) + ((int) (bcol * 255.0F));
		} else {
			return -1;
		}
	}
}