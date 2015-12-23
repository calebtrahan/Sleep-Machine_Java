package sleepmachine.util.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.File;

//@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {"category", "name", "file", "duration", "format", "description"})
public class Noise {
    private String name;
    private File file;
    private Double duration;
    private String format;
    private String description;
    private String category;

    public Noise() {}
    public Noise(String name, File file, Double duration, String description, String category) {
            this.name = name;
            this.file = file.getAbsoluteFile();
            this.duration = duration;
            this.description = description;
            this.format = this.file.getName().substring(file.getName().length() - 3);
            this.category = category;
    }

// Getters And Seters
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getDuration() {
        return duration;
    }
    public void setDuration(Double duration) {
        this.duration = duration;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    @Override
    public String toString() {
        return String.format("Name: %s File: %s Duration: %s Format: %s Description: %s Category: %s",
            name, file.getAbsolutePath(), duration, format, description, category);
    }

// Other Methods
    public boolean isthisnoisevalid() {return file.exists();}

//
//    public Media getMedia() {return media;}
//    public String getName() {return name;}
//    public String getDescription() {return description;}
//    public MediaPlayer getPlayer() {return player;}
//    public Duration getDuration() {return duration;}
//    public Boolean getValid() {return valid;}

}

