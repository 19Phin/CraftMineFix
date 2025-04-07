package net.dialingspoon.craftminefix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.dialingspoon.craftminefix.Config;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @WrapOperation(method = "cleanInventoryAndReward", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/player/Inventory.iterator ()Ljava/util/Iterator;", ordinal = 0))
    private Iterator<ItemStack> use(Inventory instance, Operation<Iterator<ItemStack>> original) {
        if (Config.BUNDLED_REWARDS_ENABLED) {
            List<ItemStack> flattened = new ArrayList<>();
            for (ItemStack itemStack : instance) {
                if (itemStack.has(DataComponents.BUNDLE_CONTENTS)) {
                    var contents = itemStack.get(DataComponents.BUNDLE_CONTENTS);
                    if (contents != null) flattened.addAll((Collection<? extends ItemStack>) contents.items());
                }

                if (itemStack.has(DataComponents.CONTAINER)) {
                    var container = itemStack.get(DataComponents.CONTAINER);
                    if (container != null)
                        flattened.addAll((Collection<? extends ItemStack>) container.nonEmptyItems());
                }

                flattened.add(itemStack);
            }
            return flattened.iterator();
        }
        return original.call(instance);
    }

}