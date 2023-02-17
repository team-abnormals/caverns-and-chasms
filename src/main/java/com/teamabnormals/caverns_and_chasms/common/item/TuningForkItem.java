package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.ControllableGolem;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

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
		Direction direction = context.getClickedFace();
		Player player = context.getPlayer();
		BlockState state = level.getBlockState(pos);
		ItemStack stack = context.getItemInHand();
		CompoundTag tag = stack.getOrCreateTag();

		if (player != null) {
			if (state.getBlock() instanceof NoteBlock && player.isCrouching()) {
				int note = state.getValue(NoteBlock.NOTE);

				if (!tag.contains("Note") || tag.getInt("Note") != note) {
					tag.putInt("Note", state.getValue(NoteBlock.NOTE));

					player.displayClientMessage(Component.translatable(this.getDescriptionId() + ".capture_note", Component.translatable(this.getDescriptionId() + ".note." + note)).append(" (" + note + ")"), true);
					this.playNote(level, player.getX(), player.getY(), player.getZ(), CCSoundEvents.TUNING_FORK_VIBRATE.get(), note);
				}

				return InteractionResult.sidedSuccess(level.isClientSide());
			} else {
				if (tag.contains("Note")) {
					BlockPos targetpos = state.getCollisionShape(level, pos).isEmpty() ? pos : pos.relative(direction);
					int note = tag.getInt("Note");

					player.displayClientMessage(Component.translatable(this.getDescriptionId() + ".note").append(": ").append(Component.translatable(this.getDescriptionId() + ".note." + note)).append(" (" + note + ")"), true);
					this.playNote(level, targetpos.getX() + 0.5D, targetpos.getY() + 0.5D, targetpos.getZ() + 0.5D, CCSoundEvents.TUNING_FORK_VIBRATE.get(), note);

					if (level.isClientSide)
						level.addParticle(ParticleTypes.NOTE, targetpos.getX() + 0.5D, targetpos.getY() + 0.25D, targetpos.getZ() + 0.5D, (double) note / 24.0D, 0.0D, 0.0D);
					else
						attractGolemToPos(targetpos, player);

					return InteractionResult.sidedSuccess(level.isClientSide());
				}
			}
		}

		return super.useOn(context);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		CompoundTag tag = stack.getOrCreateTag();

		if (tag.contains("Note")) {
			int note = tag.getInt("Note");
			player.displayClientMessage(Component.translatable(this.getDescriptionId() + ".note").append(": ").append(Component.translatable(this.getDescriptionId() + ".note." + note)).append(" (" + note + ")"), true);

			Vec3 vec3 = player.getEyePosition().add(player.getViewVector(1.0F).normalize().scale(1.5D));
			this.playNote(level, vec3.x(), vec3.y(), vec3.z(), CCSoundEvents.TUNING_FORK_VIBRATE.get(), note);

			if (level.isClientSide) {
				level.addParticle(ParticleTypes.NOTE, vec3.x(), vec3.y(), vec3.z(), (double) note / 24.0D, 0.0D, 0.0D);
			} else {
				attractGolemToPos(new BlockPos(vec3.x(), vec3.y(), vec3.z()), player);
			}

			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
		}

		return super.use(level, player, hand);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		CompoundTag tag = stack.getOrCreateTag();

		int note = tag.getInt("Note");
		this.playNote(player.level, target.getX(), target.getEyeY(), target.getZ(), CCSoundEvents.TUNING_FORK_VIBRATE.get(), note);

		if (player.level.isClientSide) {
			player.level.addParticle(ParticleTypes.NOTE, target.getX(), target.getEyeY(), target.getZ(), (double) note / 24.0D, 0.0D, 0.0D);
		}

		if (target instanceof ControllableGolem && ((ControllableGolem) target).canBeTuningForkControlled(player) && tag.contains("Note")) {
			ControllableGolem golem = (ControllableGolem) target;
			Player originalcontroller = golem.getTuningForkController();

			if (originalcontroller != player) {
				setControlledGolem(player, golem);
				setForgetGolemTime(player, 200);

				if (originalcontroller != null)
					setControlledGolem(originalcontroller, null);

				golem.setTuningForkPos(null);
				golem.setTuningForkTarget(null);
				((Mob) golem).getNavigation().stop();
				((Mob) golem).setTarget(null);
				golem.onTuningForkControlStart(player);
			} else {
				setControlledGolem(player, null);
				golem.onTuningForkControlEnd(player);
			}
		} else {
			player.displayClientMessage(Component.translatable(this.getDescriptionId() + ".note").append(": ").append(Component.translatable(this.getDescriptionId() + ".note." + note)).append(" (" + note + ")"), true);
			if (!player.level.isClientSide)
				attractGolemToPos(target.blockPosition(), player);
		}

		return InteractionResult.sidedSuccess(player.level.isClientSide);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		CompoundTag tag = stack.getOrCreateTag();

		if (tag.contains("Note")) {
			int note = tag.getInt("Note");

			this.playNote(target.getLevel(), target.getX(), target.getEyeY(), target.getZ(), CCSoundEvents.TUNING_FORK_VIBRATE.get(), note);
			NetworkUtil.spawnParticle("minecraft:note", target.getX(), target.getEyeY(), target.getZ(), (double) note / 24.0D, 0.0D, 0.0D);

			if (attacker instanceof Player) {
				((Player) attacker).displayClientMessage(Component.translatable(this.getDescriptionId() + ".note").append(": ").append(Component.translatable(this.getDescriptionId() + ".note." + note)).append(" (" + note + ")"), true);
				orderGolemToAttackEntity(target, (Player) attacker);
			}
		}

		return super.hurtEnemy(stack, target, attacker);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		CompoundTag tag = stack.getTag();
		if (tag != null && tag.contains("Note")) {
			int note = tag.getInt("Note");
			tooltip.add(Component.translatable(this.getDescriptionId() + ".note." + note).append(" (" + note + ")").withStyle(ChatFormatting.GRAY));
		}
	}

	private void playNote(Level level, double x, double y, double z, SoundEvent soundEvent, int note) {
		float pitch = (float) Math.pow(2.0D, (double) (note - 12) / 12.0D);
		level.playSound(null, x, y, z, soundEvent, SoundSource.NEUTRAL, 1.0F, pitch);
	}

	public static boolean isTuningForkWithNote(ItemStack stack) {
		return stack.getItem() instanceof TuningForkItem && stack.getTag() != null && stack.getTag().contains("Note");
	}

	public static void setControlledGolem(Player player, ControllableGolem golem) {
		((IDataManager) player).setValue(CCDataProcessors.CONTROLLED_GOLEM_UUID, golem != null ? Optional.of(((Mob) golem).getUUID()) : Optional.empty());
	}

	public static ControllableGolem findControlledGolem(Player player) {
		for (Mob mob : player.level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(16.0D))) {
			if (mob instanceof ControllableGolem) {
				ControllableGolem golem = (ControllableGolem) mob;
				if (golem.isTuningForkControlledBy(player) && golem.canBeTuningForkControlled(player))
					return golem;
			}
		}
		return null;
	}

	public static void setForgetGolemTime(Player player, int time) {
		((IDataManager) player).setValue(CCDataProcessors.FORGET_GOLEM_TIME, time);
	}

	public static int getForgetGolemTime(Player player) {
		return ((IDataManager) player).getValue(CCDataProcessors.FORGET_GOLEM_TIME);
	}

	private static void attractGolemToPos(BlockPos pos, Player player) {
		ControllableGolem golem = findControlledGolem(player);
		if (golem != null && golem.shouldMoveToTuningForkPos(pos, player)) {
			golem.setTuningForkPos(pos);
			golem.setTuningForkTarget(null);
			((Mob) golem).setTarget(null);
		}
	}

	private static void orderGolemToAttackEntity(LivingEntity target, Player player) {
		ControllableGolem golem = findControlledGolem(player);
		if (golem != null) {
			if (golem.shouldAttackTuningForkTarget(target, player)) {
				golem.setTuningForkTarget(target);
				golem.setTuningForkPos(null);
			} else if (golem.shouldMoveToTuningForkPos(target.blockPosition(), player)) {
				golem.setTuningForkPos(target.blockPosition());
				golem.setTuningForkTarget(null);
				((Mob) golem).setTarget(null);
			}
		}
	}

	public static int getNoteColor(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("Note")) {
			int note = tag.getInt("Note");
			float f = note / 24.0F;
			float r = Math.max(0.0F, Mth.sin((f + 0.0F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F);
			float g = Math.max(0.0F, Mth.sin((f + 0.33333334F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F);
			float b = Math.max(0.0F, Mth.sin((f + 0.6666667F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F);
			return ((int) (r * 255.0F) << 16) + ((int) (g * 255.0F) << 8) + ((int) (b * 255.0F));
		} else {
			return -1;
		}
	}
}