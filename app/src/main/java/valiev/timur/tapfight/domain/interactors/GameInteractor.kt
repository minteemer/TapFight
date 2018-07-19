package valiev.timur.tapfight.domain.interactors

import io.reactivex.Single
import valiev.timur.tapfight.domain.entities.PlayerId
import valiev.timur.tapfight.repositories.impl.preferences.GamePreferencesDAO
import valiev.timur.tapfight.repositories.preferences.GamePreferencesRepository
import java.util.concurrent.TimeUnit

class GameInteractor {

    private val preferences: GamePreferencesRepository = GamePreferencesDAO.instance

    private val score: HashMap<PlayerId, Int> = HashMap()

    /**
     * Starts game timer
     * @returns score of the players by the end of the game
     */
    fun startGame(): Single<Map<PlayerId, Int>> {
        PlayerId.values().forEach { score[it] = 0 }

        return Single.timer(preferences.fightTimeSec, TimeUnit.SECONDS)
                .map { score }
    }

    /**
     * Increments score of [player]
     * @returns new score of the player
     */
    fun tap(player: PlayerId): Int =
            score[player]?.let { (it + 1).apply { score[player] = this } } ?: 0 // lol

}


