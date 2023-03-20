package org.jukeboxmc.util;

import com.nukkitx.nbt.NBTInputStream;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtUtils;
import org.jetbrains.annotations.Nullable;
import org.jukeboxmc.Bootstrap;

import java.io.InputStream;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BiomeDefinitions {

    private static @Nullable NbtMap BIOME_DEFINITIONS = null;

    public static void init() {
        try ( InputStream inputStream = Bootstrap.class.getClassLoader().getResourceAsStream("biome_definitions.dat")){
            if(inputStream == null) {
                throw new AssertionError("Could not find biome_definitions.dat");
            }
            try(final NBTInputStream stream = NbtUtils.createNetworkReader(inputStream)) {
                BIOME_DEFINITIONS = (NbtMap) stream.readTag();
            } catch(Exception e) {
                throw new AssertionError("Error whilst loading biome_definitions.dat", e);
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public static NbtMap getBiomeDefinitions() {
        return BIOME_DEFINITIONS;
    }
}
