package org.jukeboxmc.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import com.nukkitx.protocol.bedrock.packet.InventorySlotPacket;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.player.Player;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public abstract class ContainerInventory extends Inventory {

    public ContainerInventory( InventoryHolder holder, int size ) {
        super( holder, size );
    }

    public ContainerInventory( InventoryHolder holder, int holderId, int size ) {
        super( holder, holderId, size );
    }

    @Override
    public ContainerType getWindowTypeId() {
        return ContainerType.CONTAINER;
    }

    @Override
    public void sendContents(@NotNull Player player ) {
        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setContainerId( WindowId.OPEN_CONTAINER.getId() );
        inventoryContentPacket.setContents( this.getItemDataContents() );
        player.getPlayerConnection().sendPacket( inventoryContentPacket );
    }

    @Override
    public void sendContents(int slot, @NotNull Player player ) {
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.setContainerId( WindowId.OPEN_CONTAINER.getId() );
        inventorySlotPacket.setSlot( slot );
        inventorySlotPacket.setItem( this.getContents()[slot].toItemData() );
        player.getPlayerConnection().sendPacket( inventorySlotPacket );
    }

    public void addViewer(@NotNull Player player, @NotNull Vector position, byte windowId ) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setUniqueEntityId( this.holderId );
        containerOpenPacket.setId( windowId );
        containerOpenPacket.setType( this.getWindowTypeId() );
        containerOpenPacket.setBlockPosition( position.toVector3i() );
        player.getPlayerConnection().sendPacket( containerOpenPacket );
        super.addViewer( player );
        this.onOpen( player );
    }

    @Override
    public void removeViewer(@NotNull Player player ) {
        super.removeViewer( player );
        this.onClose( player );
    }

    public void onOpen(@NotNull Player player ) {
        this.sendContents( player );
    }

    public void onClose( Player player ) {

    }
}
