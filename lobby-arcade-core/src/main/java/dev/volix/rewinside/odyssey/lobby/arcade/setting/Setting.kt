package dev.volix.rewinside.odyssey.lobby.arcade.setting

/**
 * @author Benedikt Wüller
 */
abstract class Setting<T>(val key: String, val name: String, val description: String, val options: List<SettingOption<T>>, defaultOptionIndex: Int = 0) {

    var selectedOptionIndex = defaultOptionIndex

    fun getSelected() = this.options[this.selectedOptionIndex]

    fun select(identifier: String) {
        val index = this.options.indexOfFirst { it.identifier == identifier }
        if (index < 0) return
        this.selectedOptionIndex = index
    }

}
