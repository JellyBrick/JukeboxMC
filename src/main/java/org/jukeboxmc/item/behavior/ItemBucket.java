package org.jukeboxmc.item.behavior;

import com.nukkitx.protocol.bedrock.data.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.Server;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.BlockType;
import org.jukeboxmc.block.behavior.BlockLava;
import org.jukeboxmc.block.behavior.BlockLiquid;
import org.jukeboxmc.block.behavior.Waterlogable;
import org.jukeboxmc.event.player.PlayerBucketEmptyEvent;
import org.jukeboxmc.event.player.PlayerBucketFillEvent;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.item.ItemType;
import org.jukeboxmc.math.Location;
import org.jukeboxmc.player.GameMode;
import org.jukeboxmc.player.Player;
import org.jukeboxmc.util.Identifier;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class ItemBucket extends Item {

    public ItemBucket( Identifier identifier ) {
        super( identifier );
    }

    public ItemBucket( ItemType itemType ) {
        super( itemType );
    }

    @Override
    public boolean useOnBlock(@NotNull Player player, Block block, Location placeLocation ) {
        if ( !( block instanceof BlockLiquid ) && !( block.getType().equals( BlockType.POWDER_SNOW ) ) ) {
            block = player.getWorld().getBlock( block.getLocation(), 1 );
        }

        if ( block instanceof BlockLiquid || block.getType().equals( BlockType.POWDER_SNOW ) ) {
            if ( this.getType() != ItemType.BUCKET ) {
                return false;
            }

            Item item = block.getType().equals( BlockType.POWDER_SNOW ) ? Item.create( ItemType.POWDER_SNOW_BUCKET ) : block instanceof BlockLava ? Item.create( ItemType.LAVA_BUCKET ) : Item.create( ItemType.WATER_BUCKET );
            PlayerBucketFillEvent playerBucketFillEvent = new PlayerBucketFillEvent( player, this, item, block );
            Server.getInstance().getPluginManager().callEvent( playerBucketFillEvent );

            if ( playerBucketFillEvent.isCancelled() ) {
                player.getInventory().sendContents( player );
                return false;
            }
            player.getWorld().setBlock( block.getLocation(), Block.create( BlockType.AIR ), 0 );

            if ( block.getType().equals( BlockType.POWDER_SNOW )) {
                player.getWorld().playSound( player.getLocation(), SoundEvent.BUCKET_FILL_POWDER_SNOW );
            } else if ( block instanceof BlockLava ) {
                player.getWorld().playSound( player.getLocation(), SoundEvent.BUCKET_FILL_LAVA );
            } else {
                player.getWorld().playSound( player.getLocation(), SoundEvent.BUCKET_FILL_WATER );
            }

            if ( player.getGameMode() != GameMode.CREATIVE ) {
                if ( this.getAmount() - 1 <= 0 ) {
                    player.getInventory().setItemInHand( playerBucketFillEvent.getItemInHand() );
                } else {
                    Item clone = this.clone();
                    clone.setAmount( this.getAmount() - 1 );
                    player.getInventory().setItemInHand( clone );
                    if ( !player.getInventory().addItem( playerBucketFillEvent.getItemInHand() ) ) {
                        player.getWorld().dropItem( item, player.getLocation(), null );
                    }
                }
            }
        } else {
            block = block.getLocation().getWorld().getBlock( block.getLocation(), 0 );
            Block placedBlock;
            switch (this.getType()) {
                case BUCKET, MILK_BUCKET, COD_BUCKET, SALMON_BUCKET, PUFFERFISH_BUCKET, TROPICAL_FISH_BUCKET, AXOLOTL_BUCKET -> {
                    return false;
                }
                default -> {
                    placedBlock = this.toBlock();
                    if (block instanceof Waterlogable && this.getType() == ItemType.WATER_BUCKET) {
                        placedBlock.setLocation(block.getLocation());
                        placedBlock.setLayer(1);
                    } else if (block instanceof BlockLiquid) {
                        return false;
                    } else {
                        placedBlock.setLocation(placeLocation);
                    }
                }
            }

            PlayerBucketEmptyEvent playerBucketEmptyEvent = new PlayerBucketEmptyEvent( player, this,
                    Item.create( ItemType.BUCKET ), block, placedBlock );

            Server.getInstance().getPluginManager().callEvent( playerBucketEmptyEvent );

            if ( playerBucketEmptyEvent.isCancelled() ) {
                player.getInventory().sendContents( player );
                return false;
            }

            if ( placedBlock.getType().equals( BlockType.POWDER_SNOW ) ) {
                player.getWorld().playSound( player.getLocation(), SoundEvent.BUCKET_EMPTY_POWDER_SNOW );
            } else if ( placedBlock instanceof BlockLava ) {
                player.getWorld().playSound( player.getLocation(), SoundEvent.BUCKET_EMPTY_LAVA );
            } else {
                player.getWorld().playSound( player.getLocation(), SoundEvent.BUCKET_EMPTY_WATER );
            }

            player.getWorld().setBlock( placeLocation, placedBlock, placedBlock.getLayer() );
            if ( placedBlock instanceof BlockLiquid blockLiquid) {
                player.getWorld().scheduleBlockUpdate( placeLocation, blockLiquid.getTickRate() );
            }

            if ( player.getGameMode() != GameMode.CREATIVE ) {
                if ( this.getAmount() - 1 <= 0 ) {
                    player.getInventory().setItemInHand( playerBucketEmptyEvent.getItemInHand() );
                } else {
                    Item clone = this.clone();
                    clone.setAmount( this.getAmount() - 1 );
                    player.getInventory().setItemInHand( clone );
                    if ( !player.getInventory().addItem( playerBucketEmptyEvent.getItemInHand() ) ) {
                        player.getWorld().dropItem( Item.create( ItemType.BUCKET ), player.getLocation(), null );
                    }
                }
            }
        }
        return true;
    }
}
