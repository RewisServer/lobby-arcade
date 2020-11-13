package dev.volix.rewinside.odyssey.lobby.arcade.nbs.song;

import dev.volix.rewinside.odyssey.lobby.arcade.FrameGame;

/**
 * @author Benedikt Wüller
 */
public interface SongPlayerCreator {

    SongPlayer createPlayer(final FrameGame game, final Song song);

}
