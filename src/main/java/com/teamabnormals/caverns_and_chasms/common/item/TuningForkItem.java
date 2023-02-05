package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.ControllableGolem;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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
                        attractGolemsToPos(level, targetpos, player);
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
			this.playNote(level, player.getX(), player.getY(), player.getZ(), CCSoundEvents.TUNING_FORK_VIBRATE.get(), note);
            if (!level.isClientSide)
			    attractGolemsToPos(level, player.blockPosition(), player);
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
		}

		return super.use(level, player, hand);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		CompoundTag tag = stack.getOrCreateTag();

		if (target instanceof ControllableGolem && ((ControllableGolem) target).canBeControlled(player) && tag.contains("Note")) {
			ControllableGolem golem = (ControllableGolem) target;
			if (golem.getController() == null) {
                golem.setController(player);
				golem.setForgetControllerTime(200);
				golem.setTuningForkPos(null);
				golem.setTuningForkTarget(null);

                golem.onTuningForkControl(player);

                int note = tag.getInt("Note");
                this.playNote(player.level, player.getX(), player.getY(), player.getZ(), CCSoundEvents.TUNING_FORK_VIBRATE.get(), note);
                if (player.level.isClientSide) {
                    player.level.addParticle(ParticleTypes.NOTE, target.getX(), target.getEyeY(), target.getZ(), (double) note / 24.0D, 0.0D, 0.0D);
                }
			} else {
                golem.setController(null);
			}

			return InteractionResult.sidedSuccess(player.level.isClientSide);
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		CompoundTag tag = stack.getOrCreateTag();

		if (tag.contains("Note")) {
			int note = tag.getInt("Note");
			this.playNote(target.getLevel(), target.getX(), target.getY(), target.getZ(), CCSoundEvents.TUNING_FORK_VIBRATE.get(), note);
			NetworkUtil.spawnParticle("minecraft:note", target.getX(), target.getY() + target.getEyeHeight(), target.getZ(), (double) note / 24.0D, 0.0D, 0.0D);

			if (!target.level.isClientSide && attacker instanceof Player) {
                orderGolemsToAttackEntity(target.getLevel(), target, (Player) attacker);
            }
		}

		return super.hurtEnemy(stack, target, attacker);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		CompoundTag tag = stack.getTag();
		if (tag != null) {
			if (tag.contains("Note")) {
				int note = tag.getInt("Note");
				tooltip.add(Component.translatable(this.getDescriptionId() + ".note." + note).append(" (" + note + ")").withStyle(ChatFormatting.GRAY));
			}
		}
	}

	private void playNote(Level level, double x, double y, double z, SoundEvent soundEvent, int note) {
		float pitch = (float) Math.pow(2.0D, (double) (note - 12) / 12.0D);
		level.playSound(null, x, y, z, soundEvent, SoundSource.NEUTRAL, 1.0F, pitch);
	}

	private static void attractGolemsToPos(Level level, BlockPos pos, Player player) {
		for (Mob mob : getControlledGolems(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, player)) {
			ControllableGolem golem = (ControllableGolem) mob;
			if (golem.shouldMoveToTuningForkPos(pos, player)) {
				golem.setTuningForkPos(pos);
			}
		}
	}

    private static void orderGolemsToAttackEntity(Level level, LivingEntity target, Player player) {
		for (Mob mob : getControlledGolems(level, target.getX(), target.getY(), target.getZ(), player)) {
			ControllableGolem golem = (ControllableGolem) mob;
			if (golem.shouldAttackTuningForkTarget(target, player)) {
				golem.setTuningForkTarget(target);
			} else if (golem.shouldMoveToTuningForkPos(target.blockPosition(), player)) {
				golem.setTuningForkPos(target.blockPosition());
			}
		}
    }

	public static List<Mob> getControlledGolems(Level level, double x, double y, double z, Player player) {
		return level.getEntitiesOfClass(Mob.class, (new AABB(new Vec3(x - 0.5D, y, z - 0.5D), new Vec3(x + 0.5D, y + 1.0D, z + 0.5D))).inflate(8.0D), (entity) -> {
			return entity != null && entity.isAlive() && entity instanceof ControllableGolem && ((ControllableGolem) entity).getController() == player && ((ControllableGolem) entity).canBeControlled(player);
		});
	}

	public static int getNoteColor(ItemStack stack) {
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