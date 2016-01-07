package sleepmachine.interfaces;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public interface Playable {
    int playcount = 0;
    boolean create(Duration duration);
    void startplayback();
    void playnextfile();
    void play();
    void pause();
    void stop();
    void endofplayback();
    MediaPlayer getCurrentPlayer();
    void setCurrentPlayer(MediaPlayer mediaPlayer);
}
