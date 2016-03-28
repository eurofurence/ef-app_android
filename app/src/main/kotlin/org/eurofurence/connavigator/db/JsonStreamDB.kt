package org.eurofurence.connavigator.db

import com.google.common.collect.ImmutableMap
import com.google.common.io.CharSink
import com.google.common.io.CharSource
import com.google.common.io.Files
import org.eurofurence.connavigator.extensions.gson
import org.eurofurence.connavigator.extensions.not
import org.eurofurence.connavigator.extensions.sec
import java.io.File
import java.io.IOException

/**
 * An off memory database persisting an identified data structure using GSON.
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
        // Delete old file and move the temp file if successful
        if (file.delete())
            tempFile.renameTo(file)
    })

    /**
     * Gets the entry with the specified id, returns null otherwise.
     */
    operator fun get(id: String): T? = sec {
        // Load reader
        gson.reader(source).use {
            // Scroll for the first element that has the given id
            if (it.hasNext()) {
                it.beginObject()
                while (it.hasNext())
                    if (it.nextName() == id)
                        return@sec gson.read(it, t)
                it.endObject()
            }
            return@sec null
        }
    }

    /**
     * Gets all entries in the persisted database, the returned structure is **not** mutable.
     */
    fun getAll(): Map<String, T> = sec {
        // Load reader
        gson.reader(source).use {
            // Collect all elements while traversing
            val m = ImmutableMap.builder<String, T>()
            if (it.hasNext()) {
                it.beginObject()
                while (it.hasNext()) {
                    val n = it.nextName()
                    val v: T = gson.read(it, t)
                    m.put(n, v)
                }
                it.endObject()
            }

            return@sec m.build()
        }
    } ?: emptyMap()

    /**
     * Replaces or appends a value in the database. The condition for a *replace* is
     * equality of equality of read id and calculated [id]
     */
    fun put(value: T) = try {
        // Load reader and writer
        gson.reader(source).use { r ->
            gson.writer(target).use { w ->

                // Track if there was an object for the ID
                var found = false

                // Write and read at the same time
                w.beginObject()

                // Traverse all the objects and write
                if (r.hasNext()) {
                    r.beginObject()
                    while (r.hasNext()) {
                        val n = r.nextName()
                        val v: T = gson.read(r, t)

                        w.name(n)

                        // Write the replaced object if equal, otherwise write the original
                        if (n == id(value)) {
                            gson.write(value, w, t)
                            found = true
                        } else {
                            gson.write(v, w, t)
                        }
                    }
                    r.endObject()
                }

                // If not found while overwriting, append
                if (!found) {
                    w.name(id(value))
                    gson.write(value, w, t)
                }

                w.endObject()
                afterWriting()
            }
        }
    } catch(e: IOException) {
        replaceAll(listOf(value))
    }

    /**
     * Replaces the database with all the values in the given iterable
     */
    fun replaceAll(values: Iterable<T>) {
        // Load writer
        gson.writer(target).use {
            // Stream objects
            it.beginObject()
            for (value in values) {
                it.name(id(value))
                gson.write(value, it, t)
            }
            it.endObject()
            afterWriting()
        }
    }

    /**
     * Put many objects. This implementation is more efficient that manually invoking [put]
     * repeatedly.
     */
    fun putAll(values: Iterable<T>) = try {
        // Load writer
        gson.writer(target).use {
            // Get the original map
            val ca = getAll()

            // Create the new map
            val builder = ImmutableMap.builder<String, T>()
            for (value in values)
                builder.put(id(value), value)
            val cb = builder.build()

            // Stream objects
            it.beginObject()
            for ((k, v) in ca.entries) {
                if (cb.containsKey(k))
                    continue
                it.name(k)
                gson.write(v, it, t)
            }

            for ((k, v) in cb.entries) {
                it.name(k)
                gson.write(v, it, t)
            }

            it.endObject()

            afterWriting()
        }
    } catch(e: IOException) {
        replaceAll(values)
    }

    /**
     * Removes all elements satisfying a given condition.
     */
    fun removeIf(predicate: (T) -> Boolean) {
        // Load reader and writer
        gson.reader(source).use { r ->
            gson.writer(target).use { w ->
                // Stream to writer
                w.beginObject()
                if (r.hasNext()) {
                    r.beginObject()
                    while (r.hasNext()) {
                        val n = r.nextName()
                        val v: T = gson.read(r, t)

                        if (predicate(v)) else {
                            w.name(n)
                            gson.write(v, w, t)
                        }
                    }
                    r.endObject()
                }
                w.endObject()
                afterWriting()
            }
        }
    }


    /**
     * Removes an element by key, delegates to [removeIf]
     */
    fun remove(id: String) = removeIf { this.id(it) == id }


    /**
     * Syncs to a delta-set by finding those elements that have been deleted and a set of new elements
     */
    fun sync(elements: List<T>, deleted: (T) -> Boolean) {
        try {
            // Find removed elements and associate by key
            val drop = elements.filter(deleted).map(id).toSet()
            removeIf { id(it) in drop }

            // Log debug info
            println("Dropped: <= ${drop.size}")
        } catch(ex: IOException) {
            System.err?.println("Error while deleting removed elements.")
        }

        try {
            // Filter the elements that are added
            val add = elements.filter(!deleted)
            putAll(add)

            // Log debug info
            println("Added: ${add.size}")
        } catch(ex: IOException) {
            System.err?.println("Error while adding elements.")
        }
    }
}