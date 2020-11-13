package dev.volix.rewinside.odyssey.lobby.arcade

/**
 * @author Benedikt Wüller
 */
data class KeyMapping(val left: InputKey = InputKey.LEFT, val right: InputKey = InputKey.RIGHT,
                      val up: InputKey = InputKey.UP, val down: InputKey = InputKey.DOWN,
                      val space: InputKey = InputKey.SPACE) {

    fun getMappedKey(key: InputKey): InputKey {
        return when(key) {
            InputKey.LEFT -> this.left
            InputKey.UP -> this.up
            InputKey.DOWN -> this.down
            InputKey.RIGHT -> this.right
            InputKey.SPACE -> this.space
        }
    }
}
