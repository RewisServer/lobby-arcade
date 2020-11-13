package dev.volix.rewinside.odyssey.lobby.arcade.setting

import dev.volix.rewinside.odyssey.lobby.arcade.KeyMapping

/**
 * @author Benedikt Wüller
 */
class KeyMappingSetting(key: String, name: String, description: String, options: List<SettingOption<KeyMapping>>, selectedOptionIndex: Int = 0)
    : Setting<KeyMapping>(key, name, description, options, selectedOptionIndex)
