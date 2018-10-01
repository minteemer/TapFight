package valiev.timur.tapfight.repositories.impl.preferences

import android.content.Context
import android.content.SharedPreferences
import valiev.timur.tapfight.TapFightApplication
import valiev.timur.tapfight.repositories.preferences.GamePreferencesRepository

class GamePreferencesDAO private constructor(preferences: SharedPreferences) :
        BaseSharedPreferences(preferences), GamePreferencesRepository {

    companion object {
        private const val PREFERENCES_FILE_NAME = "game_preferences"

        val instance: GamePreferencesDAO by lazy {
            GamePreferencesDAO(TapFightApplication.instance.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)) }
    }

    override var fightTimeSec : Long by longPreference("fight_time_sec", 10)
}
