@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.eurofurence.connavigator.util.v2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.RequiresApi
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
            "Intent {act=$action (${extras.toMap()}) }"
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

    override fun encodeTaggedValue(name: String, value: Any) {
        if (value is Parcelable) {
            target.putByte("$name._TYPE", VALUE_TYPE_PARCELABLE)
            target.putParcelable(name, value)
        } else if (value is java.io.Serializable) {
            target.putByte("$name._TYPE", VALUE_TYPE_SERIALIZABLE)
            target.putSerializable(name, value)
        } else if (defaultJson && value is List<*> && value.isEmpty()) {
            target.putByte("$name._TYPE", VALUE_TYPE_JSON_LIST_EMPTY)
            target.putString(name, JsonUtil.serialize(value))
        } else if (defaultJson && value is List<*>) {
            target.putByte("$name._TYPE", VALUE_TYPE_JSON_LIST)
            target.putString("$name._CLASS", value.first()!!.javaClass.name)
            target.putString(name, JsonUtil.serialize(value))
        } else if (defaultJson) {
            target.putByte("$name._TYPE", VALUE_TYPE_JSON)
            target.putString("$name._CLASS", value.javaClass.name)
            target.putString(name, JsonUtil.serialize(value))
        } else
            throw SerializationException("Not supported $name=$value in ${target.toMapString()}")
    }

    override fun encodeTaggedBoolean(name: String, value: Boolean) {
        target.putBoolean(name, value)
    }

    override fun encodeTaggedByte(name: String, value: Byte) {
        target.putByte(name, value)
    }

    override fun encodeTaggedChar(name: String, value: Char) {
        target.putChar(name, value)
    }

    override fun encodeTaggedDouble(name: String, value: Double) {
        target.putDouble(name, value)
    }

    override fun encodeTaggedEnum(tag: String, enumDescription: EnumDescriptor, ordinal: Int) {
        target.putString(tag, enumDescription.getElementName(ordinal))
    }


    override fun encodeTaggedFloat(name: String, value: Float) {
        target.putFloat(name, value)
    }

    override fun encodeTaggedInt(name: String, value: Int) {
        target.putInt(name, value)
    }

    override fun encodeTaggedLong(name: String, value: Long) {
        target.putLong(name, value)
    }

    override fun encodeTaggedNotNullMark(name: String) {
        target.putBoolean("$name._NULL", false)
    }

    override fun encodeTaggedNull(name: String) {
        target.putBoolean("$name._NULL", true)
    }

    override fun encodeTaggedShort(name: String, value: Short) {
        target.putShort(name, value)
    }

    override fun encodeTaggedString(name: String, value: String) {
        target.putString(name, value)
    }

    override fun encodeTaggedUnit(name: String) {
        target.putByte("$name._TYPE", VALUE_TYPE_UNIT)
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

    override fun decodeTaggedValue(name: String): Any =
            when (target.getByte("$name._TYPE")) {
                // Base cases
                VALUE_TYPE_UNIT -> Unit
                VALUE_TYPE_PARCELABLE -> target.getParcelable(name)
                VALUE_TYPE_SERIALIZABLE -> target.getSerializable(name)

                // Special JSON Deserialization
                VALUE_TYPE_JSON_LIST_EMPTY -> ArrayList<Any>()
                VALUE_TYPE_JSON_LIST -> JsonUtil.deserializeToList(
                        target.getString(name), Class.forName(target.getString("$name._CLASS")))
                VALUE_TYPE_JSON -> JsonUtil.deserializeToObject(
                        target.getString(name), Class.forName(target.getString("$name._CLASS")))

                // Default to exception
                else -> throw SerializationException("Not supported $name in ${target.toMapString()}")
            }

    override fun decodeTaggedBoolean(tag: String) =
            target.getBoolean(tag)

    override fun decodeTaggedByte(name: String) =
            target.getByte(name)

    override fun decodeTaggedChar(name: String) =
            target.getChar(name)

    override fun decodeTaggedDouble(name: String) =
            target.getDouble(name)

    override fun decodeTaggedEnum(tag: String, enumDescription: EnumDescriptor) =
            enumDescription.getElementIndexOrThrow(target.getString(tag))

    override fun decodeTaggedFloat(name: String) =
            target.getFloat(name)

    override fun decodeTaggedInt(name: String) =
            target.getInt(name)

    override fun decodeTaggedLong(name: String) =
            target.getLong(name)

    override fun decodeTaggedNotNullMark(name: String) =
            !target.getBoolean("$name._NULL")

    override fun decodeTaggedShort(name: String) =
            target.getShort(name)

    override fun decodeTaggedString(name: String): String =
            target.getString(name)

    override fun decodeTaggedUnit(name: String) {
        if (target.getByte("$name._TYPE") != VALUE_TYPE_UNIT)
            throw IllegalStateException()
    }
}

/**
 * Serializes to an intent.
 */
class IntentOutput(val target: Intent, val defaultJson: Boolean = true) : NamedValueEncoder(target.action) {

    override fun composeName(parentName: String, childName: String) =
            if (parentName.isEmpty())
                childName
            else
                "$parentName.$childName"

    override fun elementName(desc: KSerialClassDesc, index: Int) =
            desc.getElementName(index)

    override fun encodeTaggedValue(name: String, value: Any) {
        if (value is Parcelable) {
            target.putExtra("$name._TYPE", VALUE_TYPE_PARCELABLE)
            target.putExtra(name, value)
        } else if (value is java.io.Serializable) {
            target.putExtra("$name._TYPE", VALUE_TYPE_SERIALIZABLE)
            target.putExtra(name, value)
        } else if (defaultJson && value is List<*> && value.isEmpty()) {
            target.putExtra("$name._TYPE", VALUE_TYPE_JSON_LIST_EMPTY)
            target.putExtra(name, JsonUtil.serialize(value))
        } else if (defaultJson && value is List<*>) {
            target.putExtra("$name._TYPE", VALUE_TYPE_JSON_LIST)
            target.putExtra("$name._CLASS", value.first()!!.javaClass.name)
            target.putExtra(name, JsonUtil.serialize(value))
        } else if (defaultJson) {
            target.putExtra("$name._TYPE", VALUE_TYPE_JSON)
            target.putExtra("$name._CLASS", value.javaClass.name)
            target.putExtra(name, JsonUtil.serialize(value))
        } else
            throw SerializationException("Not supported $name=$value in ${target.toMapString()}")
    }

    override fun encodeTaggedBoolean(name: String, value: Boolean) {
        target.putExtra(name, value)
    }

    override fun encodeTaggedByte(name: String, value: Byte) {
        target.putExtra(name, value)
    }

    override fun encodeTaggedChar(name: String, value: Char) {
        target.putExtra(name, value)
    }

    override fun encodeTaggedDouble(name: String, value: Double) {
        target.putExtra(name, value)
    }

    override fun encodeTaggedEnum(tag: String, enumDescription: EnumDescriptor, ordinal: Int) {
        target.putExtra(tag, enumDescription.getElementName(ordinal))
    }

    override fun encodeTaggedFloat(name: String, value: Float) {
        target.putExtra(name, value)
    }

    override fun encodeTaggedInt(name: String, value: Int) {
        target.putExtra(name, value)
    }

    override fun encodeTaggedLong(name: String, value: Long) {
        target.putExtra(name, value)
    }

    override fun encodeTaggedNotNullMark(name: String) {
        target.putExtra("$name._NULL", false)
    }

    override fun encodeTaggedNull(name: String) {
        target.putExtra("$name._NULL", true)
    }

    override fun encodeTaggedShort(name: String, value: Short) {
        target.putExtra(name, value)
    }

    override fun encodeTaggedString(name: String, value: String) {
        target.putExtra(name, value)
    }

    override fun encodeTaggedUnit(name: String) {
        target.putExtra("$name._TYPE", VALUE_TYPE_UNIT)
    }
}

/**
 * Deserializes from an intent.
 */
class IntentInput(val target: Intent) : NamedValueDecoder(target.action) {
    override fun composeName(parentName: String, childName: String) =
            if (parentName.isEmpty())
                childName
            else
                "$parentName.$childName"


    override fun elementName(desc: KSerialClassDesc, index: Int) =
            desc.getElementName(index)

    override fun decodeTaggedValue(name: String): Any =
            when (target.getByteExtra("$name._TYPE", (-1).toByte())) {
                // Base cases
                VALUE_TYPE_UNIT -> Unit
                VALUE_TYPE_PARCELABLE -> target.getParcelableExtra(name)
                VALUE_TYPE_SERIALIZABLE -> target.getSerializableExtra(name)

                // Special JSON Deserialization
                VALUE_TYPE_JSON_LIST_EMPTY ->
                    ArrayList<Any>()
                VALUE_TYPE_JSON_LIST -> JsonUtil.deserializeToList(
                        target.getStringExtra(name), Class.forName(target.getStringExtra("$name._CLASS")))
                VALUE_TYPE_JSON -> JsonUtil.deserializeToObject(
                        target.getStringExtra(name), Class.forName(target.getStringExtra("$name._CLASS")))

                // Default to exception
                else -> throw SerializationException("Not supported $name in ${target.toMapString()}")
            }

    override fun decodeTaggedBoolean(name: String) =
            target.getBooleanExtra(name, false)

    override fun decodeTaggedByte(name: String) =
            target.getByteExtra(name, 0.toByte())

    override fun decodeTaggedChar(name: String) =
            target.getCharExtra(name, 0.toChar())

    override fun decodeTaggedDouble(name: String) =
            target.getDoubleExtra(name, 0.0)

    override fun decodeTaggedEnum(tag: String, enumDescription: EnumDescriptor) =
            enumDescription.getElementIndexOrThrow(target.getStringExtra(tag))

    override fun decodeTaggedFloat(name: String) =
            target.getFloatExtra(name, 0.0f)

    override fun decodeTaggedInt(name: String) =
            target.getIntExtra(name, 0)

    override fun decodeTaggedLong(name: String) =
            target.getLongExtra(name, 0L)

    override fun decodeTaggedNotNullMark(name: String) =
            !target.getBooleanExtra("$name._NULL", false)

    override fun decodeTaggedShort(name: String) =
            target.getShortExtra(name, 0.toShort())

    override fun decodeTaggedString(name: String): String =
            target.getStringExtra(name)

    override fun decodeTaggedUnit(name: String) {
        if (target.getByteExtra("$name._TYPE", (-1).toByte()) != VALUE_TYPE_UNIT)
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