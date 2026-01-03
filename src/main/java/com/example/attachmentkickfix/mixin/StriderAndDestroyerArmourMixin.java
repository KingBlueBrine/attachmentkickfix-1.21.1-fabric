package com.example.attachmentkickfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.ModifyArgs;
//import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.armory_rpgs.item.ArmorSets;

@Mixin(value = ArmorSets.class, remap = false)
public abstract class StriderAndDestroyerArmourMixin {

    /*@ModifyArgs(
        method = "<clinit>()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/armory_rpgs/item/ArmorSets;material(Ljava/lang/String;IIIIILnet/minecraft/registry/entry/RegistryEntry;Ljava/util/function/Supplier;)Lnet/armory_rpgs/item/ArmorMaterialType;"
        )
    )
    private static void modifyArmourStats(Args args) {
        String name = args.get(0);

        if ("warrior_armor".equals(name) || "archer_armor".equals(name)) {
            args.set(1, 5); // helmet
            args.set(2, 9); // chestplate
            args.set(3, 7); // leggings
            args.set(4, 5); // boots
        }
    }*/
}
