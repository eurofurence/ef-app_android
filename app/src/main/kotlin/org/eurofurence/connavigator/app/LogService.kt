package org.eurofurence.connavigator.app

import android.content.Context
import android.util.Log
import com.google.common.io.FileWriteMode
import com.google.common.io.Files
import org.eurofurence.connavigator.BuildConfig
import java.io.File
import java.io.PrintWriter
import java.util.*

/**
 * Logging services.
 */
object logService {
    val DEFAULT_TAG = "LOG"

    /**
     * Charset used for log message encoding.
     */
    val CHARSET = Charsets.UTF_8

    /**
     * Minimum level for logging
     */
    var level = Log.VERBOSE

    /**
     * The log file.
     */
    lateinit var logfile: File

    /**
     * Logs a message with a severity -- specified in [Log] -- to both Android output and the File system.
     */
    inline fun log(tag: String = DEFAULT_TAG, severity: Int, message: () -> String) {
        // Skip logging if less then level or no debug enabled
        if (!BuildConfig.DEBUG || severity < level)
            return

        // Log appropriately and get the severity name
        val severityName = when (severity) {
            Log.VERBOSE -> {
                Log.v(tag, message())
                "Verbose"
            }
            Log.DEBUG -> {
                Log.d(tag, message())
                "Debug"
            }
            Log.INFO -> {
                Log.i(tag, message())
                "Info"
            }
            Log.WARN -> {
                Log.w(tag, message())
                "Warn"
            }
            Log.ERROR -> {
                Log.e(tag, message())
                "Error"
            }
            else -> "Unknown"
        }

        // Write to stream
        Files.asCharSink(logfile, CHARSET, FileWriteMode.APPEND).openBufferedStream()
                .let { PrintWriter(it) }
                .use {
                    it.println("$severityName $tag ${Date()}")
                    it.println(message())
                    it.println()
                }
    }

    /**
     * Logs a message with a severity -- specified in [Log] -- to both Android output and the File system. Prints an
     * exception.
     */
    inline fun log(tag: String = DEFAULT_TAG, severity: Int, throwable: Throwable, message: () -> String) {
        // Skip logging if less then level or no debug enabled
        if (!BuildConfig.DEBUG || severity < level)
            return

        // Log appropriately and get the severity name
        val severityName = when (severity) {
            Log.VERBOSE -> {
                Log.v(tag, message(), throwable)
                "Verbose"
            }
            Log.DEBUG -> {
                Log.d(tag, message(), throwable)
                "Debug"
            }
            Log.INFO -> {
                Log.i(tag, message(), throwable)
                "Info"
            }
            Log.WARN -> {
                Log.w(tag, message(), throwable)
                "Warn"
            }
            Log.ERROR -> {
                Log.e(tag, message(), throwable)
                "Error"
            }
            else -> "Unknown"
        }

        // Write to stream
        Files.asCharSink(logfile, CHARSET, FileWriteMode.APPEND).openBufferedStream()
                .let { PrintWriter(it) }
                .use {
                    it.println("$severityName $tag ${Date()}")
                    it.println(message())
                    throwable.printStackTrace(it)
                    it.println()
                }
    }

    fun messages() = Files.readLines(logfile, CHARSET)

    fun initialize(context: Context) {

        logfile = File(context.cacheDir, "log")
    }
}