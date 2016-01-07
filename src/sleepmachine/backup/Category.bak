//package sleepmachine.backup;
//import com.sun.xml.internal.txw2.annotation.XmlElement;
//import sleepmachine.util.FileUtils;
//import sleepmachine.util.StringUtils;
//import sleepmachine.util.xml.NoiseWidget;
//
//import java.io.File;
//import java.util.ArrayList;
//
//@XmlElement
//public class Category {
//    public String name;
//    private Boolean valid;
//    public String nameformatted;
//    public File projectdirectory;
////    private NoiseWidget selectednoise;
//    private ArrayList<NoiseWidget> noises = new ArrayList<>();
////    private ArrayList<String> noisenames = new ArrayList<>();
////    private HashMap<String, ArrayList<NoiseWidget>> noiselist = new HashMap<>();
//
////    public NoiseWidget getSelectednoise() {return selectednoise;}
////    public void setSelectednoise(NoiseWidget selectednoise) {this.selectednoise = selectednoise;}
//
//    public Boolean getValid() {
//        return valid;
//    }
//
//    public void setValid(Boolean valid) {
//        this.valid = valid;
//    }
//
//    Category(String name, File projectdirectory) {
//        this.name = name;
//        this.projectdirectory = projectdirectory;
//        nameformatted = StringUtils.reformatcapatalized(name);
//    }
//
//////    public HashMap<String, ArrayList<NoiseWidget>> getNoiselist() {
////        return noiselist;
////    }
////
////    public void setNoiselist(HashMap<String, ArrayList<NoiseWidget>> noiselist) {
////        this.noiselist = noiselist;
////    }
//
//    public void findnoise(String name) {
////        for (NoiseWidget noise : noises) {
////            if (noise.name.toLowerCase().equals(name.toLowerCase())) {
//////                selectednoise = noise;
////                break;
////            }
////        }
//    }
//
////    public ArrayList<String> getnoiseNames() {return noisenames;}
//
//    public Boolean testifcorrectCategory(String name) {return name.toLowerCase().equals(this.name);}
//
//    public void addnoisefiles() {
//        if (projectdirectory.listFiles() != null) {
//            for (File i : projectdirectory.listFiles()) {
//                Boolean tested = FileUtils.testmediafile(i);
//                if (tested) {
//                    String n1 = StringUtils.reformatcapatalized(i.getName());
//                    String newname = FileUtils.removefileextension(n1);
////                    noises.add(new NoiseWidget(newname, i));
////                    noisenames.add(newname);
//                }
//            }
//            setValid(!noises.isEmpty());
//        }
//    }
//
//    @Override
//    public String toString() {return this.name;}
//
//}