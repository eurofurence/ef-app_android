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

/**
 * Executes the block for a substitute file, if successful (no
 * exception thrown), renames the temporary file to the original.
 * @param block The block to execute
 */
fun File.substitute(block: (File) -> Unit) {
    File.createTempFile(name, ".$extension", parentFile).let {
        block(it)
        if (!it.renameTo(this))
            throw IOException("Error executing substitution block, could not rename")
    }
}