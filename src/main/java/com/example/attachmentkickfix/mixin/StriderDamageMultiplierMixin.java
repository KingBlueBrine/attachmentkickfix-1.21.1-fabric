package com.example.attachmentkickfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.armory_rpgs.item.ArmorSets;

@Mixin(value = ArmorSets.class, remap = false)
public class StriderDamageMultiplierMixin {
    @Shadow
    @Mutable
    public static float arrow_damage;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyArrowDamage(CallbackInfo ci) {
        arrow_damage = 0.12F;
    }
}
