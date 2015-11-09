package sleepmachine.util.xml;

import sleepmachine.MainController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Noises")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Noises {

    private List<Noise> Noise;

    public List<Noise> getNoise() {
        return Noise;
    }

    public void setNoise(List<Noise> noise) {this.Noise = noise;}

    public void printoutallnoises() {
        for (Noise i : Noise) {
            System.out.println(i.toString());
        }
    }

    public void populatefromxml() throws JAXBException {
        // TODO Pull All The Data From XML File And Create New NoiseWidget Files
        if (MainController.NOISESXMLFILE.exists()) {
            JAXBContext context = JAXBContext.newInstance(Noises.class);
            Unmarshaller createMarshaller = context.createUnmarshaller();
            Noises noises1 = (Noises) createMarshaller.unmarshal(MainController.NOISESXMLFILE);
            setNoise(noises1.getNoise());
        }
    }

    public ArrayList<String> getallcategories() {
        getNoise();
        if (Noise != null) {
            ArrayList<String> allcategories = new ArrayList<>();
            for (Noise i : Noise) {
                String thiscategory = i.getCategory();
                if (allcategories.size() > 0) {
                    boolean categorynew = true;
                    for (String x : allcategories) {if (x.equals(thiscategory)) {categorynew = false;}}
                    if (! categorynew) {continue;}
                }
                allcategories.add(thiscategory);
            }
            return allcategories;
        } else {return null;}
    }

    public List<Noise> getnoisesincategory(String categoryname) {
        List<Noise> thiscategorynoises = new ArrayList<>();
        for (Noise i : Noise) {
            if (i.getCategory().equals(categoryname)) {thiscategorynoises.add(i);}
        }
        return thiscategorynoises;
    }

    public ArrayList<String> getnoisenamesincategory(String categoryname) {
        ArrayList<String> thiscategorynoises = new ArrayList<>();
        for (Noise i : Noise) {
            if (i.getCategory().equals(categoryname)) {thiscategorynoises.add(i.getName());}
        }
        return thiscategorynoises;
    }

    public Noise getselectednoise(String name) {
        sleepmachine.util.xml.Noise selectednoise = null;
        for (sleepmachine.util.xml.Noise noise : Noise) {
            if (noise.getName().equals(name)) {selectednoise = noise;}
        }
        if (selectednoise != null && selectednoise.getFile().exists()) {return selectednoise;}
        else {return null;}
    }
}
