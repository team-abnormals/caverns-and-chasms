package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.Grazer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

@Mixin(DebugScreenOverlay.class)
public abstract class DebugScreenOverlayMixin {

	@Shadow
	protected abstract Level getLevel();

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	@Nullable
	protected abstract ServerLevel getServerLevel();

	@WrapOperation(method = "getGameInformation", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 11))
	private boolean getGameInformation(List<String> instance, Object e, Operation<Boolean> original) {
		original.call(instance, e);

		Level level = this.getLevel();
		ServerLevel serverLevel = this.getServerLevel();

		instance.add(String.format(Locale.ROOT, "Grazer Chance: %.2f", Grazer.calculateGrazerMigrationChance(serverLevel, this.minecraft.getCameraEntity().blockPosition(), level.getMoonPhase())));
		instance.add(String.format(Locale.ROOT, "Optimal Moon Phase: %d (Current: %d)", Grazer.calculateOptimalMoonPhaseForPos(serverLevel, this.minecraft.getCameraEntity().blockPosition()), level.getMoonPhase()));

		return true;
	}
}