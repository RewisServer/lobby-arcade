package dev.volix.rewinside.odyssey.lobby.arcade.setting

/**
 * @author Benedikt Wüller
 */
data class SettingOption<T>(val identifier: String, val name: String, val value: T)
