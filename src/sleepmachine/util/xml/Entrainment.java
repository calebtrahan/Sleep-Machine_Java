package sleepmachine.util.xml;

import javafx.util.Duration;
import sleepmachine.util.FileUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {"name", "partcount", "description", "intro", "part1", "part2", "part3", "part4", "filler", "outro", "minimumduration"})
public class Entrainment {
    private String name;
    private File intro;
    private File part1;
    private File part2;
    private File part3;
    private File part4;
    private File filler; // Should Only Be 1 Minute Long
    private File outro;
    private String description;
    private int partcount;
    private double minimumduration; // In seconds

// CONSTRUCTORS
    public Entrainment() {}
    public Entrainment(String name, File intro, File part1, File filler, File outro, String description) {
        setName(name);
        setIntro(intro);
        setPart1(part1);
        setFiller(filler);
        setOutro(outro);
        setDescription(description);
        setPartcount(1);
    }
    public Entrainment(String name, File intro, File part1, File part2, File filler, File outro, String description) {
        setName(name);
        setIntro(intro);
        setPart1(part1);
        setPart2(part2);
        setFiller(filler);
        setOutro(outro);
        setDescription(description);
        setPartcount(2);
    }
    public Entrainment(String name, File intro, File part1, File part2, File part3, File filler, File outro, String description) {
        setName(name);
        setIntro(intro);
        setPart1(part1);
        setPart2(part2);
        setPart3(part3);
        setFiller(filler);
        setOutro(outro);
        setDescription(description);
        setPartcount(3);
    }
    public Entrainment(String name, File intro, File part1, File part2, File part3, File part4, File filler, File outro, String description) {
        setName(name);
        setIntro(intro);
        setPart1(part1);
        setPart2(part2);
        setPart3(part3);
        setPart4(part4);
        setFiller(filler);
        setOutro(outro);
        setDescription(description);
        setPartcount(4);
    }

// GETTERS AND SETTERS
    public int getPartcount() {
        return partcount;
    }
    public void setPartcount(int partcount) {
        this.partcount = partcount;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public File getIntro() {
        return intro;
    }
    public void setIntro(File intro) {
        this.intro = intro;
    }
    public File getPart1() {
        return part1;
    }
    public void setPart1(File part1) {
        this.part1 = part1;
    }
    public File getPart2() {
        return part2;
    }
    public void setPart2(File part2) {
        this.part2 = part2;
    }
    public File getPart3() {
        return part3;
    }
    public void setPart3(File part3) {
        this.part3 = part3;
    }
    public File getFiller() {
        return filler;
    }
    public void setFiller(File filler) {
        this.filler = filler;
    }
    public File getOutro() {
        return outro;
    }
    public void setOutro(File outro) {
        this.outro = outro;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public File getPart4() {
        return part4;
    }
    public void setPart4(File part4) {
        this.part4 = part4;
    }
    public double getMinimumduration() {
        return minimumduration;
    }
    public void setMinimumduration(double minimumduration) {
        this.minimumduration = minimumduration;
    }

// METHODS
    public boolean build(Duration wakeupduration, Duration totalsessionduration) {
        if (getMinimumduration() > totalsessionduration.toSeconds()) {return false;}
        if (wakeupduration != null) {totalsessionduration = totalsessionduration.subtract(wakeupduration);}
        double totalentrainmentseconds = totalsessionduration.toSeconds();
        double currententrainmentseconds = 0.0;
        ArrayList<File> entrainmentfiles = new ArrayList<>();
    // Add Intro
        currententrainmentseconds += FileUtils.getaudioduration(getIntro());
    // Calculate Part Time
        currententrainmentseconds += FileUtils.getaudioduration(getOutro());
        double secondslefttofill = totalentrainmentseconds - currententrainmentseconds;
    // Add Parts
        ArrayList<File> partfiles = new ArrayList<>();
        if (getPart1() != null) {partfiles.add(getPart1());}
        if (getPart2() != null) {partfiles.add(getPart2());}
        if (getPart3() != null) {partfiles.add(getPart3());}
        if (getPart4() != null) {partfiles.add(getPart4());}
    // Add All Parts
        entrainmentfiles.addAll(partfiles);
        for (File i : partfiles) {currententrainmentseconds += FileUtils.getaudioduration(i);}
    // Add Parts If Time Left
        Random random = new Random();
        while (currententrainmentseconds < secondslefttofill) {
            int randindex = random.nextInt(partfiles.size());
            File randompartfile = partfiles.get(randindex);
            entrainmentfiles.add(randompartfile);
            currententrainmentseconds += FileUtils.getaudioduration(randompartfile);
        }
    // Add Filler (If Needed)
        if (currententrainmentseconds > totalsessionduration.toSeconds()) {
        // Remove Last File
            File filetoremove = entrainmentfiles.get(entrainmentfiles.size() - 1);
            currententrainmentseconds -= FileUtils.getaudioduration(filetoremove);
            entrainmentfiles.remove(entrainmentfiles.size() - 1);
        // Add Filler Until >= totalsessionduration
            double fillerduration = FileUtils.getaudioduration(getFiller());
            while (currententrainmentseconds < totalsessionduration.toSeconds()) {
                entrainmentfiles.add(getFiller());
                currententrainmentseconds += fillerduration;
            }
        }
    // Shuffle Middle Files
        for (int i=0; i<5; i++) {Collections.shuffle(entrainmentfiles);}
    // Add Intro At Beginning
        entrainmentfiles.add(0, getIntro());
    // Add Outtro To End
        entrainmentfiles.add(getOutro());
        return entrainmentfiles.size() > 0;
    }
    public boolean checkifallpartsexist() {
        if (! getIntro().exists()) {return false;}
        if (! getPart1().exists()) {return false;}
        if (getPartcount() > 1) {if (! getPart2().exists()) return false;}
        if (getPartcount() > 2) {if (! getPart3().exists()) return false;}
        if (getPartcount() > 3) {if (! getPart4().exists()) return false;}
        if (! getFiller().exists()) {return false;}
        return getOutro().exists();
    }

}
