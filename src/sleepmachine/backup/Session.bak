//package sleepmachine.helpers;
//
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.util.Duration;
//import sleepmachine.util.xml.NoiseWidget;
//
//import java.util.ArrayList;
//import java.util.Optional;
//
//// TODO entrainmentplayers is empty! Make Sure The Session Can't Be Instantianized Without Working EntrainmentOld
//
//public class Session {
//    private Duration duration;
//    private ArrayList<MediaPlayer> ambienceplayers;
//    private ArrayList<MediaPlayer> entrainmentplayers;
//    private ArrayList<MediaPlayer> noiseplayers;
//    private MediaPlayer currententrainmentplayer;
//    private MediaPlayer nextentrainmentplayer;
//    private MediaPlayer currentnoiseplayer;
//    private MediaPlayer nextnoiseplayer;
//    private MediaPlayer currentambienceplayer;
//    private MediaPlayer nextambienceplayer;
//    private MediaPlayer wakeupfileplayer;
//    private Boolean entrainmentinsession;
//    private Boolean ambienceinsession;
//    private  Boolean noiseinsession;
//    private Boolean wakeupfileinsession;
//    private NoiseWidget selectednoise;
//    private Media wakeupfile;
//    private EntrainmentOld selectedentrainment;
//    private Boolean sessionplaying;
//
//    public Session(NoiseWidget selectednoise, EntrainmentOld selectedentrainment, Duration sessionduration, Media wakeupfile) {
//        this.selectednoise = selectednoise;
//        this.selectedentrainment = selectedentrainment;
//        this.duration = sessionduration;
//        this.wakeupfile = wakeupfile;
//        noiseinsession = this.selectednoise != null;
//        entrainmentinsession = this.selectedentrainment != null;
//        wakeupfileinsession = this.wakeupfile != null;
//        ambienceinsession = false;
//        sessionplaying = false;
//        // Set AmbienceController In Session Here
//    }
//
////    public void extendambiencetoduration() {
////        Double currentlength = 0.0;
////        while (currentlength <= this.duration) {
////            for (MediaPlayer i : ambienceplayers) {
////                Media x = i.getMedia();
////                this.duration += x.getDuration().toMinutes();
////            }
////            // Append AmbienceController Files Here
////        }
////        for (int x=0; x<5; x++) {Collections.shuffle(ambienceplayers);}
////    }
//
//    public boolean sessionprechecks() {
//        if (selectednoise != null) {                                                                                    // Test If NoiseWidget Loop Is In Session
//            // Confirmation Dialog Here To Continue Without NoiseWidget In The Session
//        }
//        if (wakeupfile != null) {
//            // Confirmation Dialog Here To Continue Without Wakeup File In The Session
//        }
//        return false;
//    }
//
//    public Boolean getSessionplaying() {return sessionplaying;}
//    public void setSessionplaying(Boolean sessionplaying) {this.sessionplaying = sessionplaying;}
//
//    public Boolean create() {
////        extendambiencetoduration();
////        boolean noisegood = selectednoise.getValid();
////        boolean entrainmentgood = selectedentrainment.build(duration);
////        if (entrainmentgood) {
////            entrainmentplayers = new ArrayList<>();
////            for (Media i : selectedentrainment.getmediafiles()) {
////                MediaPlayer c = new MediaPlayer(i);
////                entrainmentplayers.add(new MediaPlayer(i));
////            }           // Add entrainment media files to Session
////        }
////        if (noisegood) {noiseplayers.add(selectednoise.getPlayer());}                                                   // Add noise media files to Session
////        return entrainmentgood;
//        return true;
//    }
//
//    public void resume() {
//        System.out.println("Resuming Session");
//        if (isPaused(currentnoiseplayer) || isPaused(currententrainmentplayer) || isPaused(currentambienceplayer) || isPaused(wakeupfileplayer)) {
//            if (isPaused(currentnoiseplayer)) {currentnoiseplayer.play();}
//            if (isPaused(currentambienceplayer)) {currentambienceplayer.play();}
//            if (isPaused(currententrainmentplayer)) {currentnoiseplayer.play();}
//            if (isPaused(currentnoiseplayer)) {currentnoiseplayer.play();}
//        } else {System.out.println("Cannot Pause Because No Session Playing!");}
//    }
//
//    public void pause() {
//        System.out.println("Pausing Session");
//        // Change Textbox To 'Resume Session'
//        if (isPlaying(currentnoiseplayer) || isPlaying(currententrainmentplayer) || isPlaying(currentambienceplayer) || isPlaying(wakeupfileplayer)) {
//            if (isPlaying(currentnoiseplayer)) {currentnoiseplayer.pause();}
//            if (isPlaying(currentambienceplayer)) {currentambienceplayer.pause();}
//            if (isPlaying(currententrainmentplayer)) {currentnoiseplayer.pause();}
//            if (isPlaying(currentnoiseplayer)) {currentnoiseplayer.pause();}
//        } else {System.out.println("Cannot Pause Because No Session Playing!");}
//    }
//
//    public void stop() {
//        if (isPlaying(currentnoiseplayer) || isPlaying(currententrainmentplayer) || isPlaying(currentambienceplayer) || isPlaying(wakeupfileplayer)) {
//            if (confirmationtostopsession()) {
//                if (isPlaying(currentnoiseplayer) || isPaused(currentnoiseplayer)) {currentnoiseplayer.stop();}
//                if (isPlaying(currentambienceplayer) || isPaused(currentambienceplayer)) {currentambienceplayer.stop();}
//                if (isPlaying(currententrainmentplayer) || isPaused(currententrainmentplayer)) {currentnoiseplayer.stop();}
//                if (isPlaying(currentnoiseplayer) || isPaused(currentnoiseplayer)) {currentnoiseplayer.stop();}
//            }
//        } else {System.out.println("Cannot Stop Because No Session Playing");}
//    }
//
//    public boolean confirmationtostopsession() {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Confirmation To Stop Session");
//        alert.setHeaderText("Really Stop Session Playback?");
//        alert.setContentText("If You Stop, The Session Cannot be Resumed, But If Played Will Be Restarted");
//        Optional<ButtonType> result = alert.showAndWait();
//        return result.get() == ButtonType.OK;
//    }
//
//    public boolean isPlaying(MediaPlayer mp) {try {return mp.getStatus().equals(MediaPlayer.Status.PLAYING);} catch (NullPointerException e) {return false;}}
//
//    public boolean isPaused(MediaPlayer mp) {try {return mp.getStatus().equals(MediaPlayer.Status.PAUSED);} catch (NullPointerException e) {return false;}}
//
//    public boolean isPlayingOrPaused(MediaPlayer mp) {try {return (mp.getStatus().equals(MediaPlayer.Status.PLAYING) || mp.getStatus().equals((MediaPlayer.Status.PAUSED)));} catch (NullPointerException e) {return false;}}
//
//    public boolean isStopped(MediaPlayer mp) {return mp.getStatus().equals(MediaPlayer.Status.STOPPED);}
//
//    public void play() {
//        System.out.println("Started The Play Method");
//        if (isPlaying(currententrainmentplayer) || isPlaying(currentambienceplayer) || isPlaying(currentnoiseplayer) || isPlaying(wakeupfileplayer)) {
//            System.out.println("Session Is Already Playing!#");
//        } else if (isPaused(currententrainmentplayer) || isPaused(currentambienceplayer) || isPaused(currentnoiseplayer) || isPaused(wakeupfileplayer)) {
//            resume();
//        } else {
//            start();
//        }
//    }
//
//    public void testifplayingnow() {
//        if (isPlaying(currententrainmentplayer)) {
//            System.out.println("Session Is Playing Now");
//        } else {
//            System.out.println("You Fucked Up");
//        }
//    }
//
//    public void start() {
//        System.out.println("Starting A New Session");
//        Boolean sessioncreated = create();                                                                              // Create Session
//        if (sessioncreated) {
//            Duration endofsessionduration = duration;
//            if (wakeupfileinsession) {                                                                                  // Set Timers
//                endofsessionduration.subtract(wakeupfile.getDuration());
//                new Timeline(new KeyFrame(endofsessionduration, ae -> endsession(wakeupfile))).play();
//            } else {
//                new Timeline(new KeyFrame(endofsessionduration, ae -> endsession())).play();
//            }
//            if (entrainmentinsession) {                                                                                 // Start And Queue EntrainmentOld Players
//                currententrainmentplayer = entrainmentplayers.get(0);
////                currententrainmentplayer.play();
//                currententrainmentplayer.setAutoPlay(true);
//                currententrainmentplayer.setVolume(0.6);
//                currententrainmentplayer.setOnEndOfMedia(() -> {
//                    nextentrainmentplayer = entrainmentplayers.get(1);
//                    nextentrainmentplayer.setVolume(0.6);
//                    nextentrainmentplayer.play();
//                });
//            }
//            if (ambienceinsession) {                                                                                    // Start And Queue AmbienceController Players
//                currentambienceplayer = ambienceplayers.get(0);
////                currentambienceplayer.play();
//                currentambienceplayer.setAutoPlay(true);
//                currentambienceplayer.setOnEndOfMedia(() -> {
//                    nextambienceplayer = ambienceplayers.get(1);
//                    nextambienceplayer.play();
//                });
//            }
//            if (noiseinsession) {                                                                                       // Start And Queue NoiseWidget Players
//                currentnoiseplayer = noiseplayers.get(0);
////                currentnoiseplayer.play();
//                currentnoiseplayer.setAutoPlay(true);
//                currentnoiseplayer.setOnEndOfMedia(() -> {
//                    nextnoiseplayer = noiseplayers.get(0);
//                    nextnoiseplayer.play();
//                });
//            }
//        }
//    }
//
//    public void endofsession() {
//        if (entrainmentinsession) {nextentrainmentplayer.stop(); nextambienceplayer.dispose();}
//        if (ambienceinsession) {nextambienceplayer.stop(); nextambienceplayer.dispose();}
//        if (noiseinsession) {nextnoiseplayer.stop(); nextnoiseplayer.dispose();}
//    }
//
//    public void endsession(Media wakeupfile) {
//
//        // Fadeout NoiseWidget And Start WakeupFile (Maybe CrossFade?)
//        // When WakeupFile Is Done, Fadeout Wakeup File And EntrainmentOld
//    }
//
//    public void endsession() {
//        // Fadeout NoiseWidget And EntrainmentOld Till End Of Session
//
//    }
//}
