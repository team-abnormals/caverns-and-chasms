package com.teamabnormals.caverns_and_chasms.common.item.copper;

import com.teamabnormals.caverns_and_chasms.common.entity.decoration.OxidizedCopperGolem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class OxidizedCopperGolemItem extends Item {
	private final boolean waxed;

	public OxidizedCopperGolemItem(Properties properties, boolean waxed) {
		super(properties);
		this.waxed = waxed;
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
			AABB aabb = CCEntityTypes.OXIDIZED_COPPER_GOLEM.get().getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());
			if (level.noCollision(null, aabb) && level.getEntities(null, aabb).isEmpty()) {
				if (level instanceof ServerLevel serverlevel) {
					Consumer<OxidizedCopperGolem> consumer = EntityType.createDefaultStackConfig(serverlevel, itemstack, context.getPlayer());
					OxidizedCopperGolem golem = CCEntityTypes.OXIDIZED_COPPER_GOLEM.get().create(serverlevel, consumer, blockpos, MobSpawnType.SPAWN_EGG, true, true);
					if (golem == null) {
						return InteractionResult.FAIL;
					}

					golem.setWaxed(waxed);

					float yRot = (float) Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
					golem.moveTo(golem.getX(), golem.getY(), golem.getZ(), yRot, 0.0F);
					golem.setYHeadRot(yRot);
					serverlevel.addFreshEntityWithPassengers(golem);
					level.playSound(null, golem.getX(), golem.getY(), golem.getZ(), SoundEvents.COPPER_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
					golem.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
				}

				itemstack.shrink(1);
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else {
				return InteractionResult.FAIL;
			}
		}
	}
}