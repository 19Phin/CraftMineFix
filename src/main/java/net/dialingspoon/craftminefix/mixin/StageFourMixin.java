package net.dialingspoon.craftminefix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.dialingspoon.craftminefix.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.warden.Stages.StageFour;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.schedule.Activity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StageFour.class)
public class StageFourMixin{

    @WrapOperation(method = "start(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/monster/warden/Warden;J)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/warden/Warden;setPose(Lnet/minecraft/world/entity/Pose;)V"))
    private void dontDig(Warden instance, Pose pose, Operation<Void> original) {
        if (!Config.WARDEN) {
            original.call(instance, pose);
        }
    }

    @Inject(method = "stop(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/monster/warden/Warden;J)V", at = @At(value = "TAIL"))
    private void dig(ServerLevel serverLevel, Warden warden, long l, CallbackInfo ci) {
        if (Config.WARDEN) {
            warden.getBrain().eraseMemory(MemoryModuleType.ACTING_STAGE);
            warden.getBrain().eraseMemory(MemoryModuleType.DIG_COOLDOWN);
            warden.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
            warden.getBrain().setActiveActivityIfPossible(Activity.DIG);
        }
    }
}