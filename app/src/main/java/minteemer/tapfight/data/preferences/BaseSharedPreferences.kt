package minteemer.tapfight.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

// TODO migrate to new preferences lib
open class BaseSharedPreferences(private val preferences: SharedPreferences) {

    constructor(context: Context, preferencesName: String, mode: Int = Context.MODE_PRIVATE)
        : this(preferences = context.getSharedPreferences(preferencesName, mode))

    protected fun booleanPreference(key: String, defaultValue: Boolean) = PreferenceDelegate(
        { preferences.edit().putBoolean(key, it).apply() },
        { preferences.getBoolean(key, defaultValue) }
    )

    protected fun intPreference(key: String, default: Int) = PreferenceDelegate(
        { preferences.edit().putInt(key, it).apply() },
        { preferences.getInt(key, default) }
    )

    protected fun longPreference(key: String, default: Long) = PreferenceDelegate(
        { preferences.edit().putLong(key, it).apply() },
        { preferences.getLong(key, default) }
    )

    protected fun floatPreference(key: String, default: Float) = PreferenceDelegate(
        { preferences.edit().putFloat(key, it).apply() },
        { preferences.getFloat(key, default) }
    )

    protected fun doublePreference(key: String, default: Double) = PreferenceDelegate(
        { preferences.edit().putLong(key, it.toRawBits()).apply() },
        { Double.fromBits(preferences.getLong(key, default.toRawBits())) }
    )

    protected fun stringPreference(key: String, default: String) = PreferenceDelegate(
        { preferences.edit().putString(key, it).apply() },
        { preferences.getString(key, default) }
    )

    protected fun stringSetPreference(key: String, default: Set<String>) = PreferenceDelegate(
        { preferences.edit().putStringSet(key, it).apply() },
        { preferences.getStringSet(key, default) }
    )

    class PreferenceDelegate<T>(
        private val saveToPreferences: (value: T) -> Unit,
        private val readFromPreferences: () -> T
    ) {
        @Volatile
        private var value: T? = null

        operator fun getValue(preferences: Any, prop: KProperty<*>): T = value ?: initValue()

        operator fun setValue(preferences: Any, prop: KProperty<*>, newValue: T) {
            value = newValue
            saveToPreferences(newValue)
        }

        @Synchronized
        private fun initValue(): T = value ?: readFromPreferences().also { value = it }
    }
}

