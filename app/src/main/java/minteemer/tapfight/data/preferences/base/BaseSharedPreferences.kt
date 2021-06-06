package minteemer.tapfight.data.preferences.base

import android.content.Context
import android.content.SharedPreferences

// TODO migrate to DataStore?
open class BaseSharedPreferences(
    context: Context,
    preferencesName: String,
    mode: Int = Context.MODE_PRIVATE
) {

    protected val preferences: SharedPreferences = context.getSharedPreferences(preferencesName, mode)
}

