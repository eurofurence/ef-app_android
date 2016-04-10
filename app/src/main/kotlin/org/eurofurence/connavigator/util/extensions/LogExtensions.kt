package org.eurofurence.connavigator.util.extensions

import android.util.Log
import org.eurofurence.connavigator.app.logService

/**
 * Logs a verbose message to the log service.
 */
inline fun logv(tag: String = logService.DEFAULT_TAG, message: () -> Any?) =
        logService.log(tag, Log.VERBOSE, { message().toString() })

/**
 * Logs a verbose message to the log service.
 */
inline fun logv(tag: String = logService.DEFAULT_TAG, throwable: Throwable, message: () -> Any?) =
        logService.log(tag, Log.VERBOSE, throwable, { message().toString() })

/**
 * Logs a debug message to the log service.
 */
inline fun logd(tag: String = logService.DEFAULT_TAG, message: () -> Any?) =
        logService.log(tag, Log.DEBUG, { message().toString() })

/**
 * Logs a debug message to the log service.
 */
inline fun logd(tag: String = logService.DEFAULT_TAG, throwable: Throwable, message: () -> Any?) =
        logService.log(tag, Log.DEBUG, throwable, { message().toString() })

/**
 * Logs an info message to the log service.
 */
inline fun logi(tag: String = logService.DEFAULT_TAG, message: () -> Any?) =
        logService.log(tag, Log.INFO, { message().toString() })

/**
 * Logs an info message to the log service.
 */
inline fun logi(tag: String = logService.DEFAULT_TAG, throwable: Throwable, message: () -> Any?) =
        logService.log(tag, Log.INFO, throwable, { message().toString() })

/**
 * Logs a warning message to the log service.
 */
inline fun logw(tag: String = logService.DEFAULT_TAG, message: () -> Any?) =
        logService.log(tag, Log.WARN, { message().toString() })

/**
 * Logs a warning message to the log service.
 */
inline fun logw(tag: String = logService.DEFAULT_TAG, throwable: Throwable, message: () -> Any?) =
        logService.log(tag, Log.WARN, throwable, { message().toString() })

/**
 * Logs an error message to the log service.
 */
inline fun loge(tag: String = logService.DEFAULT_TAG, message: () -> Any?) =
        logService.log(tag, Log.ERROR, { message().toString() })

/**
 * Logs an error message to the log service.
 */
inline fun loge(tag: String = logService.DEFAULT_TAG, throwable: Throwable, message: () -> Any?) =
        logService.log(tag, Log.ERROR, throwable, { message().toString() })