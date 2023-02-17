package com.teamabnormals.caverns_and_chasms.common.entity.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class PrimedTmt extends PrimedTnt {
	@Nullable
	private LivingEntity tmtPlacedBy;

	public PrimedTmt(EntityType<? extends PrimedTmt> type, Level level) {
		super(type, level);
	}

	public PrimedTmt(Level level, double x, double y, double z, @Nullable LivingEntity igniter) {
		this(CCEntityTypes.TMT.get(), level);
		this.setPos(x, y, z);
		double d0 = level.getRandom().nextDouble() * (double) ((float) Math.PI * 2F);
		this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
		this.setFuse(30);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.tmtPlacedBy = igniter;
	}

	@Override
	protected void explode() {
		this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 4.0F, Explosion.BlockInteraction.NONE);
	}

	@Override
	@Nullable
	public LivingEntity getOwner() {
		return this.tmtPlacedBy;
	}
}