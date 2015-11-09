package sleepmachine.widgets;

import javafx.scene.control.Label;
import javafx.util.Duration;
import sleepmachine.dialogs.DurationType;
import sleepmachine.dialogs.SelectDurationDialog;
import sleepmachine.dialogs.SelectTimeDialog;
import sleepmachine.util.TimeUtils;

import java.util.Calendar;

public class SleepDurationWidget implements Widget {
    private Calendar wakeuptime;
    private Duration totalsessionduration;
    private Label DurationDescription;
    private Label DurationActual;
    private DurationType durationType;

    public SleepDurationWidget(Label durationdescription, Label durationactual) {
        DurationDescription = durationdescription;
        DurationActual = durationactual;
        updateui();
    }

// GETTERS AND SETTERS
    public Duration getTotalsessionduration() {return totalsessionduration;}
    public void setTotalsessionduration(Duration duration) {
        totalsessionduration = duration;}
    public Calendar getWakeuptime() {return wakeuptime;}
    public void setWakeuptime(Calendar wakeuptime) {this.wakeuptime = wakeuptime;}
    public DurationType getDurationType() {
        return durationType;
    }
    public void setDurationType(DurationType durationType) {
        this.durationType = durationType;
    }

// Widget Methods
    @Override
    public boolean isValid() {
        if (getDurationType() == DurationType.TIME) {return getTotalsessionduration() != null;}
        else if (getDurationType() == DurationType.DURATION) {return getWakeuptime() != null;}
        else {return false;}
    }
    @Override
    public void disable() {
        DurationActual.setDisable(true);
        DurationDescription.setDisable(true);
    }
    @Override
    public void enable() {
        DurationActual.setDisable(false);
        DurationDescription.setDisable(false);
    }
    @Override
    public void resetallvalues() {
        setDurationType(null);
        setWakeuptime(null);
        setTotalsessionduration(null);
        resetui();
    }

// Other Methods
    public void openwakeuptimedialog() {
    SelectTimeDialog selectTimeDialog = new SelectTimeDialog(null);
    selectTimeDialog.showAndWait();
    setWakeuptime(selectTimeDialog.getRealcalendar());
    if (getWakeuptime() != null) {setDurationType(DurationType.TIME);}
    updateui();
}
    public void openwakeupdurationdialog() {
        SelectDurationDialog selectDurationDialog = new SelectDurationDialog(null);
        selectDurationDialog.showAndWait();
        setTotalsessionduration(selectDurationDialog.getSessionduration());
        if (getTotalsessionduration() != null) {setDurationType(DurationType.DURATION);}
        updateui();
    }
    public void updateui() {
        if (getDurationType() == DurationType.DURATION) {
            if (getTotalsessionduration() != null) {
                DurationDescription.setText("Sleep Duration:");
                DurationActual.setText(TimeUtils.formatlengthlong((int) getTotalsessionduration().toSeconds()));
            }
            else {resetui();}
        } else if (getDurationType() == DurationType.TIME) {
            if (getWakeuptime() != null) {
                DurationDescription.setText("Wake Up Time:");
                DurationActual.setText(TimeUtils.formattedtimefromcalendar(getWakeuptime()));
            } else {resetui();}
        } else {resetui();}
    }
    public void resetui() {
        DurationDescription.setText("No Duration Set");
        DurationActual.setText("No Duration Set");
    }
}
