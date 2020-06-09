package ninjaphenix.containerlib.inventory;

import net.minecraft.inventory.SidedInventory;

/**
 * @deprecated {@link ninjaphenix.containerlib.api.inventory.DoubleSidedInventory}
 */
@Deprecated
public class DoubleSidedInventory extends ninjaphenix.containerlib.api.inventory.DoubleSidedInventory
{
    public DoubleSidedInventory(SidedInventory firstInventory, SidedInventory secondInventory) { super(firstInventory, secondInventory); }
}