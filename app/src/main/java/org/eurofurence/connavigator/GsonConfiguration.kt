package org.eurofurence.connavigator

import android.util.Log
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import io.swagger.client.JsonUtil
import java.util.*

/**
 * Created by Pazuzu on 25.02.2016.
 */
fun toFirstUpper(s: String?): String? {
    if (s == null || s.length == 0) return s
    return s[0].toUpperCase() + s.substring(1)
}

fun configureGson() {
    val es = object : ExclusionStrategy {
        override fun shouldSkipField(f: FieldAttributes?): Boolean {
            if (f == null)
                return true

            try {
                // If the superclass exposes a field for the given name, skip
                f.declaringClass.superclass.getMethod("get${toFirstUpper(f.name)}")
                println("Skipping ${f.name} from ${f.declaringClass}")
                return true
            } catch(e: NoSuchMethodException) {
                // The attribute is the first instance in the type hierarchy, serialize
                println("Serializing ${f.name} from ${f.declaringClass}")
                return false
            }
        }

        override fun shouldSkipClass(c: Class<*>?): Boolean {
            return false
        }

    }
    println("Configuring JSON")
    JsonUtil.gsonBuilder.addDeserializationExclusionStrategy(es)
    JsonUtil.gsonBuilder.addSerializationExclusionStrategy(es)
}
