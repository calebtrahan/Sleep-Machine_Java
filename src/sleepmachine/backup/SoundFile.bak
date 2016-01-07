//package sleepmachine.helpers;
//
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaException;
//import javafx.scene.media.MediaPlayer;
//import javafx.util.Duration;
//import sleepmachine.util.FileUtils;
//import sleepmachine.util.StringUtils;
//
//import java.io.File;
//
//public class SoundFile {
//    private String name;
//    private File file;
//    private Duration duration;
//    private Media mediafile;
//    private MediaPlayer player;
//    private String description;
//
//    public SoundFile(File filename) {
//        this.file = filename;
//        String tempname = StringUtils.reformatcapatalized(file.getName());
//        name = FileUtils.removefileextension(tempname);
//        getaudioduration();
//    }
//
//    public SoundFile(File filename, String description) {
//        this.file = filename;
//        String tempname = StringUtils.reformatcapatalized(file.getName());
//        name = FileUtils.removefileextension(tempname);
//        this.description = description;
//        getaudioduration();
//    }
//
//    private void getaudioduration() throws MediaException {
//        mediafile = new Media(file.toURI().toString());
//        player = new MediaPlayer(mediafile);
//        player.setOnReady(new Runnable() {
//            @Override
//            public void run() {
//                duration = mediafile.getDuration();
//                player.dispose();
//            }
//        });
//    }
//
//    public String getName() {return name;}
//    public File getFile() {return file;}
//    public Duration getDuration() {return duration;}
//    public Media getMediafile() {return mediafile;}
//    public MediaPlayer getPlayer() {return player;}
//}
