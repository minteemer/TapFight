package minteemer.tapfight.domain.entity

class MutableScores : Scores {

    private val scores: IntArray = Player.values().map { 0 }.toIntArray()

    override operator fun get(player: Player): Int = scores[player.ordinal]

    operator fun set(player: Player, score: Int) {
        scores[player.ordinal] = score
    }

    override fun toString(): String = Player.values().joinToString(prefix = "[", postfix = "]") { player -> "$player: ${get(player)}" }
}
