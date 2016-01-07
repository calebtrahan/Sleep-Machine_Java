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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sleepmachine.MainController;
import sleepmachine.Tools;
import sleepmachine.interfaces.Playable;
import sleepmachine.interfaces.Widget;
import sleepmachine.xml.Noise;
import sleepmachine.xml.Noises;

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

public class NoiseWidget implements Widget, Playable {
    private CheckBox OnOffSwitch;
    private ChoiceBox Categories;
    private ChoiceBox Selection;
    private TextArea Description;
    private Button PreviewButton;
    private MediaPlayer currentplayer;
    private int playcount = 0;
    private ArrayList<Media> sessionmedia;
    private Noises allnoises;
    private Noise selectednoise;
    private boolean enabled;
    private MediaPlayer previewplayer;

    public NoiseWidget(CheckBox onOffSwitch, ChoiceBox categories, ChoiceBox selection, TextArea description, Button previewButton) {
        OnOffSwitch = onOffSwitch;
        Categories = categories;
        Selection = selection;
        Description = description;
        PreviewButton = previewButton;
        allnoises = new Noises();
        syncnoise();
    }

// Getters And Setters
    public Noise getSelectednoise() {
        return selectednoise;
    }
    public void setSelectednoise(Noise selectednoise) {
        this.selectednoise = selectednoise;
    }
    public Noises getAllnoises() {return allnoises;}
    public void setAllnoises(Noises noises) {allnoises = noises;}
    public boolean isEnabled() {return enabled;}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}

// Playable Implementation
    @Override
    public boolean create(Duration duration) {return getSelectednoise() != null;}
    @Override
    public void startplayback() {
        currentplayer = new MediaPlayer(new Media(selectednoise.getFile().toURI().toString()));
        currentplayer.setOnEndOfMedia(this::playnextfile);
    }
    @Override
    public void playnextfile() {
        currentplayer.stop();
        currentplayer = new MediaPlayer(new Media(selectednoise.getFile().toURI().toString()));
        currentplayer.setOnEndOfMedia(this::playnextfile);
    }
    @Override
    public void play() {
        if (currentplayer == null) {startplayback();}
        else {currentplayer.play();}
    }
    @Override
    public void pause() {
        if (currentplayer != null) {currentplayer.pause();}
    }
    @Override
    public void stop() {
        if (currentplayer != null) {currentplayer.stop();}
    }
    @Override
    public void endofplayback() {}
    @Override
    public void setCurrentPlayer(MediaPlayer mediaPlayer) {

    }
    @Override
    public MediaPlayer getCurrentPlayer() {return currentplayer;}

// Widget Implementation
    @Override
    public boolean isValid() {
        return false;
    }
    @Override
    public void disable() {
        OnOffSwitch.setDisable(true);
        Selection.setDisable(true);
        Categories.setDisable(true);
        Description.setDisable(true);
        PreviewButton.setDisable(true);
    }
    @Override
    public void enable() {
        OnOffSwitch.setDisable(false);
        Selection.setDisable(false);
        Categories.setDisable(false);
        Description.setDisable(false);
        PreviewButton.setDisable(false);
    }
    @Override
    public void resetallvalues() {

    }
    @Override
    public void statusswitch() {
        boolean status = OnOffSwitch.isSelected();
        Tools.togglecheckboxtext(OnOffSwitch);
        setEnabled(status);
        Description.setDisable(!status);
        Categories.setDisable(!status);
        Selection.setDisable(!status);
        PreviewButton.setDisable(!status);
    }

// Other Methods
    public void syncnoise() {
        allnoises.getNoise();
        try {allnoises.populatefromxml();} catch (JAXBException e) {e.printStackTrace(); return;}
        ArrayList<String> allcategorynames = allnoises.getallcategories();
        if (allcategorynames != null) {
            ObservableList<String> categorylist = FXCollections.observableArrayList();
            categorylist.addAll(allcategorynames);
            Categories.setItems(categorylist);
            setSelectednoise(selectednoise);
        } else {
            // TODO No Noises Exist. Dialog To Add Noises Here
        }
    }
    public void changecategory() {
        Selection.getItems().clear();
        ObservableList<String> noisesincategory = FXCollections.observableArrayList();
        noisesincategory.addAll(allnoises.getnoisenamesincategory(Categories.getSelectionModel().getSelectedItem().toString()));
        Selection.setItems(noisesincategory);
    }
    public void changeselection() {
        String selectionname = Selection.getSelectionModel().getSelectedItem().toString();
        selectednoise = allnoises.getselectednoise(selectionname);
        if (selectednoise != null) {Description.setText(selectednoise.getDescription());}
        else {Description.setText(selectionname + "'s Actual File Is Missing. Please Choose Another");}
        PreviewButton.setDisable(selectednoise == null);
    }
    public void preview() {
        if (selectednoise != null) {
            if (previewplayer == null) {
                Media noisemedia = new Media(selectednoise.getFile().toURI().toString());
                previewplayer = new MediaPlayer(noisemedia);
                previewplayer.play();
                PreviewButton.setText("Stop");
            } else {
                previewplayer.stop();
                PreviewButton.setText("Preview");
            }
        } else {
            Tools.showinformationdialog("Cannot Preview", "Cannot Preview", "Select A Noise To Preview");}
    }
    public void addnoiseloop() {
        allnoises.getNoise();
        AddNewNoiseDialog a = new AddNewNoiseDialog(null, this);
        a.showAndWait();
        try {allnoises.populatefromxml();} catch (JAXBException e) {e.printStackTrace();}
        syncnoise();
    }

// Subclasses/Dialogs
    public static class AddNewNoiseDialog extends Stage implements Initializable {
        public Label FileLabel;
        public Label DurationLabel;
        public TextField NameField;
        public Button OpenButton;
        public Button PreviewButton;
        public TextArea DescriptionTextArea;
        public Label NameCharactersLeftLabel;
        public Label DescriptionCharactersLeftLabel;
        public Button AddButton;
        public Button CancelButton;
        public ChoiceBox CategoryChoiceBox;
        public Button AddCategoryButton;
        private int maxdescriptioncharacters = 120;
        private int maxnamecharacters = 20;
        private File soundfile;
        private MediaPlayer previewplayer;
        private NoiseWidget noiseWidget;
        private double duration;
        private String category;
        private ArrayList<String> categorylist;

        public AddNewNoiseDialog(Parent parent, NoiseWidget noiseWidget) {
            this.noiseWidget = noiseWidget;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../assets/fxml/AddNewNoiseDialog.fxml"));
            fxmlLoader.setController(this);
            try {setScene(new Scene(fxmlLoader.load())); this.setTitle("Add Ambience");}
            catch (IOException e) {e.printStackTrace();}
            categorylist = noiseWidget.getAllnoises().getallcategories();
            synccategories();
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            NameCharactersLeftLabel.setText(maxnamecharacters + " Characters Left");
            NameField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (soundfile == null) {
                    NameField.setText(oldValue);
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Select A Sound First");
                    a.setContentText("Please Open A Sound First");
                    a.showAndWait();
                } else {
                    if (NameField.getText().length() >= maxnamecharacters) {
                        NameField.setText(oldValue);
                        NameCharactersLeftLabel.setText("0 Characters Left");
                    } else {
                        int charactersleft = maxnamecharacters - NameField.getText().length();
                        NameCharactersLeftLabel.setText(charactersleft + " Characters Left");
                    }
                }
            });
            DescriptionCharactersLeftLabel.setText(maxdescriptioncharacters + "Characters Left");
            DescriptionTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
                if (DescriptionTextArea.getText().length() >= maxdescriptioncharacters) {
                    DescriptionTextArea.setText(oldValue);
                    DescriptionCharactersLeftLabel.setText("0 Characters Left");
                } else {
                    int charactersleft = maxdescriptioncharacters - DescriptionTextArea.getText().length();
                    DescriptionCharactersLeftLabel.setText(charactersleft + " Characters Left");
                }
            });
            this.setOnCloseRequest(event -> this.close());
            installtooltips();
        }

        public void installtooltips() {
            Tooltip.install(FileLabel, new Tooltip("Short Name Of Opened File"));
            Tooltip.install(DurationLabel, new Tooltip("Duration Of Opened File"));
            Tooltip.install(OpenButton, new Tooltip("Opens And Loads A Sound File"));
            Tooltip.install(PreviewButton, new Tooltip("Preview's The Loaded Sound File"));
            Tooltip.install(NameField, new Tooltip("This Will Be The Display Name Of This Sound File When Added"));
            Tooltip.install(CategoryChoiceBox, new Tooltip("A List Of Your Current Categories"));
            Tooltip.install(AddCategoryButton, new Tooltip("Add A New Category To Category List"));
            Tooltip.install(DescriptionTextArea, new Tooltip("A Short Description Of This NoiseWidget"));
            Tooltip.install(AddButton, new Tooltip("Add This NoiseWidget To The Sleep Machine And Close"));
            Tooltip.install(CancelButton, new Tooltip("Cancel Adding NoiseWidget And Return To Main Program"));
        }

        public void previewsound(ActionEvent actionEvent) {
            if (soundfile != null) {
                if (previewplayer.getStatus() == MediaPlayer.Status.READY || previewplayer.getStatus() == MediaPlayer.Status.STOPPED) {
                    previewplayer.play();
                    PreviewButton.setText("Stop");
                } else if (previewplayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    previewplayer.stop();
                    PreviewButton.setText("Play");
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

        public void opensound(ActionEvent actionEvent) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open A Sound File");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                // Test If That Sound Is A Supported Audio File
                if (Tools.supportedaudioformat(file)) {
                    soundfile = file;
                    FileLabel.setText("Loading Your File...");
                    Media previewmedia = new Media(soundfile.toURI().toString());
                    previewplayer = new MediaPlayer(previewmedia);
                    previewplayer.setOnReady(() -> {
                        duration = previewplayer.getTotalDuration().toSeconds();
                        int minutes = (int) duration / 60;
                        int seconds = (int) duration % 60;
                        String text = minutes + " Min " + seconds + " Sec";
                        FileLabel.setText(soundfile.getName());
                        DurationLabel.setText(text);
                        FileLabel.setText(file.getName().substring(0, file.getName().length() - 4));
                    });
                } else {
                    Tools.invalidfile(file);}
            }
        }

        public void addthisnoise(ActionEvent actionEvent) {
            boolean namegood = NameField.getText().length() > 0;
            boolean descriptiongood = DescriptionTextArea.getText().length() > 0;
            boolean filegood = soundfile != null;
            boolean categorygood = category != null;
            if (filegood && namegood && descriptiongood && categorygood) {
                try {
                    Noises noises = noiseWidget.getAllnoises();
                    List<Noise> noiselist;
                    if (MainController.NOISESXMLFILE.exists()) {
                        noiselist = noises.getNoise();
                    } else {
                        noiselist = new ArrayList<>();
                    }
                    noiselist.add(new Noise(NameField.getText(), soundfile, duration, DescriptionTextArea.getText(), category)
                    );
                    noises.setNoise(noiselist);
                    JAXBContext context = JAXBContext.newInstance(Noises.class);
                    Marshaller createMarshaller = context.createMarshaller();
                    createMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    createMarshaller.marshal(noises, MainController.NOISESXMLFILE);
                } catch (JAXBException e) {
                    e.printStackTrace();
                    return;
                }
                if (previewplayer != null) {
                    previewplayer.dispose();
                }
                this.close();
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Cannot Add NoiseWidget");
                if (!filegood) {a.setContentText("Please Open A File To Add As A NoiseWidget");}
                if (!namegood) {a.setContentText("Please Enter A Name For This NoiseWidget");}
                if (!descriptiongood) {a.setContentText("Please Enter A Description For This NoiseWidget");}
                if (!categorygood) {a.setContentText("Please Select A Category To Add This NoiseWidget To");}
                a.showAndWait();
            }
        }

        public void cancelthisnoise(ActionEvent actionEvent) {close();}

        @Override
        public void close() {
            if (previewplayer != null) {previewplayer.dispose();}
            super.close();
        }

        public void selectcategory(ActionEvent actionEvent) {
            int index = CategoryChoiceBox.getSelectionModel().getSelectedIndex();
            category = categorylist.get(index);
        }

        public void synccategories() {
            if (CategoryChoiceBox.getItems().size() > 0) {CategoryChoiceBox.getItems().clear();}
            ObservableList<String> categoryobservablelist = FXCollections.observableArrayList();
            categoryobservablelist.addAll(categorylist);
            CategoryChoiceBox.setItems(categoryobservablelist);
        }

        public void addnewcategory(ActionEvent actionEvent) {
            TextInputDialog dialog = new TextInputDialog("Enter A Category Name");
            dialog.setTitle("Add A Category");
            dialog.setHeaderText("Add Category");
            dialog.setContentText("New Category Name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String value = result.get();
                if (value.length() == 0) {
                    Alert valuetooshort = new Alert(Alert.AlertType.WARNING);
                    valuetooshort.setTitle("No Value Entered");
                    valuetooshort.setHeaderText("Cannot Add Category");
                    valuetooshort.setContentText("No Value Entered");
                    valuetooshort.showAndWait();
                    return;
                } else {
                    boolean valueexists = false;
                    for (String i : categorylist) {
                        if (value.equals(i)) {
                            valueexists = true;
                        }
                    }
                    if (valueexists) {
                        Alert alreadyexists = new Alert(Alert.AlertType.WARNING);
                        alreadyexists.setTitle("Invalid Value");
                        alreadyexists.setHeaderText("Category Already Exists");
                        alreadyexists.setContentText("Cannot Add A Category That Already Exists");
                        alreadyexists.showAndWait();
                        return;
                    }
                }
                categorylist.add(result.get());
                synccategories();
            }
        }
    }
}
