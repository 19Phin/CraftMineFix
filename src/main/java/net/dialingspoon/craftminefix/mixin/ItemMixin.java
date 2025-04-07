package net.dialingspoon.craftminefix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.dialingspoon.craftminefix.Config;
import net.minecraft.server.players.PlayerUnlocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.Equippable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public class ItemMixin {
    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/equipment/Equippable;swappable()Z"))
    private boolean dontEquip(Equippable instance, Operation<Boolean> original, @Local(argsOnly = true) Player player) {
        if (Config.ARMOR_ENABLED) {
            return original.call(instance) && player.isActive(PlayerUnlocks.ARMAMENTS);
        }
        return original.call(instance);
    }

}