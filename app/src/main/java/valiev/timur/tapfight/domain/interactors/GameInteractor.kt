package valiev.timur.tapfight.domain.interactors

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
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
            score[player]?.let { oldScore ->
                (oldScore + 1).also { newScore ->
                    score[player] = newScore
                    playersScore.onNext(player to newScore)
                }
            } ?: 0

    fun getUserScoreObservable() : Observable<Pair<PlayerId, Int>> = playersScore

    private val playersScore: BehaviorSubject<Pair<PlayerId, Int>> = BehaviorSubject.create()
}


