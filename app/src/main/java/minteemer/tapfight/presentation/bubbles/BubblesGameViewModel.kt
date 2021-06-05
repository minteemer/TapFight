package minteemer.tapfight.presentation.bubbles

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import minteemer.tapfight.domain.entity.GameEvent
import minteemer.tapfight.domain.entity.Player
import minteemer.tapfight.domain.model.BubblesGameModeModel
import minteemer.tapfight.util.extensions.setValueIfChanged

class BubblesGameViewModel(
    private val gameModel: BubblesGameModeModel = BubblesGameModeModel()
) : ViewModel(), LifecycleObserver {

    val gameState: MutableLiveData<GameState> = MutableLiveData()
    val player1Score: MutableLiveData<Int> = MutableLiveData()
    val player2Score: MutableLiveData<Int> = MutableLiveData()
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
                player1Score.setValueIfChanged(gameEvent.scores[Player.P1])
                player2Score.setValueIfChanged(gameEvent.scores[Player.P2])
            }
            is GameEvent.SpawnBubble -> {
                bubbleSpawnEvents.value = gameEvent.player
            }
            is GameEvent.Started -> {
                gameState.value = GameState.Started(gameEvent.duration)
            }
            GameEvent.GameOver -> {
                gameState.value = GameState.GameOver(
                    player1Score = player1Score.value ?: 0,
                    player2Score = player2Score.value ?: 0
                )
            }
        }
    }

    override fun onCleared() {
        disposables.dispose()
    }

    sealed class GameState {
        class Started(val duration: Long) : GameState()
        class GameOver(val player1Score: Int, val player2Score: Int) : GameState()
    }
}
