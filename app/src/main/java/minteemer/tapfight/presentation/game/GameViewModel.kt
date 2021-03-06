package minteemer.tapfight.presentation.game

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import minteemer.tapfight.domain.entities.Player
import minteemer.tapfight.domain.interactors.GameModel
import minteemer.tapfight.repositories.impl.preferences.GamePreferencesRepositoryImpl
import minteemer.tapfight.repositories.preferences.GamePreferencesRepository
import minteemer.tapfight.util.updateValue

class GameViewModel(
    private val gameModel: GameModel = GameModel(),
    private val gamePreferencesRepository: GamePreferencesRepository = GamePreferencesRepositoryImpl.INSTANCE
) : ViewModel(), LifecycleObserver {

    val gameState: MutableLiveData<GameState> = MutableLiveData()
    val player1Score: MutableLiveData<Int> = MutableLiveData()
    val player2Score: MutableLiveData<Int> = MutableLiveData()

    private val disposables = CompositeDisposable()

    fun onTap(player: Player) {
        gameModel.tap(player)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startGame() {
        val gameDuration = gamePreferencesRepository.fightTimeSec

        gameState.value = GameState.Started(gameDuration)
        gameModel.runGame(gameDuration)
            .toFlowable(BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { scores ->
                    player1Score.updateValue(scores[Player.P1] ?: 0)
                    player2Score.updateValue(scores[Player.P2] ?: 0)
                },
                onComplete = {
                    gameState.value = GameState.GameOver(
                        player1Score = player1Score.value ?: 0,
                        player2Score = player2Score.value ?: 0
                    )
                },
                onError = { Log.e("GameActivity", "Game timer error", it) } // TODO Timber?
            )
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
    }

    sealed class GameState {
        class Started(val duration: Long) : GameState()
        class GameOver(val player1Score: Int, val player2Score: Int) : GameState()
    }
}
