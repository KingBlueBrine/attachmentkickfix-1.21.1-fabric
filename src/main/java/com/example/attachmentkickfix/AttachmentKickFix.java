package com.example.attachmentkickfix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

public class AttachmentKickFix implements ModInitializer {
    public static final String MOD_ID = "attachmentkickfix";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {
        LOGGER.info("Loaded Attachment Kick Fix");
    }
}