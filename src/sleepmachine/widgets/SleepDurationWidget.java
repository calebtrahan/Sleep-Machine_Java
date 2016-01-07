package sleepmachine.widgets;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import sleepmachine.Tools;
import sleepmachine.interfaces.Widget;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class SleepDurationWidget implements Widget {
    public static int MAXSESSIONDURATION = 14;
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

// Getters And Setters
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
    public Duration getadjustedduration() {
        if (getTotalsessionduration() != null) {
            return getTotalsessionduration();
        } else {
            return new Duration(getWakeuptime().getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
        }
    }

// Widget Implementation
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
    @Override
    public void statusswitch() {}

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
                DurationActual.setText(Tools.formatlengthlong((int) getTotalsessionduration().toSeconds()));
            }
            else {resetui();}
        } else if (getDurationType() == DurationType.TIME) {
            if (getWakeuptime() != null) {
                DurationDescription.setText("Wake Up Time:");
                DurationActual.setText(Tools.formattedtimefromcalendar(getWakeuptime()));
            } else {resetui();}
        } else {resetui();}
    }
    public void resetui() {
        DurationDescription.setText("No Duration Set");
        DurationActual.setText("No Duration Set");
    }

// Subclasses/Dialogs
    public static class SelectDurationDialog extends Stage implements Initializable {
    public Spinner HoursSpinner;
    public Spinner MinutesSpinner;
    public Button AcceptButton;
    public Button CancelButton;
    public Label WakeUpTimeLabel;
    private Duration sessionduration;

    public SelectDurationDialog(Parent parent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../assets/fxml/DurationSelector.fxml"));
        fxmlLoader.setController(this);
        try {setScene(new Scene(fxmlLoader.load())); this.setTitle("Set Sleep Duration");}
        catch (IOException e) {e.printStackTrace();}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAXSESSIONDURATION, 0));
        HoursSpinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> updateui());
        MinutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        MinutesSpinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> updateui());
        HoursSpinner.setEditable(true);
        MinutesSpinner.setEditable(true);
        this.setOnCloseRequest(event -> cancel(null));
    }

    public void updateui() {
        Calendar wakeuptime = Calendar.getInstance();
        wakeuptime.add(Calendar.HOUR_OF_DAY, getHours());
        wakeuptime.add(Calendar.MINUTE, getMinutes());
        WakeUpTimeLabel.setText(Tools.formattedtimefromcalendar(wakeuptime));
    }

    public void accept(ActionEvent actionEvent) {
        setSessionduration(new Duration(Tools.converttomilliseconds(getHours(), getMinutes())));
        super.close();
    }

    public void cancel(ActionEvent actionEvent) {
        setSessionduration(null);
        super.close();
    }

    public int getHours() {return (int) HoursSpinner.getValue();}

    public int getMinutes() {return (int) MinutesSpinner.getValue();}

    public Duration getSessionduration() {
        return sessionduration;
    }

    public void setSessionduration(Duration sessionduration) {
        this.sessionduration = sessionduration;
    }
}
    public static class SelectTimeDialog extends Stage implements Initializable {
        public Button AcceptButton;
        public Button CancelButton;
        public Label HourLabel;
        public Button AddHour;
        public Button SubtractHour;
        public Label MinuteLabel;
        public Button AddMinute;
        public Button SubtractMinute;
        public Label AM_PM_Label;
        public Label DateLabel;
        private Calendar calendar;
        private Calendar realcalendar;
        private int maxhours = 14;

        public SelectTimeDialog(Parent parent) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../assets/fxml/TimeSelector.fxml"));
            fxmlLoader.setController(this);
            try {setScene(new Scene(fxmlLoader.load())); this.setTitle("Add Ambience");}
            catch (IOException e) {e.printStackTrace();}
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            calendar = Calendar.getInstance();
            syncui();
        }

        // Button Actions
        public void accept(ActionEvent actionEvent) {
            // Test Here
            setRealcalendar(calendar);
            super.close();
        }
        public void cancel(ActionEvent actionEvent) {
            super.close();
        }

        // Methods
        public void addminute(ActionEvent actionEvent) {
            calendar.add(Calendar.MINUTE, 1);
            if (!isbelowmaxtime()) {calendar.add(Calendar.MINUTE, -1); cant_be_more_than_max_time();}
            syncui();
        }
        public void addhour(ActionEvent actionEvent) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            if (!isbelowmaxtime()) {calendar.add(Calendar.HOUR_OF_DAY, -1); cant_be_more_than_max_time();}
            syncui();
        }
        public void subtractminute(ActionEvent actionEvent) {
            calendar.add(Calendar.MINUTE, -1);
            if (! isafternow()) {calendar.add(Calendar.MINUTE, 1); cant_be_before_now();}
            syncui();
        }
        public void subtracthour(ActionEvent actionEvent) {
            calendar.add(Calendar.HOUR_OF_DAY, -1);
            if (! isafternow()) {calendar.add(Calendar.HOUR_OF_DAY, 1); cant_be_before_now();}
            syncui();
        }
        public boolean isafternow() {return calendar.getTime().after(Calendar.getInstance().getTime()) || calendar.getTime().equals(Calendar.getInstance().getTime());}
        public boolean isbelowmaxtime() {
            Long timedifference = calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
            int minutes = (timedifference.intValue() / 1000) / 60;
            return minutes < maxhours * 60;
        }
        public void syncui() {
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            if (hours >= 12) {
                if (hours > 12) {HourLabel.setText(Integer.toString(hours - 12));}
                else {HourLabel.setText(Integer.toString(12));}
                AM_PM_Label.setText("PM");
            } else {
                if (hours == 0) {HourLabel.setText(Integer.toString(12));}
                else {HourLabel.setText(Integer.toString(hours));}
                AM_PM_Label.setText("AM");
            }
            MinuteLabel.setText(String.format("%02d", calendar.get(Calendar.MINUTE)));
            if (calendar.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {DateLabel.setText("Today");}
            else {DateLabel.setText("Tomorrow");}
        }
        public void cant_be_before_now() {
            Alert notinpast = new Alert(Alert.AlertType.INFORMATION);
            notinpast.setTitle("Information");
            notinpast.setHeaderText("Cannot Be Before Now");
            notinpast.setContentText("Wake Up Time Must Be After Now");
            notinpast.showAndWait();
        }
        public void cant_be_more_than_max_time() {
            Alert overmaxtime = new Alert(Alert.AlertType.INFORMATION);
            overmaxtime.setTitle("Information");
            overmaxtime.setHeaderText("Cannot More Than " + maxhours + " Hours");
            overmaxtime.setContentText("Wake Up Time Must Be Less Than " + maxhours + " Hours");
            overmaxtime.showAndWait();
        }

        // Getters And Setters
        public Calendar getRealcalendar() {
            return realcalendar;
        }
        public void setRealcalendar(Calendar realcalendar) {
            this.realcalendar = realcalendar;
        }
    }

// Enums
    public enum DurationType {
        DURATION, TIME
    }

}
