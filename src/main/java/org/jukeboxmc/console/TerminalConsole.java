package org.jukeboxmc.console;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.jetbrains.annotations.NotNull;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jukeboxmc.Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class TerminalConsole extends SimpleTerminalConsole {

    private final Server server;
    private final @NotNull ExecutorService executor;

    public TerminalConsole( Server server ) {
        this.server = server;
        this.executor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat( "JukeboxMC Terminal Console" ).build());
    }

    @Override
    protected void runCommand(@NotNull String command ) {
        if(command.equals( "checkexception" )) {
            Exception exception = new Exception();
            exception.setStackTrace( this.server.getMainThread().getStackTrace() );
            exception.printStackTrace(System.out);
        } else if ( command.equalsIgnoreCase( "test" ) ) {
            server.getOnlinePlayers().forEach( player -> player.sendMessage( "Hallo das ist ein Test!" ) );
        }
        if ( this.isRunning() ) {
            this.server.getScheduler().execute( () -> this.server.dispatchCommand( this.server.getConsoleSender(), command ));
        }
    }

    @Override
    protected void shutdown() {
        this.executor.shutdownNow();
    }

    @Override
    protected boolean isRunning() {
        return this.server.getRunningState().get();
    }

    @Override
    protected LineReader buildReader(@NotNull LineReaderBuilder builder ) {
        builder.completer( new CommandCompleter( this.server ) );
        builder.appName( "JukeboxMC" );
        builder.option( LineReader.Option.HISTORY_BEEP, false );
        builder.option( LineReader.Option.HISTORY_IGNORE_DUPS, true );
        builder.option( LineReader.Option.HISTORY_IGNORE_SPACE, true );
        return super.buildReader( builder );
    }

    @Override
    public void start() {
        super.start();
    }

    public void startConsole() {
        this.executor.execute( this::start );
    }

    public void stopConsole() {
        this.executor.shutdownNow();
    }
}
