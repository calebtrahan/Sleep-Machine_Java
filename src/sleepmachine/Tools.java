package sleepmachine;

import javafx.animation.FadeTransition;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.control.*;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;

public class Tools {

// File Utils
    public static File getFile() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(null);
    }
    public static ArrayList<File> getFiles() {
        FileChooser fileChooser = new FileChooser();
        return new ArrayList<>(fileChooser.showOpenMultipleDialog(null));
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
        if (supportedaudioformat(tempfile)) {button.setText("Selected");return tempfile;}
        else {
            invalidfile(tempfile);return null;}
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

// Audio Utils
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

// Time Utils
    public static int converttomilliseconds(int hours, int minutes) {
        int seconds = (minutes + (hours * 60)) * 60;
        return seconds * 1000;
    }
    public static String getformattedhoursandminutes(int hours, int minutes) {
        StringBuilder text = new StringBuilder();
        if (hours > 0) {
            text.append(hours).append(" Hr");
            if (hours > 1) {text.append("s");}
        }
        if (minutes > 0 && hours > 0) {text.append(" & ");}
        if (minutes > 0) {
            text.append(minutes).append(" Min");
            if (minutes > 1) {text.append("s");}
        }
        return text.toString();
    }
    public static String getapproximateendtime(int totalminutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, totalminutes);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(cal.getTime());
    }
    public static String formatlengthshort(int sec) {
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        if (sec >= 3600) {hours = sec / 3600; sec -= hours * 3600;}
        if (sec >= 60) {minutes = sec / 60; sec -= minutes * 60;}
        seconds = sec;
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
    public static String formattedtimefromcalendar(Calendar calendar) {
        StringBuilder time = new StringBuilder();
        if (calendar.get(Calendar.HOUR_OF_DAY) != 0) {time.append(String.format("%d", calendar.get(Calendar.HOUR)));}
        else {time.append("12");}
        time.append(":");
        time.append(String.format("%02d", calendar.get(Calendar.MINUTE)));
        time.append(" ");
        int i = calendar.get(Calendar.AM_PM);
        if (i == 0) {time.append("AM");}
        else {time.append("PM");}
        return time.toString();
    }
    public static String formatlengthlong(int sec) {
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        if (sec >= 3600) {
            hours = sec / 3600;
            sec -= hours * 3600;
        }
        if (sec >= 60) {
            minutes = sec / 60;
            sec -= minutes * 60;
        }
        seconds = sec;
        boolean valid = hours > 0 || minutes > 0;
        StringBuilder text = new StringBuilder();
        if (valid) {
            if (hours > 0) {
                text.append(hours);
                text.append(" Hr");
                if (hours > 1) text.append("s");
            }
            if (minutes > 0) {
                text.append(minutes);
                text.append(" Min");
                if (minutes > 1) text.append("s");
            }
            return text.toString();
        } else {
            return null;
        }
    }
    public static int getMinutesTillCalendar(Calendar calendar) {
        Calendar now = Calendar.getInstance();
        long milliseconds = calendar.getTimeInMillis() - now.getTimeInMillis();
        return (int) (milliseconds / 1000) / 60;
    }

// GuiUtils
    public static void validate(TextField txtfield, int highvalue, int valtotest) {
        ObservableList<String> styleclass = txtfield.getStyleClass();
        if (valtotest > highvalue) {if (!styleclass.contains("error")) {styleclass.add("error");}}
        else {styleclass.removeAll(Collections.singleton("error"));}
    }
    public static void validate(ChoiceBox<String> choicebox, Boolean val) {
        ObservableList<String> styleclass = choicebox.getStyleClass();
        if (! val ) {if (!styleclass.contains("error")) {styleclass.add("error");}}
        else {styleclass.removeAll(Collections.singleton("error"));}
    }
    public static void validate(TextField txtfield, Boolean val) {
        ObservableList<String> styleclass = txtfield.getStyleClass();
        if (! val ) {if (!styleclass.contains("error")) {styleclass.add("error");}}
        else {styleclass.removeAll(Collections.singleton("error"));}
    }
    public static void validate(Label lbl, Boolean val) {
        ObservableList<String> styleclass = lbl.getStyleClass();
        if (! val ) {if (!styleclass.contains("error")) {styleclass.add("error");}
        } else {styleclass.removeAll(Collections.singleton("error"));}
    }
    public static void numerictextfield(TextField textField, int maxval) {
        final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
        textField.pseudoClassStateChanged(errorClass, true);
        textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.equals("")) {
                try {
                    Integer newval = Integer.parseInt(newValue);
                    validate(textField, maxval, newval);
                } catch (NumberFormatException e) {
                    textField.setText(oldValue);
                }
            }
        });
    }
    public static boolean getanswerdialog(String titletext, String headertext, String contenttext) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(titletext);
        a.setHeaderText(headertext);
        a.setContentText(contenttext);
        Optional<ButtonType> answer = a.showAndWait();
        return answer.isPresent() && answer.get() == ButtonType.OK;
    }
    public static void showinformationdialog(String titletext, String headertext, String contexttext) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titletext);
        a.setHeaderText(headertext);
        a.setContentText(contexttext);
        a.showAndWait();
    }
    public static void showerrordialog(String titletext, String headertext, String contenttext) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titletext);
        a.setHeaderText(headertext);
        a.setContentText(contenttext);
        a.showAndWait();
    }
    public static void togglecheckboxtext(CheckBox checkbox) {
        if (checkbox.isSelected()) {checkbox.setText("ON");} else {checkbox.setText("OFF");}
    }

// String Utils
    public static String reformatcapatalized(String text) {
        StringBuilder newname = new StringBuilder();
        char[] tempname = text.toCharArray();
        for (int x = 0; x < tempname.length; x++) {
            if (x == 0) {
                newname.append(Character.toUpperCase(tempname[0]));
            } else if (Character.isUpperCase(tempname[x])) {
                newname.append(" ");
                newname.append(tempname[x]);
            } else {
                newname.append(tempname[x]);
            }
        }
        return newname.toString();
    }

}
