package dev.volix.rewinside.odyssey.lobby.arcade.nbs.song

import dev.volix.rewinside.odyssey.lobby.arcade.nbs.Instrument
import dev.volix.rewinside.odyssey.lobby.arcade.nbs.Note

/**
 * @author Benedikt Wüller
 */
class Song(var ticks: Int, var speed: Double, var noteInterval: Double) {

    private val notes = mutableMapOf<Int, MutableSet<Note>>()

    fun addNote(tick: Int, note: Note) {
        this.notes.getOrPut(tick, { mutableSetOf() })
        this.notes[tick]!!.add(note)
    }

    fun setLayerVolume(layer: Int, volume: Double) {
        this.notes.values.forEach { notes ->
            notes.filter { it.layer == layer }.forEach { it.volume = volume }
        }
    }

    fun getNotes(tick: Int) = this.notes.getOrElse(tick, { setOf<Note>() })

    fun getInstruments(): Set<Instrument> {
        val instruments = mutableSetOf<Instrument>()
        this.notes.values.forEach { it.forEach { note -> instruments.add(note.instrument) } }
        return instruments
    }

    fun getSuggestedMinecraftVersion(): String {
        for (instrument in this.getInstruments()) {
            if (instrument.ordinal >= 10) return "1.14+"
            if (instrument.ordinal >= 5) return "1.12+"
        }
        return "1.8+"
    }

}
