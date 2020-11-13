package dev.volix.rewinside.odyssey.lobby.arcade.standalone

import dev.volix.rewinside.odyssey.lobby.arcade.FrameGame
import dev.volix.rewinside.odyssey.lobby.arcade.FrameGameCreator
import dev.volix.rewinside.odyssey.lobby.arcade.GameState
import dev.volix.rewinside.odyssey.lobby.arcade.InputKey
import java.awt.BorderLayout
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JCheckBox
import javax.swing.JFrame
import kotlin.system.exitProcess

/**
 * @author Benedikt Wüller
 */
class FrameGameApplication(name: String, private val gameCreator: FrameGameCreator) : KeyListener {

    constructor(name: String, creator: () -> FrameGame) : this(name, object : FrameGameCreator {
        override fun create() = creator()
    })

    private var game = this.gameCreator.create()
    private val frame = JFrame("Standalone Game - $name")
    private val viewport = FrameGameViewport(this.frame)

    private var paused = false

    init {
        this.frame.layout = BorderLayout()
        this.frame.isResizable = false
        this.frame.add(this.viewport, BorderLayout.CENTER)

        val debugCheckbox = JCheckBox("Debug Mode", this.viewport.debug)
        debugCheckbox.addChangeListener {
            this.viewport.debug = debugCheckbox.isSelected
            this.viewport.requestFocus()
        }
        this.frame.add(debugCheckbox, BorderLayout.PAGE_END)

        this.viewport.addKeyListener(this)
        this.viewport.debug = false
        this.viewport.requestFocus()
    }

    private fun tick() {
        this.game.tick()
        if (!this.game.hasUpdatedSections()) return

        this.viewport.sections.clear()
        this.viewport.sections.addAll(this.game.pullUpdatedSections())

        this.viewport.currentImage = this.game.getViewport()
        this.viewport.paintImmediately(0, 0, this.viewport.width, this.viewport.height)
    }

    fun start() {
        val loop = Thread {
            while (true) {
                if (Thread.currentThread().isInterrupted) break
                if (this.paused) continue
                this.tick()
            }
        }
        loop.isDaemon = true
        loop.start()

        this.frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                super.windowClosing(e)
                loop.interrupt()
                exitProcess(0)
            }
        })

        this.frame.isVisible = true
        this.frame.pack()
    }

    private fun mapKey(keyCode: Int): InputKey? {
        when (keyCode) {
            KeyEvent.VK_W -> return InputKey.UP
            KeyEvent.VK_A -> return InputKey.LEFT
            KeyEvent.VK_S -> return InputKey.DOWN
            KeyEvent.VK_D -> return InputKey.RIGHT
            KeyEvent.VK_SPACE -> return InputKey.SPACE
        }
        return null
    }

    override fun keyPressed(event: KeyEvent) {
        if (this.game.state == GameState.IDLE) {
            this.game.start()
            return
        }

        if (event.keyCode == KeyEvent.VK_ESCAPE) {
            this.game.stop()
            this.game = this.gameCreator.create()
            return
        }

        if (event.keyCode == KeyEvent.VK_P) {
            if (this.game.isPaused) {
                this.game.resume()
            } else {
                this.game.pause()
            }
        }

        val key = this.mapKey(event.keyCode) ?: return
        this.game.updateInput(key, true)
    }

    override fun keyReleased(event: KeyEvent) {
        val key = this.mapKey(event.keyCode) ?: return
        this.game.updateInput(key, false)
    }

    override fun keyTyped(event: KeyEvent) = Unit

}
