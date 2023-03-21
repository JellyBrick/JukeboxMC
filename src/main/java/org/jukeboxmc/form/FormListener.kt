package org.jukeboxmc.form

import java.util.function.Consumer
import lombok.Getter

/**
 * @author GoMint
 * @version 1.0
 */
@Getter
class FormListener<R> {
    private var responseConsumer = Consumer { r: R -> }
    private var closeConsumer = Consumer { aVoid: Void? -> }
    fun onResponse(consumer: Consumer<R>): FormListener<R> {
        responseConsumer = consumer
        return this
    }

    fun onClose(consumer: Consumer<Void>): FormListener<R> {
        closeConsumer = consumer
        return this
    }
}