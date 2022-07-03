package org.eurofurence.connavigator.dropins

import android.app.Activity
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.floatingactionbutton.FloatingActionButton

typealias ViewGroupInit = ViewGroup.LayoutParams.() -> Unit
typealias FrameLayoutInit = FrameLayout.LayoutParams.() -> Unit
typealias LinearLayoutInit = LinearLayout.LayoutParams.() -> Unit
typealias DrawerLayoutInit = DrawerLayout.LayoutParams.() -> Unit
typealias MarginLayoutInit = ViewGroup.MarginLayoutParams.() -> Unit
typealias RelativeLayoutInit = RelativeLayout.LayoutParams.() -> Unit
typealias CoordinatorLayoutInit = CoordinatorLayout.LayoutParams.() -> Unit

val matchParent get() = ViewGroup.LayoutParams.MATCH_PARENT
val wrapContent get() = ViewGroup.LayoutParams.WRAP_CONTENT

fun viewGroupLayoutParams(w: Int, h: Int) =
    ViewGroup.LayoutParams(w, h)

inline fun viewGroupLayoutParams(w: Int, h: Int, crossinline fn: ViewGroupInit) =
    ViewGroup.LayoutParams(w, h).apply(fn)

fun linearLayoutParams(w: Int, h: Int) =
    LinearLayout.LayoutParams(w, h)

inline fun linearLayoutParams(w: Int, h: Int, crossinline fn: LinearLayoutInit) =
    LinearLayout.LayoutParams(w, h).apply(fn)

fun linearLayoutParams(w: Int, h: Int, weight: Float) =
    LinearLayout.LayoutParams(w, h, weight)

inline fun linearLayoutParams(w: Int, h: Int, weight: Float, crossinline fn: LinearLayoutInit) =
    LinearLayout.LayoutParams(w, h, weight).apply(fn)


fun drawerLayoutParams(w: Int, h: Int) =
    DrawerLayout.LayoutParams(w, h)

inline fun drawerLayoutParams(w: Int, h: Int, crossinline fn: DrawerLayoutInit) =
    DrawerLayout.LayoutParams(w, h).apply(fn)

fun drawerLayoutParams(w: Int, h: Int, gravity: Int) =
    DrawerLayout.LayoutParams(w, h, gravity)

inline fun drawerLayoutParams(w: Int, h: Int, gravity: Int, crossinline fn: DrawerLayoutInit) =
    DrawerLayout.LayoutParams(w, h, gravity).apply(fn)


fun marginLayoutParams(w: Int, h: Int) =
    ViewGroup.MarginLayoutParams(w, h)

inline fun marginLayoutParams(w: Int, h: Int, crossinline fn: MarginLayoutInit) =
    ViewGroup.MarginLayoutParams(w, h).apply(fn)

fun frameLayoutParams(w: Int, h: Int) =
    FrameLayout.LayoutParams(w, h)

inline fun frameLayoutParams(w: Int, h: Int, crossinline fn: FrameLayoutInit) =
    FrameLayout.LayoutParams(w, h).apply(fn)

fun frameLayoutParams(w: Int, h: Int, g: Int) =
    FrameLayout.LayoutParams(w, h, g)

inline fun frameLayoutParams(w: Int, h: Int, g: Int, crossinline fn: FrameLayoutInit) =
    FrameLayout.LayoutParams(w, h, g).apply(fn)

fun relativeLayoutParams(w: Int, h: Int) =
    RelativeLayout.LayoutParams(w, h)

inline fun relativeLayoutParams(w: Int, h: Int, crossinline fn: RelativeLayoutInit) =
    RelativeLayout.LayoutParams(w, h).apply(fn)


fun coordinatorLayoutParams(w: Int, h: Int) =
    CoordinatorLayout.LayoutParams(w, h)

inline fun coordinatorLayoutParams(w: Int, h: Int, crossinline fn: CoordinatorLayoutInit) =
    CoordinatorLayout.LayoutParams(w, h).apply(fn)

fun Activity.dip(value: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)

fun Activity.dip(value: Int) =
    dip(value.toFloat()).toInt()

fun Fragment.dip(value: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)

fun Fragment.dip(value: Int) =
    dip(value.toFloat()).toInt()

fun View.dip(value: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)

fun View.dip(value: Int) =
    dip(value.toFloat()).toInt()

fun Activity.sp(value: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics)

fun Activity.sp(value: Int) =
    sp(value.toFloat()).toInt()

fun Fragment.sp(value: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics)

fun Fragment.sp(value: Int) =
    sp(value.toFloat()).toInt()

fun View.sp(value: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics)

fun View.sp(value: Int) =
    sp(value.toFloat()).toInt()

/**
 * Sets all paddings. Getting returns the left padding.
 */
var View.padding: Int
    get() = paddingLeft
    set(value) {
        setPadding(value, value, value, value)
    }

/**
 * Sets the compat appearance.
 */
var TextView.compatAppearance: Int
    get() = error("Cannot get")
    set(value) {
        TextViewCompat.setTextAppearance(this, value)
    }

/**
 * Sets the text color from resource.
 */
var TextView.textColorResource: Int
    get() = error("Cannot get")
    set(value) {
        setTextColor(ContextCompat.getColor(context, value))
    }

/**
 * Sets the text from resource.
 */
var TextView.textResource: Int
    get() = error("Cannot get")
    set(value) {
        setText(value)
    }

/**
 * Sets the single-linedness.
 */
var TextView.singleLine: Boolean
    get() = error("Cannot get")
    set(value) {
        isSingleLine = value
    }

/**
 * Sets the single-linedness.
 */
var EditText.singleLine: Boolean
    get() = error("Cannot get")
    set(value) {
        isSingleLine = value
    }

/**
 * Sets the hint from resources.
 */
var EditText.hintResource: Int
    get() = error("Cannot get")
    set(value) {
        setHint(value)
    }

/**
 * Sets the background from resource.
 */
var View.backgroundResource: Int
    get() = error("Cannot get")
    set(value) {
        setBackgroundResource(value)
    }

/**
 * Sets the background color from resource.
 */
var View.backgroundColorResource: Int
    get() = error("Cannot get")
    set(value) {
        setBackgroundColor(ContextCompat.getColor(context, value))
    }

/**
 * Sets the image from resource.
 */
var PhotoView.imageResource: Int
    get() = error("Cannot get")
    set(value) {
        setImageResource(value)
    }

/**
 * Sets the image from resource.
 */
var FloatingActionButton.imageResource: Int
    get() = error("Cannot get")
    set(value) {
        setImageResource(value)
    }

/**
 * Sets the image from resource.
 */
var FloatingActionButton.backgroundColorResource: Int
    get() = error("Cannot get")
    set(value) {
        setBackgroundColor(ContextCompat.getColor(context, value))
    }

