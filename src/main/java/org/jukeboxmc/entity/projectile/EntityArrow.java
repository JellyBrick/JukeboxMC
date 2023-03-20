package org.jukeboxmc.entity.projectile;

import com.nukkitx.protocol.bedrock.data.LevelEventType;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.Server;
import org.jukeboxmc.entity.Entity;
import org.jukeboxmc.entity.EntityLiving;
import org.jukeboxmc.entity.EntityType;
import org.jukeboxmc.event.player.PlayerPickupItemEvent;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.item.ItemType;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.player.Player;
import org.jukeboxmc.util.Identifier;

import java.util.concurrent.TimeUnit;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class EntityArrow extends EntityProjectile{

    private long pickupDelay;
    private boolean canBePickedUp;
    private boolean wasInfinityArrow;

    private float force;
    private int powerModifier;
    private int punchModifier;
    private int flameModifier;

    @Override
    public void update( long currentTick ) {
        super.update( currentTick );

        if ( this.onGround || this.isCollided || this.hadCollision ) {
            this.canBePickedUp = true;
        }

        if ( this.age >= TimeUnit.MINUTES.toMillis( 5 ) / 50 ) {
            this.close();
        }
    }

    @Override
    public @NotNull String getName() {
        return "Arrow";
    }

    @Override
    public float getWidth() {
        return 0.5f;
    }

    @Override
    public float getHeight() {
        return 0.5f;
    }

    @Override
    public @NotNull EntityType getType() {
        return EntityType.ARROW;
    }

    @Override
    public Identifier getIdentifier() {
        return Identifier.fromString( "minecraft:arrow" );
    }

    @Override
    public void onCollideWithPlayer(@NotNull Player player ) {
        if ( Server.getInstance().getCurrentTick() > this.pickupDelay && this.canBePickedUp && !this.closed && !player.isDead() ) {
            Item arrow = Item.create( ItemType.ARROW );

            if ( !player.getInventory().canAddItem( arrow ) ) {
                return;
            }

            PlayerPickupItemEvent pickupItemEvent = new PlayerPickupItemEvent( player, arrow );
            player.getWorld().getServer().getPluginManager().callEvent( pickupItemEvent );
            if ( pickupItemEvent.isCancelled() ) {
                return;
            }

            this.close();
            player.getWorld().sendLevelEvent( player.getLocation(), LevelEventType.SOUND_INFINITY_ARROW_PICKUP );
            if ( !this.wasInfinityArrow ) {
                player.getInventory().addItem( arrow );
            }
            player.getInventory().sendContents( player );
        }
    }

    @Override
    protected void applyCustomKnockback(@NotNull Entity hitEntity ) {
        if ( this.punchModifier > 0 ) {
            float sqrtMotion = (float) Math.sqrt( this.velocity.getX() * this.velocity.getX() + this.velocity.getZ() * this.velocity.getZ() );
            if ( sqrtMotion > 0.0F ) {
                Vector toAdd = new Vector(
                        this.velocity.getX() * this.punchModifier * 0.6f / sqrtMotion,
                        0.1f,
                        this.velocity.getZ() * this.punchModifier * 0.6f / sqrtMotion
                );

                hitEntity.setVelocity( hitEntity.getVelocity().add( toAdd.getX(), toAdd.getY(), toAdd.getZ() ) );
            }
        }
    }

    @Override
    protected void applyCustomDamageEffects( Entity hitEntity ) {
        if ( this.flameModifier > 0 && hitEntity instanceof EntityLiving ) {
            hitEntity.setBurning( 5, TimeUnit.SECONDS );
        }
    }

    @Override
    public float getDamage() {
        if ( this.powerModifier > 0 ) {
            return 2 + ( this.powerModifier * 0.5f + 0.5f );
        }

        return 2;
    }

    public long getPickupDelay() {
        return this.pickupDelay;
    }

    public void setPickupDelay(long duration, @NotNull TimeUnit timeUnit ) {
        this.pickupDelay = Server.getInstance().getCurrentTick() + timeUnit.toMillis( duration ) / 50;
    }

    public boolean wasInfinityArrow() {
        return this.wasInfinityArrow;
    }

    public void setWasInfinityArrow( boolean wasInfinityArrow ) {
        this.wasInfinityArrow = wasInfinityArrow;
    }

    public float getForce() {
        return this.force;
    }

    public void setForce( float force ) {
        this.force = force;
    }

    public int getPowerModifier() {
        return this.powerModifier;
    }

    public void setPowerModifier( int powerModifier ) {
        this.powerModifier = powerModifier;
    }

    public int getPunchModifier() {
        return this.punchModifier;
    }

    public void setPunchModifier( int punchModifier ) {
        this.punchModifier = punchModifier;
    }

    public int getFlameModifier() {
        return this.flameModifier;
    }

    public void setFlameModifier( int flameModifier ) {
        this.flameModifier = flameModifier;
    }
}
