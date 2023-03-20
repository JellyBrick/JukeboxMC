package org.jukeboxmc.inventory;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.Server;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.player.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class FakeDoubleChestInventory extends FakeChestInventory {

    public FakeDoubleChestInventory() {
        super( null, -1, 27 * 2 );
    }

    public FakeDoubleChestInventory(String customName) {
        super( null, -1, 27 * 2 );
        this.customName = customName;
    }

    @Override
    public @NotNull InventoryType getType() {
        return InventoryType.FAKE_DOUBLE_CHEST;
    }

    @Override
    public void addViewer(@NotNull Player player, @NotNull Vector position, byte windowId ) {
        this.viewer.add( player );

        if ( this.openInventory.put( player, this ) != null ){
            return;
        }

        List<Vector> blockPosition = this.onOpenChest( player );
        this.blockPositions.put( player, blockPosition );

        Server.getInstance().getScheduler().scheduleDelayed( () -> {
            Vector vector = blockPosition.get( 0 );

            ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
            containerOpenPacket.setId( windowId );
            containerOpenPacket.setUniqueEntityId( this.holderId );
            containerOpenPacket.setType( this.getWindowTypeId() );
            containerOpenPacket.setBlockPosition( vector.toVector3i() );

            player.getPlayerConnection().sendPacket( containerOpenPacket );

            this.sendContents( player );
        }, 3 );
    }

    @Override
    public @NotNull List<Vector> onOpenChest(@NotNull Player player ) {
        Vector positionA = new Vector( player.getBlockX(), player.getBlockY() + 2, player.getBlockZ() );
        Vector positionB = positionA.add( 1, 0, 0 );

        this.placeFakeChest( player, positionA );
        this.placeFakeChest( player, positionB );

        this.pair( player, positionA, positionB );
        this.pair( player, positionB, positionA );

        return Arrays.asList( positionA, positionB );
    }

    private void pair(@NotNull Player player, @NotNull Vector positionA, @NotNull Vector positionB ) {
        BlockEntityDataPacket blockEntityDataPacket = new BlockEntityDataPacket();
        blockEntityDataPacket.setBlockPosition( positionA.toVector3i() );
        blockEntityDataPacket.setData( this.toChestNBT( positionA, positionB ) );
        player.getPlayerConnection().sendPacket( blockEntityDataPacket );
    }

    private NbtMap toChestNBT(@NotNull Vector positionA, @NotNull Vector positionB ) {
        return NbtMap.builder()
                .putString( "id", "Chest" )
                .putInt( "x", positionA.getBlockX() )
                .putInt( "y", positionA.getBlockY() )
                .putInt( "z", positionA.getBlockZ() )
                .putInt( "pairx", positionB.getBlockX() )
                .putInt( "pairz", positionB.getBlockZ() )
                .putString( "CustomName", this.customName == null ? "Chest" : this.customName )
                .build();
    }

}
