package valiev.timur.tapfight.game;

import valiev.timur.tapfight.Player;

public interface GameModel {
    // get current score of the given player
    int getPlayerScore(Player player);

    // increment score of the given player and return current score
    void incrementPlayerScore(Player player);
}
