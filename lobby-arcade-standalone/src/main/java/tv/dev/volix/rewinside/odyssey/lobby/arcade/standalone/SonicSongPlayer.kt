package tv.dev.volix.rewinside.odyssey.lobby.arcade.standalone

import dev.volix.rewinside.odyssey.lobby.arcade.nbs.Note
import dev.volix.rewinside.odyssey.lobby.arcade.nbs.song.Song
import dev.volix.rewinside.odyssey.lobby.arcade.nbs.song.SongPlayer
import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem

/**
 * @author Benedikt Wüller
 */
class SonicSongPlayer(song: Song) : SongPlayer(song) {

    override fun onPlayTick(tick: Int) = Unit

    override fun playNote(note: Note, tick: Int) {
        try {
            val stream = this.javaClass.classLoader.getResourceAsStream("sounds/${note.instrument.fileName}") ?: return
            Sonic.play(AudioSystem.getAudioInputStream(BufferedInputStream(stream)), 1.0f, note.pitch.toFloat(), note.volume.toFloat())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

}
