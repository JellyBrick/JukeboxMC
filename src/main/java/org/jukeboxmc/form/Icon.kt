package org.jukeboxmc.form

class Icon(
    val data: String,
    val type: String = if (data.startsWith("http") || data.startsWith("https")) "url" else "path",
)
