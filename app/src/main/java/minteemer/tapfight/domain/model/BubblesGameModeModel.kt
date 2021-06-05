package minteemer.tapfight.domain.model

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import minteemer.tapfight.domain.entity.GameEvent
import minteemer.tapfight.domain.entity.MutableScores
import minteemer.tapfight.domain.entity.Player
import minteemer.tapfight.repository.config.GameConfigRepository
import minteemer.tapfight.repository.config.GameConfigRepositoryImpl
import minteemer.tapfight.util.extensions.completeAfter
import java.util.concurrent.TimeUnit

class BubblesGameModeModel(
    private val gameConfig: GameConfigRepository = GameConfigRepositoryImpl()
) {
    private val taps: PublishSubject<Player> = PublishSubject.create()
    private val bubbleTimeouts: PublishSubject<Player> = PublishSubject.create()

    /**
     * Registers tap of bubble by [player]
     */
    fun bubbleTap(player: Player) {
        taps.onNext(player)
    }

    /**
     * Registers timeout of bubble of [player]
     */
    fun bubbleTimeout(player: Player) {
        bubbleTimeouts.onNext(player)
    }

    /**
     * Starts new game and emits game events. Completes once game is over.
     * @see GameEvent
     */
    fun runGame(): Observable<GameEvent> =
        Observable.concat(
            Observable.just<GameEvent>(GameEvent.Started(gameConfig.bubblesTimeoutSec)),
            startGame(),
            Observable.just(GameEvent.GameOver)
        )

    private fun startGame(): Observable<GameEvent> {
        val timedTapSource = taps.hide().completeAfter(gameConfig.bubblesTimeoutSec, TimeUnit.SECONDS)

        return Observable.merge(
            scoreTracking(timedTapSource).subscribeOn(Schedulers.computation()),
            bubbleRespawn(timedTapSource).subscribeOn(Schedulers.computation()),
            bubbleRespawn(bubbleTimeouts.hide()).subscribeOn(Schedulers.computation()),
            initialBubbleSpawn(gameConfig.initialBubbleSpawnDelayMills).subscribeOn(Schedulers.computation())
        )
    }

    private fun scoreTracking(tapSource: Observable<Player>): Observable<GameEvent.ScoreUpdated> =
        tapSource
            .scan(MutableScores()) { scores, player ->
                scores[player] += 1
                scores
            }
            .map { scores -> GameEvent.ScoreUpdated(scores) }

    private fun bubbleRespawn(tapSource: Observable<Player>): Observable<GameEvent.SpawnBubble> =
        tapSource.map { player -> GameEvent.SpawnBubble(player) }

    private fun initialBubbleSpawn(intervalMills: Long): Observable<GameEvent.SpawnBubble> =
        Observable.interval(intervalMills, TimeUnit.MILLISECONDS)
            .take(gameConfig.bubbles.toLong())
            .flatMap {
                Observable.fromIterable(Player.values().map { GameEvent.SpawnBubble(it) })
            }
}


