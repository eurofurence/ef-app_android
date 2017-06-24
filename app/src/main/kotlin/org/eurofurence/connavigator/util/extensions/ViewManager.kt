package org.eurofurence.connavigator.util.extensions

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewManager
import com.github.lzyzsd.circleprogress.ArcProgress
import org.jetbrains.anko.custom.ankoView

/**
 * Extensions to anko viewmanager
 */

public inline fun ViewManager.arcProgress(init: ArcProgress.() -> Unit) = ankoView({ ArcProgress(it) }, 0, init)
public inline fun ViewManager.recycler(init: RecyclerView.() -> Unit) = ankoView({ RecyclerView(it) }, 0, init)
