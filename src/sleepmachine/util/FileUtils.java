package sleepmachine.util;

import javafx.animation.FadeTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Optional;

public class FileUtils {

    public static File getFile() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(null);
    }
    public static ArrayList<File> getFiles() {
        return null;
    }

    public static File openfileandsetbutton(Stage stage, Button button) {
        if (button.getText().equals("Selected")) {
            Alert overwritefile = new Alert(Alert.AlertType.CONFIRMATION);
            overwritefile.setTitle("Overwrite File");
            overwritefile.setContentText("Overwrite Already Selected File?");
            Optional<ButtonType> answer = overwritefile.showAndWait();
            if (! answer.isPresent()) {return null;}
//            if (answer.get() == ButtonType.OK || answer.get() == ButtonType.YES) {
//
//            }
            return null;
        }
        FileChooser fileChooser = new FileChooser();
        File tempfile = fileChooser.showOpenDialog(stage);
        if (tempfile == null) {return null;}
        if (FileUtils.supportedaudioformat(tempfile)) {button.setText("Selected");return tempfile;}
        else {FileUtils.invalidfile(tempfile);return null;}
    }

    public static void invalidfile(File file) {
        Alert invalidfile = new Alert(Alert.AlertType.WARNING);
        invalidfile.setTitle("Invalid File");
        invalidfile.setHeaderText("'." + getaudioformat(file) + "' Is Not A Supported Audio Format");
        invalidfile.setContentText("Supported Audio Formats: .mp3, .aif, .aiff, .wav");
        invalidfile.showAndWait();
    }

    public static String removefileextension(String text) {
        return text.substring(0, text.lastIndexOf('.'));
    }

    public static void previewaudiofile(MediaPlayer mediaplayer) {
        FadeTransition fadeout = new FadeTransition(Duration.seconds(2));
        fadeout.setFromValue(1.0);
        fadeout.setToValue(0.0);
        FadeTransition fadein = new FadeTransition(Duration.seconds(2));
        fadein.setFromValue(0.0);
        fadein.setToValue(1.0);
        //
        Duration a = Duration.seconds(15);
        mediaplayer.setStopTime(a);
        mediaplayer.setVolume(50);
        mediaplayer.play();
    }

    public static boolean supportedaudioformat(File file) {
        String extension = getaudioformat(file);
        String[] supportedextensions = {"mp3", "aif", "aiff", "wav"};
        for (String i : supportedextensions) {
            if (extension.equals(i)) {return true;}
        }
        return false;
    }

    public static String getaudioformat(File file) {
        int i = file.getName().lastIndexOf('.');
        return file.getName().substring(i + 1);
    }

    public static double getaudioduration(File audiofile) {
        try {
            Runtime rt = Runtime.getRuntime();
            String[] commands = {"ffprobe", "-v", "quiet", "-print_format", "compact=print_section=0:nokey=1:escape=csv",
                    "-show_entries", "format=duration", audiofile.getAbsolutePath()};
            Process proc = rt.exec(commands);
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));
            String s = null;
            StringBuilder a = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                a.append(s);
            }
            return Double.parseDouble(a.toString());
        } catch (IOException e) {e.printStackTrace();}
        return 0.0;
    }

    public static Boolean testmediafile(File file) {
        if (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav") || file.getName().endsWith(".aiff")) {
            // Pass To Media And MediaPlayer Here
            try {
                Media ca = new Media(file.toURI().toString());
                MediaPlayer ta = new MediaPlayer(ca);
                ta.dispose();
                return true;
            } catch (MediaException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static int converttomilliseconds(int hours, int minutes) {
        int seconds = (minutes + (hours * 60)) * 60;
        return seconds * 1000;
    }
}
