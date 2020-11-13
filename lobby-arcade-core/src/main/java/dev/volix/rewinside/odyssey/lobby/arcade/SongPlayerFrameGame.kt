package dev.volix.rewinside.odyssey.lobby.arcade

import dev.volix.rewinside.odyssey.common.frames.color.ColorTransformer
import dev.volix.rewinside.odyssey.common.frames.resource.font.FontAdapter
import dev.volix.rewinside.odyssey.lobby.arcade.nbs.song.SongPlayer
import java.awt.Dimension

/**
 * @author Benedikt Wüller
 */
abstract class SongPlayerFrameGame(fontAdapter: FontAdapter, canvasDimensions: Dimension, viewportDimension: Dimension = canvasDimensions,
                          initialUpdateInterval: Long = 0L, transformer: ColorTransformer, private val songPlayer: SongPlayer)
    : SimpleFrameGame(fontAdapter, canvasDimensions, viewportDimension, initialUpdateInterval, transformer) {

    private var songPlayerStarted = false

    init {
        this.songPlayer.looping = true
        this.songPlayer.loopDelay = 5
    }

    override fun onStart() {
        super.onStart()
        this.songPlayer.play()
        this.songPlayerStarted = true
    }

    override fun onPause() {
        super.onPause()
        this.songPlayer.stop()
    }

    override fun onResume() {
        super.onResume()
        if (!this.songPlayerStarted) return
        this.songPlayer.play()
    }

    override fun onStateChange(oldState: GameState, newState: GameState) {
        super.onStateChange(oldState, newState)
        if (newState == GameState.RUNNING) return
        this.songPlayer.stop()
    }

}
