package nl.bryanderidder.byheart.shared

import android.content.Context
import android.content.SharedPreferences

/**
 * Wrapper for SharedPreferences. All keys are added as global properties.
 * Note that this is an object, all methods and properties are static.
 * @author Bryan de Ridder
 */
object Preferences {
    private var pref: SharedPreferences? = null
    private const val KEY_REHEARSAL_REVERSE = "REHEARSAL_REVERSE"
    private const val KEY_REHEARSAL_SHUFFLE = "REHEARSAL_SHUFFLE"
    private const val KEY_REHEARSAL_TYPED = "REHEARSAL_TYPED"
    private const val KEY_REHEARSAL_PRONOUNCE = "REHEARSAL_PRONOUNCE"
    const val KEY_NOT_FIRST_START = "NOT_FIRST_START"
    const val KEY_DARK_MODE = "DARK_MODE"
    const val KEY_REHEARSAL_MULTIPLE_CHOICE = "REHEARSAL_MULTIPLE_CHOICE"
    const val KEY_REHEARSAL_MEMORY = "REHEARSAL_MEMORY"
    val DARK_MODE: Boolean get() = this.read(KEY_DARK_MODE)
    val REHEARSAL_REVERSE: Boolean get() = this.read(KEY_REHEARSAL_REVERSE)
    val REHEARSAL_MULTIPLE_CHOICE: Boolean get() = this.read(KEY_REHEARSAL_MULTIPLE_CHOICE)
    val REHEARSAL_SHUFFLE: Boolean get() = this.read(KEY_REHEARSAL_SHUFFLE)
    val REHEARSAL_TYPED: Boolean get() = this.read(KEY_REHEARSAL_TYPED)
    val REHEARSAL_MEMORY: Boolean get() = this.read(KEY_REHEARSAL_MEMORY)
    val REHEARSAL_PRONOUNCE: Boolean get() = this.read(KEY_REHEARSAL_PRONOUNCE)
    val NOT_FIRST_START: Boolean get() = this.read(KEY_NOT_FIRST_START)

    fun init(context: Context) {
        if (pref == null)
            pref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    fun read(key: String, default: String): String? = pref?.getString(key.toUpperCase(), default)

    fun write(key: String, value: String): Unit = pref!!.edit().putString(key.toUpperCase(), value).apply()

    fun read(key: String, default: Boolean = false): Boolean = pref!!.getBoolean(key.toUpperCase(), default)

    fun write(key: String, value: Boolean): Unit = pref!!.edit().putBoolean(key.toUpperCase(), value).apply()

    fun read(key: String, default: Int): Int = pref!!.getInt(key.toUpperCase(), default)

    fun write(key: String, value: Int?): Unit = pref!!.edit().putInt(key.toUpperCase(), value!!).apply()

    fun toggle(key:String): Unit = pref!!.edit().putBoolean(key.toUpperCase(), !read(key, false)).apply()
}