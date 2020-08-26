package ninjaphenix.expandedstorage.common.item;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import ninjaphenix.expandedstorage.common.Const;

import java.util.function.Function;

public enum MutatorMode
{
    MERGE(new TranslatableText("tooltip.expandedstorage.chest_mutator.merge"),
          src -> new TranslatableText("tooltip.expandedstorage.chest_mutator.merge_desc", src), 1),
    UNMERGE(new TranslatableText("tooltip.expandedstorage.chest_mutator.unmerge"),
            src -> new TranslatableText("tooltip.expandedstorage.chest_mutator.unmerge_desc", src), 2),
    ROTATE(new TranslatableText("tooltip.expandedstorage.chest_mutator.rotate"),
           src -> new TranslatableText("tooltip.expandedstorage.chest_mutator.rotate_desc", src), 0);

    public final Text title, description;
    public final byte next;

    MutatorMode(final Text title, final Function<Text, MutableText> description, final int next)
    {
        this.title = title;
        this.description = description.apply(Const.leftShiftRightClick).formatted(Formatting.GRAY);
        this.next = (byte) next;
    }
}