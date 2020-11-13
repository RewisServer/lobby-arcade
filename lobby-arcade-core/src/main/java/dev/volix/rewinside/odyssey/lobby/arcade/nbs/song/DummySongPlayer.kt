package dev.volix.rewinside.odyssey.lobby.arcade.nbs.song

import dev.volix.rewinside.odyssey.lobby.arcade.nbs.Note

/**
 * @author Benedikt Wüller
 */
class DummySongPlayer : SongPlayer(Song(1, 1.0, 1.0)) {

    override fun onPlayTick(tick: Int) = Unit
    override fun playNote(note: Note, tick: Int) = Unit

}
