package minteemer.tapfight.domain.entity

interface Scores {
    operator fun get(player: Player): Int
}

val Scores.player1
    get() = get(Player.P1)

val Scores.player2
    get() = get(Player.P2)
