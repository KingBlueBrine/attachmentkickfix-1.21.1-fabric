package com.example.enchantmentmigrator.screen.custom;

import java.util.Arrays;


//import java.util.ArrayList;
//import java.util.List;

import com.example.enchantmentmigrator.EnchantmentMigratorMod;
//import com.example.enchantmentmigrator.client.CyclingSlotIcon;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CyclingSlotIcon;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.item.ItemStack;
//import net.minecraft.screen.slot.Slot;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EnchantmentMigratorScreen extends HandledScreen<EnchantmentMigratorScreenHandler> { 

    //private static final Identifier[] GHOST_TEXTURES = new Identifier[9];
    private static String modID = EnchantmentMigratorMod.MOD_ID;
    private static final String path = "textures/gui/";
    //private static final String cyclingPath = path + "cyclingmain/";
    private static final Identifier GUI_TEXTURE = Identifier.of(modID, path + "enchantment_migrator_gui.png");
    private static final Identifier ARROW_TEXTURE = Identifier.of(modID, path + "arrow_progress.png");
    private static final Identifier NOT_ENOUGH_LEVELS = Identifier.of(modID, path + "error.png");
    
    //private static final Identifier BOOK_GHOST_TEXTURE = Identifier.of(modID, path + "empty_slot_book.png");
    //private static final Identifier RAZULI_GHOST_TEXTURE = Identifier.of(modID, path + "empty_slot_razuli_dust.png");

    /*private static final Identifier[] GHOST_TEXTURES = new Identifier[] {
        Identifier.of(modID, cyclingPath + "0.png"),
        Identifier.of(modID, cyclingPath + "1.png"),
        Identifier.of(modID, cyclingPath + "2.png"),
        Identifier.of(modID, cyclingPath + "3.png"),
        Identifier.of(modID, cyclingPath + "4.png"),
        Identifier.of(modID, cyclingPath + "5.png"),
        Identifier.of(modID, cyclingPath + "6.png"),
        Identifier.of(modID, cyclingPath + "7.png"),
        Identifier.of(modID, cyclingPath + "8.png")
    };

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
    private Identifier DISPLAY_TEXTURE = GHOST_TEXTURES[rand];*/
    private final PlayerEntity player; 

    private final CyclingSlotIcon inputSlotIcon = new CyclingSlotIcon(0);
    private static Identifier[] tempList = new Identifier[18];
    //private static final List<Identifier> INPUT_SLOT_ICONS = Arrays.asList(tempList);

    static {
        
        for (int i = 0; i < 18; i++) {
            //INPUT_SLOT_ICONS.add(Identifier.of(modID, path + "cycling/" + i + ".png"));
            tempList[i] = Identifier.of(modID, path + "cycling/" + i + ".png");
            EnchantmentMigratorMod.LOGGER.info("registering "+i+".png texture.");
            //INPUT_SLOT_ICONS.set(i, Identifier.of(modID, path + "cycling/" + i + ".png"));
        }
        //INPUT_SLOT_ICONS = List.of(tempList);

    }

    /*static { for (int i = 0; i < 9; i++) { GHOST_TEXTURES[i] = Identifier.of(EnchantmentMigratorMod.MOD_ID,"textures/gui/cycling/" + i + ".png"); }}
    private static List<Identifier> GHOST_ICONS = new ArrayList<>();
    static {
        for (int i = 0; i <= 8; i++) {
            GHOST_ICONS = List.of(Identifier.of(EnchantmentMigratorMod.MOD_ID, "textures/gui/cycling/" + i + ".png"));
            //GHOST_ICONS.add(Identifier.of(EnchantmentMigratorMod.MOD_ID, "textures/gui/cycling/" + i + ".png"));
        }
    }
    private final CyclingSlotIcon ghostIcon = new CyclingSlotIcon(0);
    
    static {
        for (int i = 0; i <= 8; i++) {
            GHOST_ICONS.add(
                Identifier.of("enchantmentmigrator", "gui/cycling/" + i));//"+.png"
        }
    }*/

    public EnchantmentMigratorScreen(EnchantmentMigratorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
       // enchantableSlotIcon.setTextures(GHOST_ICONS);
        this.titleX = 60;
        this.player = inventory.player;
    }

    //private int ghostTick = 0;      // increments every client tick
    //private int ghostIndex = 0;     // current texture index

    /*@Override
    public void handledScreenTick() {
        super.handledScreenTick();
        ghostTick++;
        EnchantmentMigratorMod.LOGGER.info("Ghost Tick: " + ghostTick + " | Ghost Index: " + ghostIndex);

        // Cycle every 100 ticks (~5s at 20 TPS)
        if (ghostTick % 100 == 0) {
            ghostIndex = (ghostIndex + 1) % GHOST_TEXTURES.length;
        }
        if (ghostTick >= 900) {
            ghostTick = 0; // prevent overflow
        }
    }*/

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();

        this.inputSlotIcon.updateTexture(Arrays.asList(tempList));

        if (handler.getSlot(0).hasStack()) {return;}

        /*tick++;

        int index = (tick / ghostDuration) % GHOST_TEXTURES.length;
        DISPLAY_TEXTURE = GHOST_TEXTURES[index];

        if (tick >= (ghostDuration*GHOST_TEXTURES.length)) {
            tick = 0; // prevent overflow
        }*/

        /*switch (tick) {
            case 0:
                DISPLAY_TEXTURE = GHOST_TEXTURES[0];
                break;
            case ghostDuration*1:
                DISPLAY_TEXTURE = GHOST_TEXTURES[1];
                break;
            case ghostDuration*2:
                DISPLAY_TEXTURE = GHOST_TEXTURES[2];
                break;
            case ghostDuration*3:
                DISPLAY_TEXTURE = GHOST_TEXTURES[3];
                break;
            case ghostDuration*4:
                DISPLAY_TEXTURE = GHOST_TEXTURES[4];
                break;
            case ghostDuration*5:
                DISPLAY_TEXTURE = GHOST_TEXTURES[5];
                break;
            case ghostDuration*6:
                DISPLAY_TEXTURE = GHOST_TEXTURES[6];
                break;
            case ghostDuration*7:
                DISPLAY_TEXTURE = GHOST_TEXTURES[7];
                break;
            case ghostDuration*8:
                DISPLAY_TEXTURE = GHOST_TEXTURES[8];
                break;
            case ghostDuration*9:
                tick = 0;
                DISPLAY_TEXTURE = GHOST_TEXTURES[0];
                break;
            default:
                break;
        }*/
        //EnchantmentMigratorMod.LOGGER.info("Ghost Tick: " + tick + " | Ghost Index: " + DISPLAY_TEXTURE);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        //RenderSystem.setShaderTexture(0, DISPLAY_TEXTURE);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        inputSlotIcon.render(this.handler, context, delta, this.x, this.y);
        EnchantmentMigratorMod.LOGGER.info("Rendering sprites at x="+this.x+" and at y="+this.y);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        //if (handler.getSlot(0).hasStack()) {
            context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        //} else {
            //context.drawTexture(DISPLAY_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        //}
        

        //context.drawText(textRenderer, "Migrate Enchantments", x, mouseY, y, cursorDragging);
        //new TextFieldWidget(this.textRenderer, x + 119, y + 24, 103, 12, Text.translatable("container.enchantment_migrate"));
        //context.drawText(textRenderer, Text.translatable("container.enchantment_migrate"), x+119, y+24, 1325400064, cursorDragging); 

        

        //if (!slot0.hasStack()) {
        //    context.drawTexture(GHOST_TEXTURES[ghostIndex], 27, 47, 0, 0, 16, 16);
        //}
        //enchantableSlotIcon.render(handler, context, this.x, this.y);

        //ghostIcon.render(this.handler, context, slot0.x, slot0.y, GHOST_ICONS);
        

    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
      super.drawForeground(context, mouseX, mouseY);

        //int colour = 0xFF5555;
        int xpCost = handler.getXpCost();
        if(xpCost > 0) {
            boolean hasEnoughLevels = handler.hasEnoughLevels(player);

            //context.drawText(textRenderer, String.valueOf(xpCost), x, y, xpCost, cursorDragging);
            int colour = hasEnoughLevels ? 8453920 : 16736352;
            Identifier DISPLAYED_ARROW_TEXURE = hasEnoughLevels ? ARROW_TEXTURE: NOT_ENOUGH_LEVELS;
            
            Object text = Text.translatable("container.enchantment_migrate.cost", new Object[]{xpCost});
            int k = this.backgroundWidth - 8 - this.textRenderer.getWidth((StringVisitable)text) - 2;

            context.drawTexture(DISPLAYED_ARROW_TEXURE, 101, 48, 0, 0, 24, 16, 24, 16);
            context.drawTextWithShadow(textRenderer, (Text)text, k, 69, colour);
            context.fill(k-2, 67, this.backgroundWidth-8, 79, 1325400064);

            /*ItemStack outputStack = handler.getOutputStack();
            if (!outputStack.isEmpty()) {
                context.drawItem(outputStack, 134, 47);
            }*/
            

        }

        /*
        Slot slot0 = handler.getSlot(0);
        Slot slot1 = handler.getSlot(1);
        Slot slot2 = handler.getSlot(2);

        if (!slot0.hasStack()) {
            context.drawTexture(DISPLAY_TEXTURE, 27, 22, 0, 0, 16, 16);
        }
        if (!slot1.hasStack()) {
            context.drawTexture(BOOK_GHOST_TEXTURE, 76, 47, 0, 0, 16, 16);
        }
        if (!slot2.hasStack()) {
            context.drawTexture(RAZULI_GHOST_TEXTURE, 76, 22, 0, 0, 16, 16);
        }*/

    }


     @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }


}
