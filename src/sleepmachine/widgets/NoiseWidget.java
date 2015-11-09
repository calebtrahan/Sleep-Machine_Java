package sleepmachine.widgets;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import sleepmachine.util.xml.Noise;

import java.util.ArrayList;

public class NoiseWidget implements Widget, Playable {
    private CheckBox OnOffSwitch;
    private ChoiceBox Category;
    private ChoiceBox Selection;
    private TextArea Description;
    private Button PreviewButton;
    private MediaPlayer currentplayer;
    private int playcount = 0;
    private ArrayList<Media> sessionmedia;
    private Noise selectednoise;

    public NoiseWidget(CheckBox onOffSwitch, ChoiceBox category, ChoiceBox selection, TextArea description, Button previewButton) {
        OnOffSwitch = onOffSwitch;
        Category = category;
        Selection = selection;
        Description = description;
        PreviewButton = previewButton;
    }

// Getters And Setters
    public Noise getSelectednoise() {
        return selectednoise;
    }
    public void setSelectednoise(Noise selectednoise) {
        this.selectednoise = selectednoise;
    }

// Playable Methods
    @Override
    public boolean create(Duration duration) {return getSelectednoise() != null;}
    @Override
    public void startplayback() {
        currentplayer = new MediaPlayer(new Media(selectednoise.getFile().toURI().toString()));
        currentplayer.setOnEndOfMedia(this::playnextfile);
    }
    @Override
    public void playnextfile() {
        currentplayer.stop();
        currentplayer = new MediaPlayer(new Media(selectednoise.getFile().toURI().toString()));
        currentplayer.setOnEndOfMedia(this::playnextfile);
    }
    @Override
    public void play() {
        if (currentplayer == null) {startplayback();}
        else {currentplayer.play();}
    }
    @Override
    public void pause() {
        if (currentplayer != null) {currentplayer.pause();}
    }
    @Override
    public void stop() {
        if (currentplayer != null) {currentplayer.stop();}
    }
    @Override
    public void endofplayback() {}
    @Override
    public void setCurrentPlayer(MediaPlayer mediaPlayer) {

    }
    @Override
    public MediaPlayer getCurrentPlayer() {return currentplayer;}

// Widget Methods
    @Override
    public boolean isValid() {
        return false;
    }
    @Override
    public void disable() {
        OnOffSwitch.setDisable(true);
        Selection.setDisable(true);
        Category.setDisable(true);
        Description.setDisable(true);
        PreviewButton.setDisable(true);
    }
    @Override
    public void enable() {
        OnOffSwitch.setDisable(false);
        Selection.setDisable(false);
        Category.setDisable(false);
        Description.setDisable(false);
        PreviewButton.setDisable(false);
    }
    @Override
    public void resetallvalues() {

    }

// Other Methods

}
