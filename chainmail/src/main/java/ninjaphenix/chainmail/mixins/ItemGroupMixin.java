package ninjaphenix.chainmail.mixins;

import net.minecraft.item.ItemGroup;
import ninjaphenix.chainmail.impl.mixinhelpers.ItemGroupArrayExpander;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemGroup.class)
public class ItemGroupMixin implements ItemGroupArrayExpander
{
    @Shadow public static ItemGroup[] GROUPS;

    @Override
    public int chainmail_expandArraySize()
    {
        ItemGroup[] temp = GROUPS.clone();
        GROUPS = new ItemGroup[temp.length + 1];
        System.arraycopy(temp, 0, GROUPS, 0, temp.length);
        return temp.length;
    }
}
