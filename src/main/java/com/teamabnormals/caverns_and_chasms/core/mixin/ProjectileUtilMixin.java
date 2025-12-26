package com.teamabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.world.entity.projectile.ProjectileUtil;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ProjectileUtil.class)
public abstract class ProjectileUtilMixin {
	/*
	@Definition(id = "entity1", local = @Local(type = Entity.class, ordinal = 1))
	@Definition(id = "p_37288_", local = @Local(type = Entity.class))
	@Definition(id = "getRootVehicle", method = "Lnet/minecraft/world/entity/Entity;getRootVehicle()Lnet/minecraft/world/entity/Entity;")
	@Definition(id = "canRiderInteract", method = "Lnet/minecraftforge/common/extensions/IForgeEntity;canRiderInteract()Z")
	@Expression("entity1.getRootVehicle() == p_37288_.getRootVehicle() && entity1.canRiderInteract()")
	@ModifyExpressionValue(at = @At("MIXINEXTRAS:EXPRESSION"), method = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;")
	private boolean isAttachedRat(Entity entity, Operation<Boolean> original) {
		return original.call(entity) || entity instanceof Rat rat && rat.isAttachedToEntity();
	}
	*/
}