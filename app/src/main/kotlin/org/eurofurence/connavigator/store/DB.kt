package org.eurofurence.connavigator.store

import com.google.common.base.Preconditions
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.swagger.client.JsonUtil
import org.eurofurence.connavigator.util.extensions.safeInStream
import org.eurofurence.connavigator.util.extensions.safeOutStream
import org.eurofurence.connavigator.util.extensions.safeReader
import org.eurofurence.connavigator.util.extensions.safeWriter
import java.io.*

/**
 * A database on objects of type [T].
 */
interface DB<T> {
    /**
     * A time stamp, or null if the backend does not exist
     */
    val time: Long?
    /**
     * The elements of the database
     */
    var items: List<T>
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

/**
 * A database of elements that are [Serializable].
 */
class SerializableDB<T>(val from: File, val elementClass: Class<T>) : DB<T> {
    override val time: Long?
        get() = if (from.exists())
            from.lastModified()
        else
            null

    override var items: List<T>
        get() = if (time != null)
            ObjectInputStream(from.safeInStream()).use { os ->
                // Return a sequence that is provided by the iterator of objects in the stream
                Sequence {
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

        set(values) = ObjectOutputStream(from.safeOutStream()).use { os ->
            // Write all objects to the stream
            for (value in values)
                os.writeObject(value)


            os.flush()
        }
}

/**
 * Provides GSON databases.
 */
fun <T> createGson(from: File, elementClass: Class<T>): DB<T> {
    return GsonDB(from, elementClass)
}

/**
 * A database of elements that can be written and read by GSON.
 */
class GsonDB<T>(val from: File, val elementClass: Class<T>) : DB<T> {
    override val time: Long?
        get() = if (from.exists())
            from.lastModified()
        else
            null

    override var items: List<T>
        get() = if (time != null)
            JsonReader(from.safeReader()).use { jr ->
                // Deserialize using GSON
                JsonUtil.getGson().fromJson<List<T>>(jr, JsonUtil.getListTypeForDeserialization(elementClass))
            }
        else
            emptyList()

        set(values) = JsonWriter(from.safeWriter()).use { jw ->
            // Serialize using GSON
            JsonUtil.getGson().toJson(values, JsonUtil.getListTypeForDeserialization(elementClass), jw)
        }
}
