import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main Class
 * Run the UI and open the Application ogin page.
 *
 * @author Guido Soncini <guido.soncini1@studenti.unipr.it> - 285140
 * @author Mattia Ricci <mattia.ricci1@studenti.unipr.it> - 285237
 */
public class Program extends Application {

    private static String UI = "../Application/Application.fxml";

    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(UI));
        Scene frame = new Scene(root);
        primaryStage.getIcons().add(new Image("/images/voip/.png"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("VoIP Application");
        primaryStage.setScene(frame);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}