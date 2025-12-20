package com.example.enchantmentmigrator.client;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CyclingSlotIcon {
    private final int slotIndex;
    private int frame = 0;
    private int tickCounter = 0;

    public CyclingSlotIcon(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    public void render(ScreenHandler handler, DrawContext context, int x, int y, List<Identifier> textures) {
        if (!handler.getSlot(slotIndex).hasStack() && !textures.isEmpty()) {

            tickCounter++;
            if (tickCounter % 100 == 0) { // 5 second per frame
                frame = (frame + 1) % textures.size();
            }

            context.drawGuiTexture(
                textures.get(frame),
                x + handler.getSlot(slotIndex).x,
                y + handler.getSlot(slotIndex).y,
                16,
                16
            );
        }
    }
}

