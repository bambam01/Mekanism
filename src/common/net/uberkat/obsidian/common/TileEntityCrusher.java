package net.uberkat.obsidian.common;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import net.minecraft.src.*;

public class TileEntityCrusher extends TileEntityMachine
{
	public TileEntityCrusher()
	{
		super(200, "Crusher");
	}
    
    public void onUpdate()
    {
        boolean var1 = machineBurnTime > 0;
        boolean var2 = false;

        if (machineBurnTime > 0)
        {
            --machineBurnTime;
        }

        if (!worldObj.isRemote)
        {
            if (machineBurnTime == 0 && canSmelt())
            {
                currentItemBurnTime = machineBurnTime = getItemBurnTime(machineItemStacks[1]);

                if (machineBurnTime > 0)
                {
                    var2 = true;

                    if (machineItemStacks[1] != null)
                    {
                        --machineItemStacks[1].stackSize;

                        if (machineItemStacks[1].stackSize == 0)
                        {
                            machineItemStacks[1] = null;
                        }
                    }
                }
            }

            if (isBurning() && canSmelt())
            {
                ++machineCookTime;

                if (machineCookTime == maxBurnTime)
                {
                    machineCookTime = 0;
                    smeltItem();
                    var2 = true;
                }
            }
            else
            {
                machineCookTime = 0;
            }

            if (var1 != machineBurnTime > 0)
            {
                var2 = true;
                setActive(isBurning());
            }
        }

        if (var2)
        {
            onInventoryChanged();
        }
    }

    public boolean canSmelt()
    {
        if (machineItemStacks[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack var1 = CrusherRecipes.smelting().getSmeltingResult(machineItemStacks[0]);
            if (var1 == null) return false;
            if (machineItemStacks[2] == null) return true;
            if (!machineItemStacks[2].isItemEqual(var1)) return false;
            int result = machineItemStacks[2].stackSize + var1.stackSize;
            return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
        }
    }

    public void smeltItem()
    {
        if (canSmelt())
        {
            ItemStack var1 = CrusherRecipes.smelting().getSmeltingResult(machineItemStacks[0]);

            if (machineItemStacks[2] == null)
            {
                machineItemStacks[2] = var1.copy();
            }
            else if (machineItemStacks[2].isItemEqual(var1))
            {
                machineItemStacks[2].stackSize += var1.stackSize;
            }

            --machineItemStacks[0].stackSize;

            if (machineItemStacks[0].stackSize <= 0)
            {
                machineItemStacks[0] = null;
            }
        }
    }

    public static int getItemBurnTime(ItemStack par1ItemStack)
    {
        if (par1ItemStack == null)
        {
            return 0;
        }
        else
        {
            int var1 = par1ItemStack.getItem().shiftedIndex;
            if (var1 == Item.redstone.shiftedIndex) return 600;
            if (var1 == ObsidianIngots.RedstoneIngot.shiftedIndex)
            if (par1ItemStack.getItem() instanceof ItemBlock && var1 == ObsidianIngots.RedstoneBlock.blockID) return 5400;
        }
        return 0;
    }
    
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        machineItemStacks = new ItemStack[getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < machineItemStacks.length)
            {
                machineItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        machineBurnTime = par1NBTTagCompound.getShort("BurnTime");
        machineCookTime = par1NBTTagCompound.getShort("CookTime");
        currentItemBurnTime = getItemBurnTime(machineItemStacks[1]);
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("BurnTime", (short)machineBurnTime);
        par1NBTTagCompound.setShort("CookTime", (short)machineCookTime);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < machineItemStacks.length; ++var3)
        {
            if (machineItemStacks[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                machineItemStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", var2);
    }

    public static boolean isItemFuel(ItemStack par0ItemStack)
    {
        return getItemBurnTime(par0ItemStack) > 0;
    }
}
