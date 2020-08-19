package ninjaphenix.expandedstorage.impl.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

@FunctionalInterface
public interface ServerScreenHandlerFactory<T extends AbstractScreenHandler<?>>
{
    T create(final int windowId, final BlockPos pos, final Inventory inventory, final PlayerEntity player, final Text displayName);
}