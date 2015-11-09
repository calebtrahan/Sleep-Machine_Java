package sleepmachine.dialogs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sleepmachine.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPartsDialog extends Stage implements Initializable {
    public Button OpenPart1Button;
    public CheckBox Part2Switch;
    public Label Part2Label;
    public Button OpenPart2Button;
    public CheckBox Part3Switch;
    public Label Part3Label;
    public Button OpenPart3Button;
    public CheckBox Part4Switch;
    public Label Part4Label;
    public Button OpenPart4Button;
    public Button AcceptButton;
    public Button CancelButton;
    private File Part1;
    private File Part2;
    private File Part3;
    private File Part4;

    public AddPartsDialog(Parent parent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../assets/fxml/AddEntrainmentPartsDialog.fxml"));
        fxmlLoader.setController(this);
        try {setScene(new Scene(fxmlLoader.load())); this.setTitle("Add Session Part");}
        catch (IOException e) {e.printStackTrace();}
        this.setOnCloseRequest(event -> closedialog(null));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        togglepart2(null);
        togglepart3(null);
        togglepart4(null);
    }

    public void setstatus(boolean status, CheckBox checkbox, Label label, Button button) {
        if (status) {checkbox.setText("ON");} else {checkbox.setText("OFF");}
        label.setDisable(!status);
        button.setDisable(!status);
    }

    public File getPart1() {
        return Part1;
    }
    public void setPart1(File part1) {
        Part1 = part1;
    }
    public File getPart2() {
        return Part2;
    }
    public void setPart2(File part2) {
        Part2 = part2;
    }
    public File getPart3() {
        return Part3;
    }
    public void setPart3(File part3) {
        Part3 = part3;
    }
    public File getPart4() {
        return Part4;
    }
    public void setPart4(File part4) {
        Part4 = part4;
    }

    public void openpart1(ActionEvent actionEvent) {setPart1(FileUtils.openfileandsetbutton(this, OpenPart1Button));}
    public void openpart2(ActionEvent actionEvent) {setPart2(FileUtils.openfileandsetbutton(this, OpenPart2Button));}
    public void openpart3(ActionEvent actionEvent) {setPart3(FileUtils.openfileandsetbutton(this, OpenPart3Button));}
    public void openpart4(ActionEvent actionEvent) {setPart4(FileUtils.openfileandsetbutton(this, OpenPart4Button));}

    public void togglepart2(ActionEvent actionEvent) {setstatus(Part2Switch.isSelected(), Part2Switch, Part2Label, OpenPart2Button);}
    public void togglepart3(ActionEvent actionEvent) {setstatus(Part3Switch.isSelected(), Part3Switch, Part3Label, OpenPart3Button);}
    public void togglepart4(ActionEvent actionEvent) {setstatus(Part4Switch.isSelected(), Part4Switch, Part4Label, OpenPart4Button);}

    public void acceptandclose(ActionEvent actionEvent) {
        if (getPart1() == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Missing Part 1");
            a.setContentText("Part 1 Is Mandatory");
            a.showAndWait();
        } else {super.close();}
    }

    public void closedialog(ActionEvent actionEvent) {
        System.out.println("Called Close Dialog Method");
        boolean fileseelected = false;
        if (getPart1() != null) {fileseelected = true;}
        if (getPart2() != null) {fileseelected = true;}
        if (getPart3() != null) {fileseelected = true;}
        if (getPart4() != null) {fileseelected = true;}
        if (fileseelected) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Exit Without Adding");
            confirmation.setHeaderText("You Have Selected Some Parts");
            confirmation.setContentText("Exit Without Adding These To A New EntrainmentWidget Preset?");
            Optional<ButtonType> answer = confirmation.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.OK) {super.close();}
            else {return;}
        }
        super.close();
    }
}
