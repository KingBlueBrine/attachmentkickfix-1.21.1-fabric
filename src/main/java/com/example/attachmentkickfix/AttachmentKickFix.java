package com.example.attachmentkickfix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.example.attachmentkickfix.mixin.StriderDamageMultiplierMixin;

import net.fabricmc.api.ModInitializer;

public class AttachmentKickFix implements ModInitializer {
    public static final String MOD_ID = "attachmentkickfix";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {

        //((StriderDamageMultiplierMixin) null).setArrowDamage(0.12F);
        //StriderDamageMultiplierMixin.arrow_damage = 0.12f;

        LOGGER.info("Loaded Attachment Kick Fix");
    }
}