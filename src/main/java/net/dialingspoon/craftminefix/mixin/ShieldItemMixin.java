package net.dialingspoon.craftminefix.mixin;

import net.dialingspoon.craftminefix.Config;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShieldItem.class)
public class ShieldItemMixin extends Item {
    public ShieldItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "use", at = @At(value = "RETURN"), cancellable = true)
    private void useShield(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (Config.SHIELDS_ENABLED) {
            player.startUsingItem(interactionHand);
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

}