package sleepmachine.widgets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sleepmachine.Tools;
import sleepmachine.interfaces.Playable;
import sleepmachine.interfaces.Widget;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;


public class CustomMusicWidget implements Widget, Playable {
    private CheckBox OnOffSwitch;
    private Label DescriptionLabel;
    private Button EditButton;
    private ArrayList<AddCustomMusicDialog.MusicFile> MusicFiles;
    private ArrayList<File> musicfilestoplay;
    private MediaPlayer currentplayer;
    private int playcount;
    private ArrayList<Media> sessionmedia;
    private boolean enabled;

    public CustomMusicWidget(CheckBox onOffSwitch, Label descriptionLabel, Button EditButton) {
        OnOffSwitch = onOffSwitch;
        DescriptionLabel = descriptionLabel;
        this.EditButton = EditButton;
        playcount = 0;
    }

// Getters And Setters
    public ArrayList<AddCustomMusicDialog.MusicFile> getMusicFiles() {
        return MusicFiles;
    }
    public ArrayList<File> getmusicfilesasfiles() {
        ArrayList<File> list = new ArrayList<>();
        for (AddCustomMusicDialog.MusicFile i : MusicFiles) {list.add(i.getFile());}
        return list;
    }
    public void setMusicFiles(ArrayList<AddCustomMusicDialog.MusicFile> musicFiles) {
        MusicFiles = musicFiles;
    }
    public String getdescription() {
        if (getMusicFiles() != null) {
            double totaltime = 0.0;
            for (AddCustomMusicDialog.MusicFile i : getMusicFiles()) {totaltime += i.getDuration();}
            int size = getMusicFiles().size();
            String duration = Tools.formatlengthlong((int) totaltime);
            return String.format("%s Music Files (%s Total)", size, duration);
        } else {return "No Custom Music Added";}
    }
    public ArrayList<File> getMusicfilestoplay() {
        return musicfilestoplay;
    }
    public void setMusicfilestoplay(ArrayList<File> musicfilestoplay) {
        this.musicfilestoplay = musicfilestoplay;
    }
    public boolean isEnabled() {return enabled;}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}

// Playable Methods
    public boolean create(Duration totalduration) {
        ArrayList<File> songlist = getmusicfilesasfiles();
        ArrayList<File> currentsonglist = new ArrayList<>();
        Duration currentduration = new Duration(0.0);
        while (currentduration.toMillis() <= totalduration.toMillis()) {
            File nextfile = null;
            Random myrandom = new Random();
            if (currentsonglist.size() > 1) {
                while (true) {
                    int randomindex = myrandom.nextInt(songlist.size());
                    File tempfile = songlist.get(randomindex);
                    if (! tempfile.equals(currentsonglist.get(currentsonglist.size() - 1))) {nextfile = tempfile; break;}
                }
            } else {
                int randomindex = myrandom.nextInt(songlist.size());
                nextfile = songlist.get(randomindex);
            }
            currentsonglist.add(nextfile);
            Duration nextfileduration = new Duration(Tools.getaudioduration(nextfile) * 1000);
            currentduration = currentduration.add(nextfileduration);
        }
        setMusicfilestoplay(currentsonglist);
        return getMusicfilestoplay() != null && getMusicfilestoplay().size() > 0;
    }
    @Override
    public void startplayback() {
        sessionmedia = new ArrayList<>();
        for (File i : musicfilestoplay) {
            Media tempmedia = new Media(i.toURI().toString());
            sessionmedia.add(tempmedia);
        }
        currentplayer = new MediaPlayer(sessionmedia.get(0));
        currentplayer.play();
        currentplayer.setOnEndOfMedia(this::playnextfile);
    }
    @Override
    public void playnextfile() {
        currentplayer.stop();
        playcount++;
        if (playcount < sessionmedia.size()) {
            currentplayer = new MediaPlayer(sessionmedia.get(playcount));
            currentplayer.play();
            currentplayer.setOnEndOfMedia(this::playnextfile);
        } else {endofplayback();}
    }
    @Override
    public void play() {
        if (currentplayer == null) {startplayback();}
        else {currentplayer.play();}
    }
    @Override
    public void pause() {if (currentplayer != null) {currentplayer.pause();}}
    @Override
    public void stop() {if (currentplayer != null) {currentplayer.stop(); currentplayer.dispose(); currentplayer = null;}}
    @Override
    public void endofplayback() {}
    @Override
    public MediaPlayer getCurrentPlayer() {
        return currentplayer;
    }
    @Override
    public void setCurrentPlayer(MediaPlayer mediaPlayer) {

    }

// Widget Methods
    @Override
    public boolean isValid() {return false;}
    @Override
    public void disable() {
        OnOffSwitch.setDisable(true);
        DescriptionLabel.setDisable(true);
        EditButton.setDisable(true);
    }
    @Override
    public void enable() {
        OnOffSwitch.setDisable(false);
        DescriptionLabel.setDisable(false);
        EditButton.setDisable(false);
    }
    @Override
    public void resetallvalues() {
        OnOffSwitch.setDisable(false);
        if (currentplayer != null) currentplayer.dispose();
        playcount = 0;
        if (sessionmedia != null) sessionmedia.clear();
        DescriptionLabel.setText("No Custom Music Added");
        EditButton.setText("Add");
    }
    @Override
    public void statusswitch() {
        Tools.togglecheckboxtext(OnOffSwitch);
        boolean status = OnOffSwitch.isSelected();
        setEnabled(status);
        EditButton.setDisable(!status);
        DescriptionLabel.setDisable(!status);
        if (status) {opencustommusicdialog();}
    }

// Other Methods
    public void opencustommusicdialog() {
        if (getMusicFiles() == null) {
            AddCustomMusicDialog a = new AddCustomMusicDialog(null);
            a.showAndWait();
            setMusicFiles(a.getCustomMusic());
            DescriptionLabel.setText(getdescription());
            if (a.getCustomMusic() == null) {OnOffSwitch.setSelected(false); statusswitch();}
            setEnabled(a.getCustomMusic() != null);
        } else {
            AddCustomMusicDialog a = new AddCustomMusicDialog(null, getMusicFiles());
            a.showAndWait();
            setMusicFiles(a.getCustomMusic());
            DescriptionLabel.setText(getdescription());
            if (a.getCustomMusic() == null) {OnOffSwitch.setSelected(false); statusswitch();}
            setEnabled(a.getCustomMusic() != null);
        }
    }

// Subclasses/Dialogs
    public static class AddCustomMusicDialog extends Stage implements Initializable {
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

        public AddCustomMusicDialog(Parent parent) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../assets/fxml/CustomMusicSelector.fxml"));
            fxmlLoader.setController(this);
            try {setScene(new Scene(fxmlLoader.load())); this.setTitle("Add Custom Music");}
            catch (IOException e) {e.printStackTrace();}
        }

        public AddCustomMusicDialog(Parent parent, ArrayList<MusicFile> existingmusicfiles) {
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
                    if (Tools.supportedaudioformat(i)) {
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
                setDuration(Tools.getaudioduration(file));
                length = new SimpleStringProperty(Tools.formatlengthshort((int) duration));
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

}
