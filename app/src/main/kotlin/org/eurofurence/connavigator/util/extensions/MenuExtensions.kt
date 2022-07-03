package org.eurofurence.connavigator.util.extensions

import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.text.Layout
import android.view.Menu
import androidx.core.content.ContextCompat
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.dropins.fa.Fa
import org.eurofurence.connavigator.dropins.fa.FaDrawable

fun Menu.setFAIcon(context: Context, menuIcon: Int, icon: String, white: Boolean = false) =
    this.findItem(menuIcon)?.let {
        val drawable = FaDrawable(context).apply {
            textSize = 22F
            textAlign = Layout.Alignment.ALIGN_CENTER
            setTextColor(
                ContextCompat.getColor(context, if (white) R.color.textWhite else R.color.iconColor)
            )
            text = icon
        }

        it.icon = InsetDrawable(
            drawable,
            32 - (drawable.intrinsicWidth / 2),
            32 - (drawable.intrinsicHeight / 2),
            0,
            0
        )
    }