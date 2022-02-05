package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class OxidizedCopperGolemItem extends Item {

	public OxidizedCopperGolemItem(Properties properties) {
		super(properties);
	}

	public InteractionResult useOn(UseOnContext context) {
		Direction direction = context.getClickedFace();
		if (direction == Direction.DOWN) {
			return InteractionResult.FAIL;
		} else {
			Level level = context.getLevel();
			BlockPlaceContext blockplacecontext = new BlockPlaceContext(context);
			BlockPos blockpos = blockplacecontext.getClickedPos();
			ItemStack itemstack = context.getItemInHand();
			Vec3 vec3 = Vec3.atBottomCenterOf(blockpos);
			AABB aabb = CCEntityTypes.COPPER_GOLEM.get().getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());
			if (level.noCollision((Entity)null, aabb) && level.getEntities((Entity)null, aabb).isEmpty()) {
				if (level instanceof ServerLevel) {
					ServerLevel serverlevel = (ServerLevel)level;
					CopperGolem coppergolem = CCEntityTypes.COPPER_GOLEM.get().create(serverlevel, itemstack.getTag(), (Component)null, context.getPlayer(), blockpos, MobSpawnType.SPAWN_EGG, true, true);
					if (coppergolem == null) {
						return InteractionResult.FAIL;
					}

					float f = (float)Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
					coppergolem.setOxidation(CopperGolem.Oxidation.OXIDIZED);
					coppergolem.moveTo(coppergolem.getX(), coppergolem.getY(), coppergolem.getZ(), f, 0.0F);
					coppergolem.yHeadRot = f;
					coppergolem.yBodyRot = f;
					serverlevel.addFreshEntityWithPassengers(coppergolem);
					level.playSound((Player)null, coppergolem.getX(), coppergolem.getY(), coppergolem.getZ(), SoundEvents.COPPER_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
					level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, coppergolem);
				}

				itemstack.shrink(1);
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else {
				return InteractionResult.FAIL;
			}
		}
	}
}