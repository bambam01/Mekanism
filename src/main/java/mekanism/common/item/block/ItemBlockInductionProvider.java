package mekanism.common.item.block;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.EnumColor;
import mekanism.common.block.basic.BlockInductionProvider;
import mekanism.common.item.ITieredItem;
import mekanism.common.tier.InductionProviderTier;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockInductionProvider extends ItemBlockTooltip implements ITieredItem<InductionProviderTier> {

    public ItemBlockInductionProvider(BlockInductionProvider block) {
        super(block);
    }

    @Nullable
    @Override
    public InductionProviderTier getTier(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemBlockInductionProvider) {
            return ((BlockInductionProvider) ((ItemBlockInductionProvider) item).block).getTier();
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addStats(@Nonnull ItemStack itemstack, World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag) {
        InductionProviderTier tier = getTier(itemstack);
        if (tier != null) {
            list.add(tier.getBaseTier().getColor() + LangUtils.localize("tooltip.outputRate") + ": " + EnumColor.GREY + MekanismUtils.getEnergyDisplay(tier.getOutput()));
        }
    }
}