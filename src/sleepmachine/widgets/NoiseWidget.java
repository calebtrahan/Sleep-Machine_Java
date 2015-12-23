package sleepmachine.widgets;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import sleepmachine.dialogs.AddNewNoiseDialog;
import sleepmachine.util.GuiUtils;
import sleepmachine.util.xml.Noise;
import sleepmachine.util.xml.Noises;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;

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
        GuiUtils.togglecheckboxtext(OnOffSwitch);
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
        } else {GuiUtils.showinformationdialog("Cannot Preview", "Cannot Preview", "Select A Noise To Preview");}
    }
    public void addnoiseloop() {
        allnoises.getNoise();
        AddNewNoiseDialog a = new AddNewNoiseDialog(null, this);
        a.showAndWait();
        try {allnoises.populatefromxml();} catch (JAXBException e) {e.printStackTrace();}
        syncnoise();
    }


}
