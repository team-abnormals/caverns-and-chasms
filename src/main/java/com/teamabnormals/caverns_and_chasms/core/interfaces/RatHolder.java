package com.teamabnormals.caverns_and_chasms.core.interfaces;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.world.level.entity.EntityTickList;

import java.util.List;

public interface RatHolder {
	List<Rat> getAttachedRats();

	void attachRat(Rat rat);

	void detachRat(Rat rat);

	void detachAllRats();

	void tickRats(EntityTickList entityTickList);

	int getMaxRats();

	boolean canHoldMoreRats();
}