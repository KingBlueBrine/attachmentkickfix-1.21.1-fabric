package com.example.enchantmentmigrator.screen.custom;

import com.example.enchantmentmigrator.EnchantmentMigratorMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EnchantmentMigratorScreen extends HandledScreen<EnchantmentMigratorScreenHandler> { 

    private static String modID = EnchantmentMigratorMod.MOD_ID;
    private static final String path = "textures/gui/";
    private static final Identifier GUI_TEXTURE = Identifier.of(modID, path + "enchantment_migrator_gui.png");
    private static final Identifier ARROW_TEXTURE = Identifier.of(modID, path + "arrow_progress.png");
    private static final Identifier NOT_ENOUGH_LEVELS = Identifier.of(modID, path + "error.png");
    private static final int GHOST_FRAME_COUNT = 18;
    private static final Identifier[] GHOST_TEXTURES = new Identifier[GHOST_FRAME_COUNT];
    static {
        for (int i = 0; i < GHOST_FRAME_COUNT; i++) {
            GHOST_TEXTURES[i] = Identifier.of(modID, path + "cyclingmain/" + i + ".png");
        }
    }
    private final int ghostDuration = 40;
    private int rand = (int) (Math.floor(Math.random() * GHOST_FRAME_COUNT));
    private int tick = ghostDuration * rand;
    private Identifier DISPLAY_TEXTURE = GHOST_TEXTURES[rand];
    private final PlayerEntity player; 

    public EnchantmentMigratorScreen(EnchantmentMigratorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        //this.titleX = 60;
        this.player = inventory.player;
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();

        if (handler.getSlot(0).hasStack()) {return;}

        tick++;

        int index = (tick / ghostDuration) % GHOST_TEXTURES.length;
        DISPLAY_TEXTURE = GHOST_TEXTURES[index];

        if (tick >= (ghostDuration*GHOST_TEXTURES.length)) {
            tick = 0;
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        if (handler.getSlot(0).hasStack()) {
            context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        } else {
            context.drawTexture(DISPLAY_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        }
    }

    

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
      super.drawForeground(context, mouseX, mouseY);


		context.drawText(this.textRenderer, this.title, this.titleX + 60, this.titleY, 11053224, false);
		context.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 11053224, false);

        int xpCost = handler.getXpCost();
        if(xpCost > 0) {
            boolean hasEnoughLevels = handler.hasEnoughLevels(player);

            int colour = hasEnoughLevels ? 8453920 : 16736352;
            Identifier DISPLAYED_ARROW_TEXURE = hasEnoughLevels ? ARROW_TEXTURE: NOT_ENOUGH_LEVELS;
            
            Object text = Text.translatable("container.enchantment_migrate.cost", new Object[]{xpCost});
            int k = this.backgroundWidth - 8 - this.textRenderer.getWidth((StringVisitable)text) - 2;

            context.drawTexture(DISPLAYED_ARROW_TEXURE, 101, 48, 0, 0, 24, 16, 24, 16);
            context.drawTextWithShadow(textRenderer, (Text)text, k, 69, colour);
            context.fill(k-2, 67, this.backgroundWidth-8, 79, 1325400064);  
        }
    }


     @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }


}
