package org.jukeboxmc.form

import java.util.function.Consumer

/**
 * @author GoMint
 * @version 1.0
 */
class FormListener<R> {
    var responseConsumer = Consumer { r: R -> }
    var closeConsumer = Consumer { aVoid: Void? -> }
    fun onResponse(consumer: Consumer<R>): FormListener<R> {
        responseConsumer = consumer
        return this
    }

    fun onClose(consumer: Consumer<Void?>): FormListener<R> {
        closeConsumer = consumer
        return this
    }
}
