package org.eurofurence.connavigator.util.extensions

import java.io.*

/**
 * Standard encoding for files.
 */
val standardEncoding = Charsets.UTF_8

/**
 * Opens a writer on the file. Uses the buffering decorator.
 */
fun File.safeWriter() = OutputStreamWriter(safeOutStream(), standardEncoding)

/**
 * Opens a reader on the file. Uses the buffering decorator.
 */
fun File.safeReader() = InputStreamReader(safeInStream(), standardEncoding)

/**
 * Opens a stream on the file. Uses the buffering decorator.
 */
fun File.safeOutStream() = BufferedOutputStream(FileOutputStream(this))

/**
 * Opens a stream on the file. Uses the buffering decorator.
 */
fun File.safeInStream() = BufferedInputStream(FileInputStream(this))