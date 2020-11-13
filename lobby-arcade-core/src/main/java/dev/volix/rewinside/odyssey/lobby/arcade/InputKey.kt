package dev.volix.rewinside.odyssey.lobby.arcade

/**
 * @author Benedikt Wüller
 */
enum class InputKey(val direction: Direction?, val displayName: String) {

    LEFT(Direction.WEST, "A"),
    UP(Direction.NORTH, "W"),
    DOWN(Direction.SOUTH, "S"),
    RIGHT(Direction.EAST, "D"),
    SPACE(null, "Leertaste"),

}
