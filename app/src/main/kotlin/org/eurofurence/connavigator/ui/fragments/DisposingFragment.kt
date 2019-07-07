package org.eurofurence.connavigator.ui.fragments

import androidx.fragment.app.Fragment
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.util.v2.plus

/**
 * Maintains a list of disposables that are disposed of on certain methods.
 */
abstract class DisposingFragment : Fragment() {
    /**
     * Collects all disposables that must be disposed of on destroy view.
     */
    private var disposeOnDestroyView = Disposables.empty()

    /**
     * Adds this disposable to the disposables that are disposed of in [onDestroyView].
     */
    protected fun Disposable.collectOnDestroyView() {
        disposeOnDestroyView += this
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Dispose of appropriate stack.
        disposeOnDestroyView.dispose()
        disposeOnDestroyView = Disposables.empty()
    }

}