package org.eurofurence.connavigator.util.v2

import android.support.design.widget.TabLayout
import android.support.v4.widget.TextViewCompat
import android.view.View
import android.view.ViewParent
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.applyRecursively
import org.jetbrains.anko.childrenSequence
import org.jetbrains.anko.displayMetrics

/**
 * Sets the compat appearance
 */
var TextView.compatAppearance: Int
    get() = error("Cannot get")
    set(value) {
        TextViewCompat.setTextAppearance(this, value)
    }


fun AnkoContext<*>.fw(double: Double) = (displayMetrics.widthPixels * double).toInt()
fun AnkoContext<*>.fh(double: Double) = (displayMetrics.heightPixels * double).toInt()
inline fun <T : View, reified U : View> T.applyRecursivelyOn(noinline f: (U) -> Unit) =
        applyRecursively {
            if (it is U)
                f(it)
        }

fun Int.percent() = this / 100.0
var TextView.minMaxWidth: Int
    get() = error("Cannot get")
    set(value) {
        minWidth = value
        maxWidth = value
    }
