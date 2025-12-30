package com.example.enchantmentmigrator.block.entity.custom;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.jetbrains.annotations.Nullable;
import com.example.enchantmentmigrator.block.entity.ImplementedInventory;
import com.example.enchantmentmigrator.block.entity.ModBlockEntities;
import com.example.enchantmentmigrator.item.RazuliDustItem;
import com.example.enchantmentmigrator.screen.custom.EnchantmentMigratorScreenHandler;
import com.example.enchantmentmigrator.sound.ModSounds;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class EnchantmentMigratorBlockEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory<BlockPos> {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    //private DefaultedList<ItemStack> dropInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private static final int INPUT_SLOT = 0;
    private static final int BOOK_INPUT_SLOT = 1;
    private static final int RAZULI_INPUT_SLOT = 2;
    ItemStack outputStack = ItemStack.EMPTY;
    ItemEnchantmentsComponent inputEnchants;
    ItemEnchantmentsComponent inputMinusTopEnchants;
    private int soundCooldown = 0;
    private float rotation = Math.round(ThreadLocalRandom.current().nextFloat() * 360f); //(float)(Math.random()*360);
    private static int zRotation1 = (int)(Math.round(ThreadLocalRandom.current().nextGaussian()*120)); //(int)(Math.round(Math.random()*360));
    private static int zRotation2 = (int)(Math.round(ThreadLocalRandom.current().nextGaussian()*120)); //(int)(Math.round(Math.random()*360));
    private int ptick;

    private static final Map<Item, SoundData> ITEM_SOUNDS = Map.ofEntries(
        Map.entry(Items.IRON_BOOTS,      new SoundData(ModSounds.BLUETEETH, 5f)),
        Map.entry(Items.IRON_HOE,        new SoundData(ModSounds.MOOW, 28f)),
        Map.entry(Items.DIAMOND_SHOVEL,  new SoundData(ModSounds.MILK_THISTLE, 14f)),
        Map.entry(Items.IRON_SWORD,      new SoundData(ModSounds.CHICKEN, 38f)),
        Map.entry(Items.IRON_LEGGINGS,   new SoundData(ModSounds.MENOPAUSE_SYMPTOMS, 13f)),
        Map.entry(Items.CARVED_PUMPKIN,  new SoundData(ModSounds.ZERO_CALORIES, 16f)),
        Map.entry(Items.SHEARS,          new SoundData(ModSounds.GUINEA_PIG_DYING, 14f)),
        Map.entry(Items.STONE_SWORD,     new SoundData(ModSounds.MY_GUINEA_PIG, 11f)),
        Map.entry(Items.FISHING_ROD,     new SoundData(ModSounds.WEIGHT_LOSS, 3f)),
        Map.entry(Items.DIAMOND_HOE,     new SoundData(ModSounds.UNI, 13f))
    );

    private static final List<Map.Entry<String, Integer>> RULES = List.of(
        Map.entry("iceandfire:copper_metal", 0),
        Map.entry("iceandfire:silver_metal", 0),
        Map.entry("betternether:cincinnasite", 1),
        Map.entry("deeperdarker:resonarium", 1),
        Map.entry("aether:gravitite", 1),
        Map.entry("aether:neptune", 2),
        Map.entry("iceandfire:deathworm", 1),
        Map.entry("diamond", 1),
        Map.entry("netherite", 2),
        Map.entry("ruby", 2),
        Map.entry("betterend:aeternium", 2),
        Map.entry("iceandfire:dragonbone", 2),
        Map.entry("iceandfire:tide", 2),
        Map.entry("aether:valkyrie", 2),
        Map.entry("aether:pheonix", 2),
        Map.entry("aether:obsidian", 2),
        Map.entry("iceandfire:armor", 3),
        Map.entry("betterend:crystalite", 3),
        Map.entry("deeperdarker:warden", 3),
        Map.entry("iceandfire:dragonsteel", 4)
    );

    public EnchantmentMigratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENCHANTMENT_MIGRATOR_BE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() { return inventory; }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() { return BlockEntityUpdateS2CPacket.create(this); }

    @Override
    public NbtCompound toInitialChunkDataNbt(WrapperLookup registryLookup) { return createNbt(registryLookup); }

     @Override
    protected void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

   public void onTakeOutput(PlayerEntity player) {
    world.playSound(player, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, 0.9f);

    int particleCount = 8;
    double radius = 0.6;
    Vec3d end = Vec3d.ofCenter(pos).add(0, 0.25, 0);

    for (int i = 0; i < particleCount; i++) {
        double theta = (2 * Math.PI / particleCount) * i;

        double cx = end.x + (Math.cos(theta) * radius);
        double cz = end.z + (Math.sin(theta) * radius);
        double cy = end.y + (Math.sin(theta) * 0.05);

        spawnEnchantParticle(cx, cy, cz, 0, 0.01, 0, true);
    }
   }

    @Override
    public Text getDisplayName() { return Text.translatable("block.enchantmentmigrator.enchantment_migrator"); }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new EnchantmentMigratorScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) { return this.pos; }

    public void tick() {
    
        if (this.soundCooldown > 0) this.soundCooldown--;
        rotation = (rotation + 0.5f) % 360f;

        ItemStack inputStack = inventory.get(INPUT_SLOT);
        if (inputStack.isEmpty()) return;

        ptick--;
        if (ptick > 0) return;

        Vec3d center = Vec3d.ofCenter(pos);
        Vec3d target = center.add(0, 1, 0);
        boolean hasBook = inventory.get(BOOK_INPUT_SLOT).isOf(Items.BOOK);
        boolean hasRazuli = inventory.get(RAZULI_INPUT_SLOT).isOf(RazuliDustItem.RAZULI_DUST);

        if (inputStack.hasEnchantments() && hasBook && hasRazuli) {

            ItemStack copy = inputStack.copy();
            EnchantmentHelper.set(copy, null);
            Rarity rarity = copy.getRarity();
            String name = inputStack.toString();

            boolean t0 = false;
            boolean t1 = false;
            boolean t2 = false;
            boolean t3 = false;
            boolean t4 = false;

            int tier = 0;
            for (var rule : RULES) {
                if (name.contains(rule.getKey())) {
                tier = rule.getValue();
                }
            }

            switch (tier) {
                case 1 -> t1 = true;
                case 2 -> t2 = true;
                case 3 -> t3 = true;
                case 4 -> t4 = true;
                default -> t0 = true;
            }
            if (t0) {
                switch (rarity) {
                    case UNCOMMON -> t1 = true;
                    case RARE, EPIC -> t2 = true;
                    default -> t0 = true;
                }
            }

            if (t1 || t2 || t3 || t4) {
                ptick = 60;

                double pRotation = Math.toRadians(rotation);
                int particleCount = t4 ? 32 : t3 ? 24: t2 ? 16 : 8;
                double radius = t4 || t2 ? 1 : t3 ? 0.8 : 1.2;
                Vec3d end = center.add(0, t4 || t3 ? 0 : 1, 0);

                for (int i = 0; i < particleCount; i++) {
                    double t = t4 || t3 ? (double) i / particleCount : 0;
                    double theta = t4 || t3 ? (t * Math.PI * 2) + (pRotation * 15) : ((2 * Math.PI / particleCount) * i) + pRotation * 2.5;

                    double cx = end.x + Math.cos(theta) * radius;
                    double cz = end.z + Math.sin(theta) * radius;
                    double cy = t4 ? end.y + t * 2 :  t3 ? end.y + t * 1.5 : end.y + Math.sin(theta) * 0.05;

                    double vx = t1 ? 0 : Math.sin(theta) * 0.03;
                    double vz = t1 ? 0 : -Math.cos(theta) * 0.03;
                    double vy = t4 ? 0.12 : t3 ? 0.07 : 0;

                    spawnEnchantParticle(cx, cy, cz, vx, vy, vz, false);

                    if (t4 || t3) {
                        double cx1 = end.x + Math.cos(theta + Math.PI) * radius;
                        double cz1 = end.z + Math.sin(theta + Math.PI) * radius;

                        spawnEnchantParticle(cx1, cy, cz1, vx, vy, vz, false);
                    }
                }
                return;
            } else if (t0) {

                ptick = world.random.nextBetween(20, 50);
                double spread = 0.1;
                double vx = (world.random.nextDouble() - 0.5) * spread;
                double vz = (world.random.nextDouble() - 0.5) * spread;
                double vy = 0.05 + world.random.nextDouble() * 0.02;
                spawnEnchantParticle(target.x, target.y, target.z, vx, vy, vz, false);
                return;
            }
        } else if (inputStack.hasEnchantments()) {

            ptick = world.random.nextBetween(20, 50);
            float angle = world.random.nextFloat() * (float) (Math.PI * 2);
            float radius = 1.75f + world.random.nextFloat() * 0.5f;
            float yOffset = 1f + (world.random.nextFloat() - 0.5f);

            Vec3d start = Vec3d.of(pos).add(
                Math.cos(angle) * radius,
                yOffset,
                Math.sin(angle) * radius
            );

            Vec3d velocity = target.subtract(start).multiply(0.09);
            spawnEnchantParticle(start.x, start.y, start.z, velocity.x, velocity.y, velocity.z, false);
            return;
        }
    }


    public void spawnEnchantParticle(double sx, double sy, double sz, double vx, double vy, double vz, boolean alternateParticle) {
        ParticleEffect pType = alternateParticle ? ParticleTypes.ENCHANT : ParticleTypes.END_ROD;
        world.addParticle(pType, sx, sy, sz, vx, vy, vz);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ItemStack oldStack = inventory.get(slot);
        inventory.set(slot, stack);

        if (!ItemStack.areEqual(oldStack, stack)) {
            playBlockSound(world, pos, slot);
        }
    }

    public void playBlockSound(World world, BlockPos pos, int slot) {
        ItemStack itemStack = inventory.get(slot);

        if (soundCooldown == 0 && this.world != null && this.pos != null && !itemStack.isEmpty()) {
  
            SoundData data = ITEM_SOUNDS.getOrDefault(itemStack.getItem(), new SoundData(SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, 0.2f));

            world.playSound(null, pos, data.sound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            this.soundCooldown = (int)(data.soundDuration() * 20); 
        }
    }

    public float getRotation() {
        return rotation;
    }

    public double getMovement() {
        double movement = Math.sin(Math.toRadians(rotation * 4)) * 0.05;
        return movement;
    }

    public static int getZRotation1() {
        return zRotation1;
    }
    public static int getZRotation2() {
        return zRotation2;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world == null) return false;

        if (this.world.getBlockEntity(this.pos) != this) return false;

            return player.squaredDistanceTo(
            this.pos.getX() + 0.5,
            this.pos.getY() + 0.5,
            this.pos.getZ() + 0.5
        ) <= 64.0;
    }
}