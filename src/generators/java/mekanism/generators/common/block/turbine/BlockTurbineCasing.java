package mekanism.generators.common.block.turbine;

import javax.annotation.Nonnull;
import mekanism.api.block.IHasTileEntity;
import mekanism.common.block.basic.BlockBasicMultiblock;
import mekanism.common.block.interfaces.IHasGui;
import mekanism.common.inventory.container.ContainerProvider;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.SecurityUtils;
import mekanism.generators.common.inventory.container.turbine.TurbineContainer;
import mekanism.generators.common.tile.GeneratorsTileEntityTypes;
import mekanism.generators.common.tile.turbine.TileEntityTurbineCasing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockTurbineCasing extends BlockBasicMultiblock implements IHasTileEntity<TileEntityTurbineCasing>, IHasGui<TileEntityTurbineCasing> {

    public BlockTurbineCasing() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(3.5F, 8F));
    }

    @Override
    @Deprecated
    public float getPlayerRelativeBlockHardness(BlockState state, @Nonnull PlayerEntity player, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
        return SecurityUtils.canAccess(player, MekanismUtils.getTileEntity(world, pos)) ? super.getPlayerRelativeBlockHardness(state, player, world, pos) : 0.0F;
    }

    @Override
    public INamedContainerProvider getProvider(TileEntityTurbineCasing tile) {
        return new ContainerProvider("mekanismgenerators.container.industrial_turbine", (i, inv, player) -> new TurbineContainer(i, inv, tile));
    }

    @Override
    public TileEntityType<TileEntityTurbineCasing> getTileType() {
        return GeneratorsTileEntityTypes.TURBINE_CASING.getTileEntityType();
    }
}