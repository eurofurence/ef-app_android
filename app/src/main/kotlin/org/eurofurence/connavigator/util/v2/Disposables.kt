package org.eurofurence.connavigator.util.v2

import io.reactivex.disposables.Disposable

operator fun Disposable.plus(other: Disposable) = object : Disposable {
    override fun isDisposed() =
            this@plus.isDisposed && other.isDisposed


    override fun dispose() {
        this@plus.dispose()
        other.dispose()
    }

}