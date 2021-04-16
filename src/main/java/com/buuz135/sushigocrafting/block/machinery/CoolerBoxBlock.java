package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.CoolerBoxTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public class CoolerBoxBlock extends RotatableBlock<CoolerBoxTile> {

    public CoolerBoxBlock() {
        super(Properties.from(Blocks.STONE), CoolerBoxTile.class);
    }

    @Override
    public IFactory<CoolerBoxTile> getTileEntityFactory() {
        return CoolerBoxTile::new;
    }

    @Override
    public Item asItem() {
        if (super.asItem() == null) setItem((BlockItem) Item.getItemFromBlock(this));
        return super.asItem();
    }

    @Override
    public TileEntityType getTileEntityType() {
        return SushiContent.TileEntities.COOLER_BOX.get();
    }


    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

}
