package org.eurofurence.connavigator

import com.google.common.collect.ImmutableMap
import com.google.common.io.CharSink
import com.google.common.io.CharSource
import com.google.common.io.Files
import io.swagger.client.JsonUtil
import java.io.File

/**
 * An off memory database persisting an identified datastructure using GSON.
 *
 * Created by Pazuzu on 05.03.2016.
 * @property t The class to be used for (de-)serializing in Gson
 * @property id Id computing method for an object
 * @property source Provider of database readables
 * @property target Provider of database writables
 * @property afterWriting This method is invoked after writing operations, use this to  move temp files appropriately
 */
class JsonStreamDB<T>(val t: Class<T>, val id: (T) -> String, val source: CharSource, val target: CharSink, var afterWriting: () -> Unit = {}) {
    /**
     * Uses a file as the storage method, performs moving after writing.
     * @param t Initializes the parent constructor's t
     * @param id Initializes the parent constructor's id
     * @param file Used as the source file for readables
     * @param tempFile Used as the target file for writables, if not clarified, uses a name relative to [file].
     */
    constructor(t: Class<T>, id: (T) -> String, file: File, tempFile: File = File(file.parent, "${file.name}.temp"))
    : this(t, id, Files.asCharSource(file, Charsets.UTF_8), Files.asCharSink(tempFile, Charsets.UTF_8), {
        // Delete old file and mov the temp file if successful
        if (file.delete())
            tempFile.renameTo(file)
    });

    /**
     * Gets the entry with the specified id, returns null otherwise.
     */
    fun get(id: String): T? {
        val reader = JsonUtil.getGson().newJsonReader(source.openStream())
        try {
            // Scroll for the first element that has the given id
            if (reader.hasNext()) {
                reader.beginObject()
                while (reader.hasNext())
                    if (reader.nextName() == id)
                        return JsonUtil.getGson().fromJson(reader, t)
                reader.endObject()
            }
            return null
        } finally {
            reader.close()
        }
    }

    /**
     * Gets all entries in the persisted database, the returned structure is **not** mutable.
     */
    fun getAll(): Map<String, T> {
        val reader = JsonUtil.getGson().newJsonReader(source.openStream())
        try {
            // Collect all elements while traversing
            val m = ImmutableMap.builder<String, T>()
            if (reader.hasNext()) {
                reader.beginObject()
                while (reader.hasNext()) {
                    val n = reader.nextName()
                    val v: T = JsonUtil.getGson().fromJson(reader, t)
                    m.put(n, v)
                }
                reader.endObject()
            }

            return m.build()
        } finally {
            reader.close()
        }
    }

    /**
     * Replaces or appends a value in the database. The condition for a *replace* is
     * equality of equality of read id and calculated [id]
     */
    fun put(value: T) {
        val reader = JsonUtil.getGson().newJsonReader(source.openStream())
        val writer = JsonUtil.getGson().newJsonWriter(target.openStream())
        try {
            // Track if there was an object for the ID
            var found = false

            // Write and read at the same time
            writer.beginObject()

            // Traverse all the objects and write
            if (reader.hasNext()) {
                reader.beginObject()
                while (reader.hasNext()) {
                    val n = reader.nextName()
                    val v: T = JsonUtil.getGson().fromJson(reader, t)

                    writer.name(n)

                    // Write the replaced object if equal, otherwise write the original
                    if (n == id(value)) {
                        JsonUtil.getGson().toJson(value, t, writer)
                        found = true
                    } else {
                        JsonUtil.getGson().toJson(v, t, writer)
                    }
                }
                reader.endObject()
            }

            // If not found while overwriting, append
            if (!found) {
                writer.name(id(value))
                JsonUtil.getGson().toJson(value, t, writer)
            }

            writer.endObject()
        } finally {
            writer.close()
        }
        afterWriting()
    }

    /**
     * Replaces the database with all the values in the given iterable
     */
    fun replaceAll(values: Iterable<T>) {
        val writer = JsonUtil.getGson().newJsonWriter(target.openStream())
        try {
            writer.beginObject()
            for (value in values) {
                writer.name(id(value))
                JsonUtil.getGson().toJson(value, t, writer)
            }
            writer.endObject()
        } finally {
            writer.close()
        }
        afterWriting()
    }

    /**
     * Put many objects. This implementation is more efficient that manually invoking [put]
     * repeatedly.
     */
    fun putAll(values: Iterable<T>) {
        // Get the original map
        val ca = getAll()

        // Create the new map
        val builder = ImmutableMap.builder<String, T>()
        for (value in values)
            builder.put(id(value), value)
        val cb = builder.build()

        val writer = JsonUtil.getGson().newJsonWriter(target.openStream())
        try {
            writer.beginObject()
            for ((k, v) in ca.entries) {
                if (cb.containsKey(k))
                    continue
                writer.name(k)
                JsonUtil.getGson().toJson(v, t, writer)
            }

            for ((k, v) in cb.entries) {
                writer.name(k)
                JsonUtil.getGson().toJson(v, t, writer)
            }

            writer.endObject()
        } finally {
            writer.close()
        }
        afterWriting()
    }

    /**
     * Removes an element by key, delegates to [removeIf]
     */
    fun remove(id: String) {
        removeIf { this.id(it) == id }
    }

    /**
     * Removes all elements satisfying a given condition.
     */
    fun removeIf(predicate: (T) -> Boolean) {
        val reader = JsonUtil.getGson().newJsonReader(source.openStream())
        val writer = JsonUtil.getGson().newJsonWriter(target.openStream())
        try {
            writer.beginObject()
            if (reader.hasNext()) {
                reader.beginObject()
                while (reader.hasNext()) {
                    val n = reader.nextName()
                    val v: T = JsonUtil.getGson().fromJson(reader, t)

                    if (predicate(v)) else {
                        writer.name(n)
                        JsonUtil.getGson().toJson(v, t, writer)
                    }
                }
                reader.endObject()
            }
            writer.endObject()
        } finally {
            writer.close()
        }
        afterWriting()
    }
}