package valiev.timur.tapfight.Game;

import java.util.HashMap;

import valiev.timur.tapfight.Player;

public class ModelImpl implements GameModel {

    HashMap<Player, Integer> scores = new HashMap<>();

    public ModelImpl() {
        for (Player p : Player.values())
            scores.put(p, 0);
    }

    @Override
    public int getPlayerScore(Player player) {
        return scores.get(player);
    }

    @Override
    public void incrementPlayerScore(Player player) {
        scores.put(player, scores.get(player) + 1);
    }
}
