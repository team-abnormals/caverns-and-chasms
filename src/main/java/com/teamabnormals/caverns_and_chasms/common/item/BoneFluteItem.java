package com.teamabnormals.caverns_and_chasms.common.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import java.util.function.Consumer;

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