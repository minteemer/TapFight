package minteemer.tapfight.domain.interactors

import io.reactivex.Observable
import io.reactivex.rxkotlin.cast
import io.reactivex.subjects.PublishSubject
import minteemer.tapfight.domain.entities.Player
import java.util.concurrent.TimeUnit

class GameModel {

    private val taps: PublishSubject<Player> = PublishSubject.create()

    fun tap(player: Player) {
        taps.onNext(player)
    }

    fun runGame(gameDurationSec: Long): Observable<Map<Player, Int>> =
        taps.hide()
            .scan(initScores()) { scores, player ->
                scores[player] = (scores[player] ?: 0) + 1
                scores
            }
            .completeAfter(gameDurationSec, TimeUnit.SECONDS)
            .cast()

    private fun initScores(): MutableMap<Player, Int> =
        HashMap<Player, Int>(Player.values().size).apply {
            Player.values().forEach { player -> set(player, 0) }
        }

    private fun <T> Observable<T>.completeAfter(delay: Long, timeUnit: TimeUnit) =
        takeUntil(Observable.empty<T>().delay(delay, timeUnit))

}


