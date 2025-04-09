package net.dialingspoon.craftminefix.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.dialingspoon.craftminefix.Config;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MineCraftingMenu;
import net.minecraft.world.inventory.MineCraftingSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.component.WorldModifiers;
import net.minecraft.world.level.mines.WorldEffect;
import net.minecraft.world.level.mines.WorldEffectSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(MineCraftingSlot.class)
public class MineCraftingSlotMixin extends Slot {
    @Shadow public MineCraftingMenu menu;

    public MineCraftingSlotMixin(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @WrapMethod(method = "mayPickup")
    private boolean wontAllowIncompatible(Player player, Operation<Boolean> original) {
        if (Config.PREVENT_INCOMPATIBLES) {
            List<WorldEffect> allEffects = this.menu.getCraftingSlots().stream()
                    .map(Slot::getItem)
                    .filter(stack -> stack != this.getItem())
                    .flatMap(stack -> stack
                            .getOrDefault(DataComponents.WORLD_MODIFIERS, WorldModifiers.EMPTY)
                            .effects()
                            .stream()
                    )
                    .toList();

            Map<WorldEffectSet, WorldEffect> exclusiveMap = new HashMap<>();
            for (WorldEffect effect : allEffects) {
                for (WorldEffectSet set : effect.inSets()) {
                    if (set.exclusive()) {
                        exclusiveMap.put(set, effect);
                    }
                }
            }

            List<WorldEffect> finalList = allEffects.stream()
                    .filter(effect -> effect.inSets().stream()
                            .filter(WorldEffectSet::exclusive)
                            .allMatch(set -> exclusiveMap.get(set) == effect)
                    )
                    .toList();

            boolean allValid = finalList.stream().allMatch(e -> e.isValidWith(finalList));

            return original.call(player) && allValid;
        }
        return  original.call(player);
    }
}