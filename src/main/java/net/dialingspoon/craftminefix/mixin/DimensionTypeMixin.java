package net.dialingspoon.craftminefix.mixin;

import net.dialingspoon.craftminefix.Config;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
    @Shadow @Final private boolean ultraWarm;

    @ModifyArg(method = "withAmbientLight", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;<init>(Ljava/util/OptionalLong;ZZZZDZZIIILnet/minecraft/tags/TagKey;Lnet/minecraft/world/level/dimension/DimensionSpecialEffects;FLnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;)V"), index = 3)
    private boolean notUltrawarm(boolean wasUltraWarm) {
        if (Config.UNHEAT_CAVES) {
            return ultraWarm;
        }
        return wasUltraWarm;
    }
}