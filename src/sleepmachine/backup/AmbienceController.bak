//package sleepmachine.helpers;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListView;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//import sleepmachine.util.FileUtils;
//import sleepmachine.util.GuiUtils;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Random;
//
//public class AmbienceController {
//    private ArrayList<Media> ambiencemedias;
//    private ArrayList<MediaPlayer> ambienceplayers;
//    private ArrayList<File> ambiencefiles;
//    private Duration sessionduration;
//    private Media tempmedia;
//    private MediaPlayer tempmediaplayer;
//    private File tempfile;
//    private ObservableList<String> ambiencelistofitems;
//
//    public AmbienceController() {
//        ambiencefiles = new ArrayList<>();
//        ambienceplayers = new ArrayList<>();
//        ambiencemedias = new ArrayList<>();
//    }
//
//    public AmbienceController(ArrayList<Media> ambiencemedias, ArrayList<MediaPlayer> mediaPlayers, ArrayList<File> ambiencefiles) {
//        // Constructor For Existing/Saved AmbienceController
//    }
//
//    public boolean findifempty() {return ambiencefiles.isEmpty() || ambienceplayers.isEmpty();}
//
//    public void selectnew(ActionEvent actionEvent, Label newambiencefilevalue) {
//        Node node = (Node) actionEvent.getSource();
//        Stage stage = (Stage) node.getScene().getWindow();
//        Scene scene = stage.getScene();
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select An AmbienceController File To Add");
////        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", ".wav", ".mp3", ".aac"));
//        File newambiencefile = fileChooser.showOpenDialog(scene.getWindow());
//        if (newambiencefile != null) {
//            boolean filegood = FileUtils.testmediafile(newambiencefile);
//            if (filegood) {
//                tempfile = newambiencefile;
//                tempmedia = new Media(newambiencefile.toURI().toString());
//                tempmediaplayer = new MediaPlayer(tempmedia);
//                newambiencefilevalue.setText(newambiencefile.getName());
//            } else {newambiencefilevalue.setText(newambiencefile.getName() + " Is Not A Valid Audio File");}
//            GuiUtils.validate(newambiencefilevalue, filegood);
//        }
//    }
//
//    public void selectedinlistview(ListView<String> ambiencelist, Label newambiencefilevalue) {
//        // Method To Call When User Selected A Row From ListView, Find The File In ArrayList And Load It For Preview
//        int index = ambiencelist.getSelectionModel().getSelectedIndex();
//        if (index != -1) {
//            if (tempmediaplayer.getStatus() == MediaPlayer.Status.PLAYING) {tempmediaplayer.stop();}
//            newambiencefilevalue.setText("");
//            tempfile = ambiencefiles.get(index);
//            tempmedia = ambiencemedias.get(index);
//            tempmediaplayer = new MediaPlayer(tempmedia);
//        }
//    }
//
//    public void add(ListView<String> ambiencelist, Label newambiencefilevalue) {
//        if (tempmediaplayer != null) {
//            ArrayList<String> tempnameslist = new ArrayList<>();
//            ambiencefiles.add(tempfile);
//            for (File i : ambiencefiles) {tempnameslist.add(FileUtils.removefileextension(i.getName()));}
//            ambiencelistofitems = FXCollections.observableArrayList(tempnameslist);
//            ambiencelist.setItems(ambiencelistofitems);
//            ambiencemedias.add(tempmedia);
//        } else {
//            GuiUtils.validate(newambiencefilevalue, false);}
//    }
//
//    public void remove(ListView<String> ambiencelist) {
//        int index = ambiencelist.getSelectionModel().getSelectedIndex();
//        if (index != -1) {
//            ArrayList<String> templist = new ArrayList<String>();
//            ambiencefiles.remove(index);
//            ambiencemedias.remove(index);
//            for (File i : ambiencefiles) {templist.add(i.getName().substring(0, i.getName().lastIndexOf('.')));}
//            ambiencelistofitems = FXCollections.observableArrayList(templist);
//            ambiencelist.setItems(ambiencelistofitems);
//        } else {System.out.println("Nothing To Remove");}
//    }
//
//    public void preview(Button previewbutton, Label statusbar) {
//        FileUtils.previewaudiofile(tempmediaplayer);
//    }
//
//    public void create(Duration sessionduration) {
//
//    }
//
//    public void randomize() {
//        ArrayList<File> tempnames = new ArrayList<File>();
//        Double currentlength = 0.0;
//        int size = ambiencefiles.size();
//        int tempsize;
//        //TODO Get This Working. Way Too Many Files Repeated Right After Each Other!!!!
//        while (currentlength <= sessionduration.toSeconds()) {
//            System.out.println("Entered The While Loop");
//            int num;
//            if (ambiencefiles.size() > 1) {
//                Random randomnum = new Random();
//                num = randomnum.nextInt(ambiencefiles.size() - 1);
//            } else {
//                num = 1;
//            }
//            File i = ambiencefiles.get(num);
//            Media tempmedia = ambiencemedias.get(num);
//            double duration = tempmedia.getDuration().toSeconds();
//            System.out.println(duration);
//            tempsize = tempnames.size();
////            if (tempsize < size) {
////                if (!tempnames.contains(i)) {
////                    System.out.println("Entered #1. TempList Is " + tempsize + " And     Ambiencefiles Is " + ambiencefiles.size());
////                    tempnames.add(i);
////                    currentlength += duration;
////                    print(i.getName());
////                }
////            } else {
//            System.out.println("In The ELse Loop");
//            if (tempsize > 6 && ambiencefiles.size() >= 4) {
//                if (!tempnames.subList(tempsize - 3, tempsize - 1).contains(i)) {
//                    System.out.println("Entered #2");
//                    tempnames.add(i);
//                    currentlength += duration;
//                    System.out.println(i.getName());
//                }
//            }
//            else if (tempsize > 4 && ambiencefiles.size() >= 3) {
//                if (!tempnames.subList(tempsize - 2, tempsize - 1).contains(i)) {
//                    System.out.println("Entered #3");
//                    tempnames.add(i);
//                    currentlength += duration;
//                    System.out.println(i.getName());
//                }
//            }
//            else if (tempsize > 1 && ambiencefiles.size() >= 2) {
//                if (!tempnames.subList(tempsize - 1, tempsize - 1).contains(i)) {
//                    System.out.println("Entered #4");
//                    tempnames.add(i);
//                    currentlength += duration;
//                    System.out.println(i.getName());
//                }
//            }
//            else {
//                System.out.println("Entered #5");
//                tempnames.add(i);
//                currentlength += duration;
//                System.out.println(i.getName());
//            }
//        }
//        System.out.println(currentlength);
//        System.out.println("Templist Is : " + tempnames.toString());
//        ambiencefiles = tempnames;
//    }
//
//}
