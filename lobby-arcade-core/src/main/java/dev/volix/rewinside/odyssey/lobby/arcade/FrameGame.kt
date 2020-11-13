package dev.volix.rewinside.odyssey.lobby.arcade

import dev.volix.rewinside.odyssey.common.frames.Frame
import dev.volix.rewinside.odyssey.common.frames.color.ColorTransformer
import dev.volix.rewinside.odyssey.common.frames.component.Component
import dev.volix.rewinside.odyssey.lobby.arcade.setting.KeyMappingSetting
import dev.volix.rewinside.odyssey.lobby.arcade.setting.Setting
import dev.volix.rewinside.odyssey.lobby.arcade.setting.SettingOption
import dev.volix.rewinside.odyssey.lobby.arcade.setting.SettingsMap
import java.awt.Dimension
import java.util.function.Function

/**
 * @author Benedikt Wüller
 */
abstract class FrameGame(canvasDimensions: Dimension, viewportDimension: Dimension = canvasDimensions,
                         initialUpdateInterval: Long = 0L, transformer: ColorTransformer)
    : Frame(canvasDimensions, viewportDimension, initialUpdateInterval, transformer) {

    companion object {
        val SETTING_KEY_MAPPING = "key_mapping"
    }

    private var lastState = GameState.IDLE

    var state = GameState.IDLE
        set(value) {
            if (field != value) {
                this.stateChangedAt = System.currentTimeMillis()
                this.onStateChange(field, value)
                this.gameStateListeners.forEach { it.onStateChange(field, value) }
            }

            field = value
        }

    var stateChangedAt: Long = 0; private set

    val settings = SettingsMap()

    protected var allowSimultaneousInputs = true
    protected var keyRepeatInterval = 250L

    private val currentKeys = mutableSetOf<InputKey>()
    protected val pressedKeys = mutableMapOf<InputKey, Long>()

    private val listeners = mutableListOf<FrameGameListener>()
    private val gameStateListeners = mutableSetOf<GameStateListener>()
    private val inputDescriptions = mutableMapOf<InputKey, String>()

    private val keyMapping: KeyMapping; get() = this.settings.get<KeyMapping>(SETTING_KEY_MAPPING)!!.getSelected().value

    init {
        this.settings.set(KeyMappingSetting(
                SETTING_KEY_MAPPING,
                "Steuerung",
                "Du steuerst mit den gleichen Tasten, mit denen du dich in Minecraft bewegst. §fWASD §7und §fLeertaste §7beziehen sich dabei auf die Standard-Steuerung.",
                this.getKeyMappings()
        ))
    }

    override fun onTick(currentTime: Long, delta: Long) {
        if (this.state != GameState.RUNNING) return

        synchronized(this.currentKeys) {
            for (key in InputKey.values()) {
                val isPressed = this.currentKeys.contains(key)

                // FIXME: this will break when mapping more than one input key to the same output key.
                val mappedKey = this.keyMapping.getMappedKey(key)

                val wasPressed = this.pressedKeys.contains(mappedKey)
                if (!isPressed && !wasPressed) continue

                if (isPressed && wasPressed) {
                    // Check if a new key repeat event is due.
                    val lastPressedAt = this.pressedKeys[mappedKey] ?: currentTime
                    if ((currentTime - lastPressedAt) < this.keyRepeatInterval) continue
                    this.handleKeyRepeat(mappedKey, currentTime)
                } else if (isPressed) {
                    // Key has been pressed.
                    this.handleKeyDown(mappedKey, currentTime)
                } else {
                    // Key has been released.
                    this.handleKeyUp(mappedKey, currentTime)
                }
            }
        }
    }

    protected open fun handleKeyRepeat(key: InputKey, currentTime: Long) {
        this.pressedKeys[key] = currentTime
        this.onKeyRepeat(key, currentTime)
    }

    protected open fun handleKeyDown(key: InputKey, currentTime: Long) {
        this.onKeyDown(key, currentTime)
        this.pressedKeys[key] = currentTime
    }

    protected open fun handleKeyUp(key: InputKey, currentTime: Long) {
        this.onKeyUp(key, currentTime)
        this.pressedKeys.remove(key)
    }

    override fun update(currentTime: Long, delta: Long): Boolean {
        // If this game has just started, trigger the callback.
        if (this.lastState != this.state && this.state == GameState.RUNNING) {
            this.handleStart();
        }

        // If the state changes to/from IDLE or DONE, always trigger an update.
        if (this.lastState != this.state && this.state != GameState.RUNNING) return true
        this.lastState = this.state
        return super.update(currentTime, delta)
    }

    override fun onPause() {
        super.onPause()
        this.listeners.forEach { it.onPause() }
    }

    override fun onResume() {
        super.onResume()
        this.listeners.forEach { it.onResume() }
    }

    fun start(): Boolean {
        if (this.state != GameState.IDLE) return false
        this.state = GameState.RUNNING
        return true
    }

    fun stop(): Boolean {
        if (this.state == GameState.IDLE) return false
        this.state = GameState.IDLE
        return true
    }

    fun updateInput(key: InputKey, pressed: Boolean): Boolean {
        if (this.isPaused) return false
        if (this.state != GameState.RUNNING) return false

        synchronized(this.currentKeys) {
            if (!pressed) {
                return this.currentKeys.remove(key)
            }

            if (!this.allowSimultaneousInputs && this.currentKeys.isNotEmpty()) return false
            this.currentKeys.add(key)
            return true
        }
    }

    fun addGameStateListener(listener: GameStateListener) = this.gameStateListeners.add(listener)

    fun removeGameStateListener(listener: GameStateListener) = this.gameStateListeners.remove(listener)

    fun addListener(listener: FrameGameListener) = this.listeners.add(listener)

    fun removeListener(listener: FrameGameListener) = this.listeners.remove(listener)

    override fun getRenderComponent() = this.getRenderComponent(this.state)

    protected fun setInputDescription(key: InputKey, description: String) {
        this.inputDescriptions[key] = description
    }

    fun getInputDescription(key: InputKey): String? {
        val mappedKey = this.keyMapping.getMappedKey(key)
        return this.inputDescriptions[mappedKey]
    }

    protected open fun handleStart() {
        this.listeners.forEach { it.onStart() }
        this.onStart()
    }

    protected open fun onStart() {}

    protected abstract fun getRenderComponent(state: GameState): Component

    protected open fun onKeyDown(key: InputKey, currentTime: Long) {}

    protected open fun onKeyUp(key: InputKey, currentTime: Long) {}

    protected open fun onKeyRepeat(key: InputKey, currentTime: Long) {}

    protected open fun onStateChange(oldState: GameState, newState: GameState) {}

    protected open fun getKeyMappings() = listOf(SettingOption("default", "Standard", KeyMapping()))

}
