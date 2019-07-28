package nl.bryanderidder.byheart.shared

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

object JsonExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>): Boolean {
        return false
    }

    override fun shouldSkipField(field: FieldAttributes): Boolean {
        return field.getAnnotation(Exclude::class.java) != null
    }
}