package com.example.attachmentkickfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.gen.Accessor;

import net.armory_rpgs.item.ArmorSets;

@Mixin(ArmorSets.class)
public class StriderDamageMultiplierMixin {
    @Shadow
    @Mutable
    //@Accessor("arrow_damage")
    //void setArrowDamage(float value);
    public static float arrow_damage;
}
