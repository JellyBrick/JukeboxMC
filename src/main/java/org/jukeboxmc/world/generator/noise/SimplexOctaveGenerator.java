package org.jukeboxmc.world.generator.noise;

import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.world.generator.noise.bukkit.NoiseGenerator;

import java.util.Arrays;
import java.util.Random;

public class SimplexOctaveGenerator extends PerlinOctaveGenerator {

    public SimplexOctaveGenerator(@NotNull Random rand, int octaves, int sizeX, int sizeZ ) {
        this( rand, octaves, sizeX, 1, sizeZ );
    }

    public SimplexOctaveGenerator(@NotNull Random rand, int octaves, int sizeX, int sizeY, int sizeZ ) {
        super( createOctaves( rand, octaves ), rand, sizeX, sizeY, sizeZ );
    }

    public SimplexOctaveGenerator(@NotNull Random rand, int octaves ) {
        this( rand, octaves, 0, 0, 0 );
    }

    protected static NoiseGenerator @NotNull [] createOctaves(@NotNull Random rand, int octaves ) {
        NoiseGenerator[] result = new NoiseGenerator[octaves];

        for ( int i = 0; i < octaves; i++ ) {
            result[i] = new SimplexNoise( rand );
        }

        return result;
    }

    @Override
    public double[] getFractalBrownianMotion( double x, double y, double z, double lacunarity, double persistence ) {
        Arrays.fill( noise, 0 );

        double freq = 1;
        double amp = 1;

        // fBm
        for ( NoiseGenerator octave : octaves ) {
            noise = ( (PerlinNoise) octave ).getNoise( noise, x, y, z, sizeX, sizeY, sizeZ, xScale * freq, yScale * freq, zScale * freq, 0.55D / amp );
            freq *= lacunarity;
            amp *= persistence;
        }

        return noise;
    }
}
