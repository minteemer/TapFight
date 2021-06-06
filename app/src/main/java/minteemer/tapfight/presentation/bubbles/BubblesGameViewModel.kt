package minteemer.tapfight.presentation.bubbles

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import minteemer.tapfight.domain.entity.GameEvent
import minteemer.tapfight.domain.entity.MutableScores
import minteemer.tapfight.domain.entity.Player
import minteemer.tapfight.domain.entity.Scores
import minteemer.tapfight.domain.model.BubblesGameModeModel
import javax.inject.Inject

@HiltViewModel
class BubblesGameViewModel @Inject constructor(
    private val gameModel: BubblesGameModeModel
) : ViewModel(), LifecycleObserver {

    // TODO migrate to StateFlow
    val gameState: MutableLiveData<GameState> = MutableLiveData()
    val scores: MutableLiveData<Scores> = MutableLiveData()
    val bubbleSpawnEvents: MutableLiveData<Player> = MutableLiveData()

    fun onBubbleTap(player: Player) {
        viewModelScope.launch { gameModel.bubbleTap(player) }
    }

    fun onBubbleTimeout(player: Player) {
        viewModelScope.launch { gameModel.bubbleTimeout(player) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startGame() {
        viewModelScope.launch(Dispatchers.Main) {
            gameModel.runGame()
                .conflate()
                .onEach { event -> Log.d("GameEvent", event.toString()) }
                .catch { error -> Log.e("GameActivity", error.message, error) }
                .flowOn(Dispatchers.Default)
                .collect(::handleGameEvent)
        }
    }

    private fun handleGameEvent(gameEvent: GameEvent) {
        when (gameEvent) {
            is GameEvent.ScoreUpdated -> {
                scores.value = gameEvent.scores
            }
            is GameEvent.SpawnBubble -> {
                bubbleSpawnEvents.value = gameEvent.player
            }
            is GameEvent.Started -> {
                gameState.value = GameState.Started(gameEvent.duration)
            }
            GameEvent.GameOver -> {
                gameState.value = GameState.GameOver(scores.value ?: MutableScores())
            }
        }
    }

    sealed class GameState {
        class Started(val duration: Long) : GameState()
        class GameOver(val scores: Scores) : GameState()
    }
}
