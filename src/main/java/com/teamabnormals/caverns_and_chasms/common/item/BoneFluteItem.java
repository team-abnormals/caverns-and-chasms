package com.teamabnormals.caverns_and_chasms.common.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
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

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BoneFluteItem extends Item {
	private static final double RANGE = 32.0D;

	public BoneFluteItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		HitResult hitResult = getHitResult(player);
		Command command = getCommand(player, hitResult);
		if (command != null) {
			player.startUsingItem(hand);
			level.playSound(player, player, command.sound, SoundSource.RECORDS, 3.0F, 1.0F);
			level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
			executeCommand(command, level, player, hitResult);
			player.getCooldowns().addCooldown(this, 20);
			player.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResultHolder.consume(stack);
		} else {
			return InteractionResultHolder.pass(stack);
		}
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

	private static void executeCommand(Command command, Level level, Player player, HitResult hitResult) {
		List<Rat> rats = level.getEntitiesOfClass(Rat.class, player.getBoundingBox().inflate(64D), (entity) -> entity.getOwner() == player && entity.distanceToSqr(player) <= 4096D);

		if (command == Command.SIT) {
			for (Rat rat : rats) {
				rat.setOrderedToSit(true);
			}
		} else if (command == Command.RECALL) {
			for (Rat rat : rats) {
				rat.setOrderedToSit(false);
				rat.setCommandedPos(null);
			}
		} else if (command == Command.MOVE) {
			BlockPos targetPos = hitResult.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) hitResult).getBlockPos() : BlockPos.containing(hitResult.getLocation());
			for (Rat rat : rats) {
				rat.setOrderedToSit(false);
				rat.detachFromEntity();
				rat.setCommandedPos(targetPos);
			}
		} else if (command == Command.ATTACK) {
			for (Rat rat : rats) {
				rat.setOrderedToSit(false);
				rat.setTarget((LivingEntity) ((EntityHitResult) hitResult).getEntity());
				rat.setCommandedPos(null);
			}
		}
	}

	public static Command getCommand(Player player, HitResult hitResult) {
		if (player.isSecondaryUseActive()) {
			return player.getXRot() > 15.0F ? Command.SIT : Command.RECALL;
		} else {
			HitResult.Type type = hitResult.getType();
			if (type == HitResult.Type.MISS) {
				return Command.RECALL;
			} else if (type == HitResult.Type.ENTITY) {
				if (Rat.canRatsAttack((LivingEntity) ((EntityHitResult) hitResult).getEntity(), player)) {
					return Command.ATTACK;
				}
			}
			return Command.MOVE;
		}
	}

	@NonNull
	public static HitResult getHitResult(Player player) {
		HitResult hitResult = player.pick(RANGE, 1.0F, false);
		Vec3 eyeLoc = player.getEyePosition(1.0F);

		double blockDistSqr = RANGE * RANGE;
		if (hitResult.getType() != HitResult.Type.MISS) {
			blockDistSqr = hitResult.getLocation().distanceToSqr(eyeLoc);
		}

		Vec3 viewVector = player.getViewVector(1.0F);
		Vec3 clipTargetLoc = eyeLoc.add(viewVector.x * RANGE, viewVector.y * RANGE, viewVector.z * RANGE);
		AABB aabb = player.getBoundingBox().expandTowards(viewVector.scale(RANGE)).inflate(1.0D);

		EntityHitResult entityHitResult = getBoneFluteEntityHitResult(player, eyeLoc, clipTargetLoc, aabb, entity -> {
			if (!entity.isSpectator() && entity.isPickable()) {
				LivingEntity living = entity instanceof LivingEntity ? (LivingEntity) entity : entity instanceof PartEntity<?> partEntity && partEntity.getParent() instanceof LivingEntity ? (LivingEntity) partEntity.getParent() : null;
				return living != null && Rat.canRatsAttack(living, player);
			} else {
				return false;
			}
		}, blockDistSqr);

		if (entityHitResult != null && entityHitResult.getType() != HitResult.Type.MISS) {
			Vec3 entityLoc = entityHitResult.getLocation();
			double entityDistSqr = entityLoc.distanceToSqr(eyeLoc);
			if (entityDistSqr < blockDistSqr) {
				return entityHitResult;
			}
		}

		return hitResult;
	}

	private static EntityHitResult getBoneFluteEntityHitResult(Entity entity, Vec3 startLoc, Vec3 endLoc, AABB aabb, Predicate<Entity> predicate, double range) {
		Level level = entity.level();
		double d0 = range;
		Vec3 vec3 = null;
		Entity entity1 = null;

		for (Entity entity2 : level.getEntities(entity, aabb, predicate)) {
			AABB aabb1 = entity2.getBoundingBox();
			AABB aabb2 = aabb1.inflate(entity2.getPickRadius() + 0.25D + Mth.clamp(startLoc.distanceTo(entity2.position()) * 0.05D, 0.0D, 1.0D));
			Optional<Vec3> optional = aabb2.clip(startLoc, endLoc);
			if (optional.isPresent()) {
				Vec3 vec31 = optional.get();
				double d1 = startLoc.distanceToSqr(vec31);
				if (d1 < d0) {
					Vec3 vec32 = new Vec3(Mth.clamp(vec31.x, aabb1.minX + 0.01D, aabb1.maxX - 0.01D), Mth.clamp(vec31.y, aabb1.minY + 0.01D, aabb1.maxY - 0.01D), Mth.clamp(vec31.z, aabb1.minZ + 0.01D, aabb1.maxZ - 0.01D));
					BlockHitResult hitResult = level.clip(new ClipContext(vec31, vec32, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
					if (hitResult.getType() == HitResult.Type.MISS) {
						entity1 = entity2 instanceof PartEntity<?> partEntity ? partEntity.getParent() : entity2;
						vec3 = vec31;
						d0 = d1;
					}
				}
			}
		}

		return entity1 == null ? null : new EntityHitResult(entity1, vec3);
	}

	public enum Command {
		SIT(CCSoundEvents.BONE_FLUTE_SIT.get()),
		RECALL(CCSoundEvents.BONE_FLUTE_RECALL.get()),
		MOVE(CCSoundEvents.BONE_FLUTE_MOVE.get()),
		ATTACK(CCSoundEvents.BONE_FLUTE_ATTACK.get());

		private final ResourceLocation crosshairIcon;
		private final ResourceLocation crosshairIconBackground;
		private final SoundEvent sound;

		Command(SoundEvent sound) {
			this.crosshairIcon = CavernsAndChasms.location("textures/gui/sprites/hud/bone_flute_crosshair/" + this.name().toLowerCase() + ".png");
			this.crosshairIconBackground = CavernsAndChasms.location("textures/gui/sprites/hud/bone_flute_crosshair/" + this.name().toLowerCase() + "_background.png");
			this.sound = sound;
		}

		public ResourceLocation getCrosshairIcon() {
			return this.crosshairIcon;
		}

		public ResourceLocation getCrosshairIconBackground() {
			return this.crosshairIconBackground;
		}

		public SoundEvent getSound() {
			return this.sound;
		}
	}
}