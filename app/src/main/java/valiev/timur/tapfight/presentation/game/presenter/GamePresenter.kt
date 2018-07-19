package valiev.timur.tapfight.presentation.game.presenter

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import valiev.timur.tapfight.domain.entities.PlayerId
import valiev.timur.tapfight.domain.interactors.GameInteractor
import valiev.timur.tapfight.presentation.game.view.GameView


class GamePresenter(private val view: GameView) {

    private val interactor = GameInteractor()

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

        view.startTimerAnimation()
    }

    fun tapBtn(player: PlayerId) {
        view.updatePlayerScore(player, interactor.tap(player))
    }
}
