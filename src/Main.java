import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties app_prop = load_properties();
        Parent root = FXMLLoader.load(getClass().getResource("UI\\OnBoarding.fxml"));
        primaryStage.setTitle("Python Testing tools: OnBoarding");
        int onboarding_width= Integer.parseInt(app_prop.getProperty("onboarding_width"));
        int onboarding_height = Integer.parseInt(app_prop.getProperty("onboarding_height"));
        primaryStage.setScene(new Scene(root,onboarding_width, onboarding_height));
        primaryStage.show();
    }

    private Properties load_properties() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("Configs/Application.properties"));

        } catch (IOException io) {
            io.printStackTrace();
        }
        return prop;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
