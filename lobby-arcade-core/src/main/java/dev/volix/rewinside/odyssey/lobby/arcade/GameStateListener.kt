package dev.volix.rewinside.odyssey.lobby.arcade

/**
 * @author Benedikt Wüller
 */
interface GameStateListener {

    fun onStateChange(oldState: GameState, newState: GameState)

}
