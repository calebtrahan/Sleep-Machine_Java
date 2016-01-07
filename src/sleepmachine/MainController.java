package sleepmachine;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import sleepmachine.widgets.*;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

// TODO Make Custom Music Dialog
    // TODO Maybe Use XML To Store Descriptions, Names And Paths Of:
        // EntrainmentOld Sessions Including Numerical Parts
        // NoiseWidget Categories And Selections In A Tree Format

public class MainController implements Initializable {
    public Label DurationDescription;
    public Label DurationActual;
    public Button SetSleepDurationButton;
    public Button SetWakeupTimeButton;
    public CheckBox EntrainmentSwitch;
    public ChoiceBox entrainmentchoicebox;
    public TextArea EntrainmentDescription;
    public CheckBox NoiseLoopSwitch;
    public ChoiceBox noisecategorychoicebox;
    public ChoiceBox noisechoicebox;
    public TextArea NoiseDescription;
    public Button noisepreviewbutton;
    public CheckBox WakeupSoundSwitch;
    public TextField WakeupFileSelection;
    public Button wakeupfileselectbutton;
    public Button WakeupPreviewButton;
    public CheckBox CustomMusicSwitch;
    public ProgressBar playerprogressbar;
    public Button startbutton;
    public Button stopbutton;
    public Label statusbar;
    public Label CustomMusicDescriptionLabel;
    public Button CustomMusicEditButton;
    public CheckBox SessionPlayerSwitch;
    public Button adjustvolumebutton;
    public Button TestButton;
    private SleepDurationWidget SleepDurationWidget;
    private sleepmachine.widgets.EntrainmentWidget EntrainmentWidget;
    private sleepmachine.widgets.NoiseWidget NoiseWidget;
    private WakeUpSoundWidget WakeUpSoundWidget;
    private sleepmachine.widgets.CustomMusicWidget CustomMusicWidget;
    private PlayerWidget playerWidget;
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
// Main Controller Fields
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
        playerWidget = new PlayerWidget(SessionPlayerSwitch, playerprogressbar, startbutton, stopbutton, adjustvolumebutton, statusbar,
                SleepDurationWidget, EntrainmentWidget, NoiseWidget, WakeUpSoundWidget, CustomMusicWidget, current_program_State, this);
        // Get Noises
        noiseloopstatusswitch(null);
        entrainmentstatusswitch(null);
        custommusicstatusswitch(null);
        wakeupsoundstatusswitch(null);
        playerstatusswitch(null);
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
        sleepmachine.widgets.SleepDurationWidget.DurationType sessiontype = SleepDurationWidget.getDurationType();
        if (sessiontype == sleepmachine.widgets.SleepDurationWidget.DurationType.DURATION) {
            return SleepDurationWidget.getTotalsessionduration().toMinutes();
        } else if (sessiontype == sleepmachine.widgets.SleepDurationWidget.DurationType.TIME) {
            return (double) Tools.getMinutesTillCalendar(SleepDurationWidget.getWakeuptime());
        } else {return 0.0;}
    }

// EntrainmentWidget Widget Methods
    public void entrainmentstatusswitch(ActionEvent actionEvent) {EntrainmentWidget.statusswitch();}
    public void entrainmentselectionchanged(ActionEvent actionEvent) {EntrainmentWidget.changeentrainmentselection();}

// NoiseWidget Loop Widget Methods
    public void noiseloopstatusswitch(ActionEvent actionEvent) {NoiseWidget.statusswitch();}
    public void noisecategorychanged(ActionEvent actionEvent) {NoiseWidget.changecategory();}
    public void noiseselectionchanged(ActionEvent actionEvent) {NoiseWidget.changeselection();}
    public void previewnoiseselected(ActionEvent actionEvent) {NoiseWidget.preview();}

// Wake Up Sound Widget Methods
    public void wakeupsoundstatusswitch(ActionEvent actionEvent) {WakeUpSoundWidget.statusswitch();}
    public void selectnewwakeupfile(ActionEvent actionEvent) {WakeUpSoundWidget.choosenewwakeupsound();}
    public void previewwakeupfile(ActionEvent actionEvent) {WakeUpSoundWidget.preview();}

// Custom Music Widget Methods
    public void custommusicstatusswitch(ActionEvent actionEvent) {CustomMusicWidget.statusswitch();}
    public void opencustommusicdialog(ActionEvent actionEvent) {CustomMusicWidget.opencustommusicdialog();}

// Session Player Methods
    public void playerstatusswitch(ActionEvent actionEvent) {
        playerWidget.statusswitch();}
    public void startsessionplayback(ActionEvent actionEvent) {
        playerWidget.startsessionplayback();}
    public void stopsessionplayback(ActionEvent actionEvent) {
        playerWidget.stopsessionplayback();}
    public void adjustvolume(ActionEvent actionEvent) {
        playerWidget.adjustvolume();}

// Menu Bar Action Methods
    public void addnoiseloop(ActionEvent actionEvent) {NoiseWidget.addnoiseloop();}
    public void addentrainment(ActionEvent actionEvent) {EntrainmentWidget.addentrainment();}
    public void resetallvalues(ActionEvent actionEvent) {
        boolean answer = Tools.getanswerdialog("Conirmation", "Clear All Values", "Really Clear And Reset All Selected Options?");
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
    public sleepmachine.widgets.CustomMusicWidget getCustomMusicWidgetWidget() {return CustomMusicWidget;}
    public sleepmachine.widgets.EntrainmentWidget getEntrainmentWidget() {return EntrainmentWidget;}
    public sleepmachine.widgets.NoiseWidget getNoiseWidget() {return NoiseWidget;}
    public SleepDurationWidget getSleepDurationWidget() {return SleepDurationWidget;}
    public WakeUpSoundWidget getWakeUpSoundWidget() {return WakeUpSoundWidget;}

// Other Methods
    public void test(ActionEvent actionEvent) {
//        CustomMusicWidget.create(new Duration(28800 * 1000));
    }

// Enums
    public enum ProgramState {
        Setting_Up, Ready_To_Play, Playing_Session, Playing_WakeUpFile, Stopped, Session_Ended
    }
}