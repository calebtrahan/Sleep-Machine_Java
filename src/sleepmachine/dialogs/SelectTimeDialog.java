package sleepmachine.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;


public class SelectTimeDialog extends Stage implements Initializable {
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
