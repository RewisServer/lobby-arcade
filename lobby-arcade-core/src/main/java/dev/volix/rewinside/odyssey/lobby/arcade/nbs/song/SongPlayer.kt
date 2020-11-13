package dev.volix.rewinside.odyssey.lobby.arcade.nbs.song

import dev.volix.rewinside.odyssey.lobby.arcade.nbs.Note

/**
 * @author Benedikt Wüller
 */
abstract class SongPlayer(private val song: Song) {

    var playbackRate: Double = 1.0
    var currentTick: Long = 0

    var looping: Boolean = false
    var loopDelay: Int = 0

    private var thread: Thread? = null

    @JvmOverloads
    fun play(delay: Long = 0) {
        if (this.isPlaying()) return

        this.thread = Thread {
            try {
                val noteInterval = song.noteInterval * (1.0 / playbackRate)
                Thread.sleep(delay)

                val startedAt = System.currentTimeMillis()
                val firstTick = currentTick

                while (!Thread.currentThread().isInterrupted) {
                    val targetDelta = ((currentTick - firstTick) * noteInterval).toLong()

                    this.updateTick()

                    val delta = System.currentTimeMillis() - startedAt
                    Thread.sleep(0L.coerceAtLeast(targetDelta - delta))
                }
            } catch (ignored: InterruptedException) {}
        }

        this.thread?.start()
    }

    fun stop() {
        if (!this.isPlaying()) return
        this.thread?.interrupt()
        this.thread = null
    }

    fun isPlaying() = this.thread != null

    fun playTick(tick: Long, triggerCallback: Boolean) {
        if (tick < 0 || tick >= this.song.ticks + 1) return

        if (triggerCallback) {
            this.onPlayTick(tick.toInt())
        }

        this.getNotes(tick.toInt()).forEach { this.playNote(it, tick.toInt()) }
    }

    private fun updateTick() {
        if (!this.looping && this.currentTick > this.song.ticks + 1) {
            stop()
            return
        }

        playTick(this.currentTick % (this.song.ticks + 2 + this.loopDelay), true)
        this.currentTick++
    }

    private fun getNotes(tick: Int): Collection<Note> = this.song.getNotes(tick)

    protected abstract fun onPlayTick(tick: Int)

    protected abstract fun playNote(note: Note, tick: Int)

}
