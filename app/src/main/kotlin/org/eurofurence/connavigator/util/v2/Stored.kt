@file:Suppress("unused")

package org.eurofurence.connavigator.util.v2

import android.content.Context
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.swagger.client.JsonUtil.getGson
import io.swagger.client.JsonUtil.getListTypeForDeserialization
import org.eurofurence.connavigator.util.extensions.*
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Container of stored values.
 */
abstract class Stored(val context: Context) {
    /**
     * A stored value.
     */
    inner class StoredValue<T : Any>(
            private val elementClass: KClass<T>, private val swaggerStored: Boolean) {

        operator fun getValue(any: Any, kProperty: KProperty<*>): T? {
            val file = File(context.filesDir, "${kProperty.name}.val")
            if (!file.exists())
                return null
            else if (swaggerStored)
                JsonReader(file.safeReader()).use {
                    return getGson().fromJson<T>(it, elementClass.java)
                }
            else
                ObjectInputStream(file.safeInStream()).use {
                    @Suppress("unchecked_cast")
                    return it.readObject() as T
                }
        }

        operator fun setValue(any: Any, kProperty: KProperty<*>, t: T?) {
            val file = File(context.filesDir, "${kProperty.name}.val")

            if (t == null)
                file.delete()
            else if (swaggerStored)
                JsonWriter(file.safeWriter()).use {
                    getGson().toJson(t, elementClass.java, it)
                }
            else
                file.substitute { sub ->
                    ObjectOutputStream(sub.safeOutStream()).use {
                        it.writeObject(t)
                    }
                }
        }
    }

    /**
     * A stored list of values.
     */
    inner class StoredValues<T : Any>(
            private val elementClass: KClass<T>, private val swaggerStored: Boolean) {

        operator fun getValue(any: Any, kProperty: KProperty<*>): List<T> {
            val file = File(context.filesDir, "${kProperty.name}.val")

            if (!file.exists())
                return emptyList()
            else if (swaggerStored)
                JsonReader(file.safeReader()).use {
                    return getGson().fromJson<List<T>>(it, getListTypeForDeserialization(elementClass.java))
                }
            else
                ObjectInputStream(file.safeInStream()).use {
                    @Suppress("unchecked_cast")
                    return it.readObject() as List<T>
                }
        }

        operator fun setValue(any: Any, kProperty: KProperty<*>, t: List<T>) {
            val file = File(context.filesDir, "${kProperty.name}.val")

            when {
                t.isEmpty() -> file.delete()
                swaggerStored -> JsonWriter(file.safeWriter()).use {
                    getGson().toJson(t, getListTypeForDeserialization(elementClass.java), it)
                }
                else -> file.substitute { sub ->
                    ObjectOutputStream(sub.safeOutStream()).use {
                        it.writeObject(t)
                    }
                }
            }
        }
    }

    /**
     * A stored source that caches elements and creates an index via the [id] function.
     */
    inner class StoredSource<T : Any>(
            private val elementClass: KClass<T>,
            val id: (T) -> UUID) : Source<T, UUID> {
        /**
         * Storage file, generated from the type name.
         */
        private val file = File(context.filesDir, "${elementClass.qualifiedName}.db")

        /**
         * Last time of read or write-through.
         */
        private var readTime: Long? = null

        /**
         * Last value of read or write-through.
         */
        private var readValue: Map<UUID, T> = emptyMap()

        /**
         * The entries of the database, writing serializes with GSON.
         */
        var entries: Map<UUID, T>
            get() {
                synchronized(file) {
                    // File does not exist, therefore not content
                    if (!file.exists())
                        return emptyMap()

                    // File modified since last read, load changes
                    if (file.lastModified() != readTime)
                        JsonReader(file.safeReader()).use {
                            readTime = file.lastModified()
                            readValue = getGson()
                                    .fromJson<List<T>>(it, getListTypeForDeserialization(elementClass.java))
                                    .associateBy(id)
                        }

                    // Return the cached value
                    return readValue
                }
            }
            set(values) {
                synchronized(file) {
                    // Write values
                    file.substitute { sub ->
                        JsonWriter(sub.safeWriter()).use {
                            getGson().toJson(values.values, getListTypeForDeserialization(elementClass.java), it)
                        }
                    }

                    // Cache values and store the write time
                    readTime = file.lastModified()
                    readValue = values
                }
            }

        var items: Collection<T>
            get() = entries.values
            set(values) {
                entries = values.associateBy(id)
            }

        val fileTime get() = if (file.exists()) file.lastModified() else null

        override fun get(i: UUID?) = if (i != null) entries[i] else null

        /**
         * Applies the delta to the store.
         */
        fun apply(abstractDelta: AbstractDelta<T>) {
            // Make new entries from original or new empty map
            val newEntries = if (abstractDelta.clearBeforeInsert)
                hashMapOf()
            else
                entries.toMutableMap()

            // Remove removed entities
            for (d in abstractDelta.deleted)
                newEntries.remove(d)

            // Replace changed entities
            for (c in abstractDelta.changed)
                newEntries[id(c)] = c

            // Transfer value if new.
            if (entries != newEntries)
                entries = newEntries
        }

        /**
         * Deletes the content and resets the values.
         */
        fun delete() {
            synchronized(file) {
                file.delete()
                readTime = null
                readValue = emptyMap()
            }
        }

        fun <U : Comparable<U>> asc(by: (T) -> U) = items.sortedBy(by)

        fun <U : Comparable<U>> desc(by: (T) -> U) = items.sortedByDescending(by)

        val size get() = items.size

        val isPresent get() = file.exists()
    }


    /**
     * Creates a stored value.
     */
    protected inline fun <reified T : Any> storedValue(swaggerStored: Boolean = false) =
            StoredValue(T::class, swaggerStored)


    /**
     * Creates a stored list of values.
     */
    protected inline fun <reified T : Any> storedValues(swaggerStored: Boolean = false) =
            StoredValues(T::class, swaggerStored)

    /**
     * Creates a stored source with the given identity function
     */
    protected inline fun <reified T : Any> storedSource(noinline id: (T) -> UUID) = StoredSource(T::class, id)
}