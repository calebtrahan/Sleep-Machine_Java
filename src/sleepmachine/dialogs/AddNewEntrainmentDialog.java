package sleepmachine.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sleepmachine.MainController;
import sleepmachine.util.FileUtils;
import sleepmachine.util.TimeUtils;
import sleepmachine.util.xml.Entrainment;
import sleepmachine.util.xml.Entrainments;
import sleepmachine.widgets.EntrainmentWidget;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddNewEntrainmentDialog extends Stage implements Initializable {
    public TextField Name;
    public Button IntroButton;
    public Button OutroButton;
    public Button FillerButton;
    public TextArea Description;
    public Button AddButton;
    public Button CancelButton;
    public Button SessionPartsButton;
    private EntrainmentWidget entrainmentWidget;
    private File introfile;
    private File fillerfile;
    private File part_1_file;
    private File part_2_file;
    private File part_3_file;
    private File part_4_file;
    private File outrofile;
    private int sessionparts;

    public AddNewEntrainmentDialog(Parent parent, EntrainmentWidget entrainmentWidget) {
        this.entrainmentWidget = entrainmentWidget;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../assets/fxml/AddNewEntrainmentDialog.fxml"));
        fxmlLoader.setController(this);
        try {setScene(new Scene(fxmlLoader.load())); this.setTitle("Add Ambience");}
        catch (IOException e) {e.printStackTrace();}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        installtooltips();
    }

    // TODO Add Methods And Connect To XML Marshaller, And Test

    public void installtooltips() {

    }

    // Button Action Methods

    public void selectentrainmentparts(ActionEvent actionEvent) {
        AddPartsDialog addPartsDialog = new AddPartsDialog(null);
        addPartsDialog.showAndWait();
        setPart_1_file(addPartsDialog.getPart1());
        setPart_2_file(addPartsDialog.getPart2());
        setPart_3_file(addPartsDialog.getPart3());
        setPart_4_file(addPartsDialog.getPart4());
        int partcount = 0;
        if (getPart_1_file() != null) {partcount++;}
        if (getPart_2_file() != null) {partcount++;}
        if (getPart_3_file() != null) {partcount++;}
        if (getPart_4_file() != null) {partcount++;}
        setSessionparts(partcount);
        if (getSessionparts() > 0) {SessionPartsButton.setText(partcount + " Selected");}
    }
    public void selectouttro(ActionEvent actionEvent) {setOutrofile(FileUtils.openfileandsetbutton(this, OutroButton));}
    public void selectfiller(ActionEvent actionEvent) {
        File tempfile = FileUtils.openfileandsetbutton(this, FillerButton);
        if (tempfile != null) {
            double durationinseconds = FileUtils.getaudioduration(tempfile);
            if (durationinseconds > 60.0) {
                Alert toolong = new Alert(Alert.AlertType.CONFIRMATION);
                toolong.setTitle("Confirmation");
                toolong.setHeaderText("Filler File Is Too Long At " + TimeUtils.formatlengthlong((int) durationinseconds) +
                        ". Filler File Should Be Less Than Or Equal To 1 Minute");
                toolong.setContentText("Add Anyway?");
                Optional<ButtonType> answer = toolong.showAndWait();
                if (answer.isPresent()) {
                    if (answer.get() != ButtonType.YES) {return;}}
            }
            setFillerfile(tempfile);
        }
    }
    public void selectintro(ActionEvent actionEvent) {setIntrofile(FileUtils.openfileandsetbutton(this, IntroButton));}
    public void addandclose(ActionEvent actionEvent) {
        boolean namegood = Name.getText().length() > 0;
        boolean descriptiongood = Description.getText().length() > 0;
        boolean introgood = getIntrofile() != null;
        boolean partsgood = getSessionparts() > 0;
        boolean fillergood = getFillerfile() != null;
        boolean outrogood = getOutrofile() != null;
        if (namegood && descriptiongood && introgood && partsgood && fillergood && outrogood) {
            try {
                Entrainments entrainments = entrainmentWidget.getAllentrainments();
                int parts = getSessionparts();
                List<Entrainment> entrainmentList;
                if (MainController.ENTRAINMENTXMLFILE.exists()) {entrainmentList = entrainments.getEntrainment();}
                else {entrainmentList = new ArrayList<>();}
                Entrainment thisentrainment = null;
                if (parts == 1) {thisentrainment = new Entrainment(Name.getText(), getIntrofile(), getPart_1_file(), getFillerfile(), getOutrofile(), Description.getText());}
                if (parts == 2) {thisentrainment = new Entrainment(Name.getText(), getIntrofile(), getPart_1_file(), getPart_2_file(), getFillerfile(), getOutrofile(), Description.getText());}
                if (parts == 3) {thisentrainment = new Entrainment(Name.getText(), getIntrofile(), getPart_1_file(), getPart_2_file(), getPart_3_file(), getFillerfile(), getOutrofile(), Description.getText());}
                if (parts == 4) {thisentrainment = new Entrainment(Name.getText(), getIntrofile(), getPart_1_file(), getPart_2_file(), getPart_3_file(), getPart_4_file(), getFillerfile(), getOutrofile(), Description.getText());}
                if (thisentrainment != null) {
                    thisentrainment.setMinimumduration(getminimumduration());
                    entrainmentList.add(thisentrainment);
                    entrainments.setEntrainment(entrainmentList);
                    JAXBContext context = JAXBContext.newInstance(Entrainments.class);
                    Marshaller createMarshaller = context.createMarshaller();
                    createMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    createMarshaller.marshal(entrainments, MainController.ENTRAINMENTXMLFILE);
                }
            } catch (JAXBException e) {e.printStackTrace(); return;}
        } else {
            ArrayList<String> partsmissing = new ArrayList<>();
            if (!namegood) {partsmissing.add("Name");}
            if (!descriptiongood) {partsmissing.add("Description");}
            if (!introgood) {partsmissing.add("Intro File");}
            if (!partsgood) {partsmissing.add("Session Part(s)");}
            if (!fillergood) {partsmissing.add("Filler File");}
            if (!outrogood) {partsmissing.add("Outro File");}
            StringBuilder msgtext = new StringBuilder();
            for (String i : partsmissing) {
                msgtext.append(i);
                if (!i.equals(partsmissing.get(partsmissing.size() - 1))) {msgtext.append(", ");}
            }
            Alert cannotaddalert = new Alert(Alert.AlertType.ERROR);
            cannotaddalert.setTitle("Cannot Add EntrainmentWidget");
            cannotaddalert.setHeaderText("Cannot Add Because I Am Missing The Following Parts");
            cannotaddalert.setContentText(msgtext.toString());
            cannotaddalert.showAndWait();
            return;
        }
        super.close();
    }

    // Other Methods

    public double getminimumduration() {
        double durationinminutes = 0.0;
        if (getIntrofile() != null) {durationinminutes += FileUtils.getaudioduration(getIntrofile());}
        if (getFillerfile() != null) {durationinminutes += FileUtils.getaudioduration(getFillerfile());}
        if (getPart_1_file() != null) {durationinminutes += FileUtils.getaudioduration(getPart_1_file());}
        if (getPart_2_file() != null) {durationinminutes += FileUtils.getaudioduration(getPart_2_file());}
        if (getPart_3_file() != null) {durationinminutes += FileUtils.getaudioduration(getPart_3_file());}
        if (getPart_4_file() != null) {durationinminutes += FileUtils.getaudioduration(getPart_4_file());}
        if (getOutrofile() != null) {durationinminutes += FileUtils.getaudioduration(getOutrofile());}
        return durationinminutes;
    }


    // Getters And Setters
    public File getIntrofile() {
        return introfile;
    }
    public void setIntrofile(File introfile) {
        this.introfile = introfile;
    }
    public File getFillerfile() {
        return fillerfile;
    }
    public void setFillerfile(File fillerfile) {
        this.fillerfile = fillerfile;
    }
    public File getPart_1_file() {
        return part_1_file;
    }
    public void setPart_1_file(File part_1_file) {
        this.part_1_file = part_1_file;
    }
    public File getPart_2_file() {
        return part_2_file;
    }
    public void setPart_2_file(File part_2_file) {
        this.part_2_file = part_2_file;
    }
    public File getPart_3_file() {
        return part_3_file;
    }
    public void setPart_3_file(File part_3_file) {
        this.part_3_file = part_3_file;
    }
    public File getPart_4_file() {
        return part_4_file;
    }
    public void setPart_4_file(File part_4_file) {
        this.part_4_file = part_4_file;
    }
    public File getOutrofile() {
        return outrofile;
    }
    public void setOutrofile(File outrofile) {
        this.outrofile = outrofile;
    }
    public int getSessionparts() {
        return sessionparts;
    }
    public void setSessionparts(int sessionparts) {
        this.sessionparts = sessionparts;
    }

}
