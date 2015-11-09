package sleepmachine.dialogs;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import sleepmachine.MainController;

import java.io.IOException;

public class VolumeAdjustmentDialog extends Stage {
    public Slider EntrainmentSlider;
    public Slider NoiseSlider;
    public Slider CustomMusicSlider;
    public Label EntrainmentLabel;
    public Label NoiseLabel;
    public Label CustomMusicLabel;
    private MainController root;
    private MediaPlayer custommusicplayer;
    private MediaPlayer entrainmentplayer;
    private MediaPlayer noiseplayer;

    public VolumeAdjustmentDialog(Parent parent, MainController root) {
        this.root  = root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../assets/fxml/VolumeAdjustment.fxml"));
        fxmlLoader.setController(this);
        try {setScene(new Scene(fxmlLoader.load()));this.setTitle("Add Ambience");
        } catch (IOException e) {e.printStackTrace();}
        custommusicplayer = root.getCustomMusicWidgetWidget().getCurrentPlayer();
        entrainmentplayer = root.getEntrainmentWidget().getCurrentPlayer();
        noiseplayer = root.getNoiseWidget().getCurrentPlayer();
        if (custommusicplayer != null) {
            CustomMusicSlider.setDisable(false);
            CustomMusicSlider.setValue(custommusicplayer.volumeProperty().getValue());
            custommusicplayer.volumeProperty().bind(CustomMusicSlider.valueProperty());
        } else {CustomMusicLabel.setText("Custom Music (Disabled)"); CustomMusicSlider.setDisable(true);}
        if (entrainmentplayer != null) {
            EntrainmentSlider.setDisable(false);
            EntrainmentSlider.setValue(entrainmentplayer.volumeProperty().getValue());
            entrainmentplayer.volumeProperty().bind(CustomMusicSlider.valueProperty());
        } else {EntrainmentLabel.setText("EntrainmentWidget (Disabled)"); EntrainmentSlider.setDisable(true);}
        if (noiseplayer != null) {
            NoiseSlider.setDisable(false);
            NoiseSlider.setValue(noiseplayer.volumeProperty().getValue());
            noiseplayer.volumeProperty().bind(CustomMusicSlider.valueProperty());
        } else {NoiseLabel.setText("NoiseWidget Loop (Disabled)"); NoiseSlider.setDisable(true);}
        this.setOnCloseRequest(event -> close());
    }

    @Override
    public void close() {
        if (custommusicplayer != null) {custommusicplayer.volumeProperty().unbind();}
        if (entrainmentplayer != null) {entrainmentplayer.volumeProperty().unbind();}
        if (noiseplayer != null) {noiseplayer.volumeProperty().unbind();}
        super.close();
    }

    public void closedialog(ActionEvent actionEvent) {this.close();}
}
