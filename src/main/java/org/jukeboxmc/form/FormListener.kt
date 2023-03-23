package org.jukeboxmc.form

/**
 * @author GoMint
 * @version 1.0
 */
class FormListener<R> {
    var response = { _: R -> }
    var close = { }

    fun onResponse(consumer: (R) -> Unit): FormListener<R> {
        response = consumer
        return this
    }

    fun onClose(consumer: () -> Unit): FormListener<R> {
        close = consumer
        return this
    }
}
