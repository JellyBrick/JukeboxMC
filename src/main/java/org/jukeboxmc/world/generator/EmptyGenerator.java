package org.jukeboxmc.world.generator;

import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.world.World;
import org.jukeboxmc.world.chunk.Chunk;
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class EmptyGenerator extends Generator {

    public EmptyGenerator( World world ) {

    }

    @Override
    public void generate( Chunk chunk, int chunkX, int chunkZ ) {

    }

    @Override
    public void populate( PopulationChunkManager manager, int chunkX, int chunkZr ) {

    }

    @Override
    public void finish( PopulationChunkManager manager, int chunkX, int chunkZ ) {

    }

    @Override
    public @NotNull Vector getSpawnLocation() {
        return new Vector( 0, 64, 0 );
    }
}
