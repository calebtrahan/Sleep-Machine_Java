package sleepmachine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class SleepMachine extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        test();
        Parent root = FXMLLoader.load(getClass().getResource("assets/fxml/Main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sleepmachine/assets/styles/Default.css");
        primaryStage.setTitle("Sleep Machine");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void test() {

    }
}
