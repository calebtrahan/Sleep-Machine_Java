package sleepmachine.widgets;


import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import sleepmachine.util.FileUtils;
import sleepmachine.util.GuiUtils;

import java.io.File;
import java.util.Optional;


public class WakeUpSoundWidget implements Widget, Playable {
    private CheckBox OnOffSwitch;
    private TextField SelectedFile;
    private Button OpenFileButton;
    private Button PreviewButton;
    private File wakeupfile;
    private Duration wakeupduration;
    private MediaPlayer currentplayer;
    private MediaPlayer previewplayer;
    private boolean enabled;

    public WakeUpSoundWidget(CheckBox onOffSwitch, TextField selectedFile, Button openFileButton, Button previewButton) {
        OnOffSwitch = onOffSwitch;
        SelectedFile = selectedFile;
        OpenFileButton = openFileButton;
        PreviewButton = previewButton;
    }

// Getters And Setters
    public Duration getWakeupduration() {
        return wakeupduration;
    }
    public void setWakeupduration(Duration wakeupduration) {
        this.wakeupduration = wakeupduration;
    }
    public File getWakeupfile() {
        return wakeupfile;
    }
    public void setWakeupfile(File wakeupfile) {this.wakeupfile = wakeupfile;}
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

// Playable Implementation
    @Override
    public boolean create(Duration duration) {return getWakeupduration() != null;}
    @Override
    public void startplayback() {}
    @Override
    public void playnextfile() {}
    @Override
    public void play() {
        Media media = new Media(wakeupfile.toURI().toString());
        currentplayer = new MediaPlayer(media);
        currentplayer.play();
        currentplayer.setOnEndOfMedia(this::endofplayback);
    }
    @Override
    public void pause() {}
    @Override
    public void stop() {}
    @Override
    public void endofplayback() {
        currentplayer.stop();
        currentplayer.dispose();
    }
    @Override
    public MediaPlayer getCurrentPlayer() {return currentplayer;}
    @Override
    public void setCurrentPlayer(MediaPlayer mediaPlayer) {

    }

// Widget Implementation
    @Override
    public boolean isValid() {
        return false;
    }
    @Override
    public void disable() {
        OnOffSwitch.setDisable(true);
        OpenFileButton.setDisable(true);
        PreviewButton.setDisable(true);
    }
    @Override
    public void enable() {
        OnOffSwitch.setDisable(false);
        OpenFileButton.setDisable(false);
        PreviewButton.setDisable(false);
    }
    @Override
    public void resetallvalues() {
        SelectedFile.setText("Please Select...");
        wakeupduration = null;
        if (currentplayer != null) currentplayer.dispose();
        wakeupfile = null;
    }
    @Override
    public void statusswitch() {
        boolean status = OnOffSwitch.isSelected();
        GuiUtils.togglecheckboxtext(OnOffSwitch);
        setEnabled(status);
        SelectedFile.setDisable(!status);
        OpenFileButton.setDisable(!status);
        PreviewButton.setDisable(!status);
    }

// Other Methods
    public void choosenewwakeupsound() {
    if (getWakeupfile() != null) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmation");
        a.setContentText("Replace Current Wakeup File?");
        Optional<ButtonType> b = a.showAndWait();
        if (b.get() != ButtonType.YES) {return;}
    }
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select A File To Be Used As A Wakeup File");
    File newwakeupfile = fileChooser.showOpenDialog(null);
    if (newwakeupfile != null) {
        Boolean filegood = FileUtils.testmediafile(newwakeupfile);
        if (filegood) {
            GuiUtils.validate(SelectedFile, true);
            SelectedFile.setText(newwakeupfile.getName());
            setWakeupfile(newwakeupfile);
            setWakeupduration(new Duration(FileUtils.getaudioduration(newwakeupfile) * 1000));
        } else {
            SelectedFile.setText("<" + newwakeupfile.getName() + "> Isn't A Valid Audio File");
            GuiUtils.validate(SelectedFile, false);
        }
    }
}
    public void preview() {
        if (getWakeupfile() != null) {
            if (previewplayer != null) {
                if (previewplayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    previewplayer.stop();
                    PreviewButton.setText("Preview");
                } else {
                    previewplayer.play();
                    PreviewButton.setText("Stop");
                }
            } else {
                Media tempmedia = new Media(getWakeupfile().toURI().toString());
                previewplayer = new MediaPlayer(tempmedia);
                previewplayer.play();
                PreviewButton.setText("Stop");
            }
        } else {
            GuiUtils.showerrordialog("Cannot Preview", "No Wakeup File Selected", "Select A Wakeup File First To Preview");
        }
    }
}
