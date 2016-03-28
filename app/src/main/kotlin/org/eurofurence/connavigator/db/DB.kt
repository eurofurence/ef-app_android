package org.eurofurence.connavigator.db

import com.google.common.base.Preconditions
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.swagger.client.JsonUtil
import java.io.*

/**
 * Provides a database of objects of type [T].
 */
interface DBProvider {
    /**
     * Creates the database from a storage file [from] and the type of elements [elementClass].
     */
    fun <T> create(from: File, elementClass: Class<T>): DB<T>
}

/**
 * A database on objects of type [T].
 */
interface DB<T> {
    /**
     * True if the database exists.
     */
    val exists: Boolean

    /**
     * The elements of the database
     */
    var elements: List<T>
}

/**
 * Provides [Serializable] databases.
 */
object sdbProvider : DBProvider {
    override fun <T> create(from: File, elementClass: Class<T>): DB<T> {
        // Serializable DB can only create DBs for serializable types
        Preconditions.checkArgument(Serializable::class.java.isAssignableFrom(elementClass))

        // Return a database of serializable elements
        return SerializableDB(from, elementClass)
    }
}

/**
 * A database of elements that are [Serializable].
 */
class SerializableDB<T>(val from: File, val elementClass: Class<T>) : DB<T> {
    override val exists: Boolean
        get() = from.exists()

    override var elements: List<T>
        get() = if (exists)
            ObjectInputStream(FileInputStream(from)).use { os ->
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

        set(values) = ObjectOutputStream(FileOutputStream(from)).use { os ->
            // Write all objects to the stream
            for (value in values)
                os.writeObject(value)
        }
}

/**
 * Provides GSON databases.
 */
object gsdbProvider : DBProvider {
    override fun <T> create(from: File, elementClass: Class<T>): DB<T> {
        return GsonDB(from, elementClass)
    }
}

/**
 * A database of elements that can be written and read by GSON.
 */
class GsonDB<T>(val from: File, val elementClass: Class<T>) : DB<T> {
    override val exists: Boolean
        get() = from.exists()

    override var elements: List<T>
        get() = if (exists)
            JsonReader(FileReader(from)).use { jr ->
                // Deserialize using GSON
                JsonUtil.getGson().fromJson<List<T>>(jr, JsonUtil.getListTypeForDeserialization(elementClass))
            }
        else
            emptyList()

        set(values) = JsonWriter(FileWriter(from)).use { jw ->
            // Serialize using GSON
            JsonUtil.getGson().toJson(values, JsonUtil.getListTypeForDeserialization(elementClass), jw)
        }
}
