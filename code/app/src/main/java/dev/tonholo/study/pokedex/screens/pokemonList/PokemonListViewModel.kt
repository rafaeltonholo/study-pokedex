package dev.tonholo.study.pokedex.screens.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor() : ViewModel() {
    fun getDominantColor(drawable: Drawable): Color {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
        val colorRgb = newBitmap.getPixel(0, 0)

        newBitmap.recycle()
        bitmap.recycle()

        return Color(colorRgb)
    }
}
