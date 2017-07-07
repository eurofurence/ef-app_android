package org.eurofurence.connavigator.app

import android.content.Context
import android.util.Log
import org.eurofurence.connavigator.BuildConfig
import java.io.File

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
        val m = message()
        val severityName = when (severity) {
            Log.VERBOSE -> {
                Log.v(tag, m)
                //println("VER $tag: $m")
                "Verbose"
            }
            Log.DEBUG -> {
                Log.d(tag, m)
                //println("DBG $tag: $m")
                "Debug"
            }
            Log.INFO -> {
                Log.i(tag, m)
                //println("INF $tag: $m")
                "Info"
            }
            Log.WARN -> {
                Log.w(tag, m)
                //println("WRN $tag: $m")
                "Warn"
            }
            Log.ERROR -> {
                Log.e(tag, m)
                //println("ERR $tag: $m")
                "Error"
            }
            else -> "Unknown"
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
        val m = message()
        val severityName = when (severity) {
            Log.VERBOSE -> {
                Log.v(tag, m)
                //println("VER $tag: $m")
                //throwable.printStackTrace()
                "Verbose"
            }
            Log.DEBUG -> {
                Log.d(tag, m)
                //println("DBG $tag: $m")
                //throwable.printStackTrace()
                "Debug"
            }
            Log.INFO -> {
                Log.i(tag, m, throwable)
                //println("INF $tag: $m")
                //throwable.printStackTrace()
                "Info"
            }
            Log.WARN -> {
                Log.w(tag, m, throwable)
                //println("WRN $tag: $m")
                //throwable.printStackTrace()
                "Warn"
            }
            Log.ERROR -> {
                Log.e(tag, m, throwable)
                //println("ERR $tag: $m")
                //throwable.printStackTrace()
                "Error"
            }
            else -> "Unknown"
        }
    }

    fun initialize(context: Context) {

        logfile = File(context.cacheDir, "log")
    }
}