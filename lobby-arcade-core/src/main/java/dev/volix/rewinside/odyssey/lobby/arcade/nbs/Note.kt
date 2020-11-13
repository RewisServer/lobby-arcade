package dev.volix.rewinside.odyssey.lobby.arcade.nbs

import kotlin.math.pow

/**
 * @author Benedikt Wüller
 */
data class Note(val key: Key, val octave: Octave, val instrument: Instrument, val pitch: Double, val layer: Int) {

    var volume: Double = 0.0

    companion object {
        fun fromKeyAndInstrument(key: Int, instrument: Int, layer: Int): Note {
            val ordinalKey = key % 12
            val ordinalOctave = (key + 9) / 12

            var pitchedKey = key
            while (pitchedKey < 33) pitchedKey += 12
            while (pitchedKey > 57) pitchedKey -= 12

            val pitch = 2.0.pow((pitchedKey - 33 - 12) / 12.0)
            return Note(Key.values()[ordinalKey], Octave.values()[ordinalOctave], Instrument.values()[instrument], pitch, layer)
        }
    }

}
