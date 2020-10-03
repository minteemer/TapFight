package valiev.timur.tapfight.presentation.game.presenter

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import valiev.timur.tapfight.domain.entities.PlayerId
import valiev.timur.tapfight.domain.interactors.GameInteractor
import valiev.timur.tapfight.presentation.game.view.GameView


class GamePresenter(private val view: GameView) {

    private val interactor = GameInteractor()

    private val disposables = CompositeDisposable()

    fun startGame() {
        interactor.startGame()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view.endGame(it[PlayerId.P1] ?: 0, it[PlayerId.P2] ?: 1)
                        },
                        {
                            Log.e("GameActivity", "Game timer error", it)
                        }
                )
                .also { disposables.add(it) }

        view.startTimerAnimation()

        view.getTapObservable()
                .observeOn(Schedulers.computation())
                .subscribe { interactor.tap(it) }
                .also { disposables.add(it) }

        interactor.userScoreObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (playerId, score) ->
                    view.updatePlayerScore(playerId, score)
                }
                .also { disposables.add(it) }
    }

    fun destroy() {
        disposables.dispose()
    }
}
