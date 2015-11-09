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

@XmlRootElement(name = "Entrainments")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Entrainments {
    private List<Entrainment> Entrainment;

    public List<sleepmachine.util.xml.Entrainment> getEntrainment() {
        return Entrainment;
    }

    public void setEntrainment(List<sleepmachine.util.xml.Entrainment> entrainment) {
        Entrainment = entrainment;
    }

    public void populatefromxml() throws JAXBException {
        // TODO Pull All The Data From XML File And Create New NoiseWidget Files
        if (MainController.ENTRAINMENTXMLFILE.exists()) {
            JAXBContext context = JAXBContext.newInstance(Entrainments.class);
            Unmarshaller createMarshaller = context.createUnmarshaller();
            Entrainments entrainments1 = (Entrainments) createMarshaller.unmarshal(MainController.ENTRAINMENTXMLFILE);
            setEntrainment(entrainments1.getEntrainment());
        }
    }

    public ArrayList<String> getallentrainmentnames() {
        if (Entrainment != null) {
            ArrayList<String> allentrainmentnames = new ArrayList<>();
            for (Entrainment i : Entrainment) {allentrainmentnames.add(i.getName());}
            return  allentrainmentnames;
        } else {return null;}
    }

    public Entrainment getselectedentrainment(String name) {
        sleepmachine.util.xml.Entrainment entrainment = null;
        for (sleepmachine.util.xml.Entrainment ent : Entrainment) {if (ent.getName().equals(name)) {entrainment = ent;}}
        if (entrainment != null) {
            // Iterate Through All Files And Make Sure They Exist

        }
        return entrainment;
    }
}
