package dev.volix.rewinside.odyssey.lobby.arcade.setting

/**
 * @author Benedikt Wüller
 */
class BooleanSetting(key: String, name: String, description: String, selectedOptionIndex: Int = 0)
    : Setting<Boolean>(key, name, description, listOf(SettingOption("true", "Aktiviert", true), SettingOption("false", "Deaktiviert", false)), selectedOptionIndex)
