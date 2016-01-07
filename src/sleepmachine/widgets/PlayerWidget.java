package sleepmachine.widgets;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import sleepmachine.MainController;
import sleepmachine.Tools;
import sleepmachine.Widget;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class PlayerWidget implements Widget {
    private CheckBox OnOffSwitch;
    private ProgressBar progressBar;
    private Button startbutton;
    private Button stopbutton;
    private Button adjustvolumebutton;
    private Label statusbar;
    private SleepDurationWidget sleepDurationWidget;
    private EntrainmentWidget entrainmentWidget;
    private NoiseWidget noiseWidget;
    private WakeUpSoundWidget wakeUpSoundWidget;
    private CustomMusicWidget customMusicWidget;
    private Duration sessionduration;
    private Calendar wakeuptime;
    private Timeline timeline;
    private Calendar stoptime;
    private Calendar startwakeupfiletime;
    private MainController.ProgramState programState;
    private MainController root;

    public PlayerWidget(CheckBox onOffSwitch, ProgressBar progressBar, Button startbutton, Button stopbutton, Button adjustvolumebutton, Label statusbar, SleepDurationWidget sleepDurationWidget,
                        EntrainmentWidget entrainmentWidget, NoiseWidget noiseWidget, WakeUpSoundWidget wakeUpSoundWidget, CustomMusicWidget customMusicWidget, MainController.ProgramState programState, MainController root) {
        OnOffSwitch = onOffSwitch;
        this.progressBar = progressBar;
        this.startbutton = startbutton;
        this.stopbutton = stopbutton;
        this.adjustvolumebutton = adjustvolumebutton;
        this.statusbar = statusbar;
        this.sleepDurationWidget = sleepDurationWidget;
        this.entrainmentWidget = entrainmentWidget;
        this.noiseWidget = noiseWidget;
        this.wakeUpSoundWidget = wakeUpSoundWidget;
        this.customMusicWidget = customMusicWidget;
        this.programState = programState;
        this.root = root;
        this.statusbar.textProperty().addListener((observable, oldValue, newValue) -> {
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(3000), ae -> statusbar.setText("")));
            timeline1.play();
        });
    }

// Widget Implementation
    @Override
    public void statusswitch() {
        boolean status = OnOffSwitch.isSelected();
        OnOffSwitch.setSelected(status);
        progressBar.setDisable(!status);
        startbutton.setDisable(!status);
        stopbutton.setDisable(!status);
        adjustvolumebutton.setDisable(!status);
        if (status) {
            if (programState == MainController.ProgramState.Setting_Up) {
                if (pre_session_playback_checks()) {getsessionplayerready();}
            }
        } else {
            if (programState == MainController.ProgramState.Playing_Session) {
                if (Tools.getanswerdialog("Session Playing", "End This Session Prematurely?", "This Will Stop And Reset The Playing Session")) {
                    stopandresetsession();
                    enableallwidgets();
                }
            } else if (programState == MainController.ProgramState.Playing_WakeUpFile){stopandresetsession(); enableallwidgets();}
            else {enableallwidgets();}
        }
    }
    @Override
    public boolean isValid() {return false;}
    @Override
    public void disable() {}
    @Override
    public void enable() {}
    @Override
    public void resetallvalues() {}
    public void disableallwidgets() {
        entrainmentWidget.disable();
        customMusicWidget.disable();
        sleepDurationWidget.disable();
        wakeUpSoundWidget.disable();
    }
    public void enableallwidgets() {
        entrainmentWidget.enable();
        customMusicWidget.enable();
        sleepDurationWidget.enable();
        wakeUpSoundWidget.enable();
    }

// Other Methods
    public void stopandresetsession() {

    }
    public boolean pre_session_playback_checks() {
        try {
            // Check If Session Duration Is Set
            if (sleepDurationWidget.getDurationType() == SleepDurationWidget.DurationType.DURATION) {
                if (sessionduration == null) {throw new NullPointerException("No Session Duration Set");}
            } else if (sleepDurationWidget.getDurationType() == SleepDurationWidget.DurationType.TIME) {
                if (wakeuptime == null) {throw new NullPointerException("No Session Duration Set");}
            } else {throw new NullPointerException("No Session Duration Set");}
            // Check If Session Duration Is Long Enough For EntrainmentWidget
            if (sessionduration.toSeconds() < entrainmentWidget.getSelectedentrainment().getMinimumduration()) {throw new NullPointerException("Session Duration");}
            // Check Session Part Status
            int session_parts = 0;
            if (entrainmentWidget.isEnabled()) {if (! entrainmentWidget.isValid()) {throw new NullPointerException("EntrainmentWidget");} else {session_parts++;}}
            if (customMusicWidget.isEnabled()) {if (! customMusicWidget.isValid()) {throw new NullPointerException("Custom Music");} else {session_parts++;}}
            if (noiseWidget.isEnabled()) {if (! noiseWidget.isValid()) {throw new NullPointerException("NoiseWidget Loop");} else {session_parts++;}}
            if (wakeUpSoundWidget.isEnabled()) {if (! wakeUpSoundWidget.isValid()) {throw new NullPointerException("Wakeup Sound");}}
            // Check If Enough Parts And Get Confirmation From User If EntrainmentWidget Or Sound(s) Missing
            if (session_parts == 0) {throw new NullPointerException("No Session Parts, Cannot Get Ready");}
            boolean entrainmentenabled = entrainmentWidget.isEnabled();
            boolean somebackgroundsoundenabled = noiseWidget.isEnabled() || customMusicWidget.isEnabled();
            if (! entrainmentenabled) {
                Alert noentrainment = new Alert(Alert.AlertType.CONFIRMATION);
                noentrainment.setTitle("Confirmation");
                noentrainment.setHeaderText("No EntrainmentWidget In Session");
                noentrainment.setContentText("Really Make A Session Without EntrainmentWidget?");
                Optional<ButtonType> answer = noentrainment.showAndWait();
                if (! answer.isPresent() && answer.get() == ButtonType.OK) return false;
            }
            if (! somebackgroundsoundenabled) {
                Alert nobackgroundsound = new Alert(Alert.AlertType.CONFIRMATION);
                nobackgroundsound.setTitle("Confirmation");
                nobackgroundsound.setHeaderText("No Background Sound(s) Selected");
                nobackgroundsound.setContentText("Really Make A Session With No Background Sound(s)?");
                Optional<ButtonType> answer = nobackgroundsound.showAndWait();
                if (! answer.isPresent() && answer.get() == ButtonType.OK) return false;
            }
            return true;
        } catch (NullPointerException e) {
            Alert cantcreate = new Alert(Alert.AlertType.ERROR);
            cantcreate.setTitle("Can't Get Session Player Ready");
            cantcreate.setHeaderText("Can't Get Session Player Ready");
            cantcreate.setContentText(e.getMessage() + " Wasn't Selected Correctly. Please Try Again");
            cantcreate.showAndWait();
            return false;
        }
    }
    public void getsessionplayerready() {
        boolean status = OnOffSwitch.isSelected();
        if (status) {
            if (pre_session_playback_checks()) {
                Alert getsessionready = new Alert(Alert.AlertType.CONFIRMATION);
                getsessionready.setTitle("Done Configuring");
                getsessionready.setHeaderText("Everything Looks Good");
                getsessionready.setContentText("Get This Session Ready For Playback?");
                Optional<ButtonType> answer = getsessionready.showAndWait();
                if (answer.isPresent() && answer.get() == ButtonType.OK) {
                    disableallwidgets();
                    programState = MainController.ProgramState.Ready_To_Play;
                } else {OnOffSwitch.setSelected(false); statusswitch();}
            }
        }
    }
    public void starttimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> updateui()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        stoptime = Calendar.getInstance();
        stoptime.add(Calendar.MINUTE, (int) sessionduration.toMinutes());
        if (wakeUpSoundWidget.isEnabled()) {
            Duration wakeupsoundduration = wakeUpSoundWidget.getWakeupduration();
            startwakeupfiletime = stoptime;
            startwakeupfiletime.add(Calendar.MINUTE, (int) -wakeupsoundduration.toMinutes());
        }
    }
    public void startsessionplayback() {
        if (programState == MainController.ProgramState.Ready_To_Play) {
            starttimeline();
            if (entrainmentWidget.isEnabled()) {entrainmentWidget.play();}
            if (customMusicWidget.isEnabled()) {customMusicWidget.play();}
            if (noiseWidget.isEnabled()) {noiseWidget.play();}
            programState = MainController.ProgramState.Playing_Session;
            startbutton.setDisable(true);
        } else if (programState == MainController.ProgramState.Playing_Session || programState == MainController.ProgramState.Playing_WakeUpFile) {
            statusbar.setText("Session Already Playing");
        } else if (programState == MainController.ProgramState.Stopped) {

            statusbar.setText("Session Stopped");
        }
    }
    public void stopsessionplayback() {
        if (programState == MainController.ProgramState.Ready_To_Play) {
            statusbar.setText("No Session Playing");
        } else if (programState == MainController.ProgramState.Playing_Session) {
            if (entrainmentWidget.isEnabled()) {
                entrainmentWidget.stop();}
            if (customMusicWidget.isEnabled()) {
                customMusicWidget.stop();}
            if (noiseWidget.isEnabled()) {
                noiseWidget.stop();}
        } else if (programState == MainController.ProgramState.Playing_WakeUpFile) {
            // TODO Stop Session And Close Program Here
        }
    }
    public void adjustvolume() {
        if (programState == MainController.ProgramState.Playing_Session) {
            VolumeAdjustmentDialog volumeAdjustmentDialog = new VolumeAdjustmentDialog(null, root);
            volumeAdjustmentDialog.showAndWait();
        } else if (programState == MainController.ProgramState.Playing_WakeUpFile) {
            statusbar.setText("Cannot Adjust Volume On Wakeup File");
        } else {statusbar.setText("No Session Playing");}
    }
    public void updateui() {
        Calendar now = Calendar.getInstance();
        if (wakeUpSoundWidget.isEnabled()) {
            Date startwakeupsoundfiletime = new Date(wakeuptime.getTimeInMillis() - (long) wakeUpSoundWidget.getWakeupduration().toMillis());
            if (now.getTime().before(startwakeupsoundfiletime)) {
                // TODO Find A Way To Stop All Players, Start Playing Wakeup Sound And
            } else {}
        }
        if (now.getTime().before(stoptime.getTime())) {
            long milliseconds = stoptime.getTimeInMillis() - now.getTimeInMillis();
            long hours = milliseconds / (60 * 60 * 1000);
            long minutes =  (milliseconds % (60 * 60 * 1000)) / (60 * 1000);
            double percent = (now.getTimeInMillis() / stoptime.getTimeInMillis());
            progressBar.setProgress(percent);
            Long totalminutes = (hours * 60) + minutes;
            String text = Tools.formatlengthlong(totalminutes.intValue());
            statusbar.setText(text);
        } else {endofsession();}
    }
    public void endofsession() {

    }

// Subclasses/Dialogs
    public static class VolumeAdjustmentDialog extends Stage {
    public Slider EntrainmentSlider;
    public Slider NoiseSlider;
    public Slider CustomMusicSlider;
    public Label EntrainmentLabel;
    public Label NoiseLabel;
    public Label CustomMusicLabel;
    private MainController root;
    private MediaPlayer custommusicplayer;
    private MediaPlayer entrainmentplayer;
    private MediaPlayer noiseplayer;

    public VolumeAdjustmentDialog(Parent parent, MainController root) {
        this.root  = root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../assets/fxml/VolumeAdjustment.fxml"));
        fxmlLoader.setController(this);
        try {setScene(new Scene(fxmlLoader.load()));this.setTitle("Add Ambience");
        } catch (IOException e) {e.printStackTrace();}
        custommusicplayer = root.getCustomMusicWidgetWidget().getCurrentPlayer();
        entrainmentplayer = root.getEntrainmentWidget().getCurrentPlayer();
        noiseplayer = root.getNoiseWidget().getCurrentPlayer();
        if (custommusicplayer != null) {
            CustomMusicSlider.setDisable(false);
            CustomMusicSlider.setValue(custommusicplayer.volumeProperty().getValue());
            custommusicplayer.volumeProperty().bind(CustomMusicSlider.valueProperty());
        } else {CustomMusicLabel.setText("Custom Music (Disabled)"); CustomMusicSlider.setDisable(true);}
        if (entrainmentplayer != null) {
            EntrainmentSlider.setDisable(false);
            EntrainmentSlider.setValue(entrainmentplayer.volumeProperty().getValue());
            entrainmentplayer.volumeProperty().bind(CustomMusicSlider.valueProperty());
        } else {EntrainmentLabel.setText("EntrainmentWidget (Disabled)"); EntrainmentSlider.setDisable(true);}
        if (noiseplayer != null) {
            NoiseSlider.setDisable(false);
            NoiseSlider.setValue(noiseplayer.volumeProperty().getValue());
            noiseplayer.volumeProperty().bind(CustomMusicSlider.valueProperty());
        } else {NoiseLabel.setText("NoiseWidget Loop (Disabled)"); NoiseSlider.setDisable(true);}
        this.setOnCloseRequest(event -> close());
    }

    @Override
    public void close() {
        if (custommusicplayer != null) {custommusicplayer.volumeProperty().unbind();}
        if (entrainmentplayer != null) {entrainmentplayer.volumeProperty().unbind();}
        if (noiseplayer != null) {noiseplayer.volumeProperty().unbind();}
        super.close();
    }

    public void closedialog(ActionEvent actionEvent) {this.close();}
}

// Enums
    public enum PlayerState {
        NOTPLAYING, PLAYING, PAUSED, STOPPED
    }

}
