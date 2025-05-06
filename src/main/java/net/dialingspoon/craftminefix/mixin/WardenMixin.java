package net.dialingspoon.craftminefix.mixin;

import net.dialingspoon.craftminefix.Config;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.monster.warden.WardenAi;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Warden.class)
public class WardenMixin extends Monster {
    @Shadow public int timeUntilRockets;

    protected WardenMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/Brain;getMemory(Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;)Ljava/util/Optional;", ordinal = 0))
    private void correctPersistence(CallbackInfo ci) {
        if (Config.WARDEN) {
            if (this.isPersistenceRequired() || this.requiresCustomPersistence()) {
                WardenAi.setDigCooldown(this);
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/monster/warden/Warden;timeUntilMobHead:I", ordinal = 2))
    private void correctTimer(CallbackInfo ci) {
        if (Config.WARDEN) {
            this.timeUntilRockets--;
        }
    }
}