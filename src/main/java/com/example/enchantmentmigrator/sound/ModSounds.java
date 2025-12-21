package com.example.enchantmentmigrator.sound;

import com.example.enchantmentmigrator.EnchantmentMigratorMod;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent BLUETEETH = registerSoundEvent("blueteeth");
    public static final SoundEvent MOOW = registerSoundEvent("moow");
    public static final SoundEvent CHICKEN = registerSoundEvent("chicken");
    public static final SoundEvent MENOPAUSE_SYMPTOMS = registerSoundEvent("menopause_symptoms");
    public static final SoundEvent MILK_THISTLE = registerSoundEvent("milk_thistle");
    public static final SoundEvent ZERO_CALORIES = registerSoundEvent("zero_calories");
    public static final SoundEvent WEIGHT_LOSS = registerSoundEvent("weight_loss");
    public static final SoundEvent MY_GUINEA_PIG = registerSoundEvent("my_guinea_pig");
    public static final SoundEvent GUINEA_PIG_DYING = registerSoundEvent("guinea_pig_dying");
    public static final SoundEvent UNI = registerSoundEvent("uni");
    //public static final SoundEvent  = registerSoundEvent("");

    /*public static final BlockSoundGroup ENCHANTMENT_MIGRATOR_SOUNDS = new BlockSoundGroup(1.0F, 1.0F,
        ModSounds.blueteeth,
        ModSounds.moow,
        ModSounds.chicken,
        ModSounds.menopause_symptoms,
        ModSounds.milk_thistle
    );*/

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(EnchantmentMigratorMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        EnchantmentMigratorMod.LOGGER.info("Registering Sounds for " + EnchantmentMigratorMod.MOD_ID);
    }

}
