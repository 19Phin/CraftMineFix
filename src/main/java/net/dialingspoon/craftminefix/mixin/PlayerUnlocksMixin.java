package net.dialingspoon.craftminefix.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.dialingspoon.craftminefix.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerUnlocks;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerUnlocks.class)
public class PlayerUnlocksMixin {
    @WrapMethod(method = "method_69247")
    private static Boolean isDragonFire(ServerLevel serverLevel, ServerPlayer serverPlayer, DamageSource damageSource, Float float_, Operation<Boolean> original) {
        if (Config.DRAGON_FIRE_ENABLED) {
            return original.call(serverLevel, serverPlayer, damageSource, float_) ||
                    damageSource.getDirectEntity() != null && damageSource.getEntity() != null &&
                            damageSource.getDirectEntity().getType() == EntityType.AREA_EFFECT_CLOUD &&
                            damageSource.getEntity().getType() == EntityType.ENDER_DRAGON;
        }
        return original.call(serverLevel, serverPlayer, damageSource, float_);
    }
}