package valiev.timur.tapfight.game;

import valiev.timur.tapfight.Player;

public interface GameView {
    void updatePlayerScore(Player player, int score);

    void updateProgressBar(int progress);

    void endGame(int p1Score, int p2Score);
}
