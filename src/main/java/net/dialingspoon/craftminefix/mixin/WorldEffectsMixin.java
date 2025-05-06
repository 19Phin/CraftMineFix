package net.dialingspoon.craftminefix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.dialingspoon.craftminefix.Config;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.UnlockCondition;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.mines.WorldEffect;
import net.minecraft.world.level.mines.WorldEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(WorldEffects.class)
public class WorldEffectsMixin {
    @WrapOperation(method = "method_70146", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;is(Lnet/minecraft/tags/TagKey;)Z"))
    private static boolean isKindaBadlands(Holder instance, TagKey<Biome> tTagKey, Operation<Boolean> original) {
        if (Config.DRY_LAND_ENABLED) {
            if (tTagKey == BiomeTags.IS_BADLANDS) {
                return instance.getRegisteredName().endsWith("badlands");
            } else throw new RuntimeException("Unexpected Biome: " + instance.getRegisteredName());
        }
        return original.call(instance, tTagKey);
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=wither_skeletons"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/mines/WorldEffect$Builder;withItemModelOf(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/level/mines/WorldEffect$Builder;",
                    ordinal = 0
            )
    )
    private static Item notZiglin(Item item) {
        if (Config.WITHER_SKELETON_EGG) {
            return Items.WITHER_SKELETON_SPAWN_EGG;
        }
        return item;
    }

    @WrapOperation(
            method = "<clinit>",
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=no_drops"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/mines/WorldEffect$Builder;register()Lnet/minecraft/world/level/mines/WorldEffect;",
                    ordinal = 0
            )
    )
    private static WorldEffect rarelyAward(WorldEffect.Builder instance, Operation<WorldEffect> original) {
        if (Config.NO_DROPS_ENABLED) {
            return original.call(instance.unlockedBy(UnlockCondition.playerKilledEntity((serverLevel, serverPlayer, entity) -> serverLevel.getRandom().nextFloat() < 0.005f)));
        }
        return original.call(instance);
    }

    @ModifyArg(
            method = "method_69980",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;<init>(Lnet/minecraft/world/entity/EntityType;II)V"
            )
    )
    private static EntityType<?> correctSpawnRates(EntityType<?> type) {
        if (Config.RABBITS_SPAWN) {
            return EntityType.RABBIT;
        }
        return type;
    }

    @ModifyConstant(
            method = "method_69982",
            constant = @Constant(intValue = 100)
    )
    private static int correctSpawnRates(int instance) {
        if (Config.SPAWN_RATES) {
            return 20;
        }
        return instance;
    }

    @ModifyConstant(
            method = "method_69981",
            constant = @Constant(intValue = 100)
    )
    private static int correctSpawnRates1(int instance) {
        if (Config.SPAWN_RATES) {
            return 20;
        }
        return instance;
    }

    @ModifyConstant(
            method = "method_69980",
            constant = @Constant(intValue = 100)
    )
    private static int correctSpawnRates2(int instance) {
        if (Config.SPAWN_RATES) {
            return 20;
        }
        return instance;
    }
}