package ninjaphenix.expandedstorage.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public abstract class StorageBlockEntity extends LootableContainerBlockEntity implements SidedInventory
{
    protected Text defaultContainerName;
    protected int inventorySize;
    protected DefaultedList<ItemStack> inventory;
    protected int[] SLOTS;
    protected Identifier block;

    protected StorageBlockEntity(final BlockEntityType<?> blockEntityType, final Identifier block)
    {
        super(blockEntityType);
        if (block != null) { initialize(block); }
    }

    protected abstract void initialize(final Identifier block);

    public Identifier getBlock() { return block; }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() { return inventory; }

    @Override
    public void setInvStackList(final DefaultedList<ItemStack> inventory) { this.inventory = inventory; }

    @Override
    protected ScreenHandler createScreenHandler(final int i, final PlayerInventory playerInventory) { return null; }

    @Override
    public int[] getAvailableSlots(final Direction side) { return SLOTS; }

    @Override
    public boolean canInsert(final int slot, final ItemStack stack, final Direction direction) { return isValid(slot, stack); }

    @Override
    public boolean canExtract(final int slot, final ItemStack stack, final Direction direction) { return true; }

    @Override
    public int size() { return inventorySize; }

    @Override
    protected Text getContainerName() { return defaultContainerName; }

    @Override
    public boolean isEmpty() { return inventory.stream().allMatch(ItemStack::isEmpty); }

    @Override
    public void fromTag(final BlockState state, final CompoundTag tag)
    {
        super.fromTag(state, tag);
        initialize(new Identifier(tag.getString("type")));
        if (!deserializeLootTable(tag)) { Inventories.fromTag(tag, inventory); }
    }

    @Override
    public CompoundTag toTag(final CompoundTag tag)
    {
        super.toTag(tag);
        tag.putString("type", block.toString());
        if (!serializeLootTable(tag)) { Inventories.toTag(tag, inventory); }
        return tag;
    }

    @Override
    public CompoundTag toInitialChunkDataTag()
    {
        final CompoundTag initialChunkTag = super.toTag(new CompoundTag());
        initialChunkTag.putString("type", block.toString());
        return initialChunkTag;
    }
}