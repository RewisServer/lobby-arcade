package dev.volix.rewinside.odyssey.lobby.arcade.setting

/**
 * @author Benedikt Wüller
 */
class SettingsMap {

    val settings: LinkedHashMap<String, Setting<*>> = linkedMapOf()

    val size: Int; get() = this.settings.size

    fun <T> get(key: String): Setting<T>? {
        return this.settings[key] as Setting<T>?
    }

    fun <T> set(setting: Setting<T>) {
        this.settings[setting.key] = setting
    }

}
