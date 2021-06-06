package minteemer.tapfight.data.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import minteemer.tapfight.data.preferences.base.BaseSharedPreferences
import minteemer.tapfight.util.extensions.int
import minteemer.tapfight.util.extensions.long
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamePreferences @Inject constructor(
    @ApplicationContext context: Context
) : BaseSharedPreferences(context, preferencesName = PREFERENCES_FILE_NAME) {

    var bubblesTimeoutSec: Long by preferences.long(key = "bubbles_timeout_sec", default = 20)

    var speedTapTimeoutSec: Long by preferences.long(key = "speed_tap_timout_sec", default = 10)

    var bubbles: Int by preferences.int(key = "bubbles_number", default = 3)
        private set

    companion object {

        private const val PREFERENCES_FILE_NAME = "game_preferences"
    }
}
