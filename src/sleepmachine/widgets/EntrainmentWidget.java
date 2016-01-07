package sleepmachine.widgets;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import sleepmachine.MainController;
import sleepmachine.Playable;
import sleepmachine.Tools;
import sleepmachine.Widget;
import sleepmachine.xml.Entrainment;
import sleepmachine.xml.Entrainments;

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

public class EntrainmentWidget implements Widget, Playable {
    private CheckBox OnOffSwitch;
    private ChoiceBox Presets;
    private TextArea Description;
    private MediaPlayer currentplayer;
    private int playcount;
    private ArrayList<Media> sessionmedia;
    private Entrainments allentrainments;
    private Entrainment selectedentrainment;
    private MainController root;
    private boolean enabled;

    public EntrainmentWidget(CheckBox onOffSwitch, ChoiceBox presets, TextArea description, MainController root) {
        OnOffSwitch = onOffSwitch;
        Presets = presets;
        Description = description;
        this.root = root;
        allentrainments = new Entrainments();

    }

// Getters And Setters
    public Entrainments getAllentrainments() {
    return allentrainments;
}
    public void setAllentrainments(Entrainments allentrainments) {this.allentrainments = allentrainments;}
    public Entrainment getSelectedentrainment() {return selectedentrainment;}
    public void setSelectedentrainment(Entrainment selectedentrainment) {this.selectedentrainment = selectedentrainment;}
    public boolean isEnabled() {return enabled;}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}

// Playable Methods
    @Override
    public boolean create(Duration duration) {
        if (getSelectedentrainment() != null && root.getSleepDurationWidget().getTotalsessionduration().toSeconds() > 0.0) {
            if (root.getWakeUpSoundWidget().getWakeupduration() != null) {
                return getSelectedentrainment().build(root.getWakeUpSoundWidget().getWakeupduration(), root.getSleepDurationWidget().getadjustedduration());
            } else {
                return getSelectedentrainment().build(new Duration(0.0), root.getSleepDurationWidget().getadjustedduration());
            }
        } else {return false;}
    }
    @Override
    public void startplayback() {
        playcount = 0;
        sessionmedia = new ArrayList<>();
        for (File i : getSelectedentrainment().getEntrainmentfiles()) {sessionmedia.add(new Media(i.toURI().toString()));}
        setCurrentPlayer(new MediaPlayer(sessionmedia.get(playcount)));
        getCurrentPlayer().play();
        getCurrentPlayer().setOnEndOfMedia(this::playnextfile);
    }
    @Override
    public void playnextfile() {
        getCurrentPlayer().stop();
        getCurrentPlayer().dispose();
        increaseplaycount();
        setCurrentPlayer(new MediaPlayer(sessionmedia.get(playcount)));
        System.out.println("Playcount: " + playcount + ". Now Playing: " + getCurrentPlayer().getMedia().getSource());
        getCurrentPlayer().play();
        getCurrentPlayer().setOnEndOfMedia(this::playnextfile);
    }
    @Override
    public void play() {
        if (getCurrentPlayer() == null) {startplayback();}
        else {getCurrentPlayer().play();}
    }
    @Override
    public void pause() {if (getCurrentPlayer() != null) {getCurrentPlayer().pause();}}
    @Override
    public void stop() {if (getCurrentPlayer() != null) {getCurrentPlayer().stop();}}
    @Override
    public void endofplayback() {
        getCurrentPlayer().stop();
        getCurrentPlayer().dispose();
    }
    @Override
    public void setCurrentPlayer(MediaPlayer mediaPlayer) {currentplayer = mediaPlayer;}
    @Override
    public MediaPlayer getCurrentPlayer() {return currentplayer;}

// Widget Methods
    @Override
    public boolean isValid() {
        return false;
    }
    @Override
    public void disable() {
        OnOffSwitch.setDisable(true);
        Presets.setDisable(true);
        Description.setDisable(true);
    }
    @Override
    public void enable() {
        OnOffSwitch.setDisable(false);
        Presets.setDisable(false);
        Description.setDisable(false);
    }
    @Override
    public void resetallvalues() {


    }
    @Override
    public void statusswitch() {
        boolean status = OnOffSwitch.isSelected();
        setEnabled(status);
        Tools.togglecheckboxtext(OnOffSwitch);
        Presets.setDisable(!status);
        Description.setDisable(!status);
    }

// Other Methods
    protected void increaseplaycount() {playcount++;}
    public void changeentrainmentselection() {
        setSelectedentrainment(allentrainments.getselectedentrainment(Presets.getSelectionModel().getSelectedItem().toString()));
        if (getSelectedentrainment().checkifallpartsexist()) {
            Description.setText(getSelectedentrainment().getDescription());
        } else {
            Description.setText(getSelectedentrainment().getName() + " Is Missing Files Necessary To Play It. Please Select Another");
            setSelectedentrainment(null);
        }
    }
    public void syncentrainment() {
        allentrainments.getEntrainment();
        try {allentrainments.populatefromxml();} catch (JAXBException e) {e.printStackTrace(); return;}
        ArrayList<String> entrainmentnames = allentrainments.getallentrainmentnames();
        if (entrainmentnames != null) {
            ObservableList<String> namelist = FXCollections.observableArrayList();
            namelist.addAll(entrainmentnames);
            Presets.setItems(namelist);
            setAllentrainments(allentrainments);
        } else {
            // TODO No EntrainmentWidget Exists. Dialog Here To Add Noises
        }
    }
    public void addentrainment() {
        allentrainments.getEntrainment();
        AddNewEntrainmentDialog a = new AddNewEntrainmentDialog(null, this);
        a.showAndWait();
        try {allentrainments.populatefromxml();} catch (JAXBException e) {e.printStackTrace();}
        syncentrainment();
    }

// Subclasses/Dialogs
    public static class AddNewEntrainmentDialog extends Stage implements Initializable {
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
        public void selectouttro(ActionEvent actionEvent) {setOutrofile(Tools.openfileandsetbutton(this, OutroButton));}
        public void selectfiller(ActionEvent actionEvent) {
            File tempfile = Tools.openfileandsetbutton(this, FillerButton);
            if (tempfile != null) {
                double durationinseconds = Tools.getaudioduration(tempfile);
                if (durationinseconds > 60.0) {
                    Alert toolong = new Alert(Alert.AlertType.CONFIRMATION);
                    toolong.setTitle("Confirmation");
                    toolong.setHeaderText("Filler File Is Too Long At " + Tools.formatlengthlong((int) durationinseconds) +
                            ". Filler File Should Be Less Than Or Equal To 1 Minute");
                    toolong.setContentText("Add Anyway?");
                    Optional<ButtonType> answer = toolong.showAndWait();
                    if (answer.isPresent()) {
                        if (answer.get() != ButtonType.YES) {return;}}
                }
                setFillerfile(tempfile);
            }
        }
        public void selectintro(ActionEvent actionEvent) {setIntrofile(Tools.openfileandsetbutton(this, IntroButton));}
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
            if (getIntrofile() != null) {durationinminutes += Tools.getaudioduration(getIntrofile());}
            if (getFillerfile() != null) {durationinminutes += Tools.getaudioduration(getFillerfile());}
            if (getPart_1_file() != null) {durationinminutes += Tools.getaudioduration(getPart_1_file());}
            if (getPart_2_file() != null) {durationinminutes += Tools.getaudioduration(getPart_2_file());}
            if (getPart_3_file() != null) {durationinminutes += Tools.getaudioduration(getPart_3_file());}
            if (getPart_4_file() != null) {durationinminutes += Tools.getaudioduration(getPart_4_file());}
            if (getOutrofile() != null) {durationinminutes += Tools.getaudioduration(getOutrofile());}
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
    public static class AddPartsDialog extends Stage implements Initializable {
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

        public void openpart1(ActionEvent actionEvent) {setPart1(Tools.openfileandsetbutton(this, OpenPart1Button));}
        public void openpart2(ActionEvent actionEvent) {setPart2(Tools.openfileandsetbutton(this, OpenPart2Button));}
        public void openpart3(ActionEvent actionEvent) {setPart3(Tools.openfileandsetbutton(this, OpenPart3Button));}
        public void openpart4(ActionEvent actionEvent) {setPart4(Tools.openfileandsetbutton(this, OpenPart4Button));}

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
}
