package org.eurofurence.connavigator.util.v2

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import kotlin.reflect.KClass
import kotlin.serialization.*

val VALUE_TYPE_UNIT: Byte = 1

val VALUE_TYPE_PARCELABLE: Byte = 2

val VALUE_TYPE_SERIALIZABLE: Byte = 3

/**
 * Serializes to a bundle.
 */
class BundleOutput(val target: Bundle, val root: String) : NamedValueOutput(root) {

    override fun composeName(parentName: String, childName: String) =
            if (parentName.isEmpty())
                childName
            else
                "$parentName.$childName"

    override fun elementName(desc: KSerialClassDesc, index: Int) =
            desc.getElementName(index)

    override fun <T> isNamedSerializableRecursive(name: String, saver: KSerialSaver<T>, value: T) =
            true

    override fun writeNamed(name: String, value: Any) {
        if (value is Parcelable) {
            target.putByte("$name._TYPE", VALUE_TYPE_PARCELABLE)
            target.putParcelable(name, value)
        } else if (value is java.io.Serializable) {
            target.putByte("$name._TYPE", VALUE_TYPE_SERIALIZABLE)
            target.putSerializable(name, value)
        } else
            super.writeNamed(name, value)
    }

    override fun writeNamedBoolean(name: String, value: Boolean) {
        target.putBoolean(name, value)
    }

    override fun writeNamedByte(name: String, value: Byte) {
        target.putByte(name, value)
    }

    override fun writeNamedChar(name: String, value: Char) {
        target.putChar(name, value)
    }

    override fun writeNamedDouble(name: String, value: Double) {
        target.putDouble(name, value)
    }

    override fun <T : Enum<T>> writeNamedEnum(name: String, enumClass: KClass<T>, value: T) {
        target.putSerializable(name, value)
    }

    override fun writeNamedFloat(name: String, value: Float) {
        target.putFloat(name, value)
    }

    override fun writeNamedInt(name: String, value: Int) {
        target.putInt(name, value)
    }

    override fun writeNamedLong(name: String, value: Long) {
        target.putLong(name, value)
    }

    override fun writeNamedNotNullMark(name: String) {
        target.putBoolean("$name._NULLABLE", false)
    }

    override fun writeNamedNull(name: String) {
        target.putBoolean("$name._NULLABLE", true)
    }

    override fun writeNamedShort(name: String, value: Short) {
        target.putShort(name, value)
    }

    override fun writeNamedString(name: String, value: String) {
        target.putString(name, value)
    }

    override fun writeNamedUnit(name: String) {
        target.putByte("$name._TYPE", VALUE_TYPE_UNIT)
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

    override fun readNamed(name: String) =
            when (target.getByte("$name._TYPE")) {
                VALUE_TYPE_UNIT -> Unit
                VALUE_TYPE_PARCELABLE -> target.getParcelable(name)
                VALUE_TYPE_SERIALIZABLE -> target.getSerializable(name)
                else -> super.readNamed(name)
            }

    override fun readNamedBoolean(name: String) =
            target.getBoolean(name)

    override fun readNamedByte(name: String) =
            target.getByte(name)

    override fun readNamedChar(name: String) =
            target.getChar(name)

    override fun readNamedDouble(name: String) =
            target.getDouble(name)

    @Suppress("unchecked_cast")
    override fun <T : Enum<T>> readNamedEnum(name: String, enumClass: KClass<T>) =
            target.getSerializable(name) as T

    override fun readNamedFloat(name: String) =
            target.getFloat(name)

    override fun readNamedInt(name: String) =
            target.getInt(name)

    override fun readNamedLong(name: String) =
            target.getLong(name)

    override fun readNamedNotNullMark(name: String) =
            target.getBoolean("$name._NULLABLE")

    override fun readNamedShort(name: String) =
            target.getShort(name)

    override fun readNamedString(name: String): String =
            target.getString(name)

    override fun readNamedUnit(name: String) {
        if (target.getByte("$name._TYPE") != VALUE_TYPE_UNIT)
            throw IllegalStateException()
    }
}

/**
 * Serializes to an intent.
 */
class IntentOutput(val target: Intent) : NamedValueOutput(target.action) {

    override fun composeName(parentName: String, childName: String) =
            if (parentName.isEmpty())
                childName
            else
                "$parentName.$childName"

    override fun elementName(desc: KSerialClassDesc, index: Int) =
            desc.getElementName(index)

    override fun <T> isNamedSerializableRecursive(name: String, saver: KSerialSaver<T>, value: T) =
            true

    override fun writeNamed(name: String, value: Any) {
        if (value is Parcelable) {
            target.putExtra("$name._TYPE", VALUE_TYPE_PARCELABLE)
            target.putExtra(name, value)
        } else if (value is java.io.Serializable) {
            target.putExtra("$name._TYPE", VALUE_TYPE_SERIALIZABLE)
            target.putExtra(name, value)
        } else
            super.writeNamed(name, value)
    }

    override fun writeNamedBoolean(name: String, value: Boolean) {
        target.putExtra(name, value)
    }

    override fun writeNamedByte(name: String, value: Byte) {
        target.putExtra(name, value)
    }

    override fun writeNamedChar(name: String, value: Char) {
        target.putExtra(name, value)
    }

    override fun writeNamedDouble(name: String, value: Double) {
        target.putExtra(name, value)
    }

    override fun <T : Enum<T>> writeNamedEnum(name: String, enumClass: KClass<T>, value: T) {
        target.putExtra(name, value)
    }

    override fun writeNamedFloat(name: String, value: Float) {
        target.putExtra(name, value)
    }

    override fun writeNamedInt(name: String, value: Int) {
        target.putExtra(name, value)
    }

    override fun writeNamedLong(name: String, value: Long) {
        target.putExtra(name, value)
    }

    override fun writeNamedNotNullMark(name: String) {
        target.putExtra("$name._NULLABLE", false)
    }

    override fun writeNamedNull(name: String) {
        target.putExtra("$name._NULLABLE", true)
    }

    override fun writeNamedShort(name: String, value: Short) {
        target.putExtra(name, value)
    }

    override fun writeNamedString(name: String, value: String) {
        target.putExtra(name, value)
    }

    override fun writeNamedUnit(name: String) {
        target.putExtra("$name._TYPE", VALUE_TYPE_UNIT)
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

    override fun readNamed(name: String) =
            when (target.getByteExtra("$name._TYPE", (-1).toByte())) {
                VALUE_TYPE_UNIT -> Unit
                VALUE_TYPE_PARCELABLE -> target.getParcelableExtra(name)
                VALUE_TYPE_SERIALIZABLE -> target.getSerializableExtra(name)
                else -> super.readNamed(name)
            }

    override fun readNamedBoolean(name: String) =
            target.getBooleanExtra(name, false)

    override fun readNamedByte(name: String) =
            target.getByteExtra(name, 0.toByte())

    override fun readNamedChar(name: String) =
            target.getCharExtra(name, 0.toChar())

    override fun readNamedDouble(name: String) =
            target.getDoubleExtra(name, 0.0)

    @Suppress("unchecked_cast")
    override fun <T : Enum<T>> readNamedEnum(name: String, enumClass: KClass<T>) =
            target.getSerializableExtra(name) as T

    override fun readNamedFloat(name: String) =
            target.getFloatExtra(name, 0.0f)

    override fun readNamedInt(name: String) =
            target.getIntExtra(name, 0)

    override fun readNamedLong(name: String) =
            target.getLongExtra(name, 0L)

    override fun readNamedNotNullMark(name: String) =
            target.getBooleanExtra("$name._NULLABLE", false)

    override fun readNamedShort(name: String) =
            target.getShortExtra(name, 0.toShort())

    override fun readNamedString(name: String): String =
            target.getStringExtra(name)

    override fun readNamedUnit(name: String) {
        if (target.getByteExtra("$name._TYPE", (-1).toByte()) != VALUE_TYPE_UNIT)
            throw IllegalStateException()
    }
}