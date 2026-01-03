package com.example.attachmentkickfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import net.armory_rpgs.item.ArmorSets;

@Mixin(ArmorSets.class)
public abstract class StriderAndDestroyerArmourMixin {

    @ModifyArgs(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/armory_rpgs/item/ArmorSets;material(Ljava/lang/String;IIIIILnet/minecraft/sound/SoundEvent;Ljava/util/function/Supplier;)Lnet/armoroy_rpgs/item/ArmorMaterialType;"
        )
    )
    private static void modifyWizardRobeArmor(Args args) {
        String name = args.get(0);

        // Only affect wizard_robe
        if ("warrior_armor".equals(name) || "archer_armor".equals(name)) {
            args.set(1, 5); // helmet
            args.set(2, 9); // chestplate
            args.set(3, 7); // leggings
            args.set(4, 5); // boots
        } else { return; }
    }

    /*public static void setArrowDamage() {
        try {
            Field field = ArmorSets.class.getField("arrow_damage"); // public field
            // Remove final modifier
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            // Set new value
            field.setFloat(null, 0.12f);
            //System.out.println("Arrow damage set to 0.12f!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
