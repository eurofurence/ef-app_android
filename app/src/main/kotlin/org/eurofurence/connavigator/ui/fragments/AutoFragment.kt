package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import org.eurofurence.connavigator.util.AutoBehaviour
import org.eurofurence.connavigator.util.AutoViewHolder
import org.jetbrains.anko.AnkoContext

abstract class AutoFragment<E, A, S> : DisposingFragment() {
    /**
     * The state of the fragment.
     */
    protected abstract var state: S

    /**
     * Reads the arguments from the [bundle].
     */
    protected abstract fun readArgs(bundle: Bundle): A

    /**
     * Reads the state from the [bundle].
     */
    protected abstract fun readState(bundle: Bundle): S

    /**
     * Stores the [state] in the [bundle].
     */
    protected abstract fun storeState(state: S, bundle: Bundle)

    /**
     * Creates the view with auto binding.
     * TODO: Extract an own reset and bind collector for fragments.
     */
    protected abstract fun AutoBehaviour<Triple<E, A?, S>, ViewGroup?>.create(): View

    /**
     * Links the auto fragment to an observable source.
     */
    protected abstract fun link(): Observable<E>

    /**
     * Memorizes the last reset method.
     */
    private var destroy = { }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Create context and binder.
        val context = AnkoContext.createReusable(requireContext(), container, false)
        val autoBinder = AutoBehaviour<Triple<E, A?, S>, ViewGroup?>(context)

        // Create view, also track binding and resetting.
        val view = autoBinder.create()

        // Memorize resetter for destroying.
        destroy = autoBinder.resetter

        // If a saved instance state was given, use it.
        savedInstanceState?.let { state = readState(it) }

        link().subscribe {

            // Bind with current values.
            autoBinder.binder(Triple(it, arguments?.let { readArgs(it) }, state), emptyList(), emptyList())
        }.collectOnDestroyView()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Invoke resetter.
        destroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Store state.
        storeState(state, outState)
    }
}