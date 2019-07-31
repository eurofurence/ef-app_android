@file:Suppress("unused")

package org.eurofurence.connavigator.util.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.text.Layout
import android.view.Menu
import android.view.ViewManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import at.grabner.circleprogress.CircleProgressView
import com.github.chrisbanes.photoview.PhotoView
import com.github.lzyzsd.circleprogress.ArcProgress
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.joanzapata.iconify.widget.IconButton
import com.joanzapata.iconify.widget.IconTextView
import info.androidhive.fontawesome.FontDrawable
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.ui.views.FontAwesomeTextView
import org.eurofurence.connavigator.ui.views.MultitouchableViewPager
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.dip
import org.jetbrains.anko.wrapContent
import us.feras.mdv.MarkdownView

/**
 * Extensions to Anko view manager
 */

inline fun ViewManager.arcProgress(init: ArcProgress.() -> Unit) = ankoView({ ArcProgress(it) }, 0, init)

inline fun ViewManager.circleProgress(init: CircleProgressView.() -> Unit) = ankoView({ CircleProgressView(it, null) }, 0, init)
inline fun ViewManager.recycler(init: RecyclerView.() -> Unit) = ankoView({ RecyclerView(it) }, 0, init)
inline fun ViewManager.photoView(init: PhotoView.() -> Unit) = ankoView({ PhotoView(it) }, 0, init)
inline fun ViewManager.markdownView(init: MarkdownView.() -> Unit) = ankoView({ MarkdownView(it) }, 0, init)
inline fun ViewManager.fontAwesomeView(init: IconTextView.() -> Unit) = ankoView({ IconTextView(it) }, 0, init)
inline fun ViewManager.fontAwesomeButton(init: IconButton.() -> Unit) = ankoView({ IconButton(it) }, 0, init)
inline fun ViewManager.floatingActionButton(init: FloatingActionButton.() -> Unit) = ankoView({ FloatingActionButton(it) }, 0, init)
inline fun ViewManager.fontAwesomeTextView(init: FontAwesomeTextView.() -> Unit) = ankoView({ FontAwesomeTextView(it) }, 0, init)
inline fun ViewManager.multitouchViewPager(init: MultitouchableViewPager.() -> Unit) = ankoView({ MultitouchableViewPager(it) }, 0, init)

fun _LinearLayout.weight(weight: Float) = lparams(dip(0), wrapContent, weight)

fun Menu.setFAIcon(context: Context, menuIcon: Int, faIconId: Int, isBrand: Boolean = false, white: Boolean = false) = this.findItem(menuIcon)?.let {

    val icon = FontDrawable(context, faIconId, !isBrand, isBrand).apply {
        textSize = 22F
        textAlign = Layout.Alignment.ALIGN_CENTER
        setTextColor(ContextCompat.getColor(context,
                if (white) R.color.textWhite else R.color.iconColor
        ))
    }

    it.icon = InsetDrawable(icon, 32 - (icon.intrinsicWidth / 2), 32 - (icon.intrinsicHeight / 2), 0, 0)
}