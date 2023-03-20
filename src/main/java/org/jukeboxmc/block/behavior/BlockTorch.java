package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.direction.BlockFace;
import org.jukeboxmc.block.direction.TorchFacing;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.math.AxisAlignedBB;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.player.Player;
import org.jukeboxmc.util.Identifier;
import org.jukeboxmc.world.World;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockTorch extends Block {

    public BlockTorch( Identifier identifier ) {
        super( identifier );
    }

    public BlockTorch( Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    @Override
    public boolean placeBlock(@NotNull Player player, @NotNull World world, Vector blockPosition, @NotNull Vector placePosition, Vector clickedPosition, Item itemInHand, @NotNull BlockFace blockFace ) {
        Block block = world.getBlock( blockPosition );

        if ( !block.isTransparent() && blockFace != BlockFace.DOWN ) {
            this.setTorchFacing( blockFace.opposite().torchFacing() );
            world.setBlock( placePosition, this );
            return true;
        }

        if ( !world.getBlock( placePosition.subtract( 0, 1, 0 ) ).isTransparent() ) {
            this.setTorchFacing( TorchFacing.TOP );
            world.setBlock( placePosition, this );
            return true;
        }
        return false;
    }

    @Override
    public @NotNull AxisAlignedBB getBoundingBox() {
        float size = 0.15f;
        switch (this.getTorchFacing()) {
            case EAST -> {
                return new AxisAlignedBB(
                        this.location.getX(),
                        this.location.getY() + 0.2f,
                        this.location.getZ() + 0.5f - size,
                        this.location.getX() + size * 2f,
                        this.location.getY() + 0.8f,
                        this.location.getZ() + 0.5f + size
                );
            }
            case WEST -> {
                return new AxisAlignedBB(
                        this.location.getX() + 1.0f - size * 2f,
                        this.location.getY() + 0.2f,
                        this.location.getZ() + 0.5f - size,
                        this.location.getX() + 1f,
                        this.location.getY() + 0.8f,
                        this.location.getZ() + 0.5f + size
                );
            }
            case SOUTH -> {
                return new AxisAlignedBB(
                        this.location.getX() + 0.5f - size,
                        this.location.getY() + 0.2f,
                        this.location.getZ(),
                        this.location.getX() + 0.5f + size,
                        this.location.getY() + 0.8f,
                        this.location.getZ() + size * 2f
                );
            }
            case NORTH -> {
                return new AxisAlignedBB(
                        this.location.getX() + 0.5f - size,
                        this.location.getY() + 0.2f,
                        this.location.getZ() + 1f - size * 2f,
                        this.location.getX() + 0.5f + size,
                        this.location.getY() + 0.8f,
                        this.location.getZ() + 1f
                );
            }
        }
        size = 0.1f;

        return new AxisAlignedBB(
                this.location.getX() + 0.5f - size,
                this.location.getY() + 0.0f,
                this.location.getZ() + 0.5f - size,
                this.location.getX() + 0.5f + size,
                this.location.getY() + 0.6f,
                this.location.getZ() + 0.5f + size
        );
    }

    public void setTorchFacing(@NotNull TorchFacing torchFacing ) {
        this.setState( "torch_facing_direction", torchFacing.name().toLowerCase() );
    }

    public @NotNull TorchFacing getTorchFacing() {
        return this.stateExists( "torch_facing_direction" ) ? TorchFacing.valueOf( this.getStringState( "torch_facing_direction" ) ) : TorchFacing.TOP;
    }
}
