package minteemer.tapfight.domain.entity

interface Scores {
    operator fun get(player: Player): Int
}