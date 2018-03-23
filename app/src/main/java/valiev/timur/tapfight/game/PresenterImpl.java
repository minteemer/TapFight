package valiev.timur.tapfight.game;

import valiev.timur.tapfight.Player;

import static java.lang.Thread.sleep;

public class PresenterImpl implements GamePresenter {
    static final int FIGHT_TIME = 10 * 1000;
    private final int PROGRESS_BAR_UPDATE_TIME = 30;
    private long startTime;

    private GameView view;
    private GameModel model;


    PresenterImpl(GameView view, GameModel model) {
        this.view = view;
        this.model = model;

        startTime = System.currentTimeMillis();
        new Thread(new GameTimer()).start();
    }

    private class GameTimer implements Runnable {
        @Override
        public void run() {
            int passedTime;
            do {
                passedTime = (int) (System.currentTimeMillis() - startTime);
                view.updateProgressBar(passedTime);
                try {
                    sleep(PROGRESS_BAR_UPDATE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (passedTime < FIGHT_TIME);

            view.endGame(model.getPlayerScore(Player.P1), model.getPlayerScore(Player.P2));
        }
    }

    @Override
    public void tapBtn(Player player) {
        model.incrementPlayerScore(player);
        view.updatePlayerScore(player, model.getPlayerScore(player));
    }
}
