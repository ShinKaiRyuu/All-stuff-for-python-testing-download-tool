import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.util.Properties;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties app_prop = new Properties();
        app_prop.load(new FileInputStream("Configs/Application.properties"));
        Parent root = FXMLLoader.load(getClass().getResource("UI\\OnBoarding.fxml"));
        primaryStage.setTitle("Python Testing tools: OnBoarding");
        int onboarding_width = Integer.parseInt(app_prop.getProperty("onboarding_width"));
        int onboarding_height = Integer.parseInt(app_prop.getProperty("onboarding_height"));
        primaryStage.setScene(new Scene(root, onboarding_width, onboarding_height));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
