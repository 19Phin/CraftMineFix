package net.dialingspoon.craftminefix.mixin;

import net.dialingspoon.craftminefix.Config;
import net.dialingspoon.craftminefix.interfaces.BattleInterface;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.mines.Battle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow private Level level;

    @Shadow protected UUID uuid;

    @Inject(method = "setRemoved", at = @At(value = "HEAD"))
    private void onDie(Entity.RemovalReason removalReason, CallbackInfo ci) {
        if (!Config.BATTLES_FIX_ENABLED) return;

        if (removalReason == Entity.RemovalReason.KILLED || removalReason == Entity.RemovalReason.DISCARDED) {
           if (level instanceof ServerLevel serverLevel) {
               serverLevel.events().stream()
                       .filter(event -> event instanceof Battle)
                       .map(event -> (Battle) event)
                       .forEach(battle -> ((BattleInterface)battle).removeMob(uuid));
           }
       }
    }

}