package ninjaphenix.containerlib.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.containerlib.impl.ContainerLibraryImpl;
import ninjaphenix.containerlib.impl.client.ScreenMiscSettings;
import org.jetbrains.annotations.ApiStatus;

public interface ContainerLibraryAPI
{
    ContainerLibraryAPI INSTANCE = ContainerLibraryImpl.INSTANCE;

    /**
     * Open's a modded container which block implements {@link net.minecraft.block.InventoryProvider}.
     *
     * @since 1.0.0
     */
    void openContainer(final PlayerEntity player, BlockPos pos, Text containerName);

    /**
     * Declares that your container type can be used users. Don't forget to also register it and the screen provider using fabric-api.
     *
     * @param containerTypeId The container factory id registered with fabric-api.
     * @param selectTextureId The texture to use on container selection screen ( client only ).
     * @param narrationMessage The narration message for when this container type is selected ( client only ).
     * @return False if the container factory id has already been registered.
     * @since 1.0.0
     */
    boolean declareContainerType(final Identifier containerTypeId, final Identifier selectTextureId, final Text narrationMessage);

    @ApiStatus.Internal
    void setPlayerPreference(final PlayerEntity player, final Identifier type);

    @ApiStatus.Internal
    @Environment(EnvType.CLIENT)
    ScreenMiscSettings getScreenSettings(final Identifier containerTypeId);
}
