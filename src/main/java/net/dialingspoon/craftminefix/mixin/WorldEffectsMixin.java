package net.dialingspoon.craftminefix.mixin;

import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.mines.WorldEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldEffects.class)
public class WorldEffectsMixin {
    @Redirect(method = "method_70146", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;is(Lnet/minecraft/tags/TagKey;)Z"))
    private static boolean redirectIsBadlands(Holder instance, TagKey<Biome> tagKey) {
        if (tagKey == BiomeTags.IS_BADLANDS) {
            System.out.println(instance.getRegisteredName().endsWith("badlands"));
            return true;
        }
        else throw new RuntimeException("Unexpected Biome: " + instance.getRegisteredName() );
    }
}