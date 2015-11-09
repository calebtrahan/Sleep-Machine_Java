package sleepmachine.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import sleepmachine.MainController;
import sleepmachine.util.FileUtils;
import sleepmachine.util.TimeUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;


public class SelectDurationDialog extends Stage implements Initializable {
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
        HoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MainController.MAXSESSIONDURATION, 0));
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
        WakeUpTimeLabel.setText(TimeUtils.formattedtimefromcalendar(wakeuptime));
    }

    public void accept(ActionEvent actionEvent) {
        setSessionduration(new Duration(FileUtils.converttomilliseconds(getHours(), getMinutes())));
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
