package sleepmachine;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import sleepmachine.dialogs.DurationType;
import sleepmachine.util.GuiUtils;
import sleepmachine.util.TimeUtils;
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
// FXML Binding Fields
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
    public Label statusbar;
    // Custom Music Widget Fields
    public Label CustomMusicDescriptionLabel;
    public Button CustomMusicEditButton;
    public CheckBox SessionPlayerSwitch;
    public Button adjustvolumebutton;
    public Button TestButton;
// Widget Controller Fields
    private SleepDurationWidget SleepDurationWidget;
    private sleepmachine.widgets.EntrainmentWidget EntrainmentWidget;
    private sleepmachine.widgets.NoiseWidget NoiseWidget;
    private WakeUpSoundWidget WakeUpSoundWidget;
    private sleepmachine.widgets.CustomMusicWidget CustomMusicWidget;
    private SessionPlayer sessionPlayer;
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
// MainController Fields
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
        sessionPlayer = new SessionPlayer(SessionPlayerSwitch, playerprogressbar, startbutton, stopbutton, adjustvolumebutton, statusbar,
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
        DurationType sessiontype = SleepDurationWidget.getDurationType();
        if (sessiontype == DurationType.DURATION) {
            return SleepDurationWidget.getTotalsessionduration().toMinutes();
        } else if (sessiontype == DurationType.TIME) {
            return (double) TimeUtils.getMinutesTillCalendar(SleepDurationWidget.getWakeuptime());
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
    public void playerstatusswitch(ActionEvent actionEvent) {sessionPlayer.statusswitch();}
    public void startsessionplayback(ActionEvent actionEvent) {sessionPlayer.startsessionplayback();}
    public void stopsessionplayback(ActionEvent actionEvent) {sessionPlayer.stopsessionplayback();}
    public void adjustvolume(ActionEvent actionEvent) {sessionPlayer.adjustvolume();}

// Menu Bar Action Methods
    public void addnoiseloop(ActionEvent actionEvent) {NoiseWidget.addnoiseloop();}
    public void addentrainment(ActionEvent actionEvent) {EntrainmentWidget.addentrainment();}
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
    public sleepmachine.widgets.CustomMusicWidget getCustomMusicWidgetWidget() {return CustomMusicWidget;}
    public sleepmachine.widgets.EntrainmentWidget getEntrainmentWidget() {return EntrainmentWidget;}
    public sleepmachine.widgets.NoiseWidget getNoiseWidget() {return NoiseWidget;}
    public SleepDurationWidget getSleepDurationWidget() {return SleepDurationWidget;}
    public WakeUpSoundWidget getWakeUpSoundWidget() {return WakeUpSoundWidget;}

// Other Methods
    public void test(ActionEvent actionEvent) {
//        CustomMusicWidget.create(new Duration(28800 * 1000));
    }

}