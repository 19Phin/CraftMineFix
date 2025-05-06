package net.dialingspoon.craftminefix.mixin;

import net.dialingspoon.craftminefix.Config;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin extends LivingEntity {
    @Shadow protected int enchantmentSeed;
    @Unique
    boolean randomized = false;
    @Shadow
    public HumanoidArm getMainArm() {
        return null;
    }

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "onEnchantmentPerformed", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Player;experienceLevel:I", ordinal = 1))
    private void didRandomize(ItemStack itemStack, int i, CallbackInfo ci) {
        if (Config.ENCHANTMENTS_RANDOMIZE) {
            randomized = true;
        }
    }

    @Inject(method = "onEnchantmentPerformed", at = @At(value = "HEAD"))
    private void randomizeEnchants(ItemStack itemStack, int i, CallbackInfo ci) {
        if (!randomized) {
            this.enchantmentSeed = this.random.nextInt();
        }
        randomized = false;
    }
}