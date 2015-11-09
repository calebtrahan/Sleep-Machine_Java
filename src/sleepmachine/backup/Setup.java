//package sleepmachine.helpers;
//import sleepmachine.backup.Category;
//import sleepmachine.util.FileUtils;
//import sleepmachine.util.StringUtils;
//import sleepmachine.util.xml.NoiseWidget;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class Setup {
//
//    public static HashMap<Category, ArrayList<NoiseWidget>> getnoiseloopoptions(File sounddirectory) {
//        HashMap<String, NoiseWidget> temp = new HashMap<>();
//        HashMap<Category, ArrayList<NoiseWidget>> finallist = new HashMap<>();
//        File noisedirectory = new File(sounddirectory, "noise");
//        File[] noisefolders = noisedirectory.listFiles();
//        for (File folder : noisefolders) {
//            if (folder.isDirectory()) {
//                ArrayList<NoiseWidget> tempnoiselist = new ArrayList<>();
//                Boolean validdirectory = false;
//                StringBuilder directoryname = new StringBuilder();
//                char[] h = folder.getName().toCharArray();
//                for (Character g : h) {                                                                                 // Format Directory Name For Category ChoiceBox
//                    if (Character.isUpperCase(g) && ! g.equals(h[0])) {directoryname.append(" ");}
//                    directoryname.append(g);
//                }
//                File thisdirectory = new File(noisedirectory, directoryname.toString());
//                File[] filesinthisdirectory = thisdirectory.listFiles();
////                for (File m : filesinthisdirectory) {System.out.println(m.getAbsoluteFile());}
//                if (filesinthisdirectory != null) {
//                    tempnoiselist.clear();
//                    for (File file : filesinthisdirectory) {
//                        Boolean tested = FileUtils.testmediafile(file);
//                        String formattedname = StringUtils.reformatcapatalized(file.getName());
//                        if (tested) {
////                            NoiseWidget tempnoise = new NoiseWidget(file.toString(), file);
////                            tempnoiselist.add(tempnoise);
//                        }
//                    }
//                    if (tempnoiselist.size() > 0) {validdirectory = true;}
//                }
//                if (validdirectory) {
//                    Category cat = new Category(directoryname.toString(), folder);
//                    cat.addnoisefiles();
//                    if (cat.getValid()) {finallist.put(cat, tempnoiselist);}
////                    System.out.println(cat.name);
//                }
//            }
//        }
//        return finallist;
//    }
//
//    public static HashMap<String, EntrainmentOld> getentrainmentoptions(File sounddirectory) {
//        HashMap<String, EntrainmentOld> temp = new HashMap<>();
//        File entrainmentdirectory = new File(sounddirectory, "entrainment");
//        File[] entrainmentfolders = entrainmentdirectory.listFiles();
//        for (File i : entrainmentfolders) {
//            if (i.isDirectory()) {
//                char[] h = i.getName().toCharArray();
//                StringBuilder name = new StringBuilder();
//                for (Character g : h) {
//                    if (Character.isUpperCase(g) && ! g.equals(h[0])) {name.append(" ");}
//                    name.append(g);
//                }
//                EntrainmentOld tempentrain = new EntrainmentOld(name.toString(), i);
//                if (tempentrain.working) {
//                    temp.put(name.toString(), tempentrain);
//                }
//            }
//        }
//        return temp;
//    }
//}
