package org.eurofurence.connavigator.util.extensions

import com.google.common.io.CharSink
import com.google.common.io.CharSource
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.swagger.client.JsonUtil
import io.swagger.client.model.EntityBase
import io.swagger.client.model.EventEntry
import org.joda.time.DateTime
import java.io.Reader
import java.io.Writer
import java.math.BigDecimal

/**
 * Utilities for model related JSON
 */
object gson {
    /**
     * Reads the element from the reader.
     * @param reader The json reader
     * @param classOfT The type to read
     * @return Returns the read element
     */
    fun <T> read(reader: JsonReader, classOfT: Class<T>): T = JsonUtil.getGson().fromJson(reader, classOfT)

    /**
     * Writes an element to the writer.
     * @param it The element to write
     * @param writer The writer to write to
     * @param classOfT The type to use to write
     */
    fun <T> write(it: T, writer: JsonWriter, classOfT: Class<T>) = JsonUtil.getGson().toJson(it, classOfT, writer)

    /**
     * Makes a new JSON reader on the [reader].
     */
    fun reader(reader: Reader) = JsonUtil.getGson().newJsonReader(reader)

    /**
     * Makes a new JSON writer on the [writer].
     */
    fun writer(writer: Writer) = JsonUtil.getGson().newJsonWriter(writer)

    /**
     * Makes a reader on a stream of [source].
     */
    fun reader(source: CharSource) = reader(source.openStream())

    /**
     * Makes a writer on a stream of [sink].
     */
    fun writer(sink: CharSink) = writer(sink.openStream())
}

/**
 * True if the deleted flag of [EntityBase] is equal to [BigDecimal]s `ONE` value.
 */
val EntityBase.deleted: Boolean
    get() = isDeleted == BigDecimal.ONE