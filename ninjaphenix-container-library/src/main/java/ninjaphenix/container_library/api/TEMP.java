package ninjaphenix.container_library.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjaphenix.container_library.impl.client.ContainerLibraryClient;
import ninjaphenix.container_library.impl.client.screen.SelectContainerScreen;
import ninjaphenix.container_library.impl.common.Const;
import ninjaphenix.container_library.impl.common.Networking;
import java.util.Map;

import static ninjaphenix.container_library.api.common.inventory.AbstractScreenHandler.*;

public class TEMP
{
    private Map<Identifier, Pair<Identifier, Text>> supportedContainerTypes;
    private Map<Identifier, Map<Integer, ScreenMeta>> screenSizes;

    /**
     * Block at the specified pos must implement {@link net.minecraft.block.InventoryProvider} and
     * @param pos
     */
    public void openContainer(final BlockState state, final World world, final BlockPos pos)
    {
        final Identifier preference = ContainerLibraryClient.CONFIG.preferred_container_type;
        if(Const.id("auto").equals(preference) || !supportedContainerTypes.containsKey(preference))
        {
            MinecraftClient.getInstance().openScreen(new SelectContainerScreen(supportedContainerTypes, id -> openContainer(state, world, pos, id)));
        }
        openContainer(state, world, pos, preference);
    }

    private void openContainer(final BlockState state, final World world, final BlockPos pos, final Identifier preference)
    {
        final Block block = world.getBlockState(pos).getBlock();
        if(block instanceof InventoryProvider) {
            final int inventorySize = ((InventoryProvider) block).getInventory(state, world, pos).size();
            final ScreenMeta meta = getNearestValue(screenSizes.get(preference), inventorySize);
            if(meta != null) {
                Networking.INSTANCE.requestContainerOpen(pos, preference, meta);
            }
        }
    }

    private <K, V> V getNearestValue(Map<K, V> map, K searchKey) {
        if(map.containsKey(searchKey)) { return map.get(searchKey); }
        return null; // todo implement
    }
}