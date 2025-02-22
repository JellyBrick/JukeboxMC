package org.jukeboxmc.plugin

/**
 * @author WaterdogPE
 * @version 1.0
 */
class PluginYAML {
    var name: String? = null
    var version: String? = null
    var author: String? = null
    var main: String? = null
    var loadOrder = PluginLoadOrder.POSTWORLD
    var authors: List<String>? = null
    var depends: List<String>? = null
    fun setLoad(load: PluginLoadOrder) {
        loadOrder = load
    }
}
