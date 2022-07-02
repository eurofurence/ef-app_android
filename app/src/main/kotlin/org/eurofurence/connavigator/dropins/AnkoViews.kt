package org.eurofurence.connavigator.dropins

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import at.grabner.circleprogress.CircleProgressView
import com.github.chrisbanes.photoview.PhotoView
import com.github.lzyzsd.circleprogress.ArcProgress
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import org.eurofurence.connavigator.ui.views.FontAwesomeTextView
import org.eurofurence.connavigator.ui.views.MultitouchableViewPager
import java.lang.IllegalStateException

fun Context.createView(block: ViewGroup.() -> Unit): View {
    // Memorize for later.
    var result: View? = null

    // Create stub and receive view on add.
    object : ViewGroup(this) {
        override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
            // Mandatory, skipped.
        }

        override fun addView(child: View?) {
            // Transfer if not-null view passed and not assigned yet.
            if (child != null) {
                if (result != null)
                    throw IllegalStateException("Already received a view.")

                result = child
            }
        }
    }.block();

    return result ?: throw IllegalStateException("No view initialized.")
}

fun Fragment.createView(block: ViewGroup.() -> Unit) =
    requireContext().createView(block)

fun Activity.setContent(block: ViewGroup.() -> Unit) {
    // Set the view as the receiver's content.
    setContentView(createView(block))
}


inline fun ViewGroup.view(crossinline block: View.() -> Unit) =
    View(context).apply(block).also { addView(it) }

inline fun ViewGroup.drawerLayout(crossinline block: DrawerLayout.() -> Unit) =
    DrawerLayout(context).apply(block).also { addView(it) }

inline fun ViewGroup.frameLayout(crossinline block: FrameLayout.() -> Unit) =
    FrameLayout(context).apply(block).also { addView(it) }

inline fun ViewGroup.linearLayout(crossinline block: LinearLayout.() -> Unit) =
    LinearLayout(context).apply(block).also { addView(it) }

inline fun ViewGroup.horizontalLayout(crossinline block: LinearLayout.() -> Unit) =
    LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        block()
    }.also { addView(it) }

inline fun ViewGroup.verticalLayout(crossinline block: LinearLayout.() -> Unit) =
    LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        block()
    }.also { addView(it) }

inline fun ViewGroup.navigationView(crossinline block: NavigationView.() -> Unit) =
    NavigationView(context).apply(block).also { addView(it) }

fun NavigationView.headerViewFrom(resId: Int) =
    inflateHeaderView(resId).apply {
        addHeaderView(this)
    }

inline fun ViewGroup.toolbar(crossinline block: Toolbar.() -> Unit) =
    Toolbar(context).apply(block).also { addView(it) }

inline fun ViewGroup.scrollView(crossinline block: ScrollView.() -> Unit) =
    ScrollView(context).apply(block).also { addView(it) }

inline fun ViewGroup.nestedScrollView(crossinline block: NestedScrollView.() -> Unit) =
    NestedScrollView(context).apply(block).also { addView(it) }

inline fun ViewGroup.relativeLayout(crossinline block: RelativeLayout.() -> Unit) =
    RelativeLayout(context).apply(block).also { addView(it) }

inline fun ViewGroup.constraintLayout(crossinline block: ConstraintLayout.() -> Unit) =
    ConstraintLayout(context).apply(block).also { addView(it) }

inline fun ViewGroup.coordinatorLayout(crossinline block: CoordinatorLayout.() -> Unit) =
    CoordinatorLayout(context).apply(block).also { addView(it) }

inline fun ViewGroup.tabLayout(crossinline block: TabLayout.() -> Unit) =
    TabLayout(context).apply(block).also { addView(it) }

inline fun ViewGroup.viewPager(crossinline block: ViewPager.() -> Unit) =
    ViewPager(context).apply(block).also { addView(it) }

inline fun ViewGroup.editText(crossinline block: EditText.() -> Unit) =
    EditText(context).apply(block).also { addView(it) }

inline fun ViewGroup.textView(crossinline block: TextView.() -> Unit) =
    TextView(context).apply(block).also { addView(it) }

inline fun ViewGroup.textView(value: String, crossinline block: TextView.() -> Unit) =
    textView {
        text = value
        block()
    }

inline fun ViewGroup.textView(resId: Int, crossinline block: TextView.() -> Unit) =
    textView {
        setText(resId)
        block()
    }

inline fun ViewGroup.button(crossinline block: Button.() -> Unit) =
    Button(context).apply(block).also { addView(it) }

// TODO: Verify text transform defaults.
inline fun ViewGroup.button(value: String, crossinline block: Button.() -> Unit) =
    button {
        setText(value)
        block()
    }

inline fun ViewGroup.button(resId: Int, crossinline block: Button.() -> Unit) =
    button {
        setText(resId)
        block()
    }

inline fun ViewGroup.imageView(crossinline block: ImageView.() -> Unit) =
    ImageView(context).apply(block).also { addView(it) }


inline fun ViewGroup.imageView(resId: Int, crossinline block: ImageView.() -> Unit) =
    imageView {
        setImageResource(resId)
        block()
    }


inline fun ViewGroup.arcProgress(crossinline block: ArcProgress.() -> Unit) =
    ArcProgress(context).apply(block).also { addView(it) }

inline fun ViewGroup.circleProgressView(crossinline block: CircleProgressView.() -> Unit) =
    CircleProgressView(context, null).apply(block).also { addView(it) }

inline fun ViewGroup.recycler(crossinline block: RecyclerView.() -> Unit) =
    RecyclerView(context).apply(block).also { addView(it) }

// TODO: Dropin.
inline fun ViewGroup.markdownView(crossinline block: TextView.() -> Unit) =
    TextView(context).apply(block).also { addView(it) }

// TODO: Dropin. Verify all sites.
inline fun ViewGroup.fontAwesomeView(
    crossinline block: FontAwesomeTextView.() -> Unit
) = FontAwesomeTextView(context).apply(block).also { addView(it) }

inline fun ViewGroup.photoView(crossinline block: PhotoView.() -> Unit) =
    PhotoView(context).apply(block).also { addView(it) }

inline fun ViewGroup.floatingActionButton(crossinline block: FloatingActionButton.() -> Unit) =
    FloatingActionButton(context).apply(block).also { addView(it) }

inline fun ViewGroup.multitouchViewPager(crossinline block: MultitouchableViewPager.() -> Unit) =
    MultitouchableViewPager(context).apply(block).also { addView(it) }

inline fun ViewGroup.progressBar(crossinline block: ProgressBar.() -> Unit) =
    ProgressBar(context).apply(block).also { addView(it) }

inline fun ViewGroup.spinner(crossinline block: Spinner.() -> Unit) =
    Spinner(context).apply(block).also { addView(it) }

inline fun ViewGroup.ratingBar(crossinline block: RatingBar.() -> Unit) =
    RatingBar(context).apply(block).also { addView(it) }

inline fun ViewGroup.checkBox(crossinline block: CheckBox.() -> Unit) =
    CheckBox(context).apply(block).also { addView(it) }

