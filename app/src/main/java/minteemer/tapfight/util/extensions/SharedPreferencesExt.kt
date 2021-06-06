package minteemer.tapfight.util.extensions

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author t.valiev
 */
fun SharedPreferences.boolean(key: String, defaultValue: Boolean): ReadWriteProperty<Any, Boolean> =
    PreferenceDelegate(
        { edit().putBoolean(key, it).apply() },
        { getBoolean(key, defaultValue) }
    )

fun SharedPreferences.int(key: String, default: Int): ReadWriteProperty<Any, Int> =
    PreferenceDelegate(
        { edit().putInt(key, it).apply() },
        { getInt(key, default) }
    )

fun SharedPreferences.long(key: String, default: Long): ReadWriteProperty<Any, Long> =
    PreferenceDelegate(
        { edit().putLong(key, it).apply() },
        { getLong(key, default) }
    )

fun SharedPreferences.float(key: String, default: Float): ReadWriteProperty<Any, Float> =
    PreferenceDelegate(
        { edit().putFloat(key, it).apply() },
        { getFloat(key, default) }
    )

fun SharedPreferences.double(key: String, default: Double): ReadWriteProperty<Any, Double> =
    PreferenceDelegate(
        { edit().putLong(key, it.toRawBits()).apply() },
        { Double.fromBits(getLong(key, default.toRawBits())) }
    )

fun SharedPreferences.string(key: String, default: String): ReadWriteProperty<Any, String> =
    PreferenceDelegate(
        { edit().putString(key, it).apply() },
        { getString(key, default)!! }
    )

fun SharedPreferences.stringSet(key: String, default: Set<String>): ReadWriteProperty<Any, Set<String>> =
    PreferenceDelegate(
        { edit().putStringSet(key, it).apply() },
        { getStringSet(key, default)!! }
    )

private class PreferenceDelegate<T : Any>(
    private val saveToPreferences: (value: T) -> Unit,
    private val readFromPreferences: () -> T
) : ReadWriteProperty<Any, T> {

    @Volatile
    private var value: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T = value ?: initValue()

    @Synchronized
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
        saveToPreferences(value)
    }

    @Synchronized
    private fun initValue(): T = value ?: readFromPreferences().also { value = it }
}

