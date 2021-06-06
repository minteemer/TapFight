package minteemer.tapfight.presentation.bubbles

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
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

    val gameState: MutableLiveData<GameState> = MutableLiveData()
    val scores: MutableLiveData<Scores> = MutableLiveData()
    val bubbleSpawnEvents: MutableLiveData<Player> = MutableLiveData()

    private val disposables = CompositeDisposable()

    fun onBubbleTap(player: Player) {
        gameModel.bubbleTap(player)
    }

    fun onBubbleTimeout(player: Player) {
        gameModel.bubbleTimeout(player)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startGame() {
        gameModel.runGame()
            .toFlowable(BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { gameEvent -> handleGameEvent(gameEvent) },
                onError = { Log.e("GameActivity", "Game timer error", it) } // TODO Timber, RxPlugin onError
            )
            .addTo(disposables)
    }

    private fun handleGameEvent(gameEvent: GameEvent) {
        Log.d("game", gameEvent::class.java.simpleName)
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

    override fun onCleared() {
        disposables.dispose()
    }

    sealed class GameState {
        class Started(val duration: Long) : GameState()
        class GameOver(val scores: Scores) : GameState()
    }
}
