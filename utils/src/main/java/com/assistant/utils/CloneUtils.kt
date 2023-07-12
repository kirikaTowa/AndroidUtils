package com.assistant.utils

import com.assistant.utils.UtilsBridge.fromJson
import com.assistant.utils.UtilsBridge.toJson
import java.lang.reflect.Type

object CloneUtils {

    /**
     * Deep clone.
     *
     * @param data The data.
     * @param type The type.
     * @param <T>  The value type.
     * @return The object of cloned.
    </T> */
    fun <T> deepClone(data: T, type: Type?): T? {
        return try {
            fromJson<T>(toJson(data), type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
