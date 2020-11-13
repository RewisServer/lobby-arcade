package dev.volix.rewinside.odyssey.lobby.arcade

import dev.volix.rewinside.odyssey.common.frames.color.ColorTransformer
import dev.volix.rewinside.odyssey.common.frames.component.Component
import dev.volix.rewinside.odyssey.common.frames.component.CompoundComponent
import dev.volix.rewinside.odyssey.lobby.arcade.component.WaitForInputComponent
import dev.volix.rewinside.odyssey.common.frames.resource.font.FontAdapter
import dev.volix.rewinside.odyssey.lobby.arcade.component.PauseComponent
import java.awt.Dimension
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
abstract class SimpleFrameGame(protected val fontAdapter: FontAdapter,
                               canvasDimensions: Dimension, viewportDimension: Dimension = canvasDimensions,
                               initialUpdateInterval: Long = 0L, transformer: ColorTransformer)
    : FrameGame(canvasDimensions, viewportDimension, initialUpdateInterval, transformer) {

    protected var started: Boolean = false
    var waitForInput: Boolean = true; protected set

    protected var idleComponent = CompoundComponent(Point(), this.canvasDimensions)
    protected var baseComponent = CompoundComponent(Point(), this.canvasDimensions)
    protected var gameOverComponent = CompoundComponent(Point(), this.canvasDimensions)

    private val waitComponent = WaitForInputComponent(Point(), this.canvasDimensions, this.fontAdapter)
    private val pauseComponent = PauseComponent(Point(), this.canvasDimensions, this.fontAdapter, this.idleComponent)

    override fun onUpdate(currentTime: Long, delta: Long): Boolean {
        if (this.state !== GameState.RUNNING) return false
        if (this.waitForInput && !this.started) return false
        return true
    }

    override fun onStateChange(oldState: GameState, newState: GameState) {
        if (oldState !== GameState.IDLE || newState !== GameState.RUNNING) return
        if (!this.waitForInput) {
            this.started = true
            super.handleStart()
            return
        }

        this.started = false
        this.baseComponent.addComponent(this.waitComponent)
    }

    override fun handleKeyDown(key: InputKey, currentTime: Long) {
        if (this.state !== GameState.RUNNING) return

        if (!this.started && this.waitForInput) {
            this.pressedKeys[key] = currentTime
            return
        }

        super.handleKeyDown(key, currentTime)
    }

    override fun handleKeyRepeat(key: InputKey, currentTime: Long) {
        if (this.state !== GameState.RUNNING) return

        if (!this.started && this.waitForInput) {
            this.pressedKeys[key] = currentTime
            return
        }

        super.handleKeyRepeat(key, currentTime)
    }

    override fun handleKeyUp(key: InputKey, currentTime: Long) {
        if (this.state !== GameState.RUNNING) return

        if (this.started || !this.waitForInput) {
            super.handleKeyUp(key, currentTime)
            return
        }

        this.started = true
        this.baseComponent.removeComponent(this.waitComponent)
        super.handleStart()
        this.pressedKeys.remove(key)
    }

    override fun getRenderComponent(state: GameState): Component {
        return when {
            this.state == GameState.DONE -> this.gameOverComponent
            this.state == GameState.IDLE -> this.idleComponent
            this.isPaused -> this.pauseComponent
            else -> this.baseComponent
        }
    }

    override fun handleStart() = Unit

}
