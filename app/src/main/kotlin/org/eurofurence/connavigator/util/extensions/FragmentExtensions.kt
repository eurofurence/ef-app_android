package org.eurofurence.connavigator.util.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun Fragment.withArguments(crossinline block: Bundle.() -> Unit) = apply {
    arguments = Bundle().apply(block)
}