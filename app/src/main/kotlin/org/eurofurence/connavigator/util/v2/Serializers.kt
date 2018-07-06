package org.eurofurence.connavigator.util.v2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.RequiresApi
import io.swagger.client.JsonUtil
import kotlinx.serialization.*
import kotlin.reflect.KClass

/**
 * This file contains very versatile serializers for Android Bundles and Intents.
 */

/**
 * Special value type for Unit fields.
 */
val VALUE_TYPE_UNIT: Byte = 1

/**
 * Special value type for parcellable values.
 */
val VALUE_TYPE_PARCELABLE: Byte = 2

/**
 * Special value type for serializable values.
 */
val VALUE_TYPE_SERIALIZABLE: Byte = 3

/**
 * Special value type for single value JSON fields.
 */
val VALUE_TYPE_JSON: Byte = 4

/**
 * Special value type for JSON lists with elements.
 */
val VALUE_TYPE_JSON_LIST: Byte = 5

/**
 * Special value type for JSON lists without detectable element type.
 */
val VALUE_TYPE_JSON_LIST_EMPTY: Byte = 6

/**
 * Writes the value to the intent.
 */
inline fun <reified T : Any> Intent.write(t: T) =
        IntentOutput(this).write(T::class.serializer(), t)

/**
 * Reads the value from the intent.
 */
inline fun <reified T : Any> Intent.read() =
        IntentInput(this).read(T::class.serializer())

/**
 * Writes the value to the bundle with the given root.
 */
inline fun <reified T : Any> Bundle.write(root: String, t: T) =
        BundleOutput(this, root).write(T::class.serializer(), t)

/**
 * Reads the value from the bundle with the given root.
 */
inline fun <reified T : Any> Bundle.read(root: String) =
        BundleInput(this, root).read(T::class.serializer())

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

class BundleOutput(val target: Bundle, val root: String, val defaultJson: Boolean = true) : NamedValueOutput(root) {

    override fun composeName(parentName: String, childName: String) =
            if (parentName.isEmpty())
                childName
            else
                "$parentName.$childName"

    override fun elementName(desc: KSerialClassDesc, index: Int) =
            desc.getElementName(index)


    override fun writeTaggedValue(tag: String, value: Any) {
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

    override fun writeTaggedBoolean(tag: String, value: Boolean) {
        target.putBoolean(tag, value)
    }

    override fun writeTaggedByte(tag: String, value: Byte) {
        target.putByte(tag, value)
    }

    override fun writeTaggedChar(tag: String, value: Char) {
        target.putChar(tag, value)
    }

    override fun writeTaggedDouble(tag: String, value: Double) {
        target.putDouble(tag, value)
    }

    override fun <T : Enum<T>> writeTaggedEnum(tag: String, enumClass: KClass<T>, value: T) {
        target.putSerializable(tag, value)
    }

    override fun writeTaggedFloat(tag: String, value: Float) {
        target.putFloat(tag, value)
    }

    override fun writeTaggedInt(tag: String, value: Int) {
        target.putInt(tag, value)
    }

    override fun writeTaggedLong(tag: String, value: Long) {
        target.putLong(tag, value)
    }

    override fun writeTaggedNotNullMark(tag: String) {
        target.putBoolean("$tag._NULL", false)
    }

    override fun writeTaggedNull(tag: String) {
        target.putBoolean("$tag._NULL", true)
    }

    override fun writeTaggedShort(tag: String, value: Short) {
        target.putShort(tag, value)
    }

    override fun writeTaggedString(tag: String, value: String) {
        target.putString(tag, value)
    }

    override fun writeTaggedUnit(tag: String) {
        target.putByte("$tag._TYPE", VALUE_TYPE_UNIT)
    }
}

/**
 * Deserializes from a bundle.
 */
class BundleInput(val target: Bundle, val root: String) : NamedValueInput(root) {
    override fun composeName(parentName: String, childName: String) =
            if (parentName.isEmpty())
                childName
            else
                "$parentName.$childName"


    override fun elementName(desc: KSerialClassDesc, index: Int) =
            desc.getElementName(index)

    override fun readTaggedValue(tag: String): Any =
            when (target.getByte("$tag._TYPE")) {
            // Base cases
                VALUE_TYPE_UNIT -> Unit
                VALUE_TYPE_PARCELABLE -> target.getParcelable(tag)
                VALUE_TYPE_SERIALIZABLE -> target.getSerializable(tag)

            // Special JSON Deserialization
                VALUE_TYPE_JSON_LIST_EMPTY -> ArrayList<Any>()
                VALUE_TYPE_JSON_LIST -> JsonUtil.deserializeToList(
                        target.getString(tag), Class.forName(target.getString("$tag._CLASS")))
                VALUE_TYPE_JSON -> JsonUtil.deserializeToObject(
                        target.getString(tag), Class.forName(target.getString("$tag._CLASS")))

            // Default to exception
                else -> throw SerializationException("Not supported $tag in ${target.toMapString()}")
            }

    override fun readTaggedBoolean(tag: String) =
            target.getBoolean(tag)

    override fun readTaggedByte(tag: String) =
            target.getByte(tag)

    override fun readTaggedChar(tag: String) =
            target.getChar(tag)

    override fun readTaggedDouble(tag: String) =
            target.getDouble(tag)

    @Suppress("unchecked_cast")
    override fun <T : Enum<T>> readTaggedEnum(tag: String, enumClass: KClass<T>) =
            target.getSerializable(tag) as T

    override fun readTaggedFloat(tag: String) =
            target.getFloat(tag)

    override fun readTaggedInt(tag: String) =
            target.getInt(tag)

    override fun readTaggedLong(tag: String) =
            target.getLong(tag)

    override fun readTaggedNotNullMark(tag: String) =
            !target.getBoolean("$tag._NULL")

    override fun readTaggedShort(tag: String) =
            target.getShort(tag)

    override fun readTaggedString(tag: String): String =
            target.getString(tag)

    override fun readTaggedUnit(tag: String) {
        if (target.getByte("$tag._TYPE") != VALUE_TYPE_UNIT)
            throw IllegalStateException()
    }
}

/**
 * Serializes to an intent.
 */
class IntentOutput(val target: Intent, val defaultJson: Boolean = true) : NamedValueOutput(target.action) {

    override fun composeName(parentName: String, childName: String) =
            if (parentName.isEmpty())
                childName
            else
                "$parentName.$childName"

    override fun elementName(desc: KSerialClassDesc, index: Int) =
            desc.getElementName(index)


    override fun writeTaggedValue(tag: String, value: Any) {
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

    override fun writeTaggedBoolean(tag: String, value: Boolean) {
        target.putExtra(tag, value)
    }

    override fun writeTaggedByte(tag: String, value: Byte) {
        target.putExtra(tag, value)
    }

    override fun writeTaggedChar(tag: String, value: Char) {
        target.putExtra(tag, value)
    }

    override fun writeTaggedDouble(tag: String, value: Double) {
        target.putExtra(tag, value)
    }

    override fun <T : Enum<T>> writeTaggedEnum(tag: String, enumClass: KClass<T>, value: T) {
        target.putExtra(tag, value)
    }

    override fun writeTaggedFloat(tag: String, value: Float) {
        target.putExtra(tag, value)
    }

    override fun writeTaggedInt(tag: String, value: Int) {
        target.putExtra(tag, value)
    }

    override fun writeTaggedLong(tag: String, value: Long) {
        target.putExtra(tag, value)
    }

    override fun writeTaggedNotNullMark(tag: String) {
        target.putExtra("$tag._NULL", false)
    }

    override fun writeTaggedNull(tag: String) {
        target.putExtra("$tag._NULL", true)
    }

    override fun writeTaggedShort(tag: String, value: Short) {
        target.putExtra(tag, value)
    }

    override fun writeTaggedString(tag: String, value: String) {
        target.putExtra(tag, value)
    }

    override fun writeTaggedUnit(tag: String) {
        target.putExtra("$tag._TYPE", VALUE_TYPE_UNIT)
    }
}

/**
 * Deserializes from an intent.
 */
class IntentInput(val target: Intent) : NamedValueInput(target.action) {
    override fun composeName(parentName: String, childName: String) =
            if (parentName.isEmpty())
                childName
            else
                "$parentName.$childName"


    override fun elementName(desc: KSerialClassDesc, index: Int) =
            desc.getElementName(index)

    override fun readTaggedValue(tag: String): Any =
            when (target.getByteExtra("$tag._TYPE", (-1).toByte())) {
            // Base cases
                VALUE_TYPE_UNIT -> Unit
                VALUE_TYPE_PARCELABLE -> target.getParcelableExtra(tag)
                VALUE_TYPE_SERIALIZABLE -> target.getSerializableExtra(tag)

            // Special JSON Deserialization
                VALUE_TYPE_JSON_LIST_EMPTY ->
                    ArrayList<Any>()
                VALUE_TYPE_JSON_LIST -> JsonUtil.deserializeToList(
                        target.getStringExtra(tag), Class.forName(target.getStringExtra("$tag._CLASS")))
                VALUE_TYPE_JSON -> JsonUtil.deserializeToObject(
                        target.getStringExtra(tag), Class.forName(target.getStringExtra("$tag._CLASS")))

            // Default to exception
                else -> throw SerializationException("Not supported $tag in ${target.toMapString()}")
            }

    override fun readTaggedBoolean(tag: String) =
            target.getBooleanExtra(tag, false)

    override fun readTaggedByte(tag: String) =
            target.getByteExtra(tag, 0.toByte())

    override fun readTaggedChar(tag: String) =
            target.getCharExtra(tag, 0.toChar())

    override fun readTaggedDouble(tag: String) =
            target.getDoubleExtra(tag, 0.0)

    @Suppress("unchecked_cast")
    override fun <T : Enum<T>> readTaggedEnum(tag: String, enumClass: KClass<T>) =
            target.getSerializableExtra(tag) as T

    override fun readTaggedFloat(tag: String) =
            target.getFloatExtra(tag, 0.0f)

    override fun readTaggedInt(tag: String) =
            target.getIntExtra(tag, 0)

    override fun readTaggedLong(tag: String) =
            target.getLongExtra(tag, 0L)

    override fun readTaggedNotNullMark(tag: String) =
            !target.getBooleanExtra("$tag._NULL", false)

    override fun readTaggedShort(tag: String) =
            target.getShortExtra(tag, 0.toShort())

    override fun readTaggedString(tag: String): String =
            target.getStringExtra(tag)

    override fun readTaggedUnit(tag: String) {
        if (target.getByteExtra("$tag._TYPE", (-1).toByte()) != VALUE_TYPE_UNIT)
            throw IllegalStateException()
    }
}
