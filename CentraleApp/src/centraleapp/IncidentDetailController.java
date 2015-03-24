/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centraleapp;

import incident.Incident;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.web.*;

/**
 * FXML Controller class
 *
 * @author Joris
 */
public class IncidentDetailController implements Initializable {

    @FXML
    Label lblIncidentName;

    @FXML
    Label lblIncidentDescription;

    @FXML
    Label lblIncidentWeather;

    @FXML
    Label lblIncidentAir;

    @FXML
    ListView<String> lvAdvices;

    @FXML
    ListView<String> lvSocialMedia;

    @FXML
    WebView webView;
    private WebEngine webEngine;

    private Incident incident;
    private ObservableList<String> advices;
    private String mapsHTML;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webEngine = webView.getEngine();
        advices = FXCollections.observableArrayList();
    }

    public void init(Incident incident) {
        this.incident = incident;
        lblIncidentName.setText(incident.getType());
        lblIncidentDescription.setText(incident.getDescription());
        lvAdvices.setItems(advices);
        webEngine.loadContent(GoogleMaps.getURL(incident.getLatitude(), incident.getLongitude()));
    }
}
