package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.RollerTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RollerBlock extends BasicTileBlock<RollerTile> {

    public RollerBlock() {
        super(Properties.from(Blocks.OAK_TRAPDOOR), RollerTile.class);
    }

    @Override
    public IFactory<RollerTile> getTileEntityFactory() {
        return RollerTile::new;
    }

    @Override
    public Item asItem() {
        if (super.asItem() == null) setItem((BlockItem) Item.getItemFromBlock(this));
        return super.asItem();
    }

    @Override
    public TileEntityType getTileEntityType() {
        return SushiContent.TileEntities.ROLLER.get();
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        this.getTile(worldIn, pos).ifPresent(rollerTile -> rollerTile.onClick(player));
    }
}
