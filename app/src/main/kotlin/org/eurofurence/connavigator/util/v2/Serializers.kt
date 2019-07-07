@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.eurofurence.connavigator.util.v2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import io.swagger.client.JsonUtil
import kotlinx.serialization.*
import kotlinx.serialization.internal.EnumDescriptor
import java.util.*

/**
 * This file contains very versatile serializers for Android Bundles and Intents.
 */

/**
 * Special value type for Unit fields.
 */
const val VALUE_TYPE_UNIT: Byte = 1

/**
 * Special value type for parcelable values.
 */
const val VALUE_TYPE_PARCELABLE: Byte = 2

/**
 * Special value type for serializable values.
 */
const val VALUE_TYPE_SERIALIZABLE: Byte = 3

/**
 * Special value type for single value JSON fields.
 */
const val VALUE_TYPE_JSON: Byte = 4

/**
 * Special value type for JSON lists with elements.
 */
const val VALUE_TYPE_JSON_LIST: Byte = 5

/**
 * Special value type for JSON lists without detectable element type.
 */
const val VALUE_TYPE_JSON_LIST_EMPTY: Byte = 6

/**
 * Writes the value to the intent.
 */
@ImplicitReflectionSerializer
inline fun <reified T : Any> Intent.write(t: T) =
        IntentOutput(this).encode(T::class.serializer(), t)

/**
 * Reads the value from the intent.
 */
@ImplicitReflectionSerializer
inline fun <reified T : Any> Intent.read() =
        IntentInput(this).decode(T::class.serializer())

/**
 * Writes the value to the bundle with the given root.
 */
@ImplicitReflectionSerializer
inline fun <reified T : Any> Bundle.write(root: String, t: T) =
        BundleOutput(this, root).encode(T::class.serializer(), t)

/**
 * Reads the value from the bundle with the given root.
 */
@ImplicitReflectionSerializer
inline fun <reified T : Any> Bundle.read(root: String) =
        BundleInput(this, root).decode(T::class.serializer())

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Bundle.toMap() =
        keySet().associate { it to get(it) }

fun Bundle.toMapString() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            toMap().toString()
        else
            toString()

fun Intent.toMapString() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            "Intent {act=$action (${extras?.toMap() ?: emptyMap()}) }"
        else
            toString()

/**
 * Serializes to a bundle.
 */

class BundleOutput(val target: Bundle, val root: String, val defaultJson: Boolean = true) : NamedValueEncoder(root) {

    override fun composeName(parentName: String, childName: String) =
            if (parentName.isEmpty())
                childName
            else
                "$parentName.$childName"

    override fun elementName(desc: KSerialClassDesc, index: Int) =
            desc.getElementName(index)

    override fun encodeTaggedValue(tag: String, value: Any) {
        if (value is Parcelable) {
            target.putByte("$tag._TYPE", VALUE_TYPE_PARCELABLE)
            target.putParcelable(tag, value)
        } else if (value is java.io.Serializable) {
            target.putByte("$tag._TYPE", VALUE_TYPE_SERIALIZABLE)
            target.putSerializable(tag, value)
        } else if (defaultJson && value is List<*> && value.isEmpty()) {
            target.putByte("$tag._TYPE", VALUE_TYPE_JSON_LIST_EMPTY)
            target.putString(tag, JsonUtil.serialize(value))
        } else if (defaultJson && value is List<*>) {
            target.putByte("$tag._TYPE", VALUE_TYPE_JSON_LIST)
            target.putString("$tag._CLASS", value.first()!!.javaClass.name)
            target.putString(tag, JsonUtil.serialize(value))
        } else if (defaultJson) {
            target.putByte("$tag._TYPE", VALUE_TYPE_JSON)
            target.putString("$tag._CLASS", value.javaClass.name)
            target.putString(tag, JsonUtil.serialize(value))
        } else
            throw SerializationException("Not supported $tag=$value in ${target.toMapString()}")
    }

    override fun encodeTaggedBoolean(tag: String, value: Boolean) {
        target.putBoolean(tag, value)
    }

    override fun encodeTaggedByte(tag: String, value: Byte) {
        target.putByte(tag, value)
    }

    override fun encodeTaggedChar(tag: String, value: Char) {
        target.putChar(tag, value)
    }

    override fun encodeTaggedDouble(tag: String, value: Double) {
        target.putDouble(tag, value)
    }

    override fun encodeTaggedEnum(tag: String, enumDescription: EnumDescriptor, ordinal: Int) {
        target.putString(tag, enumDescription.getElementName(ordinal))
    }

    override fun encodeTaggedFloat(tag: String, value: Float) {
        target.putFloat(tag, value)
    }

    override fun encodeTaggedInt(tag: String, value: Int) {
        target.putInt(tag, value)
    }

    override fun encodeTaggedLong(tag: String, value: Long) {
        target.putLong(tag, value)
    }

    override fun encodeTaggedNotNullMark(tag: String) {
        target.putBoolean("$tag._NULL", false)
    }

    override fun encodeTaggedNull(tag: String) {
        target.putBoolean("$tag._NULL", true)
    }

    override fun encodeTaggedShort(tag: String, value: Short) {
        target.putShort(tag, value)
    }

    override fun encodeTaggedString(tag: String, value: String) {
        target.putString(tag, value)
    }

    override fun encodeTaggedUnit(tag: String) {
        target.putByte("$tag._TYPE", VALUE_TYPE_UNIT)
    }
}

/**
 * Deserializes from a bundle.
 */
class BundleInput(val target: Bundle, val root: String) : NamedValueDecoder(root) {
    override fun composeName(parentName: String, childName: String) =
            if (parentName.isEmpty())
                childName
            else
                "$parentName.$childName"


    override fun elementName(desc: KSerialClassDesc, index: Int) =
            desc.getElementName(index)

    override fun decodeTaggedValue(tag: String): Any =
            when (target.getByte("$tag._TYPE")) {
                // Base cases
                VALUE_TYPE_UNIT -> Unit
                VALUE_TYPE_PARCELABLE -> target.getParcelable(tag)
                        ?: error("Should not be null here")
                VALUE_TYPE_SERIALIZABLE -> target.getSerializable(tag)
                        ?: error("Should not be null here")

                // Special JSON Deserialization
                VALUE_TYPE_JSON_LIST_EMPTY -> ArrayList<Any>()
                VALUE_TYPE_JSON_LIST -> JsonUtil.deserializeToList(
                        target.getString(tag), Class.forName(
                        target.getString("$tag._CLASS") ?: error("Should not be null here")))
                VALUE_TYPE_JSON -> JsonUtil.deserializeToObject(
                        target.getString(tag), Class.forName(
                        target.getString("$tag._CLASS") ?: error("Should not be null here")))

                // Default to exception
                else -> throw SerializationException("Not supported $tag in ${target.toMapString()}")
            }

    override fun decodeTaggedBoolean(tag: String) =
            target.getBoolean(tag)

    override fun decodeTaggedByte(tag: String) =
            target.getByte(tag)

    override fun decodeTaggedChar(tag: String) =
            target.getChar(tag)

    override fun decodeTaggedDouble(tag: String) =
            target.getDouble(tag)

    override fun decodeTaggedEnum(tag: String, enumDescription: EnumDescriptor) =
            enumDescription.getElementIndexOrThrow(target.getString(tag) ?: error("Should not be null here"))

    override fun decodeTaggedFloat(tag: String) =
            target.getFloat(tag)

    override fun decodeTaggedInt(tag: String) =
            target.getInt(tag)

    override fun decodeTaggedLong(tag: String) =
            target.getLong(tag)

    override fun decodeTaggedNotNullMark(tag: String) =
            !target.getBoolean("$tag._NULL")

    override fun decodeTaggedShort(tag: String) =
            target.getShort(tag)

    override fun decodeTaggedString(tag: String): String =
            target.getString(tag) ?: error("Should not be null here")

    override fun decodeTaggedUnit(tag: String) {
        if (target.getByte("$tag._TYPE") != VALUE_TYPE_UNIT)
            throw IllegalStateException()
    }
}

/**
 * Serializes to an intent.
 */
class IntentOutput(val target: Intent, val defaultJson: Boolean = true) : NamedValueEncoder(
        target.action ?: error("Should not be null here")) {

    override fun composeName(parentName: String, childName: String) =
            if (parentName.isEmpty())
                childName
            else
                "$parentName.$childName"

    override fun elementName(desc: KSerialClassDesc, index: Int) =
            desc.getElementName(index)

    override fun encodeTaggedValue(tag: String, value: Any) {
        if (value is Parcelable) {
            target.putExtra("$tag._TYPE", VALUE_TYPE_PARCELABLE)
            target.putExtra(tag, value)
        } else if (value is java.io.Serializable) {
            target.putExtra("$tag._TYPE", VALUE_TYPE_SERIALIZABLE)
            target.putExtra(tag, value)
        } else if (defaultJson && value is List<*> && value.isEmpty()) {
            target.putExtra("$tag._TYPE", VALUE_TYPE_JSON_LIST_EMPTY)
            target.putExtra(tag, JsonUtil.serialize(value))
        } else if (defaultJson && value is List<*>) {
            target.putExtra("$tag._TYPE", VALUE_TYPE_JSON_LIST)
            target.putExtra("$tag._CLASS", value.first()!!.javaClass.name)
            target.putExtra(tag, JsonUtil.serialize(value))
        } else if (defaultJson) {
            target.putExtra("$tag._TYPE", VALUE_TYPE_JSON)
            target.putExtra("$tag._CLASS", value.javaClass.name)
            target.putExtra(tag, JsonUtil.serialize(value))
        } else
            throw SerializationException("Not supported $tag=$value in ${target.toMapString()}")
    }

    override fun encodeTaggedBoolean(tag: String, value: Boolean) {
        target.putExtra(tag, value)
    }

    override fun encodeTaggedByte(tag: String, value: Byte) {
        target.putExtra(tag, value)
    }

    override fun encodeTaggedChar(tag: String, value: Char) {
        target.putExtra(tag, value)
    }

    override fun encodeTaggedDouble(tag: String, value: Double) {
        target.putExtra(tag, value)
    }

    override fun encodeTaggedEnum(tag: String, enumDescription: EnumDescriptor, ordinal: Int) {
        target.putExtra(tag, enumDescription.getElementName(ordinal))
    }

    override fun encodeTaggedFloat(tag: String, value: Float) {
        target.putExtra(tag, value)
    }

    override fun encodeTaggedInt(tag: String, value: Int) {
        target.putExtra(tag, value)
    }

    override fun encodeTaggedLong(tag: String, value: Long) {
        target.putExtra(tag, value)
    }

    override fun encodeTaggedNotNullMark(tag: String) {
        target.putExtra("$tag._NULL", false)
    }

    override fun encodeTaggedNull(tag: String) {
        target.putExtra("$tag._NULL", true)
    }

    override fun encodeTaggedShort(tag: String, value: Short) {
        target.putExtra(tag, value)
    }

    override fun encodeTaggedString(tag: String, value: String) {
        target.putExtra(tag, value)
    }

    override fun encodeTaggedUnit(tag: String) {
        target.putExtra("$tag._TYPE", VALUE_TYPE_UNIT)
    }
}

/**
 * Deserializes from an intent.
 */
class IntentInput(val target: Intent) : NamedValueDecoder(
        target.action ?: error("Should not be null here")) {
    override fun composeName(parentName: String, childName: String) =
            if (parentName.isEmpty())
                childName
            else
                "$parentName.$childName"


    override fun elementName(desc: KSerialClassDesc, index: Int) =
            desc.getElementName(index)

    override fun decodeTaggedValue(tag: String): Any =
            when (target.getByteExtra("$tag._TYPE", (-1).toByte())) {
                // Base cases
                VALUE_TYPE_UNIT -> Unit
                VALUE_TYPE_PARCELABLE -> target.getParcelableExtra(tag) ?: error("Should not be null here")
                VALUE_TYPE_SERIALIZABLE -> target.getSerializableExtra(tag) ?: error("Should not be null here")

                // Special JSON Deserialization
                VALUE_TYPE_JSON_LIST_EMPTY ->
                    ArrayList<Any>()
                VALUE_TYPE_JSON_LIST -> JsonUtil.deserializeToList(
                        target.getStringExtra(tag), Class.forName(
                        target.getStringExtra("$tag._CLASS") ?: error("Should not be null here")))
                VALUE_TYPE_JSON -> JsonUtil.deserializeToObject(
                        target.getStringExtra(tag), Class.forName(
                        target.getStringExtra("$tag._CLASS") ?: error("Should not be null here")))

                // Default to exception
                else -> throw SerializationException("Not supported $tag in ${target.toMapString()}")
            }

    override fun decodeTaggedBoolean(tag: String) =
            target.getBooleanExtra(tag, false)

    override fun decodeTaggedByte(tag: String) =
            target.getByteExtra(tag, 0.toByte())

    override fun decodeTaggedChar(tag: String) =
            target.getCharExtra(tag, 0.toChar())

    override fun decodeTaggedDouble(tag: String) =
            target.getDoubleExtra(tag, 0.0)

    override fun decodeTaggedEnum(tag: String, enumDescription: EnumDescriptor) =
            enumDescription.getElementIndexOrThrow(target.getStringExtra(tag) ?: error("Should not be null here"))

    override fun decodeTaggedFloat(tag: String) =
            target.getFloatExtra(tag, 0.0f)

    override fun decodeTaggedInt(tag: String) =
            target.getIntExtra(tag, 0)

    override fun decodeTaggedLong(tag: String) =
            target.getLongExtra(tag, 0L)

    override fun decodeTaggedNotNullMark(tag: String) =
            !target.getBooleanExtra("$tag._NULL", false)

    override fun decodeTaggedShort(tag: String) =
            target.getShortExtra(tag, 0.toShort())

    override fun decodeTaggedString(tag: String): String =
            target.getStringExtra(tag) ?: error("Should not be null here")

    override fun decodeTaggedUnit(tag: String) {
        if (target.getByteExtra("$tag._TYPE", (-1).toByte()) != VALUE_TYPE_UNIT)
            throw IllegalStateException()
    }
}

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    override fun serialize(output: Encoder, obj: Date) {
        output.encodeLong(obj.time)
    }

    override fun deserialize(input: Decoder): Date {
        return Date(input.decodeLong())
    }
}