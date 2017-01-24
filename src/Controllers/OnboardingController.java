package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class OnboardingController {
    public Button button;

    public void OpenDownloadPage(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UI/DownloadPage.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage new_stage = new Stage();
        new_stage.initModality(Modality.APPLICATION_MODAL);
        new_stage.setScene(new Scene(root1));
        new_stage.show();
        Stage current_stage = (Stage) button.getScene().getWindow();
        current_stage.close();
    }
}
