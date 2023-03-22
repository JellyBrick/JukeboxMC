package org.jukeboxmc.event.block

import org.jukeboxmc.block.Block
import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockSignChangeEvent
/**
 * Creates a new [BlockSignChangeEvent]
 *
 * @param block  which represents the sign block
 * @param player who changed the content of the sign
 * @param lines  which is the text content of the sign
 */(
    block: Block,
    /**
     * Retrieves the player who changed the content of the sign
     *
     * @return a fresh [Player]
     */
    val player: Player,
    private var lines: MutableList<String>,
) : BlockEvent(block), Cancellable {
    /**
     * Retrieves all lines of the sign
     *
     * @return a fresh [<]
     */
    fun getLines(): List<String> {
        return lines
    }

    /**
     * Modifies lines of the sign
     *
     * @param lines which should be modified
     */
    fun setLines(lines: MutableList<String>) {
        this.lines = lines
    }

    /**
     * Modifies the given line of the sign
     *
     * @param line which should be updated
     * @param text which represents the new content
     */
    fun setLine(line: Int, text: String) {
        if (line > 4 || line < 1) {
            return
        }
        if (lines.size < line) {
            for (i in 0 until line - lines.size) {
                lines.add("")
            }
        }
        lines[line - 1] = text
    }
}
