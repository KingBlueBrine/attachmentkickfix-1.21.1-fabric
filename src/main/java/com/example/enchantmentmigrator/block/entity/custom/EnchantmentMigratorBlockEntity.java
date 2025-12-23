package com.example.enchantmentmigrator.block.entity.custom;

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
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class EnchantmentMigratorBlockEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory<BlockPos> {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
    //private DefaultedList<ItemStack> dropInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private static final int INPUT_SLOT = 0;
    private static final int BOOK_INPUT_SLOT = 1;
    private static final int RAZULI_INPUT_SLOT = 2;
    private static final int OUTPUT_SLOT = 3;
    ItemStack outputStack = ItemStack.EMPTY;
    ItemEnchantmentsComponent inputEnchants;
    ItemEnchantmentsComponent inputMinusTopEnchants;
    protected final PropertyDelegate propertyDelegate;
    private int xpCost;
    private int soundCooldown = 0;
    private float rotation = (float)(Math.random()*360);
    private static int zRotation1 = (int)(Math.round(ThreadLocalRandom.current().nextGaussian()*120)); //(int)(Math.round(Math.random()*360));
    private static int zRotation2 = (int)(Math.round(ThreadLocalRandom.current().nextGaussian()*120)); //(int)(Math.round(Math.random()*360));
    private int ptick;
    //private float velocityMult = 0.5f;

    /*private static BlockEntityType<EnchantmentMigratorBlockEntity> ENCHANTMENT_MIGRATOR_BE;
    static {
        ENCHANTMENT_MIGRATOR_BE = BlockEntityType.Builder.create(EnchantmentMigratorBlockEntity::new, ModBlocks.ENCHANTMENT_MIGRATOR_BLOCK).build(null);
    }
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(EnchantmentMigratorMod.MOD_ID, "enchantment_migrator_be"),
                    BlockEntityType.Builder.create(EnchantmentMigratorBlockEntity::new, ModBlocks.ENCHANTMENT_MIGRATOR_BLOCK).build(null);*/

    public EnchantmentMigratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENCHANTMENT_MIGRATOR_BE, pos, state);

        //EnchantmentMigratorBlockEntity.zRotation1 = (int)(Math.round(world.random.nextGaussian()*120));
        //EnchantmentMigratorBlockEntity.zRtoation2 = (int)(Math.round(world.random.nextGaussian()*120));

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> xpCost;
                    default -> 0;
                };
            }

            @Override
            public int size() {
                return 1;
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) xpCost = value;
            }
        };
    }

    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public static int getOUTPUT_SLOT() {
        return OUTPUT_SLOT;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

     @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    private void decrementStack(int slot) {
      ItemStack itemStack = inventory.get(slot);
      if (!itemStack.isEmpty()) {
         itemStack.decrement(1);
         this.setStack(slot, itemStack);
      }
   }

   public void onTakeOutput(PlayerEntity player) {
       if (canTakeOutput(player)) {

        outputStack.onCraftByPlayer(player.getWorld(), player, outputStack.getCount());

        EnchantmentHelper.set(inventory.get(INPUT_SLOT), inputMinusTopEnchants);
        decrementStack(BOOK_INPUT_SLOT);
        decrementStack(RAZULI_INPUT_SLOT);

        markDirty(); // mark block entity as changed

        // Play sound/particles at block
        //if (world != null && pos != null) {
            //world.syncWorldEvent(1044, pos, 0); // 1044 = anvil use sound
        world.playSound(player, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, 0.9f);
        //}

       }
   }

    public boolean canTakeOutput(PlayerEntity player) {
        ItemStack inputStack = inventory.get(INPUT_SLOT);
        ItemStack bookStack = inventory.get(BOOK_INPUT_SLOT);
        ItemStack razuliStack = inventory.get(RAZULI_INPUT_SLOT);
        //ItemStack outputStack = inventory.get(OUTPUT_SLOT);

        if (inputStack.isEmpty() || !inputStack.hasEnchantments() || bookStack.isEmpty() || razuliStack.isEmpty() || outputStack.isEmpty()) {
            return false;
        }
        
        if (!player.isCreative() && player.experienceLevel < xpCost) {
            return false;
        }

        return true;

        /* Give the player the output
        if (!player.getInventory().insertStack(outputStack.copy())) {
            return false; // can't give item
        }*/

        // Trigger crafting/achievement events
        
        // Consume inputs
        //inputStack.decrement(1);  // or remove enchant only
        //bookStack.decrement(1);
        //razuliStack.decrement(1);

    }

    /*public DefaultedList<ItemStack> getDropInventoryContents() {
        DefaultedList<ItemStack> dropInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
        dropInventory.add(0, inventory.get(INPUT_SLOT));
        dropInventory.add(1, inventory.get(BOOK_INPUT_SLOT));
        dropInventory.add(2, inventory.get(RAZULI_INPUT_SLOT));
        return dropInventory;
    }*/


    @Override
    public Text getDisplayName() {
        return Text.translatable("block.enchantmentmigrator.enchantment_migrator");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new EnchantmentMigratorScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    public int getXpCost() {
        return xpCost;
    }

    //protected boolean canTakeOutput(PlayerEntity player, boolean present) {
    //    return (player.isInCreativeMode() || player.experienceLevel >= xpCost) && xpCost > 0;
    //}

    //public int calculateXPcost(int level, int weight) {
    //    return (int) (level * 11 - Math.round(weight * 0.6));
    //}

    public void tick(/*World world, BlockPos pos, BlockState state*/) {
        if (this.soundCooldown > 0) {
        this.soundCooldown--;
        }
        rotation += 0.5f;
        if (rotation > 360) {
            rotation = 0f;
        }
        if (!inventory.get(INPUT_SLOT).isEmpty()) {
            ptick--;
            if (ptick <= 0) {

                ItemStack inputStack = inventory.get(INPUT_SLOT);
                Vec3d target = Vec3d.ofCenter(pos).add(0, 1, 0);

                if (inputStack.hasEnchantments()&&inventory.get(BOOK_INPUT_SLOT).isOf(Items.BOOK)&&inventory.get(RAZULI_INPUT_SLOT).isOf(RazuliDustItem.RAZULI_DUST)) {
                    
                    ItemStack inputWithoutEnchants = inputStack.copy();
                    EnchantmentHelper.set(inputWithoutEnchants, null);
                    String inputName = inputStack.toString();
                    
                    Boolean isDiamond = inputName.contains("diamond");
                    Boolean isDragon = inputName.contains("dragon");
                    if (inputName.contains("netherite") || isDragon || isDiamond || inputWithoutEnchants.getRarity() == Rarity.EPIC) { //inputStack.isOf(Items.MACE) || inputStack.isOf(Items.TRIDENT)
                        ptick = 60;  //4;

                        int particleCount = isDragon ? 12 : 8;
                        double radius = 1.2;
                        //double angularSpeed = 0.25; // radians per tick
                        double tangentialSpeed = 0.03;

                        //double cx = pos.getX() + 0.5;
                        //double cy = isDragon ? pos.getY() + 0.5 : pos.getY() + 1;
                        //double cz = pos.getZ() + 0.5;
                        Vec3d end = Vec3d.ofCenter(pos).add(0, isDragon ? 0.5 : 1, 0);

                        for (int i = 0; i < particleCount; i++) {
                            double t = isDragon ? (double) i / particleCount : 0;
                            double theta = isDragon ? (t * Math.PI * 2) + Math.toRadians(rotation) : (2 * Math.PI / particleCount) * i;

                            double cx = end.x+ (Math.cos(theta) * radius);
                            double cz = end.z+ (Math.sin(theta) * radius);
                            double cy = isDragon ? end.y + t * 1.5 : end.y+(Math.sin(theta) * 0.05);

                            double vx = isDiamond ? 0 : (Math.sin(theta) * tangentialSpeed) /*+ (world.random.nextGaussian() * 0.03)*/;
                            double vz = isDiamond ? 0 : (-Math.cos(theta) * tangentialSpeed) /*+ (world.random.nextGaussian() * 0.03)*/;
                            double vy = isDragon ? 0.04 : 0;

                            //Vec3d start = Vec3d.add(x, x, vz, z)
                            spawnEnchantParticle(cx, cy, cz, vx, vy, vz);
                        }
                        //return;
                    } else {
                        ptick = (Math.max((int)(world.random.nextInt(99)+1),20));

                        double spread = 0.1;

                        double vx = (world.random.nextDouble() - 0.5) * spread;
                        double vz = (world.random.nextDouble() - 0.5) * spread;
                        double vy = 0.05 + world.random.nextDouble() * 0.02;

                        spawnEnchantParticle(target.x, target.y, target.z, vx, vy, vz);
                        //return;
                    }
                    
                    
                } else {
                    ptick = (Math.max((int)(world.random.nextInt(99)+1),20));

                    float angle = world.random.nextFloat() * (float)(Math.PI) * 2.0f;

                    float radius = 1.75f + (world.random.nextFloat() * 0.5f);
                    float yOffset = 1.0f + (world.random.nextFloat() - 0.5f);

                    float x = (float) (Math.cos(angle) * radius);
                    float y = yOffset;
                    float z = (float) (Math.sin(angle) * radius);

                    Vec3d start = Vec3d.of(pos).add(x, y, z);

                    Vec3d velocity = 
                        target.subtract(start)
                        //.normalize()
                        .multiply(0.09 /*- world.random.nextFloat() * 0.01f*/
                    );

                    spawnEnchantParticle(start.x, start.y, start.z, velocity.x, velocity.y, velocity.z);
                    //return;
                }   
                //EnchantmentMigratorMod.LOGGER.info("ambua noises");
            }
        }
    }

    public void spawnEnchantParticle(double sx, double sy, double sz, double vx, double vy, double vz) {
        world.addParticle(ParticleTypes.END_ROD, sx, sy, sz, vx, vy, vz);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ItemStack oldStack = inventory.get(slot);
        inventory.set(slot, stack);

        // Only play sound if this is the slot we care about and item changed
        if (slot == 0 && !ItemStack.areEqual(oldStack, stack)) {
            playBlockSound(world, pos, false);
        }

        markDirty();
    }

    public void playBlockSound(World world, BlockPos pos, boolean skip) {
        double soundDuration = 0;
        ItemStack inputStack = inventory.get(INPUT_SLOT);
        SoundEvent sound = null;

        if (soundCooldown == 0 && this.world != null && this.pos != null) {
            if (skip){
                sound = SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM;
                soundDuration = 0.2;
            } else { 
                if (!inputStack.isEmpty()) {
            switch (inputStack.getItem().toString()) {
                case "minecraft:iron_boots":
                    sound = ModSounds.BLUETEETH;
                    soundDuration = 5;
                    break;
                case "minecraft:iron_hoe":
                    sound = ModSounds.MOOW;
                    soundDuration = 28;
                    break;
                case "minecraft:diamond_shovel":
                    sound = ModSounds.MILK_THISTLE;
                    soundDuration = 14;
                    break;
                case "minecraft:iron_sword":
                    sound = ModSounds.CHICKEN;
                    soundDuration = 38;
                    break;
                case "minecraft:iron_leggings":
                    sound = ModSounds.MENOPAUSE_SYMPTOMS;
                    soundDuration = 13;
                    break;
                case "minecraft:carved_pumpkin":
                    sound = ModSounds.ZERO_CALORIES;
                    soundDuration = 16;
                    break;
                case "minecraft:shears":
                    sound = ModSounds.GUINEA_PIG_DYING;
                    soundDuration = 14;
                    break;
                case "minecraft:stone_sword":
                    sound = ModSounds.MY_GUINEA_PIG;
                    soundDuration = 11;
                    break;
                case "minecraft:fishing_rod":
                    sound = ModSounds.WEIGHT_LOSS;
                    soundDuration = 3;
                    break;
                case "minecraft:diamond_hoe":
                    sound = ModSounds.UNI;
                    soundDuration = 13;
                    break;
                default:
                    sound = SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM;
                    soundDuration = 0.2;
                    break;
            }}}
        }
        if (sound != null) {
             world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
             this.soundCooldown = (int)(soundDuration*20); // convert to ticks
        }

    }

    public void updateResult(int slot) {
        ItemStack inputStack = inventory.get(INPUT_SLOT);
        ItemStack bookStack = inventory.get(BOOK_INPUT_SLOT);
        ItemStack razuliStack = inventory.get(RAZULI_INPUT_SLOT);
        //ItemStack outputStack = inventory.get(OUTPUT_SLOT);

        if (!inputStack.isEmpty() && inputStack.hasEnchantments() && bookStack.isOf(Items.BOOK) && razuliStack.isOf(RazuliDustItem.RAZULI_DUST)) {

            if (slot == 1 || slot == 2) {
                //world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS);
                playBlockSound(world, pos, true);
            }

            /*if (inputStack.isOf(Items.IRON_BOOTS)) {
                world.playSound(null, pos, ModSounds.blueteeth, SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else if (inputStack.isOf(Items.IRON_HOE)) {
                world.playSound(null, pos, ModSounds.moow, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }*/

            /*world.getServer().getScheduler().schedule(() -> soundPlaying = false, 1, TimeUnit.SECONDS);
            }*/
           //Object2IntMap.Entry<RegistryEntry<Enchantment>>

            this.inputEnchants = EnchantmentHelper.getEnchantments(inputStack); // outputs all enchants
            var firstEnchant = this.inputEnchants.getEnchantmentEntries().iterator().next(); // outputs encahant from the top of the list

            RegistryEntry<Enchantment> firstEnchantKey = firstEnchant.getKey(); //gets enchant name
            int firstEnchantLevel = firstEnchant.getIntValue(); // gets enchant level

            ItemEnchantmentsComponent.Builder inputBuilder = new ItemEnchantmentsComponent.Builder(this.inputEnchants); //makes builder will all original enchants
            inputBuilder.remove(e -> e.equals(firstEnchantKey)); // removes first enchant from original list
            //inputStack.set(DataComponentTypes.ENCHANTMENTS, inputBuilder.build()); VVV //gives input back the original enchants except first one
            //  EnchantmentHelper.set(inputStack, inputBuilder.build());
            this.inputMinusTopEnchants = inputBuilder.build();

            ItemEnchantmentsComponent.Builder outputBuilder = new ItemEnchantmentsComponent.Builder(this.inputEnchants);//.add(firstEnchantKey, firstEnchantLevel).build();
            outputBuilder.remove(e -> true);
            outputBuilder.add(firstEnchantKey, firstEnchantLevel);

            this.outputStack = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.set(outputStack, outputBuilder.build());

            //ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder((ItemEnchantmentsComponent) firstEnchant);
            //builder.add(firstEnchant.getKey(), firstEnchant.getIntValue());
            
            //outputStack.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());
            //EnchantmentHelper.apply(outputBook, (Consumer<Builder>) builder.build());

            //ItemEnchantmentsComponent.Builder itemBuilder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(inputStack));
            //itemBuilder.remove(e -> e.equals(firstEnchant.getKey()));

            //int xpCost = calculateXPcost(firstEnchant.getIntValue(), firstEnchant.getKey().value().getWeight());
            float cmult = 1;
            if (firstEnchant.getKey().isIn(EnchantmentTags.CURSE)){ cmult = 3;}
            this.xpCost = (int)(Math.round((cmult * (firstEnchant.getIntValue() * 0.5) * (11 - (firstEnchant.getKey().value().getWeight() * 0.6)))));

            markDirty();
            
        } else { 
            this.outputStack = ItemStack.EMPTY;
            this.xpCost = 0;
            //markDirty();
        }
    }

    public float getRotation() {
        return rotation;
    }

    public double getMovement() {
        double movement = Math.sin(Math.toRadians(rotation * 4)) * 0.05;
        return movement;
    }

    //public record zRotations = (int zRotation1, int zRotation2) {}
    public static int getZRotation1() {
        return zRotation1;
    }
    public static int getZRotation2() {
        return zRotation2;
    }
}