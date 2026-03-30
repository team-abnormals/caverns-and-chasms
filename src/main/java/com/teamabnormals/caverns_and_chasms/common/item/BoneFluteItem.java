package com.teamabnormals.caverns_and_chasms.common.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.common.network.bone_flute.C2SBoneFluteAttackMessage;
import com.teamabnormals.caverns_and_chasms.common.network.bone_flute.C2SBoneFluteMoveMessage;
import com.teamabnormals.caverns_and_chasms.common.network.bone_flute.C2SBoneFluteRecallMessage;
import com.teamabnormals.caverns_and_chasms.common.network.bone_flute.C2SBoneFluteSitMessage;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.entity.PartEntity;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class BoneFluteItem extends Item {
	public static final double MAX_SEND_DIST = 64.0D;
	public static final double COMMAND_RANGE = 128.0D;

	public BoneFluteItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.startUsingItem(hand);
		level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
		if (level.isClientSide) {
			HitResult hitResult = getHitResult(player);
			broadcastCommand(getCommand(player, hitResult), hitResult);
		}
		player.getCooldowns().addCooldown(this, 20);
		player.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.CUSTOM;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private static final HumanoidModel.ArmPose FLUTE_ARM_POSE = HumanoidModel.ArmPose.create("CAVERNS_AND_CHASMS_FLUTE", true, (model, entity, arm) -> {
				float f = model.head.xRot * 0.8F;
				float f1 = model.head.yRot * Mth.cos(f);
				float xRotR = arm == HumanoidArm.RIGHT ? -1.05F : -1.55F;
				float xRotL = arm == HumanoidArm.RIGHT ? -1.55F : -1.05F;
				float yRotR = (arm == HumanoidArm.RIGHT ? -0.5F : -0.35F) + f1;
				float yRotL = (arm == HumanoidArm.RIGHT ? 0.35F : 0.5F) + f1;

				Vector3f vec3R = (new Matrix3f()).rotationZYX(0.0F, yRotR, xRotR).rotateLocalX(f).getEulerAnglesZYX(new Vector3f());
				Vector3f vec3L = (new Matrix3f()).rotationZYX(0.0F, yRotL, xRotL).rotateLocalX(f).getEulerAnglesZYX(new Vector3f());

				model.rightArm.setRotation(vec3R.x, vec3R.y, vec3R.z);
				model.leftArm.setRotation(vec3L.x, vec3L.y, vec3L.z);
			});

			@Override
			public HumanoidModel.ArmPose getArmPose(LivingEntity living, InteractionHand hand, ItemStack stack) {
				return living.getUsedItemHand() == hand && living.getUseItemRemainingTicks() > 0 ? FLUTE_ARM_POSE : ArmPose.ITEM;
			}

			@Override
			public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
				if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0) {
					int i = arm == HumanoidArm.RIGHT ? 1 : -1;
					poseStack.translate((float) i * 0.56F, -0.52F, -0.72F);
					return true;
				} else {
					return false;
				}
			}
		});
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	private static void broadcastCommand(BoneFluteCommand command, HitResult hitResult) {
		if (command == BoneFluteCommand.SIT) {
			CavernsAndChasms.CHANNEL.sendToServer(new C2SBoneFluteSitMessage());
		} else if (command == BoneFluteCommand.RECALL) {
			CavernsAndChasms.CHANNEL.sendToServer(new C2SBoneFluteRecallMessage());
		} else if (command == BoneFluteCommand.MOVE) {
			BlockPos pos = hitResult.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) hitResult).getBlockPos() : BlockPos.containing(hitResult.getLocation());
			CavernsAndChasms.CHANNEL.sendToServer(new C2SBoneFluteMoveMessage(pos));
		} else if (command == BoneFluteCommand.ATTACK) {
			LivingEntity target = (LivingEntity) ((EntityHitResult) hitResult).getEntity();
			CavernsAndChasms.CHANNEL.sendToServer(new C2SBoneFluteAttackMessage(target));
		}
	}

	public static BoneFluteCommand getCommand(Player player, HitResult hitResult) {
		if (player.isSecondaryUseActive()) {
			return player.getXRot() > 15.0F ? BoneFluteCommand.SIT : BoneFluteCommand.RECALL;
		} else {
			HitResult.Type type = hitResult.getType();
			if (type == HitResult.Type.MISS) {
				return BoneFluteCommand.RECALL;
			} else if (type == HitResult.Type.ENTITY) {
				if (Rat.canRatsAttack((LivingEntity) ((EntityHitResult) hitResult).getEntity(), player)) {
					return BoneFluteCommand.ATTACK;
				}
			}
			return BoneFluteCommand.MOVE;
		}
	}

	@NonNull
	public static HitResult getHitResult(Player player) {
		HitResult hitResult = player.pick(MAX_SEND_DIST, 1.0F, false);
		Vec3 eyeLoc = player.getEyePosition();

		double blockDistSqr = MAX_SEND_DIST * MAX_SEND_DIST;
		if (hitResult.getType() != HitResult.Type.MISS) {
			blockDistSqr = hitResult.getLocation().distanceToSqr(eyeLoc);
		}

		Vec3 viewVector = player.getViewVector(1.0F);
		Vec3 clipTargetLoc = eyeLoc.add(viewVector.x * MAX_SEND_DIST, viewVector.y * MAX_SEND_DIST, viewVector.z * MAX_SEND_DIST);
		AABB aabb = player.getBoundingBox().expandTowards(viewVector.scale(MAX_SEND_DIST)).inflate(1.0D);

		EntityHitResult entityHitResult = CCUtil.getExaggeratedHitboxEntityHitResult(player, eyeLoc, clipTargetLoc, aabb, entity -> {
			if (entity.isPickable()) {
				LivingEntity living = entity instanceof LivingEntity ? (LivingEntity) entity : entity instanceof PartEntity<?> partEntity && partEntity.getParent() instanceof LivingEntity ? (LivingEntity) partEntity.getParent() : null;
				return living != null && !living.isSpectator() && living != player.getVehicle() && Rat.canRatsAttack(living, player) && !(entity instanceof Rat rat && rat.getAttachedEntity() == player);
			} else {
				return false;
			}
		}, blockDistSqr, true);

		if (entityHitResult != null && entityHitResult.getType() != HitResult.Type.MISS) {
			Vec3 entityLoc = entityHitResult.getLocation();
			double entityDistSqr = entityLoc.distanceToSqr(eyeLoc);
			if (entityDistSqr < blockDistSqr) {
				return entityHitResult;
			}
		}

		return hitResult;
	}
}