package sleepmachine;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import sleepmachine.dialogs.*;
import sleepmachine.util.GuiUtils;
import sleepmachine.util.TimeUtils;
import sleepmachine.util.xml.Entrainments;
import sleepmachine.util.xml.Noises;
import sleepmachine.widgets.*;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.net.URL;
import java.util.*;

// TODO Make Custom Music Dialog
    // TODO Maybe Use XML To Store Descriptions, Names And Paths Of:
        // EntrainmentOld Sessions Including Numerical Parts
        // NoiseWidget Categories And Selections In A Tree Format

public class MainController implements Initializable {
    // Sleep Duration Widget Fields
    public Label DurationDescription;
    public Label DurationActual;
    public Button SetSleepDurationButton;
    public Button SetWakeupTimeButton;
    // EntrainmentOld Widget Fields
    public CheckBox EntrainmentSwitch;
    public ChoiceBox entrainmentchoicebox;
    public TextArea EntrainmentDescription;
    // NoiseWidget Loop Widget Fields
    public CheckBox NoiseLoopSwitch;
    public ChoiceBox noisecategorychoicebox;
    public ChoiceBox noisechoicebox;
    public TextArea NoiseDescription;
    public Button noisepreviewbutton;
    // Wakeup Sound Widget Fields
    public CheckBox WakeupSoundSwitch;
    public TextField WakeupFileSelection;
    public Button wakeupfileselectbutton;
    public Button WakeupPreviewButton;
    // Custom Music Widget Fields
    public CheckBox CustomMusicSwitch;
    // Session Player Widget Fields
    public ProgressBar playerprogressbar;
    public Button startbutton;
    public Button stopbutton;
    // StatusBar
    public Label statusbar;
    public Label CustomMusicDescriptionLabel;
    public Button CustomMusicEditButton;
    public CheckBox SessionPlayerSwitch;
    public Button adjustvolumebutton;
    public Button TestButton;
    // Widget Controller Classes
    private SleepDurationWidget SleepDurationWidget;
    private sleepmachine.widgets.EntrainmentWidget EntrainmentWidget;
    private sleepmachine.widgets.NoiseWidget NoiseWidget;
    private WakeUpSoundWidget WakeUpSoundWidget;
    private sleepmachine.widgets.CustomMusicWidget CustomMusicWidget;
    // Static Directory Fields
    public static String WORKINGDIRECTORY = System.getProperty("user.dir");
    public static File SOURCEDIRECTORY = new File(MainController.WORKINGDIRECTORY, "src");
    public static File PROJECTDIRECTORY = new File(MainController.SOURCEDIRECTORY, "sleepmachine");
    public static File ASSETDIRECTORY = new File(MainController.PROJECTDIRECTORY, "assets");
    public static File SOUNDDIRECTORY = new File(MainController.ASSETDIRECTORY, "sound");
    public static File NOISEDIRECTORY = new File(MainController.SOUNDDIRECTORY, "noise");
    public static File ENTRAINMENTDIRECTORY = new File(MainController.SOUNDDIRECTORY, "entrainment");
    public static File XMLDIRECTORY = new File(MainController.ASSETDIRECTORY, "xml");
    public static File ENTRAINMENTXMLFILE = new File(MainController.XMLDIRECTORY, "entrainmentlist.xml");
    public static File NOISESXMLFILE = new File(MainController.XMLDIRECTORY, "noiselist.xml");
    public static int MAXSESSIONDURATION = 14;
    // Other Fields
    private boolean _noiseEnabled;
    private boolean _custommusicEnabled;
    private boolean _wakeupsoundEnabled;
    private boolean _entrainmentEnabled;
    private Entrainments Entrainments;
    private sleepmachine.util.xml.Entrainment SelectedEntrainment;
    private Noises Noises;
    private sleepmachine.util.xml.Noise SelectedNoise;
    private MediaPlayer wakeupsoundplayer;
    private Calendar stoptime;
    private Timeline timeline;
    private Duration sessionduration;
    private Calendar wakeuptime;
    private ProgramState current_program_State;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SleepDurationWidget = new SleepDurationWidget(DurationDescription, DurationActual);
        EntrainmentWidget = new EntrainmentWidget(EntrainmentSwitch, entrainmentchoicebox, EntrainmentDescription, this);
        NoiseWidget = new NoiseWidget(NoiseLoopSwitch, noisecategorychoicebox, noisechoicebox, NoiseDescription, noisepreviewbutton);
        WakeUpSoundWidget = new WakeUpSoundWidget(WakeupSoundSwitch, WakeupFileSelection, wakeupfileselectbutton, WakeupPreviewButton);
        CustomMusicWidget = new CustomMusicWidget(CustomMusicSwitch, CustomMusicDescriptionLabel, CustomMusicEditButton);
        // Get Noises
        Noises = new Noises();
        syncnoise();
        Entrainments = new Entrainments();
        syncentrainment();
        noiseloopstatusswitch(null);
        entrainmentstatusswitch(null);
        custommusicstatusswitch(null);
        wakeupsoundstatusswitch(null);
        getsessionplayerready(null);
        current_program_State = ProgramState.Setting_Up;
    }

// Sleep Duration Widget Methods
    public void setWakeupTime(ActionEvent actionEvent) {
        SleepDurationWidget.openwakeuptimedialog();
        wakeuptime = SleepDurationWidget.getWakeuptime();
    }
    public void setSleepDuration(ActionEvent actionEvent) {
        SleepDurationWidget.openwakeupdurationdialog();
        sessionduration = SleepDurationWidget.getTotalsessionduration();
    }
    public double getsessionduration() {
        DurationType sessiontype = SleepDurationWidget.getDurationType();
        if (sessiontype == DurationType.DURATION) {
            return SleepDurationWidget.getTotalsessionduration().toMinutes();
        } else if (sessiontype == DurationType.TIME) {
            return (double) TimeUtils.getMinutesTillCalendar(SleepDurationWidget.getWakeuptime());
        } else {return 0.0;}
    }

// EntrainmentWidget Widget Methods
    public void entrainmentstatusswitch(ActionEvent actionEvent) {
        togglecheckboxtext(EntrainmentSwitch);
        boolean status = EntrainmentSwitch.isSelected();
        set_entrainmentEnabled(status);
        entrainmentchoicebox.setDisable(!status);
        EntrainmentDescription.setDisable(!status);
//        EntrainmentOld.setEnabled(EntrainmentSwitch.isSelected());
    }
    public void entrainmentselectionchanged(ActionEvent actionEvent) {
        SelectedEntrainment = Entrainments.getselectedentrainment(entrainmentchoicebox.getSelectionModel().getSelectedItem().toString());
        if (SelectedEntrainment.checkifallpartsexist()) {
            EntrainmentDescription.setText(SelectedEntrainment.getDescription());
        } else {
            EntrainmentDescription.setText(SelectedEntrainment.getName() + " Is Missing Files Necessary To Play It. Please Select Another");
            SelectedEntrainment = null;
        }
    }

// NoiseWidget Loop Widget Methods
    public void noiseloopstatusswitch(ActionEvent actionEvent) {
        boolean status = NoiseLoopSwitch.isSelected();
        togglecheckboxtext(NoiseLoopSwitch);
        set_noiseEnabled(status);
        NoiseDescription.setDisable(!status);
        noisecategorychoicebox.setDisable(!status);
        noisechoicebox.setDisable(!status);
        noisepreviewbutton.setDisable(!status);
//        NoiseWidget.setEnabled(NoiseLoopSwitch.isSelected());
    }
    public void noisecategorychanged(ActionEvent actionEvent) {
        noisechoicebox.getItems().clear();
        ObservableList<String> noisesincategory = FXCollections.observableArrayList();
        String categoryname = noisecategorychoicebox.getSelectionModel().getSelectedItem().toString();
        noisesincategory.addAll(Noises.getnoisenamesincategory(categoryname));
        noisechoicebox.setItems(noisesincategory);
    }
    public void noiseselectionchanged(ActionEvent actionEvent) {
        String selectedname = noisechoicebox.getSelectionModel().getSelectedItem().toString();
        SelectedNoise = Noises.getselectednoise(selectedname);
        if (SelectedNoise != null) {NoiseDescription.setText(SelectedNoise.getDescription());}
        else {NoiseDescription.setText(selectedname + "'s Actual File Is Missing. Please Choose Another");}
        noisepreviewbutton.setDisable(SelectedNoise == null);
    }
    public void previewnoiseselected(ActionEvent actionEvent) {
        if (SelectedNoise != null) {
            Media noisemedia = new Media(SelectedNoise.getFile().toURI().toString());
            MediaPlayer noiseplayer = new MediaPlayer(noisemedia);
            noiseplayer.play();
        }
    }

// Wake Up Sound Widget Methods
    public void wakeupsoundstatusswitch(ActionEvent actionEvent) {
        togglecheckboxtext(WakeupSoundSwitch);
        boolean status = WakeupSoundSwitch.isSelected();
        set_wakeupsoundEnabled(status);
        WakeupFileSelection.setDisable(!status);
        wakeupfileselectbutton.setDisable(!status);
        WakeupPreviewButton.setDisable(!status);
//        WakeUpSoundWidget.setEnabled(WakeupSoundSwitch.isSelected());
    }
    public void selectnewwakeupfile(ActionEvent actionEvent) {WakeUpSoundWidget.choosenewwakeupsound();}
    public void previewwakeupfile(ActionEvent actionEvent) {
        if (WakeUpSoundWidget.getWakeupfile() != null) {
            if (wakeupsoundplayer != null) {
                if (wakeupsoundplayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    wakeupsoundplayer.stop();
                    WakeupPreviewButton.setText("Preview");
                } else {
                    wakeupsoundplayer.play();
                    WakeupPreviewButton.setText("Stop");
                }
            } else {
                Media tempmedia = new Media(WakeUpSoundWidget.getWakeupfile().toURI().toString());
                wakeupsoundplayer = new MediaPlayer(tempmedia);
                wakeupsoundplayer.play();
                WakeupPreviewButton.setText("Stop");
            }
        } else {
            Alert nowakeupfile = new Alert(Alert.AlertType.ERROR);
            nowakeupfile.setTitle("Cannot Preview");
            nowakeupfile.setHeaderText("No Wakeup File Selected");
            nowakeupfile.setContentText("Select A Wakeup File First To Preview");
            nowakeupfile.showAndWait();
        }
    }
    public Duration getwakeupsoundduration() {return WakeUpSoundWidget.getWakeupduration();}

// Custom Music Widget Methods
    public void custommusicstatusswitch(ActionEvent actionEvent) {
        togglecheckboxtext(CustomMusicSwitch);
        boolean status = CustomMusicSwitch.isSelected();
        set_wakeupsoundEnabled(status);
        CustomMusicEditButton.setDisable(!status);
        CustomMusicDescriptionLabel.setDisable(!status);
        if (status) {opencustommusicdialog(null);}
    }
    public void opencustommusicdialog(ActionEvent actionEvent) {
        if (CustomMusicWidget.getMusicFiles() == null) {
            CustomMusicDialog a = new CustomMusicDialog(null);
            a.showAndWait();
            CustomMusicWidget.setMusicFiles(a.getCustomMusic());
            CustomMusicDescriptionLabel.setText(CustomMusicWidget.getdescription());
            if (a.getCustomMusic() == null) {CustomMusicSwitch.setSelected(false); custommusicstatusswitch(null);}
            set_custommusicEnabled(a.getCustomMusic() != null);
        } else {
            CustomMusicDialog a = new CustomMusicDialog(null, CustomMusicWidget.getMusicFiles());
            a.showAndWait();
            CustomMusicWidget.setMusicFiles(a.getCustomMusic());
            CustomMusicDescriptionLabel.setText(CustomMusicWidget.getdescription());
            if (a.getCustomMusic() == null) {CustomMusicSwitch.setSelected(false); custommusicstatusswitch(null);}
            set_custommusicEnabled(a.getCustomMusic() != null);
        }
    }

// Session Player Methods
    public void disableallwidgets() {
        EntrainmentWidget.disable();
        CustomMusicWidget.disable();
        SleepDurationWidget.disable();
        WakeUpSoundWidget.disable();
    }
    public void enableallwidgets() {
        EntrainmentWidget.enable();
        CustomMusicWidget.enable();
        SleepDurationWidget.enable();
        WakeUpSoundWidget.enable();
    }
    public boolean pre_session_playback_checks() {
        try {
        // Check If Session Duration Is Set
            if (getSleepDurationWidget().getDurationType() == DurationType.DURATION) {
                if (sessionduration == null) {throw new NullPointerException("No Session Duration Set");}
            } else if (getSleepDurationWidget().getDurationType() == DurationType.TIME) {
                if (wakeuptime == null) {throw new NullPointerException("No Session Duration Set");}
            } else {throw new NullPointerException("No Session Duration Set");}
        // Check If Session Duration Is Long Enough For EntrainmentWidget
            if (getsessionduration() < SelectedEntrainment.getMinimumduration()) {throw new NullPointerException("Session Duration");}
        // Check Session Part Status
            int session_parts = 0;
            if (is_entrainmentEnabled()) {if (! EntrainmentWidget.isValid()) {throw new NullPointerException("EntrainmentWidget");} else {session_parts++;}}
            if (is_custommusicEnabled()) {if (! CustomMusicWidget.isValid()) {throw new NullPointerException("Custom Music");} else {session_parts++;}}
            if (is_noiseEnabled()) {if (! NoiseWidget.isValid()) {throw new NullPointerException("NoiseWidget Loop");} else {session_parts++;}}
            if (is_wakeupsoundEnabled()) {if (! WakeUpSoundWidget.isValid()) {throw new NullPointerException("Wakeup Sound");}}
        // Check If Enough Parts And Get Confirmation From User If EntrainmentWidget Or Sound(s) Missing
            if (session_parts == 0) {throw new NullPointerException("No Session Parts, Cannot Get Ready");}
            boolean entrainmentenabled = is_entrainmentEnabled();
            boolean somebackgroundsoundenabled = is_noiseEnabled() || is_custommusicEnabled();
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
    public void getsessionplayerready(ActionEvent actionEvent) {
        boolean status = SessionPlayerSwitch.isSelected();
        if (status) {
            if (pre_session_playback_checks()) {
                Alert getsessionready = new Alert(Alert.AlertType.CONFIRMATION);
                getsessionready.setTitle("Done Configuring");
                getsessionready.setHeaderText("Everything Looks Good");
                getsessionready.setContentText("Get This Session Ready For Playback?");
                Optional<ButtonType> answer = getsessionready.showAndWait();
                if (answer.isPresent() && answer.get() == ButtonType.OK) {
                    disableallwidgets();
                } else {status = false; enableallwidgets();}
            }
        } else {enableallwidgets();}
        SessionPlayerSwitch.setSelected(status);
        togglecheckboxtext(SessionPlayerSwitch);
        playerprogressbar.setDisable(!status);
        startbutton.setDisable(!status);
        stopbutton.setDisable(!status);
        adjustvolumebutton.setDisable(!status);
    }
    public void starttimeline() {
        timeline = new Timeline(new KeyFrame(javafx.util.Duration.millis(1000), ae -> updateui()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        stoptime = Calendar.getInstance();
        stoptime.add(Calendar.MINUTE, (int) sessionduration.toMinutes());
    }
    public void startsessionplayback(ActionEvent actionEvent) {
        if (current_program_State ==  ProgramState.Ready_To_Play) {
            starttimeline();
            if (is_entrainmentEnabled()) {
                EntrainmentWidget.play();}
            if (is_custommusicEnabled()) {
                CustomMusicWidget.play();}
            if (is_noiseEnabled()) {
                NoiseWidget.play();}
            current_program_State = ProgramState.Playing_Session;
            startbutton.setDisable(true);
        } else if (current_program_State == ProgramState.Playing_Session) {
            Alert alreadyplaying = new Alert(Alert.AlertType.INFORMATION);
            alreadyplaying.setTitle("Already Playing");
            alreadyplaying.setHeaderText("Cannot Play Session");
            alreadyplaying.setHeaderText("Session Is Already Playing");
            alreadyplaying.showAndWait();
        } else if (current_program_State == ProgramState.Stopped) {
            // TODO Resume Here
        }
    }
    public void stopsessionplayback(ActionEvent actionEvent) {
        if (current_program_State == ProgramState.Ready_To_Play) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Cannot Stop");
            a.setHeaderText("Cannot Stop Playback");
            a.setContentText("No Session Playing");
            a.showAndWait();
        } else if (current_program_State == ProgramState.Playing_Session) {
            if (is_entrainmentEnabled()) {
                EntrainmentWidget.stop();}
            if (is_custommusicEnabled()) {
                CustomMusicWidget.stop();}
            if (is_noiseEnabled()) {
                NoiseWidget.stop();}
        } else if (current_program_State == ProgramState.Playing_WakeUpFile) {
            // TODO Stop Session And Close Program Here
        }
    }
    public void adjustvolume(ActionEvent actionEvent) {
        if (current_program_State == ProgramState.Playing_Session) {
            VolumeAdjustmentDialog volumeAdjustmentDialog = new VolumeAdjustmentDialog(null, this);
            volumeAdjustmentDialog.showAndWait();
        } else if (current_program_State == ProgramState.Playing_WakeUpFile) {
            statusbar.setText("Cannot Adjust Volume On Wakeup File");
        } else {statusbar.setText("No Session Playing");}
    }
    public void updateui() {
        Calendar now = Calendar.getInstance();
        if (is_wakeupsoundEnabled()) {
            Date startwakeupsoundfiletime = new Date(wakeuptime.getTimeInMillis() - (long) WakeUpSoundWidget.getWakeupduration().toMillis());
            if (now.getTime().before(startwakeupsoundfiletime)) {
                // TODO Find A Way To Stop All Players, Start Playing Wakeup Sound And
            } else {}
        }
        if (now.getTime().before(stoptime.getTime())) {
            long milliseconds = stoptime.getTimeInMillis() - now.getTimeInMillis();
            long hours = milliseconds / (60 * 60 * 1000);
            long minutes =  (milliseconds % (60 * 60 * 1000)) / (60 * 1000);
            double percent = (now.getTimeInMillis() / stoptime.getTimeInMillis());
            playerprogressbar.setProgress(percent);
            Long totalminutes = (hours * 60) + minutes;
            String text = TimeUtils.formatlengthlong(totalminutes.intValue());
            statusbar.setText(text);
        } else {endofsession();}
    }
    public void endofsession() {

    }

// Other Methods
    public void togglecheckboxtext(CheckBox checkbox) {if (checkbox.isSelected()) {checkbox.setText("ON");} else {checkbox.setText("OFF");}}
    public void syncentrainment() {
        Entrainments.getEntrainment();
        try {Entrainments.populatefromxml();} catch (JAXBException e) {e.printStackTrace(); return;}
        ArrayList<String> entrainmentnames = Entrainments.getallentrainmentnames();
        if (entrainmentnames != null) {
            ObservableList<String> namelist = FXCollections.observableArrayList();
            namelist.addAll(entrainmentnames);
            entrainmentchoicebox.setItems(namelist);
            EntrainmentWidget.setEntrainments(Entrainments);
        } else {
            // TODO No EntrainmentWidget Exists. Dialog Here To Add Noises
        }
    }
    public void syncnoise() {
        Noises.getNoise();
        try {Noises.populatefromxml();} catch (JAXBException e) {e.printStackTrace(); return;}
        ArrayList<String> allcategorynames = Noises.getallcategories();
        if (allcategorynames != null) {
            ObservableList<String> categorylist = FXCollections.observableArrayList();
            categorylist.addAll(allcategorynames);
            noisecategorychoicebox.setItems(categorylist);
            NoiseWidget.setSelectednoise(SelectedNoise);
        } else {
            // TODO No Noises Exist. Dialog To Add Noises Here
        }
    }

// Menu Bar Action Methods
    public void addnoiseloop(ActionEvent actionEvent) {
        Noises.getNoise();
        AddNewNoiseDialog a = new AddNewNoiseDialog(null, this);
        a.showAndWait();
        try {Noises.populatefromxml();} catch (JAXBException e) {e.printStackTrace();}
        syncnoise();
    }
    public void addentrainment(ActionEvent actionEvent) {
        Entrainments.getEntrainment();
        AddNewEntrainmentDialog a = new AddNewEntrainmentDialog(null, this);
        a.showAndWait();
        try {Entrainments.populatefromxml();} catch (JAXBException e) {e.printStackTrace();}
        syncentrainment();
    }
    public void resetallvalues(ActionEvent actionEvent) {
        boolean answer = GuiUtils.getanswerdialog("Conirmation", "Clear All Values", "Really Clear And Reset All Selected Options?");
        if (answer) {
            SleepDurationWidget.resetallvalues();
            EntrainmentWidget.resetallvalues();
            NoiseWidget.resetallvalues();
            CustomMusicWidget.resetallvalues();
            WakeUpSoundWidget.resetallvalues();
        }
    }
    public void exit(ActionEvent actionEvent) {}

// Getters And Setters
    public Noises getNoises() {return Noises;}
    public Entrainments getEntrainments() {return Entrainments;}
    public boolean is_noiseEnabled() {
        return _noiseEnabled;
    }
    public void set_noiseEnabled(boolean _noiseEnabled) {
        this._noiseEnabled = _noiseEnabled;
    }
    public boolean is_custommusicEnabled() {
        return _custommusicEnabled;
    }
    public void set_custommusicEnabled(boolean _custommusicEnabled) {
        this._custommusicEnabled = _custommusicEnabled;
    }
    public boolean is_wakeupsoundEnabled() {
        return _wakeupsoundEnabled;
    }
    public void set_wakeupsoundEnabled(boolean _wakeupsoundEnabled) {
        this._wakeupsoundEnabled = _wakeupsoundEnabled;
    }
    public boolean is_entrainmentEnabled() {
        return _entrainmentEnabled;
    }
    public void set_entrainmentEnabled(boolean _entrainmentEnabled) {
        this._entrainmentEnabled = _entrainmentEnabled;
    }
    public sleepmachine.widgets.CustomMusicWidget getCustomMusicWidgetWidget() {return CustomMusicWidget;}
    public sleepmachine.widgets.EntrainmentWidget getEntrainmentWidget() {return EntrainmentWidget;}
    public sleepmachine.widgets.NoiseWidget getNoiseWidget() {return NoiseWidget;}
    public SleepDurationWidget getSleepDurationWidget() {return SleepDurationWidget;}




    public void test(ActionEvent actionEvent) {
//        CustomMusicWidget.create(new Duration(28800 * 1000));
    }



}