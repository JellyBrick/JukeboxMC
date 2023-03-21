package org.jukeboxmc.util

class Identifier(val namespace: String, val name: String, val fullName: String) {
    private val hashCode: Int

    init {
        hashCode = fullName.hashCode()
    }

    override fun equals(o: Any?): Boolean {
        return o is Identifier && o.hashCode == hashCode
    }

    override fun hashCode(): Int {
        return hashCode
    }

    override fun toString(): String {
        return fullName
    }

    companion object {
        private const val DEFAULT_NAMESPACE = "minecraft"
        private const val SEPARATOR = ':'
        private val EMPTY = Identifier("", "", SEPARATOR.toString())
        fun fromString(str: String): Identifier {
            var str = str
            if (str.isBlank()) return EMPTY
            str = str.trim { it <= ' ' }
            val nameParts = if (str.indexOf(SEPARATOR) != -1) {
                str.split(SEPARATOR.toString().toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            } else {
                arrayOf(str)
            }
            val namespace = if (nameParts.size > 1) nameParts[0] else DEFAULT_NAMESPACE
            val name = if (nameParts.size > 1) nameParts[1] else str
            val fullName = namespace + SEPARATOR + name
            return Identifier(namespace, name, fullName)
        }
    }
}
