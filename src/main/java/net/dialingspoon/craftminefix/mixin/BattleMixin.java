package net.dialingspoon.craftminefix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.dialingspoon.craftminefix.Config;
import net.dialingspoon.craftminefix.interfaces.BattleInterface;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.mines.Battle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@Mixin(Battle.class)
public class BattleMixin implements BattleInterface {

    @Shadow @Final private List<UUID> spawnedMobs;
    private int mobCount;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void deleteDead(ServerLevel serverLevel, CallbackInfo ci) {
        if (Config.BATTLES_FIX_ENABLED) {
            this.spawnedMobs.removeIf((uUID) -> serverLevel.getEntity(uUID) != null && !serverLevel.getEntity(uUID).isAlive());
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 2))
    private int redirectMaxMobCount(List instance, Operation<Integer> original) {
        if (Config.BATTLES_FIX_ENABLED) {
            return mobCount;
        }
        return original.call(instance);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 3))
    private int redirectMobCount(List instance, Operation<Integer> original) {
        if (Config.BATTLES_FIX_ENABLED) {
            return spawnedMobs.size();
        }
        return original.call(instance);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
    private boolean isAllDead(List instance, Operation<Boolean> original) {
        if (Config.BATTLES_FIX_ENABLED) {
            return spawnedMobs.isEmpty();
        }
        return original.call(instance);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/List;getFirst()Ljava/lang/Object;"))
    private<E> E ifEmpty(List instance, Operation<E> original, @Local(argsOnly = true) ServerLevel level) {
        if (instance.isEmpty()) {
            return (E) level.players().getFirst();
        }
        return original.call(instance);
    }

    @Inject(method = "spawnWave", at = @At(value = "HEAD"))
    private void resetMaxMobCount(Battle.Wave wave, ServerLevel serverLevel, CallbackInfo ci) {
        mobCount = 0;
    }

    @Inject(method = "spawnWave", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void addToMaxMobCount(Battle.Wave wave, ServerLevel serverLevel, CallbackInfo ci) {
        mobCount++;
    }

    @Override
    public void removeMob(UUID uUID) {
        spawnedMobs.remove(uUID);
    }
}