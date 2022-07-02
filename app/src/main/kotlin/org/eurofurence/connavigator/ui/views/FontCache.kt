package org.eurofurence.connavigator.ui.views

import android.content.Context
import java.util.Hashtable
import android.graphics.Typeface
import org.eurofurence.connavigator.ui.views.FontCache
import java.lang.Exception

object FontCache {
    const val FA_FONT_REGULAR = "fa_regular_400.otf"
    const val FA_FONT_SOLID = "fa_solid_900.otf"
    const val FA_FONT_BRANDS = "fa_brands_400.otf"
    const val FA_OLD = "fontawesome.ttf"
    private val fontCache = Hashtable<String, Typeface?>()
    operator fun get(context: Context, name: String): Typeface? {
        var typeface = fontCache[name]
        if (typeface == null) {
            typeface = try {
                Typeface.createFromAsset(context.assets, name)
            } catch (e: Exception) {
                return null
            }
            fontCache[name] = typeface
        }
        return typeface
    }
}