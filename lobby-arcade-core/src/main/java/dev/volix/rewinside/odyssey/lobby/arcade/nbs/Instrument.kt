package dev.volix.rewinside.odyssey.lobby.arcade.nbs

/**
 * @author Benedikt Wüller
 */
enum class Instrument(val baseName: String, val soundIds: Array<String>) {

    // 1.8+
    HARP("harp", arrayOf("block.note_block.harp", "block.note.harp", "note.harp")),
    BASS("dbass", arrayOf("block.note_block.bass", "block.note.bass", "note.bassattack")),
    BASE_DRUM("bdrum", arrayOf("block.note_block.basedrum", "block.note.basedrum", "note.bd")),
    SNARE_DRUM("sdrum", arrayOf("block.note_block.snare", "block.note.snare", "note.snare")),
    HAT("click", arrayOf("block.note_block.hat", "block.note.hat", "note.hat")),

    // 1.12+
    GUITAR("guitar", arrayOf("block.note_block.guitar", "block.note.guitar")),
    FLUTE("flute", arrayOf("block.note_block.flute", "block.note.flute")),
    BELL("bell", arrayOf("block.note_block.bell", "block.note.bell")),
    CHIME("icechime", arrayOf("block.note_block.chime", "block.note.chime")),
    XYLOPHONE("xylobone", arrayOf("block.note_block.xylophone", "block.note.xylophone")),

    // 1.14+
    IRON_XYLOPHONE("iron_xylophone", arrayOf("block.note_block.iron_xylophone")),
    COW_BELL("cow_bell", arrayOf("block.note_block.cow_bell")),
    DIDGERIDOO("didgeridoo", arrayOf("block.note_block.didgeridoo")),
    BIT("bit", arrayOf("block.note_block.bit")),
    BANJO("banjo", arrayOf("block.note_block.banjo")),
    PLING("pling", arrayOf("block.note_block.pling"));

    companion object {
        val VERSION_STEPS = intArrayOf(
            477, // 1.14 and up
            335, // 1.12 and up
            28 // 1.2 and up
        )
    }

    val fileName = "${this.baseName}.wav"

    fun getSoundId(protocolVersion: Int): String {
        for (i in VERSION_STEPS.indices) {
            val version: Int = VERSION_STEPS[i]
            if (protocolVersion < version || soundIds.size <= i) continue
            return soundIds[i]
        }

        // If no sound is available for the given protocol version, use the first
        // one in case the user is using a resource pack for compatibility.
        return soundIds[0]
    }

}
