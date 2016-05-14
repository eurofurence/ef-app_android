package org.eurofurence.connavigator.store

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.swagger.client.JsonUtil
import org.eurofurence.connavigator.util.extensions.safeReader
import org.eurofurence.connavigator.util.extensions.safeWriter
import org.eurofurence.connavigator.util.extensions.substitute
import java.io.File

/**
 * A database of elements that can be written and read by GSON.
 */
class GsonDB<T>(val from: File, val elementClass: Class<T>) : DB<T> {
    override fun delete() {
        from.delete()
    }

    override val time: Long?
        get() = if (from.exists())
            from.lastModified()
        else
            null

    // TODO: Actually stream the objects, but for now, use JSON lists
    override var items: Iterable<T>
        get() = if (time != null)
            JsonReader(from.safeReader()).use { jr ->
                // Deserialize using GSON
                JsonUtil.getGson().fromJson<List<T>>(jr, JsonUtil.getListTypeForDeserialization(elementClass))
            }
        else
            emptyList()

        set(values) = from.substitute {
            JsonWriter(it.safeWriter()).use { jw ->
                // Serialize using GSON
                JsonUtil.getGson().toJson(values, JsonUtil.getListTypeForDeserialization(elementClass), jw)
            }
        }
}

/**
 * Provides GSON databases.
 */
fun <T> createGson(from: File, elementClass: Class<T>): DB<T> {
    return GsonDB(from, elementClass)
}

