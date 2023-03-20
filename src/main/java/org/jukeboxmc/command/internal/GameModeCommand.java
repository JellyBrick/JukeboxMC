package org.jukeboxmc.command.internal;

import com.nukkitx.protocol.bedrock.data.command.CommandParamType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jukeboxmc.Server;
import org.jukeboxmc.command.Command;
import org.jukeboxmc.command.CommandData;
import org.jukeboxmc.command.CommandParameter;
import org.jukeboxmc.command.CommandSender;
import org.jukeboxmc.command.annotation.Description;
import org.jukeboxmc.command.annotation.Name;
import org.jukeboxmc.command.annotation.Permission;
import org.jukeboxmc.player.GameMode;
import org.jukeboxmc.player.Player;

import java.util.Arrays;

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name ( "gamemode" )
@Description ( "Change your gamemode" )
@Permission ( "jukeboxmc.command.gamemode" )
public class GameModeCommand extends Command {

    public GameModeCommand() {
        super( CommandData.builder()
                .addAlias( "gm" )
                .setParameters( new CommandParameter[]{
                                new CommandParameter( "gamemode", CommandParamType.INT, false ),
                                new CommandParameter( "target", CommandParamType.TARGET, true )
                        },
                        new CommandParameter[]{
                                new CommandParameter( "gamemode", Arrays.asList( "survival", "creative", "adventure", "spectator" ), false ),
                                new CommandParameter( "target", CommandParamType.TARGET, true )
                        } )
                .build() );
    }

    @Override
    public void execute( CommandSender commandSender, String command, String @NotNull [] args ) {
        if ( commandSender instanceof Player player ) {

            if ( args.length == 1 ) {
                String gamemodeName = args[0];

                if ( gamemodeName == null || gamemodeName.isEmpty() ) {
                    player.sendMessage( "§cYou must specify a gamemode" );
                    return;
                }

                GameMode gameMode = this.getGameModeByName( gamemodeName );
                if ( gameMode == null ) {
                    player.sendMessage( "§cGamemode " + gamemodeName + " not found." );
                    return;
                }

                player.setGameMode( gameMode );
                player.sendMessage( "Your game mode has been updated to " + gameMode.getIdentifier() );
            } else if ( args.length == 2 ) {
                String gamemodeName = args[0];
                String targetName = args[1];

                if ( gamemodeName == null || gamemodeName.isEmpty() ) {
                    player.sendMessage( "§cYou must specify a gamemode" );
                    return;
                }

                if ( targetName == null || targetName.isEmpty() ) {
                    player.sendMessage( "§cYou must specify a player" );
                    return;
                }

                Player target = Server.getInstance().getPlayer( targetName );
                if ( target == null ) {
                    player.sendMessage( "§cThe player " + targetName + " could not be found" );
                    return;
                }
                GameMode gameMode = this.getGameModeByName( gamemodeName );
                if ( gameMode == null ) {
                    player.sendMessage( "§cGamemode " + gamemodeName + " not found." );
                    return;
                }
                target.setGameMode( gameMode );
                target.sendMessage( "Your game mode has been updated to " + gameMode.getIdentifier() );

                if ( target == commandSender ) {
                    player.sendMessage( "Set own game mode to " + gameMode.getIdentifier() );
                } else {
                    player.sendMessage( "Set " + target.getName() + "'s game mode to " + gameMode.getIdentifier() );
                }
            } else {
                player.sendMessage( "§cUsage: /gamemode <gamemode> [target]" );
            }
        }
    }

    private @Nullable GameMode getGameModeByName(@NotNull String value ) {
        GameMode gameMode = null;
        switch ( value ) {
            case "survival", "0" -> gameMode = GameMode.SURVIVAL;
            case "creative", "1" -> gameMode = GameMode.CREATIVE;
            case "adventure", "2" -> gameMode = GameMode.ADVENTURE;
            case "spectator", "3" -> gameMode = GameMode.SPECTATOR;
            default -> {
            }
        }
        return gameMode;
    }
}
