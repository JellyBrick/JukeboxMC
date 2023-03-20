package org.jukeboxmc.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jukeboxmc.command.internal.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {
        List<Class<? extends Command>> commands = Arrays.asList(
                PluginsCommand.class,
                GameModeCommand.class,
                OperatorCommand.class,
                RemoveOperatorCommand.class,
                TeleportCommand.class,
                StopCommand.class,
                SaveCommand.class,
                KillCommand.class,
                KickCommand.class,
                EffectCommand.class,
                EnchantCommand.class,
                GameRuleCommand.class,
                StatusCommand.class,
                TimeCommand.class
        );

        for( Class<? extends Command> commandClass : commands ) {
            try {
                this.registerCommand( commandClass.getConstructor().newInstance() );
            } catch ( InstantiationException | IllegalAccessException | NoSuchMethodException |
                      InvocationTargetException e ) {
                e.printStackTrace();
            }
        }
    }

    public void handleCommandInput(@NotNull CommandSender commandSender, @NotNull String input ) {
        try {
            final String[] commandParts = input.substring( 1 ).split( " " );
            final String commandIdentifier = commandParts[0];

            int consumed = 0;
            Command targetCommand = null;
            while ( targetCommand == null ) {
                for ( Command command : this.commands ) {
                    if ( command.getCommandData().getName().equalsIgnoreCase( commandIdentifier ) || ( command.getCommandData().getAliases() != null && command.getCommandData().getAliases().contains( commandIdentifier ) ) ) {
                        targetCommand = command;
                        break;
                    }
                }

                consumed++;
                if ( targetCommand == null ) {
                    if ( commandParts.length == consumed ) {
                        break;
                    }
                }
            }

            if ( targetCommand == null ) {
                commandSender.sendMessage( "The command for " + commandIdentifier + " could not be found" );
                return;
            }

            if ( targetCommand.getCommandData().getPermission() != null && !commandSender.hasPermission( targetCommand.getCommandData().getPermission() ) ) {
                if ( targetCommand.getCommandData().getPermissionMessage() != null && !targetCommand.getCommandData().getPermissionMessage().isEmpty() ) {
                    commandSender.sendMessage( targetCommand.getCommandData().getPermissionMessage() );
                } else {
                    commandSender.sendMessage( "§cYou don't have permission to do that" );
                }
                return;
            }

            String[] params;
            if ( commandParts.length > consumed ) {
                params = new String[commandParts.length - consumed];
                System.arraycopy( commandParts, consumed, params, 0, commandParts.length - consumed );
            } else {
                params = new String[0];
            }
            targetCommand.execute( commandSender, commandIdentifier, params );
        } catch ( Throwable e ) {
            e.printStackTrace();
        }
    }

    public void registerCommand( Command command ) {
        this.commands.add( command );
    }

    public void unregisterCommand( Command command ) {
        this.commands.remove( command );
    }

    public boolean isCommandRegistered( String name ) {
        for ( Command command : this.commands ) {
            if ( command.getCommandData().getName().equalsIgnoreCase( name ) ) {
                return true;
            }
        }

        return false;
    }

    public @NotNull List<Command> getCommands() {
        return new ArrayList<>( this.commands );
    }

    private @Nullable Command getCommandByNameOrAlias(String identifier ) {
        for( Command command : this.commands ) {
            if ( command.getCommandData().getName().equalsIgnoreCase( identifier ) || ( command.getCommandData().getAliases() != null &&
                    command.getCommandData().getAliases().contains( identifier ) ) ) {
                return command;
            }
        }

        return null;
    }
}
