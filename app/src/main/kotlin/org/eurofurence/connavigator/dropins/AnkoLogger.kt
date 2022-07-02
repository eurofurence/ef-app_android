package org.eurofurence.connavigator.dropins

import android.util.Log
import kotlin.reflect.KClass

val KClass<*>.tag
    get() =
        qualifiedName?.filter { it.isUpperCase() }?.fold("") { l, r -> l + r } ?: "GLOBAL"

interface AnkoLogger {
    fun info(text: String) =
        Log.i(this::class.tag, text)

    fun info(text: () -> String) =
        Log.i(this::class.tag, text())

    fun debug(text: () -> String) =
        Log.d(this::class.tag, text())

    fun debug(text: String) =
        Log.d(this::class.tag, text)

    fun warn(text: String) =
        Log.w(this::class.tag, text)

    fun warn(text: () -> String) =
        Log.w(this::class.tag, text())

    fun warn(text: String, ex: Throwable) =
        Log.w(this::class.tag, text, ex)

    fun error(text: () -> String) =
        Log.e(this::class.tag, text())

    fun error(text: String, ex: Throwable) =
        Log.e(this::class.tag, text, ex)
}