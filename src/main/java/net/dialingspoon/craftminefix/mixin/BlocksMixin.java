package net.dialingspoon.craftminefix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.dialingspoon.craftminefix.Config;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {
    @WrapOperation(
            method = "<clinit>",
            slice = @Slice(
                    from = @At(value = "CONSTANT", args = "stringValue=mine_crafter"),
                    to = @At(value = "CONSTANT", args = "stringValue=fletching_table")
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;strength(F)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 1
            )
    )
    private static BlockBehaviour.Properties blastResist(BlockBehaviour.Properties instance, float f, Operation<BlockBehaviour.Properties> original) {
        if (Config.EXPLOSION_RESIST_ENABLED) {
            return instance.strength(-1.0F, 3600000.0F);
        }
        return original.call(instance, f);
    }

}