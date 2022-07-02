package org.eurofurence.connavigator.util.extensions

import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.text.Layout
import android.view.Menu
import androidx.core.content.ContextCompat
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.ui.views.FontDrawable

fun Menu.setFAIcon(context: Context, menuIcon: Int, faIconId: Int, isBrand: Boolean = false, white: Boolean = false) = this.findItem(menuIcon)?.let {

    val icon = FontDrawable(context, faIconId, !isBrand, isBrand).apply {
        textSize = 22F
        textAlign = Layout.Alignment.ALIGN_CENTER
        setTextColor(
            ContextCompat.getColor(context,
            if (white) R.color.textWhite else R.color.iconColor
        ))
    }

    it.icon = InsetDrawable(icon, 32 - (icon.intrinsicWidth / 2), 32 - (icon.intrinsicHeight / 2), 0, 0)
}