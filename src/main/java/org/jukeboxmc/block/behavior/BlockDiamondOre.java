package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.item.ItemType;
import org.jukeboxmc.item.enchantment.Enchantment;
import org.jukeboxmc.item.enchantment.EnchantmentType;
import org.jukeboxmc.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockDiamondOre extends Block {

    public BlockDiamondOre( Identifier identifier ) {
        super( identifier );
    }

    public BlockDiamondOre( Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    @Override
    public @NotNull List<Item> getDrops(@Nullable Item item ) {
        if ( item != null && this.isCorrectToolType( item ) && this.isCorrectTierType( item ) ) {
            int amount = 1;
            Enchantment enchantment = item.getEnchantment( EnchantmentType.FORTUNE );
            if ( enchantment != null ) {
                amount += ThreadLocalRandom.current().nextInt(0, enchantment.getLevel() + 1 );
            }
            return Collections.singletonList( Item.create( ItemType.DIAMOND ).setAmount( amount ) );
        }
        return Collections.emptyList();
    }
}
