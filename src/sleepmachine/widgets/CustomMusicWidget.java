package sleepmachine.widgets;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import sleepmachine.dialogs.CustomMusicDialog;
import sleepmachine.util.FileUtils;
import sleepmachine.util.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;


public class CustomMusicWidget implements Widget, Playable {
    private CheckBox OnOffSwitch;
    private Label DescriptionLabel;
    private Button EditButton;
    private ArrayList<CustomMusicDialog.MusicFile> MusicFiles;
    private ArrayList<File> musicfilestoplay;
    private MediaPlayer currentplayer;
    private int playcount = 0;
    private ArrayList<Media> sessionmedia;

    public CustomMusicWidget(CheckBox onOffSwitch, Label descriptionLabel, Button EditButton) {
        OnOffSwitch = onOffSwitch;
        DescriptionLabel = descriptionLabel;
        this.EditButton = EditButton;
    }

// Getters And Setters
    public ArrayList<CustomMusicDialog.MusicFile> getMusicFiles() {
        return MusicFiles;
    }
    public ArrayList<File> getmusicfilesasfiles() {
        ArrayList<File> list = new ArrayList<>();
        for (CustomMusicDialog.MusicFile i : MusicFiles) {list.add(i.getFile());}
        return list;
    }
    public void setMusicFiles(ArrayList<CustomMusicDialog.MusicFile> musicFiles) {
        MusicFiles = musicFiles;
    }
    public String getdescription() {
        if (getMusicFiles() != null) {
            double totaltime = 0.0;
            for (CustomMusicDialog.MusicFile i : getMusicFiles()) {totaltime += i.getDuration();}
            int size = getMusicFiles().size();
            String duration = TimeUtils.formatlengthlong((int) totaltime);
            return String.format("%s Music Files (%s Total)", size, duration);
        } else {return "No Custom Music Added";}
    }
    public ArrayList<File> getMusicfilestoplay() {
        return musicfilestoplay;
    }
    public void setMusicfilestoplay(ArrayList<File> musicfilestoplay) {
        this.musicfilestoplay = musicfilestoplay;
    }

// Playable Methods
    public void create(Duration totalduration) {
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
            Duration nextfileduration = new Duration(FileUtils.getaudioduration(nextfile) * 1000);
            currentduration = currentduration.add(nextfileduration);
        }
        setMusicfilestoplay(currentsonglist);
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

// Other Methods

}
