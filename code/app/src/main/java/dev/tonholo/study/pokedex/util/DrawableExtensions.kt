package dev.tonholo.study.pokedex.util

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable

fun Drawable.toGreyscale() {
    mutate()
    val matrix = ColorMatrix().apply { setSaturation(0F) }
    val filter = ColorMatrixColorFilter(matrix)
    colorFilter = filter
}
