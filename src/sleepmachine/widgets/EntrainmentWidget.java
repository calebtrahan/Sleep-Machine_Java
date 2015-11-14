package sleepmachine.widgets;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import sleepmachine.MainController;
import sleepmachine.dialogs.AddNewEntrainmentDialog;
import sleepmachine.util.GuiUtils;
import sleepmachine.util.xml.Entrainment;
import sleepmachine.util.xml.Entrainments;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;

public class EntrainmentWidget implements Widget, Playable {
    private CheckBox OnOffSwitch;
    private ChoiceBox Presets;
    private TextArea Description;
    private MediaPlayer currentplayer;
    private int playcount;
    private ArrayList<Media> sessionmedia;
    private Entrainments allentrainments;
    private Entrainment selectedentrainment;
    private MainController root;
    private boolean enabled;

    public EntrainmentWidget(CheckBox onOffSwitch, ChoiceBox presets, TextArea description, MainController root) {
        OnOffSwitch = onOffSwitch;
        Presets = presets;
        Description = description;
        this.root = root;
        allentrainments = new Entrainments();

    }

// Getters And Setters
    public Entrainments getAllentrainments() {
    return allentrainments;
}
    public void setAllentrainments(Entrainments allentrainments) {this.allentrainments = allentrainments;}
    public Entrainment getSelectedentrainment() {return selectedentrainment;}
    public void setSelectedentrainment(Entrainment selectedentrainment) {this.selectedentrainment = selectedentrainment;}
    public boolean isEnabled() {return enabled;}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}

// Playable Methods
    @Override
    public boolean create(Duration duration) {
        if (getSelectedentrainment() != null && root.getSleepDurationWidget().getTotalsessionduration().toSeconds() > 0.0) {
            if (root.getWakeUpSoundWidget().getWakeupduration() != null) {
                return getSelectedentrainment().build(root.getWakeUpSoundWidget().getWakeupduration(), root.getSleepDurationWidget().getadjustedduration());
            } else {
                return getSelectedentrainment().build(new Duration(0.0), root.getSleepDurationWidget().getadjustedduration());
            }
        } else {return false;}
    }
    @Override
    public void startplayback() {
        playcount = 0;
        sessionmedia = new ArrayList<>();
        for (File i : getSelectedentrainment().getEntrainmentfiles()) {sessionmedia.add(new Media(i.toURI().toString()));}
        setCurrentPlayer(new MediaPlayer(sessionmedia.get(playcount)));
        getCurrentPlayer().play();
        getCurrentPlayer().setOnEndOfMedia(this::playnextfile);
    }
    @Override
    public void playnextfile() {
        getCurrentPlayer().stop();
        getCurrentPlayer().dispose();
        increaseplaycount();
        setCurrentPlayer(new MediaPlayer(sessionmedia.get(playcount)));
        System.out.println("Playcount: " + playcount + ". Now Playing: " + getCurrentPlayer().getMedia().getSource());
        getCurrentPlayer().play();
        getCurrentPlayer().setOnEndOfMedia(this::playnextfile);
    }
    @Override
    public void play() {
        if (getCurrentPlayer() == null) {startplayback();}
        else {getCurrentPlayer().play();}
    }
    @Override
    public void pause() {if (getCurrentPlayer() != null) {getCurrentPlayer().pause();}}
    @Override
    public void stop() {if (getCurrentPlayer() != null) {getCurrentPlayer().stop();}}
    @Override
    public void endofplayback() {
        getCurrentPlayer().stop();
        getCurrentPlayer().dispose();
    }
    @Override
    public void setCurrentPlayer(MediaPlayer mediaPlayer) {currentplayer = mediaPlayer;}
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
    @Override
    public void statusswitch() {
        boolean status = OnOffSwitch.isSelected();
        setEnabled(status);
        GuiUtils.togglecheckboxtext(OnOffSwitch);
        Presets.setDisable(!status);
        Description.setDisable(!status);
    }

// Other Methods
    protected void increaseplaycount() {playcount++;}
    public void changeentrainmentselection() {
        setSelectedentrainment(allentrainments.getselectedentrainment(Presets.getSelectionModel().getSelectedItem().toString()));
        if (getSelectedentrainment().checkifallpartsexist()) {
            Description.setText(getSelectedentrainment().getDescription());
        } else {
            Description.setText(getSelectedentrainment().getName() + " Is Missing Files Necessary To Play It. Please Select Another");
            setSelectedentrainment(null);
        }
    }
    public void syncentrainment() {
        allentrainments.getEntrainment();
        try {allentrainments.populatefromxml();} catch (JAXBException e) {e.printStackTrace(); return;}
        ArrayList<String> entrainmentnames = allentrainments.getallentrainmentnames();
        if (entrainmentnames != null) {
            ObservableList<String> namelist = FXCollections.observableArrayList();
            namelist.addAll(entrainmentnames);
            Presets.setItems(namelist);
            setAllentrainments(allentrainments);
        } else {
            // TODO No EntrainmentWidget Exists. Dialog Here To Add Noises
        }
    }
    public void addentrainment() {
        allentrainments.getEntrainment();
        AddNewEntrainmentDialog a = new AddNewEntrainmentDialog(null, this);
        a.showAndWait();
        try {allentrainments.populatefromxml();} catch (JAXBException e) {e.printStackTrace();}
        syncentrainment();
    }


}
