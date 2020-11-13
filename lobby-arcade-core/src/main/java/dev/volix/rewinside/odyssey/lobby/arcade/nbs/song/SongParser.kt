package dev.volix.rewinside.odyssey.lobby.arcade.nbs.song

import dev.volix.rewinside.odyssey.lobby.arcade.nbs.Note
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


/**
 * @author Benedikt Wüller
 */
class SongParser {

    // WARNING: Please be advised to use Safety Pig while exploring the code below.
    //
    //  _._ _..._ .-',     _.._(`))
    // '-. `     '  /-._.-'    ',/
    //    )         \            '.
    //   / _    _    |             \
    //  |  a    a    /              |
    //  \   .-.                     ;
    //   '-('' ).-'       ,'       ;
    //      '-;           |      .'
    //         \           \    /
    //         | 7  .__  _.-\   \
    //         | |  |  ``/  /`  /
    //        /,_|  |   /,_/   /
    //           /,_/      '`-'

    fun parse(file: File) = this.parse(FileInputStream(file))

    fun parse(inputStream: InputStream): Song {
        val dataInputStream = DataInputStream(inputStream)
        var ticks = this.readShort(dataInputStream).toInt()

        val nbsVersion: Int
        if (ticks == 0) {
            nbsVersion = dataInputStream.readByte().toInt()
            dataInputStream.readByte()
            if (nbsVersion >= 3) {
                ticks = this.readShort(dataInputStream).toInt()
            }
        } else {
            nbsVersion = 0
        }

        val layers = this.readShort(dataInputStream).toInt()

        // Read irrelevant data.
        this.skipString(dataInputStream)
        this.skipString(dataInputStream)
        this.skipString(dataInputStream)
        this.skipString(dataInputStream)

        val speed = this.readShort(dataInputStream) / 100.0

        // Read irrelevant data.
        dataInputStream.readBoolean()
        for (i in 0..1) {
            dataInputStream.readByte()
        }
        for (i in 0..4) {
            this.readInt(dataInputStream)
        }
        this.skipString(dataInputStream)

        if (nbsVersion >= 4) {
            dataInputStream.readByte()
            dataInputStream.readByte()
            this.readShort(dataInputStream)
        }

        val song = Song(ticks, speed, 20 / speed * 50)

        var tick = -1
        while (true) {
            val skipTicks = this.readShort(dataInputStream)
            if (skipTicks.toInt() == 0) break
            tick += skipTicks.toInt()
            var layer = -1
            while (true) {
                val skipLayers = this.readShort(dataInputStream)
                if (skipLayers.toInt() == 0) break
                layer += skipLayers.toInt()
                val instrument = dataInputStream.readByte()
                val key = dataInputStream.readByte()
                if (nbsVersion >= 4) {
                    dataInputStream.readByte()
                    dataInputStream.readByte()
                    this.readShort(dataInputStream)
                }
                song.addNote(tick, Note.fromKeyAndInstrument(key.toInt(), instrument.toInt(), layer))
            }
        }

        if (nbsVersion in 1..2) {
            song.ticks = tick
        }

        for (layer in 0 until layers) {
            this.skipString(dataInputStream)
            if (nbsVersion >= 4) {
                dataInputStream.readByte()
            }
            song.setLayerVolume(layer, dataInputStream.readByte() / 100.0)
            if (nbsVersion >= 2) {
                dataInputStream.readByte()
            }
        }

        return song
    }

    private fun readShort(input: DataInputStream) = (input.readUnsignedByte() + (input.readUnsignedByte() shl 8)).toShort()

    private fun readInt(input: DataInputStream) = input.readUnsignedByte() + (input.readUnsignedByte() shl 8) + (input.readUnsignedByte() shl 16) + (input.readUnsignedByte() shl 24)

    private fun skipString(input: DataInputStream) {
        val length = readInt(input)
        for (i in 0 until length) {
            input.readByte()
        }
    }

}
