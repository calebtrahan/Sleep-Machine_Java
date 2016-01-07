//package sleepmachine;
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.css.PseudoClass;
//import javafx.event.ActionEvent;
//import javafx.event.Event;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//import sleepmachine.helpers.*;
//import sleepmachine.widgets.CustomMusicWidget;
//
//import java.io.File;
//import java.net.URL;
//import java.util.*;
//
//// TODO Find Out Why PlaySession (Probably When Building EntrainmentOld) Opens A Fuck Ton Of Audio Streams
//
//
//public class MainController implements Initializable {
//
//    public Media tempambiencemedia;
//    public MediaPlayer tempambienceplayer;
//    public Media tempnoisemedia;
//    public MediaPlayer tempnoiseplayer;
//    public ObservableList<String> ambiencelistofitems;
//    public VBox root;
//    public ChoiceBox<String> entrainmentchoicebox;
////    public ListView<String> ambiencelist;
////    public Label newambiencefilevalue;
////    public Button ambienceselectbutton;
//    public Button ambienceaddbutton;
//    public ChoiceBox<String> noisechoicebox;
//    public Label statusbar;
//    public Button ambiencepreviewbutton;
//    public Button wakeupfileselectbutton;
////    public Button wakeupfilesetbutton;
////    public Label wakeupfilevalue;
////    public Button sleepamountsetbutton;
////    public Button sleeptimesetbutton;
////    public TextField sleepamounthours;
////    public TextField sleepamountminutes;
//    public Button removeambienceitembutton;
//    public Button noisepreviewbutton;
//    public Button startbutton;
//    public Button pausebutton;
//    public Button stopbutton;
//    public ChoiceBox<String> noisecategorychoicebox;
////    public Button createbutton;
//    ArrayList<File> noiseitems = new ArrayList<>();
//    ArrayList<String> noisenames = new ArrayList<>();
//    ArrayList<String> entrainmentnames = new ArrayList<>();
//    ArrayList<String> ambiencenames = new ArrayList<>();
//    ArrayList<File> ambiencefiles = new ArrayList<>();
//    List<Media> ambiencemedias = new ArrayList<>();
//    List<MediaPlayer> ambienceplayers = new ArrayList<>();
//    MediaPlayer currentnoiseplayer;
//    MediaPlayer currententrainmentplayer;
//    MediaPlayer currentambienceplayer;
//    File tempambiencefile;
//    Boolean sessioncreated = false;
//    Integer sessionpartslength;
//    Date wakeuptime;
//    Duration sessionduration;
//    File tempwakeupfile;
//    Media wakeupfile;
//    HashMap<String, EntrainmentOld> entrainmentchoices;
//    HashMap<Category, ArrayList<NoiseWidget>> categories;
//    String workingDir = System.getProperty("user.dir");
//    File sourcedir = new File(workingDir, "src");
//    File projectdir = new File(sourcedir, "sleepmachine");
//    File sounddirectory = new File(projectdir, "sound");
//    EntrainmentOld selectedentrainment;
//    NoiseWidget selectednoise;
//    Category selectedcategory;
//    Session currentession;
//    AmbienceController selectedambience;
//
//
//
//
//
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//
//        categories = Setup.getnoiseloopoptions(sounddirectory);
//        entrainmentchoices = Setup.getentrainmentoptions(sounddirectory);
//        setupnoisechoiceboxes();
//        setupentrainmentchoicebox();
////        setuptextfields();
//        selectedambience = new AmbienceController();
//
//    }
//
//    @FXML
//    public void startsessionplayback(ActionEvent actionEvent) {
//        // Prechecks Here To See If The User Didn't Forget To Set Anything For The Session
//        if (sessionduration != null) {
//        currentession = new Session(selectednoise, selectedentrainment, sessionduration, wakeupfile);
//        currentession.play();
//        // Set Up tick() On StatusBar Here
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Cannot Play Session");
//            alert.setContentText("No Wake-up Time Is Set");
//            alert.showAndWait();
//        }
//    }
//
//    public void setuptextfields() {
//
//    }
//
//    public void setupentrainmentchoicebox() {
//        // Append All EntrainmentOld Sessions Options To Choice Box (Don't Include If No/Not Enough Audio To Create Session)
//        Iterator<String> tempset = entrainmentchoices.keySet().iterator();
//        while (tempset.hasNext()) {
//            EntrainmentOld temp = entrainmentchoices.get(tempset.next());
//            entrainmentnames.add(temp.name);
//        }
//        ObservableList<String> toentrainmentbox = FXCollections.observableArrayList(entrainmentnames);
//        entrainmentchoicebox.setItems(toentrainmentbox);
//        entrainmentchoicebox.valueProperty().addListener(new ChangeListener() {
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                selectedentrainment = entrainmentchoices.get(newValue.toString());
//                selectedentrainment.prepare();
//            }
//        });
//    }
//
//    public void setupnoisechoiceboxes() {
//        try {
//            // Set Categories And Populate Choice Box
//            Iterator<Category> s = categories.keySet().iterator();
//            ArrayList<String> sortednames = new ArrayList<>();
//            while (s.hasNext()) {sortednames.add(MyFunctions.reformatcapatalized(s.next().toString()));}
//            Collections.sort(sortednames);
//            ObservableList<String> tocategorybox = FXCollections.observableArrayList(sortednames);
//            noisecategorychoicebox.setItems(tocategorybox);
//            noisecategorychoicebox.valueProperty().addListener(new ChangeListener() {
//                @Override
//                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                    // Find Which Category Is Selected
//                    for (Category b : categories.keySet()) {
//                        if (b.testifcorrectCategory(newValue.toString())) {
//                            selectedcategory = b;
//                            ArrayList<String> selectednoises = selectedcategory.getnoiseNames();
//                            ObservableList<String> tochoicebox = FXCollections.observableArrayList(selectednoises);
//                            noisechoicebox.setItems(tochoicebox);
//                            break;
//                        }
//                    }
//                }
//            });
//            File noisedirectory = new File("src/sleepmachine/sound/noise/");
//            File[] listofnoisefiles = noisedirectory.listFiles();
//            for (File i : listofnoisefiles) {
//                if (!i.isDirectory()) {
//                    Boolean mediagood = MyFunctions.testmediafile(i);
//                    String n1 = MyFunctions.reformatcapatalized(i.getName());
//                    String n2 = MyFunctions.removefileextension(n1);
//                    if (!mediagood) {
//                        noisenames.add("Select A Category");
//                    }
//                }
//            }
//            ObservableList<String> tochoicebox = FXCollections.observableArrayList(noisenames);
//            noisechoicebox.setItems(tochoicebox);
//            noisechoicebox.valueProperty().addListener(new ChangeListener() {
//                @Override
//                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                    if (tempnoiseplayer != null) {
//                        tempnoiseplayer.stop();
//                        // Reset Preview Button Here
//                    }
//                    selectedcategory.findnoise(newValue.toString());
//                    selectednoise = selectedcategory.getSelectednoise();
//                    tempnoisemedia = selectednoise.getMedia();
//                    tempnoiseplayer = new MediaPlayer(tempnoisemedia);
//                }
//            });
//        } catch (NullPointerException e) {statusbarmessage("No Files In That Directory", 3000.0);}
//    }
//
//    @FXML
//    public void selectanewambiencefile(ActionEvent actionEvent) {
////        selectedambience.selectnew(actionEvent, newambiencefilevalue);
//    }
//
//    @FXML
//    public void previewselectedambiencefile(ActionEvent actionEvent) {
//        selectedambience.preview(ambiencepreviewbutton, statusbar);
//    }
//
//
//
//    public Date getstoptime(int hours, int minutes) {
//        GregorianCalendar now = new GregorianCalendar();
//        if (hours != 0) {
//            now.add(GregorianCalendar.HOUR_OF_DAY, hours);
//        }
//        if (minutes != 0) {
//            now.add(GregorianCalendar.MINUTE, minutes);
//        }
//        return now.getTime();
//    }
//
//    @FXML
//    public void addnewambiencefile(ActionEvent actionEvent) {
////        selectedambience.add(ambiencelist, newambiencefilevalue);
//    }
//
//    @FXML
//    public void removeseelectedambiencefile(ActionEvent actionEvent) {
////        selectedambience.remove(ambiencelist);
//    }
//
//    @FXML
//    public void setsleepamount(ActionEvent actionEvent) {
//        // Display A Dialog Here -> Set Wakeup Time For xx:xx xM MM/DD/YYYY
//        Boolean error = false;
////        if (!sleepamounthours.getStyleClass().contains("error") && !sleepamountminutes.getStyleClass().contains("error")) {
////            int hours = 0;
////            int minutes = 0;
////            if (sleepamounthours.getText().equals("")) {
////                GuiUtils.validate(sleepamounthours, false);
////                error = true;
////            } else {hours = Integer.parseInt(sleepamounthours.getText());}
////            if (sleepamountminutes.getText().equals("")) {
////                GuiUtils.validate(sleepamountminutes, false);
////                error = true;
////            } else {minutes = Integer.parseInt(sleepamountminutes.getText());}
////            if (!error) {
////                Calendar cal = Calendar.getInstance();
////                Date now = cal.getTime();
////                cal.setTime(new Date());
////                if (hours != 0) {
////                    cal.add(Calendar.HOUR_OF_DAY, hours);
////                }
////                if (minutes != 0) {
////                    cal.add(Calendar.MINUTE, minutes);
////                }
////                Date setdate = cal.getTime();
////                System.out.println(setdate.toString());
////                Boolean acceptdate = false;
////                String datetime = String.format("Set Wakeup Time To %s", setdate);                                      // Format Date For Dialog
////                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
////                alert.setTitle("Confirmation");
////                alert.setHeaderText(datetime);
////                alert.setContentText("Set As Wakeup Time?");
////                Optional<ButtonType> result = alert.showAndWait();
////                if (result.get() == ButtonType.OK){
////                    wakeuptime = setdate;
////                    long secondstillwakeup = Math.abs((wakeuptime.getTime() - now.getTime()) / 1000);                   // Get Seconds Till Wakeup Time
////                    sessionduration = Duration.seconds(secondstillwakeup);                                              // Create A Duration Object Of When Wakeup
//////                    statusbar.setText("Wake-up Time Set To %s");                                                       // Send Confirmation To Statusbar
////                }
////            }
////        }
//    }
//
//    @FXML
//    public void previewnoiseselected(ActionEvent actionEvent) {
//        // Set ChoiceBox Error Status
//        int noiseindex = noisechoicebox.getSelectionModel().getSelectedIndex();
//        if (noiseindex == -1) {
//            GuiUtils.validate(noisechoicebox, false);}
//        else {
//            GuiUtils.validate(noisechoicebox, true);
//            Duration a = Duration.seconds(15);
//            tempnoiseplayer.setStopTime(a);
//            noisepreviewbutton.setText("Previewing NoiseWidget...");
//            tempnoiseplayer.play();
//            tempnoiseplayer.setOnEndOfMedia(new Runnable() {
//                @Override
//                public void run() {
//                    noisepreviewbutton.setText("Preview NoiseWidget Loop");
//                }
//            });
//    }}
//
//    @FXML
//    public void pausesessionplayback(ActionEvent actionEvent) {
//        if (currentession != null) {currentession.testifplayingnow();}
//        else {statusbarmessage("No Session Playing", 3000.0);}
//    }
//
//    @FXML
//    public void stopsessionplayback(ActionEvent actionEvent) {
//        if (currentession != null) {
//            if (currentession.getSessionplaying()) {
//                // Really Stop The Session?!!
//                currentession.stop();
//            }
//        } else {statusbarmessage("No Session Playing", 3000.0);}
//    }
//
//    public void selectnewwakeupfile(ActionEvent actionEvent) {
//        Node node = (Node) actionEvent.getSource();
//        Stage stage = (Stage) node.getScene().getWindow();
//
//    }
//
//    public void applywakeupfile(ActionEvent actionEvent) {
//        if (tempwakeupfile != null) {
//            GuiUtils.validate(wakeupfilevalue, true);
//            wakeupfile = new Media(tempwakeupfile.toURI().toString());
//            MediaPlayer b = new MediaPlayer(wakeupfile);
//            statusbar.setText("Wakeup File Set!");
//        } else {
//            GuiUtils.validate(wakeupfilevalue, false);
//        }
//    }
//
//    public void tableambienceitemselected(Event event) {
//        selectedambience.selectedinlistview(ambiencelist, newambiencefilevalue);
//    }
//
//    public void statusbarmessage(String text, double durationinms) {
//        statusbar.setText(text);
//        new Timeline(new KeyFrame(new Duration(durationinms), ae -> statusbar.setText(""))).play();
//    }
//}
//
