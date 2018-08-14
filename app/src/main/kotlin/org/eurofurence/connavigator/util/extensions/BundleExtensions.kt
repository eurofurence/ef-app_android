@file:Suppress("unused")

package org.eurofurence.connavigator.util.extensions

import android.os.Bundle
import io.swagger.client.JsonUtil
import java.io.Serializable

/**
 * Checks if the bundle has the given extra defined.
 */
operator fun Bundle.contains(key: String) = this.containsKey(key)

/**
 * Gets the corresponding string extra or null if not present.
 */
operator fun Bundle.get(key: String) = if (key in this) this.getString(key) else null

/**
 * Puts a string into the bundle or removes it if null specified
 */
operator fun Bundle.set(key: String, value: String?) {
    if (value == null)
        this.remove(key)
    else
        this.putString(key, value)
}

/**
 * Sets the context to booleans, [get] will return a boolean.
 */
val Bundle.booleans: BundleContextBoolean
    get() = BundleContextBoolean(this)

class BundleContextBoolean(val bundle: Bundle) {
    /**
     * Gets the corresponding boolean extra or null if not present.
     */
    operator fun get(key: String) = bundle.getBoolean(key, false)

    /**
     * Puts a boolean into the bundle or removes it if null specified
     */
    operator fun set(key: String, value: Boolean?) {
        if (value == null)
            bundle.remove(key)
        else
            bundle.putBoolean(key, value)
    }
}


/**
 * Sets the context to booleans, [get] will return an integer
 */
val Bundle.ints: BundleContextInt
    get() = BundleContextInt(this)

class BundleContextInt(val bundle: Bundle) {
    /**
     * Gets the corresponding integer extra or null if not present.
     */
    operator fun get(key: String) = if (key in bundle) bundle.getInt(key, 0) else null

    /**
     * Puts an integer into the bundle or removes it if null specified
     */
    operator fun set(key: String, value: Int?) {
        if (value == null)
            bundle.remove(key)
        else
            bundle.putInt(key, value)
    }
}

/**
 * Sets the context to booleans, [get] will return a float.
 */
val Bundle.floats: BundleContextFloat
    get() = BundleContextFloat(this)

class BundleContextFloat(val bundle: Bundle) {
    /**
     * Gets the corresponding float extra or null if not present.
     */
    operator fun get(key: String) = if (key in bundle) bundle.getFloat(key, 0.0f) else null

    /**
     * Puts a float into the bundle or removes it if null specified
     */
    operator fun set(key: String, value: Float?) {
        if (value == null)
            bundle.remove(key)
        else
            bundle.putFloat(key, value)
    }
}

/**
 * Sets the context to booleans, [get] will return a double.
 */
val Bundle.doubles: BundleContextDouble
    get() = BundleContextDouble(this)

class BundleContextDouble(val bundle: Bundle) {
    /**
     * Gets the corresponding double extra or null if not present.
     */
    operator fun get(key: String) = if (key in bundle) bundle.getDouble(key, 0.0) else null

    /**
     * Puts a double into the bundle or removes it if null specified
     */
    operator fun set(key: String, value: Double?) {
        if (value == null)
            bundle.remove(key)
        else
            bundle.putDouble(key, value)
    }
}

/**
 * Sets the context to booleans, [get] will return a object, they need to be serializable, however.
 */
val Bundle.objects: BundleContextObjects
    get() = BundleContextObjects(this)

class BundleContextObjects(val bundle: Bundle) {
    /**
     * Gets the corresponding object extra or null if not present.
     */
    operator fun get(key: String) = if (key in bundle) bundle.getSerializable(key) else null

    /**
     * Gets the corresponding object extra or null if not present.
     */
    operator fun <T : Serializable> get(key: String, classOfT: Class<T>) = get(key).let {
        if (classOfT.isInstance(it))
            classOfT.cast(it)
        else null
    }

    /**
     * Puts a serializable into the bundle or removes it if null specified
     */
    operator fun set(key: String, value: Serializable?) {
        if (value == null)
            bundle.remove(key)
        else
            bundle.putSerializable(key, value)
    }
}

/**
 * Sets the context to JSON objects, [get] will return a object, they need to be JSON serializable object, however.
 */
val Bundle.jsonObjects: BundleContextJsonObjects
    get() = BundleContextJsonObjects(this)

class BundleContextJsonObjects(val bundle: Bundle) {
    /**
     * Gets the corresponding object extra or null if not present.
     */
    inline operator fun <reified T> get(key: String): T =
            JsonUtil.deserializeToObject<T>(bundle.getString(key), T::class.java)

    /**
     * Puts an object into the bundle or removes it if null specified
     */
    operator fun set(key: String, value: Any?) {
        if (value == null)
            bundle.remove(key)
        else
            bundle.putString(key, JsonUtil.serialize(value))
    }
}

/**
 * Sets the context to JSON objects, [get] will return a object, they need to be JSON serializable object, however.
 */
val Bundle.jsonLists: BundleContextJsonLists
    get() = BundleContextJsonLists(this)

class BundleContextJsonLists(val bundle: Bundle) {
    /**
     * Gets the corresponding object extra or null if not present.
     */
    inline operator fun <reified T, reified U : List<T>> get(key: String): U =
            JsonUtil.deserializeToList<T>(bundle.getString(key), T::class.java) as U

    /**
     * Puts an object into the bundle or removes it if null specified
     */
    operator fun set(key: String, value: Any?) {
        if (value == null)
            bundle.remove(key)
        else
            bundle.putString(key, JsonUtil.serialize(value))
    }
}