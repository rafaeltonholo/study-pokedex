package dev.tonholo.study.pokedex.util

import java.util.*

fun String.toTitleCase() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
}
