package org.jukeboxmc.entity.item;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import com.nukkitx.protocol.bedrock.packet.AddItemEntityPacket;
import com.nukkitx.protocol.bedrock.packet.TakeItemEntityPacket;
import org.jukeboxmc.Server;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.entity.Entity;
import org.jukeboxmc.entity.EntityType;
import org.jukeboxmc.event.player.PlayerPickupItemEvent;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.item.ItemDiamondSword;
import org.jukeboxmc.item.ItemType;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.player.Player;

import java.util.concurrent.TimeUnit;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class EntityItem extends Entity {

    private Item item;
    private long pickupDelay;
    private boolean isReset;
    private Player playerHasThrown;

    @Override
    public void update( long currentTick ) {
        super.update( currentTick );

        if ( this.isClosed() ) {
            return;
        }
        if ( !this.isImmobile() ) {
            if ( !this.isOnGround() ) {
                this.velocity = this.velocity.subtract( 0, this.getGravity(), 0 );
            }

            this.checkObstruction( this.location.getX(), this.location.getY(), this.location.getZ() );
            this.move( this.velocity );

            float friction = 1 - this.getDrag();

            if ( this.onGround && ( Math.abs( this.velocity.getX() ) > 0.00001 || Math.abs( this.velocity.getZ() ) > 0.00001 ) ) {
                friction *= 0.6f;
            }

            this.velocity = this.velocity.multiply( friction, 1 - this.getDrag(), friction );

            if ( this.onGround ) {
                this.velocity = this.velocity.multiply( 0, -0.5f, 0 );
            }

            this.updateAbsoluteMovement();
        }

        if ( this.isCollided && !this.isReset && this.velocity.squaredLength() < 0.01f ) {
            this.setVelocity( Vector.zero() );
            this.isReset = true;
        }

        if ( this.age >= TimeUnit.MINUTES.toMillis( 5 ) / 50 ) {
            this.close();
        }
    }

    @Override
    public String getName() {
        return "EntityItem";
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getHeight() {
        return 0.25f;
    }

    @Override
    public float getGravity() {
        return 0.04f;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ITEM;
    }

    @Override
    public void saveEntity( NbtMapBuilder builder ) {
        super.saveEntity( builder );
        NbtMapBuilder itemBuilder = NbtMap.builder();
        itemBuilder.putString( "Name", this.getItem().getIdentifier() );
        itemBuilder.putShort( "Damage", (short) this.getItem().getMeta() );
        itemBuilder.putByte( "Count", (byte) this.getItem().getAmount() );
        builder.putCompound( "Item", itemBuilder.build() );

       if ( !this.item.getBlock().getBlockStates().isEmpty() ) {
           NbtMap blockStates = this.item.getBlock().getBlockStates();
           builder.putCompound( "Block", blockStates );
       }
    }

    @Override
    public void loadEntity( NbtMap compound ) {
        super.loadEntity( compound );

        NbtMap itemCompound = compound.getCompound( "Item" );
        String identifier = itemCompound.getString( "Name" );
        short damage = itemCompound.getShort( "Damage" );
        byte amount = itemCompound.getByte( "Count" );
        this.item = ItemType.get( identifier ).setMeta( damage ).setAmount( amount ).setNBT( itemCompound );
        this.setPickupDelay( 1, TimeUnit.SECONDS );
        this.setPlayerHasThrown( null );

        if ( compound.containsKey( "Block" ) ) {
            NbtMap blockCompound = compound.getCompound( "Block" );
            int blockRuntimeId = Block.STATES.get( identifier ).get( blockCompound );
            this.item.setBlockRuntimeId( blockRuntimeId );
        }
    }

    @Override
    public AddItemEntityPacket createSpawnPacket() {
        AddItemEntityPacket addItemEntityPacket = new AddItemEntityPacket();
        addItemEntityPacket.setRuntimeEntityId( this.entityId );
        addItemEntityPacket.setUniqueEntityId( this.entityId );
        addItemEntityPacket.setItemInHand( this.item.toNetwork() );
        addItemEntityPacket.setPosition( this.location.toVector3f() );
        addItemEntityPacket.setMotion( this.velocity.toVector3f() );
        addItemEntityPacket.getMetadata().putAll( this.metadata.getEntityDataMap() );
        return addItemEntityPacket;
    }

    @Override
    public void onCollideWithPlayer( Player player ) {
        if ( this.item == null ) return;
        if ( Server.getInstance().getCurrentTick() > this.pickupDelay && !this.isClosed() && !player.isDead() ) {
            PlayerPickupItemEvent playerPickupItemEvent = new PlayerPickupItemEvent( player, this.item );
            Server.getInstance().getPluginManager().callEvent( playerPickupItemEvent );
            if ( playerPickupItemEvent.isCancelled() || !player.getInventory().canAddItem( this.item )) {
                return;
            }

            TakeItemEntityPacket takeItemEntityPacket = new TakeItemEntityPacket();
            takeItemEntityPacket.setRuntimeEntityId( player.getEntityId() );
            takeItemEntityPacket.setItemRuntimeEntityId( this.entityId );
            Server.getInstance().broadcastPacket( takeItemEntityPacket );
            this.getChunk().setChanged( true );
            this.close();
            player.getInventory().addItem( this.item );
            player.getInventory().sendContents( player );
        }
    }

    @Override
    public boolean canCollideWith( Entity entity ) {
        return false;
    }

    public Item getItem() {
        return this.item.clone();
    }

    public void setItem( Item item ) {
        this.item = item;
    }

    public long getPickupDelay() {
        return this.pickupDelay;
    }

    public void setPickupDelay( long duration, TimeUnit timeUnit ) {
        this.pickupDelay = Server.getInstance().getCurrentTick() + timeUnit.toMillis( duration ) / 50;
    }

    public Player getPlayerHasThrown() {
        return this.playerHasThrown;
    }

    public void setPlayerHasThrown( Player playerHasThrown ) {
        this.playerHasThrown = playerHasThrown;
    }

}
