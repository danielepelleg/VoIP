package Call;

import Audio.OutputAudio;
import VoIP.Request;

import VoIP.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {

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

    /**
     * Initialize the page
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        String callID = "";
        for(int index = 0; index < 12; index++){
            callID += Request.getCallId().charAt(index);
        }
        call_idLabel.setText(callID);
        receiverTagLabel.setText(Request.getReceiverTag());
    }

    @FXML
    void call(ActionEvent event) {

    }

    @FXML
    void hangUp(ActionEvent event) {

    }

    @FXML
    void sendSinusoidalAudio(ActionEvent event) {

    }

    @FXML
    void sendSpoiledAudio(ActionEvent event) {

    }

    /**
     * Stop sending audio
     *
     * @param event press on Stop button
     */
    @FXML
    void stopAudio(ActionEvent event) {
        OutputAudio.setSendingAudio(false);
    }

    /**
     * Save logs on a external file in resources/requests/logs.txt              //  LOGS TAB
     *
     * @param event presso on Save button
     */
    @FXML
    void saveLogs(ActionEvent event) {
        Session.save();
    }

    @FXML
    void updateLogs(ActionEvent event) {

    }

    /**
     * Save the new settings of the UserAgent                                   // SETTINGS TAB
     *
     * @param event press on Save button
     */
    @FXML
    void saveSettings(ActionEvent event) {
        String newName = userNameLabel.getText();
        if(!Session.isActive() && !newName.equals("")) {
            Request.setSenderName(newName);
        }
    }
}
