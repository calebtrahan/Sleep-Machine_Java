package sleepmachine.dialogs;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sleepmachine.util.FileUtils;
import sleepmachine.util.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CustomMusicDialog extends Stage implements Initializable {
    public TableView<MusicFile> MusicTableView;
    public Button AddFilesButton;
    public Button PreviewSelectedButton;
    public Button RemoveSelectedButton;
    public Button AcceptButton;
    public Button CancelButton;
    public TableColumn<MusicFile, String> NameColumn;
    public TableColumn<MusicFile, String> LengthColumn;
    private Media previewmedia = null;
    private MediaPlayer previewmediaplayer = null;
    private ObservableList<MusicFile> songListData = FXCollections.observableArrayList();

    public CustomMusicDialog(Parent parent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../assets/fxml/CustomMusicSelector.fxml"));
        fxmlLoader.setController(this);
        try {setScene(new Scene(fxmlLoader.load())); this.setTitle("Add Custom Music");}
        catch (IOException e) {e.printStackTrace();}
    }

    public CustomMusicDialog(Parent parent, ArrayList<MusicFile> existingmusicfiles) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../assets/fxml/CustomMusicSelector.fxml"));
        fxmlLoader.setController(this);
        try {setScene(new Scene(fxmlLoader.load())); this.setTitle("Add Custom Music");}
        catch (IOException e) {e.printStackTrace();}
        songListData.clear();
        songListData.addAll(existingmusicfiles);
        MusicTableView.setItems(songListData);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NameColumn.setCellValueFactory(cellData -> cellData.getValue().name);
        LengthColumn.setCellValueFactory(cellDate -> cellDate.getValue().length);
        MusicTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectionchanged(newValue));
        setOnCloseRequest(event -> close());
    }

    public void addfiles(ActionEvent actionEvent) {
        FileChooser a = new FileChooser();
        List<File> files = a.showOpenMultipleDialog(this);
        ArrayList<File> notvalidfilenames = new ArrayList<>();
        if (files != null) {
            for (File i : files) {
                if (FileUtils.supportedaudioformat(i)) {
                    songListData.add(new MusicFile(i.getName(), i));
                } else {
                    notvalidfilenames.add(i);
                }
            }
        }
        MusicTableView.setItems(songListData);
        if (notvalidfilenames.size() > 0) {
            Alert b = new Alert(Alert.AlertType.WARNING);
            b.setTitle("Couldn't Add All Files");
            b.setHeaderText("These Files Couldn't Be Added:");
            StringBuilder c = new StringBuilder();
            for (File i : notvalidfilenames) {
                c.append(i.getName());
                if (i != notvalidfilenames.get(notvalidfilenames.size() - 1)) {
                    c.append("\n");
                }
            }
            b.setContentText(c.toString());
            b.showAndWait();
        }
    }

    public void preview(ActionEvent actionEvent) {
        if (previewmedia != null && previewmediaplayer != null) {
            if (previewmediaplayer.getStatus() == MediaPlayer.Status.READY || previewmediaplayer.getStatus() == MediaPlayer.Status.STOPPED) {
                previewmediaplayer.play();
                PreviewSelectedButton.setText("Stop Preview");
            } else if (previewmediaplayer.getStatus() == MediaPlayer.Status.PLAYING) {
                previewmediaplayer.stop();
                PreviewSelectedButton.setText("Preview Selected");
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Cannot Preview");
                a.setContentText("An Unknown Error Occured. Retry Loading The Sound File");
            }
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Nothing To Preview");
            a.setContentText("Please Open A File For Preview");
            a.showAndWait();
        }
    }

    public void removeselected(ActionEvent actionEvent) {
        int index = MusicTableView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            MusicTableView.getItems().remove(index);
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("No Selection");
            a.setHeaderText("Nothing Selected");
            a.setContentText("Select An Item From The Table To Delete");
            a.showAndWait();
        }
    }

    public void accept(ActionEvent actionEvent) {
        // Test If No Files And Pass Into CustomMusicWidget Class Maybe?
        super.close();
    }

    public void selectionchanged(MusicFile musicFile) {
        File tempfile = musicFile.getFile();
        if (previewmediaplayer != null) {
            previewmediaplayer.stop();
            previewmediaplayer.dispose();
        }
        PreviewSelectedButton.setText("Preview Selected");
        previewmedia = new Media(tempfile.toURI().toString());
        previewmediaplayer = new MediaPlayer(previewmedia);
    }

    public ArrayList<MusicFile> getCustomMusic() {
        if (MusicTableView.getItems().size() > 0) {
            ArrayList<MusicFile> musicfiles = new ArrayList<>();
            for (MusicFile i : MusicTableView.getItems()) {
                musicfiles.add(i);
            }
            return musicfiles;
        } else {
            return null;
        }
    }

    public class MusicFile {
        private StringProperty name;
        private StringProperty length;
        private File file;
        private double duration; // in seconds

        public MusicFile(String name, File file) {
            this.name =  new SimpleStringProperty(name);
            this.file = file;
            setDuration(FileUtils.getaudioduration(file));
            length = new SimpleStringProperty(TimeUtils.formatlengthshort((int) duration));
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getLength() {
            return length.get();
        }

        public StringProperty lengthProperty() {
            return length;
        }

        public void setLength(String length) {
            this.length.set(length);
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public double getDuration() {
            return duration;
        }

        public void setDuration(double duration) {
            this.duration = duration;
        }
    }
}
