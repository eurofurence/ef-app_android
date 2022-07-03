package org.eurofurence.connavigator.dropins.fa

import android.content.Context
import android.graphics.Typeface

object FaCache {
    private var fontAwesome: Typeface? = null

    fun fontAwesome(context: Context): Typeface =
        fontAwesome ?: Typeface.createFromAsset(context.assets, "fontawesome.ttf").also {
            fontAwesome = it
        }
}