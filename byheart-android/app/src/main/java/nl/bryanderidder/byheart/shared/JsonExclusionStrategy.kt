package nl.bryanderidder.byheart.shared

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

/**
 * ExclusionStrategy that checks if a property has an Exclude annotation,
 * if it does the property won't be serialized.
 * @author Bryan de Ridder
 */
object JsonExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>): Boolean {
        return false
    }

    override fun shouldSkipField(field: FieldAttributes): Boolean {
        return field.getAnnotation(ExcludeJson::class.java) != null
    }
}