package org.eurofurence.connavigator.store

import com.google.common.base.Preconditions
import org.eurofurence.connavigator.util.extensions.safeInStream
import org.eurofurence.connavigator.util.extensions.safeOutStream
import org.eurofurence.connavigator.util.extensions.substitute
import java.io.*

/**
 * A database of elements that are [Serializable].
 */
class SerializableDB<T>(val from: File, val elementClass: Class<T>) : DB<T> {
    override fun delete() {
        from.delete()
    }

    override val time: Long?
        get() = if (from.exists())
            from.lastModified()
        else
            null

    override var items: Iterable<T>
        get() = if (time != null)
            ObjectInputStream(from.safeInStream()).use { os ->
                // Return a sequence that is provided by the iterator
                // of objects in the stream, fully read the stream because
                // the stream will be closed as soon as possible
                Iterable {
                    object : AbstractIterator<T>() {
                        override fun computeNext() =
                                try {
                                    // Set data if there is some
                                    setNext(elementClass.cast(os.readObject()))
                                } catch(e: EOFException) {
                                    // Otherwise the stream is done
                                    done()
                                }

                    }
                }.toList()
            }
        else
            emptyList()

        set(values) = from.substitute {
            ObjectOutputStream(it.safeOutStream()).use { os ->
                // Write all objects to the stream
                for (value in values)
                    os.writeObject(value)
            }
        }
}

/**
 * Provides [Serializable] databases.
 */
fun <T> createSerialized(from: File, elementClass: Class<T>): DB<T> {
    // Serializable DB can only create DBs for serializable types
    Preconditions.checkArgument(Serializable::class.java.isAssignableFrom(elementClass))

    // Return a database of serializable elements
    return SerializableDB(from, elementClass)
}