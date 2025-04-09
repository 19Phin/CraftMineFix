package net.dialingspoon.craftminefix.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.dialingspoon.craftminefix.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.mines.Battle;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Battle.SpawnStrategy.class)
public class SpawnStrategyMixin {
    @WrapMethod(method = "isOkSpawnSpace")
    private static boolean notOnBedrock(ServerLevel serverLevel, BlockPos blockPos, Operation<Boolean> original) {
        if (Config.PREVENT_ROOF_SPAWNS) {
            return original.call(serverLevel, blockPos) && serverLevel.getBlockState(blockPos.below()).getBlock() == Blocks.BEDROCK;
        }
        return original.call(serverLevel, blockPos);
    }
}