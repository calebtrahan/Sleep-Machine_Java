//package sleepmachine.helpers;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.util.Duration;
//import sleepmachine.util.FileUtils;
//
//import java.io.File;
//import java.util.*;
//
//
//public class EntrainmentOld {
//    public Boolean working = false;
//    public String name;
//    public File directory;
//    private ArrayList<File> tempfiles = new ArrayList<>();
//    private ArrayList<Media> tempmediafiles = new ArrayList<>();
//    private ArrayList<Media> mediafiles = new ArrayList<>();
//    private ArrayList<Duration> mediafilesdurations = new ArrayList<>();
//    private ArrayList<File> fillerfiles = new ArrayList<>();
//    private ArrayList<Duration> fillerfilesdurations = new ArrayList<>();
//    private ArrayList<File> partfiles = new ArrayList<>();
//    private ArrayList<Duration> partfilesdurations = new ArrayList<>();
//    private File infile = null;
//    private Duration infileduration;
//    private File outfile = null;
//    private Duration outfileduration;
//
//    EntrainmentOld(String name, File directory) {
//        this.name = name;
//        this.directory = directory;
//        working = testifvalidsession();
//    }
//
//    public Boolean testifvalidsession() {
//        try {
//            for (File i : directory.listFiles()) {
//                String x = FileUtils.removefileextension(i.getName()).toLowerCase();
//                if (x.contains("part") && FileUtils.testmediafile(i)) {partfiles.add(i);}
//                if (x.contains("filler") && FileUtils.testmediafile(i)) {fillerfiles.add(i);}
//                if (x.contains("in") && FileUtils.testmediafile(i)) {infile = i;}
//                if (x.contains("out") && FileUtils.testmediafile(i)) {outfile = i;}
//            }
//            if (!fillerfiles.isEmpty() && !partfiles.isEmpty() && infile != null && outfile != null) {
//                System.out.println(name + " Is A Valid Session");
//                Collections.sort(partfiles);
//                Collections.sort(fillerfiles);
//                tempfiles.add(infile);
//                tempfiles.addAll(partfiles);
//                tempfiles.addAll(fillerfiles);
//                tempfiles.add(outfile);
////                getsessiondurations();
//                return true;
//            } else {
//                StringBuilder sessionfailed = new StringBuilder();
//                ArrayList<String> insession = new ArrayList<>();
//                ArrayList<String> missingfromsession = new ArrayList<>();
//                sessionfailed.append(name);
//                if (!partfiles.isEmpty()) {insession.add("Part File(s)");} else {missingfromsession.add("Part File(s)");}
//                if (!fillerfiles.isEmpty()) {insession.add("Filler File(s)");} else {missingfromsession.add("Filler File(s)");}
//                if (infile != null) {insession.add("In File");} else {missingfromsession.add("In File");}
//                if (outfile != null) {insession.add("Out File");} else {missingfromsession.add("Out File");}
//                sessionfailed.append(" Has ");
//                for (String i : insession) {
//                    sessionfailed.append(i);
//                    if (i.equals(insession.get(insession.size() - 1))) {sessionfailed.append(", ");}
//                }
//                sessionfailed.append(" But Is Missing ");
//                for (String x : missingfromsession) {
//                    sessionfailed.append(x);
//                    if (x.equals(missingfromsession.get(missingfromsession.size() - 1))) {sessionfailed.append(", ");}
//                }
//                System.out.println(sessionfailed.toString());
//                return false;
//            }
//        } catch (NullPointerException e) {return false;}
//    }
//
////    private void getsessiondurations() {
////        for (File i : fillerfiles) {}
////        for (File i : partfiles) {}
////        // Infile Duration
////        // Outfile Duration
////    }
//
//    public void prepare() {
//        for (File i : tempfiles) {
//            Media a = new Media(i.toURI().toString());
//            MediaPlayer b = new MediaPlayer(a);
//            tempmediafiles.add(a);
//            b.setOnReady(new Runnable() {
//                @Override
//                public void run() {b.dispose();}
//            });
//        }
//    }
//
//
//
//    public ArrayList<Media> getmediafiles() {return mediafiles;}
//
//    public void testifready() {
//        for (Media i : tempmediafiles) {
//            System.out.println("The Duration Of " + i.getSource() + "' Is " + i.getDuration().toSeconds() + " Seconds");
//        }
//    }
//}
