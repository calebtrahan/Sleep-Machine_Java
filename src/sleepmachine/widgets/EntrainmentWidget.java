package sleepmachine.widgets;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import sleepmachine.MainController;
import sleepmachine.util.xml.Entrainments;

import java.util.ArrayList;

public class EntrainmentWidget implements Widget, Playable {
    private CheckBox OnOffSwitch;
    private ChoiceBox Presets;
    private TextArea Description;
    private MediaPlayer currentplayer;
    private int playcount = 0;
    private ArrayList<Media> sessionmedia;
    private Entrainments entrainments;
    private MainController root;
    private Duration totalentrainmentduration;

    public EntrainmentWidget(CheckBox onOffSwitch, ChoiceBox presets, TextArea description, MainController root) {
        OnOffSwitch = onOffSwitch;
        Presets = presets;
        Description = description;
        this.root = root;
    }

// Getters And Setters
    public Entrainments getEntrainments() {
    return entrainments;
}
    public void setEntrainments(Entrainments entrainments) {this.entrainments = entrainments;}

// Playable Methods
    @Override
    public boolean create(Duration duration) {
        if (root.getSelectedEntrainment() != null && root.getSleepDurationWidget().getTotalsessionduration().toSeconds() > 0.0) {
            if (root.getWakeUpSoundWidget().getWakeupduration() != null) {
                return root.getSelectedEntrainment().build(root.getWakeUpSoundWidget().getWakeupduration(), root.getSleepDurationWidget().getadjustedduration());
            } else {
                return root.getSelectedEntrainment().build(new Duration(0.0), root.getSleepDurationWidget().getadjustedduration());
            }
        } else {return false;}
    }
    @Override
    public void startplayback() {

    }
    @Override
    public void playnextfile() {

    }
    @Override
    public void play() {

    }
    @Override
    public void pause() {

    }
    @Override
    public void stop() {

    }
    @Override
    public void endofplayback() {

    }
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
        Presets.setDisable(true);
        Description.setDisable(true);
    }
    @Override
    public void enable() {
        OnOffSwitch.setDisable(false);
        Presets.setDisable(false);
        Description.setDisable(false);
    }
    @Override
    public void resetallvalues() {


    }

// Other Methods

}
