package minteemer.tapfight.domain.model

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.withTimeoutOrNull
import minteemer.tapfight.domain.entity.GameEvent
import minteemer.tapfight.domain.entity.MutableScores
import minteemer.tapfight.domain.entity.Player
import minteemer.tapfight.repository.config.GameConfigRepository
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class BubblesGameModeModel @Inject constructor(
    private val gameConfig: GameConfigRepository
) {
    private val taps: MutableSharedFlow<Player> = MutableSharedFlow()
    private val bubbleTimeouts: MutableSharedFlow<Player> = MutableSharedFlow()

    /**
     * Registers tap of bubble by [player]
     */
    suspend fun bubbleTap(player: Player) {
        taps.emit(player)
    }

    /**
     * Registers timeout of bubble of [player]
     */
    suspend fun bubbleTimeout(player: Player) {
        bubbleTimeouts.emit(player)
    }

    /**
     * Starts new game and emits [GameEvent]s. Completes once the game is over.
     */
    fun runGame(): Flow<GameEvent> = flow {
        emit(GameEvent.Started(gameConfig.bubblesGameModeDurationSec))

        withTimeoutOrNull(gameConfig.bubblesGameModeDurationSec * 1000) {
            emitAll(startGame())
        }

        emit(GameEvent.GameOver)
    }

    private fun startGame() = merge(
        scoreTracking(taps),
        taps.map { player -> GameEvent.SpawnBubble(player) },
        bubbleTimeouts.map { player -> GameEvent.SpawnBubble(player) },
        initialBubbleSpawn(gameConfig.initialBubbleSpawnDelayMills, gameConfig.bubbles),
    )

    private fun scoreTracking(tapSource: Flow<Player>): Flow<GameEvent.ScoreUpdated> =
        tapSource
            .scan(MutableScores()) { scores, player ->
                scores[player] += 1
                scores
            }
            .map { scores -> GameEvent.ScoreUpdated(scores) }

    private fun initialBubbleSpawn(intervalMills: Long, bubbles: Int): Flow<GameEvent.SpawnBubble> =
        flow {
            repeat(bubbles) {
                delay(intervalMills)
                Player.values().forEach { player ->
                    emit(GameEvent.SpawnBubble(player))
                }
            }
        }
}
