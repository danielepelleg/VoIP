package Call;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ApplicationController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab mainTab;

    @FXML
    private AnchorPane anchorMain;

    @FXML
    private ImageView logo;

    @FXML
    private Label connectionLabel;

    @FXML
    private Label receiverLabel;

    @FXML
    private Button hangUpButton;

    @FXML
    private Button callButton;

    @FXML
    private Tab audioTab;

    @FXML
    private AnchorPane anchorAudio;

    @FXML
    private ImageView audio;

    @FXML
    private Label audioControlLabel;

    @FXML
    private Button spoiledAudioButton;

    @FXML
    private Button sinusoidalAudioButton;

    @FXML
    private Button stopButton;

    @FXML
    private Tab logsTab;

    @FXML
    private AnchorPane anchorLogs;

    @FXML
    private ImageView logsButton;

    @FXML
    private Button saveLogsButton;

    @FXML
    private Button updateButton;

    @FXML
    private Tab settingsTab;

    @FXML
    private AnchorPane anchorSettings;

    @FXML
    private TextField userNameLabel;

    @FXML
    private Label call_idLabel;

    @FXML
    private Label receiverTagLabel;

    @FXML
    private Button saveSettingsButton;

    @FXML
    void call(ActionEvent event) {

    }

    @FXML
    void hangUp(ActionEvent event) {

    }

    @FXML
    void saveLogs(ActionEvent event) {

    }

    @FXML
    void saveSettings(ActionEvent event) {

    }

    @FXML
    void sendSinusoidalAudio(ActionEvent event) {

    }

    @FXML
    void sendSpoiledAudio(ActionEvent event) {

    }

    @FXML
    void stopAudio(ActionEvent event) {

    }

    @FXML
    void updateLogs(ActionEvent event) {

    }

}
