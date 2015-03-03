/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centraleapp;

import incident.Incident;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 *
 * @author Eric
 */
public class IncidentValidatieController implements Initializable {

    @FXML
    Label lblName;
    Label lblDate;
    Label lblLocation;
    Label lblStatus;
    Label lblDescription;
    
    @FXML
    ListView lvIncidents;

    public final List<Incident> incidents = new ArrayList<>();
    ObservableList<Incident> OLincidents = FXCollections.observableList(incidents);

    @FXML
    private void btnApprove_Click(ActionEvent event)
    {

    }

    @FXML
    private void btnDeny_Click(ActionEvent event)
    {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Incident incident1 = new Incident("Eindhoven", "Eric", "Explosion", "Er was een dikke explosie");
        Incident incident2 = new Incident("Weert", "Meny", "Gaslek", "Er was een dikke explosie");
        Incident incident3 = new Incident("Best", "Joris", "Gifwolk", "Er was een dikke explosie");
        Incident incident4 = new Incident("'s-Hertogenbosch", "Aanslag", "Explosion", "Er was een dikke explosie");
        Incident incident5 = new Incident("Breda", "Henk", "Is Breda (niks aan te doen)", "Er was een dikke explosie");

        incidents.add(incident1);
        incidents.add(incident2);
        incidents.add(incident3);
        incidents.add(incident4);
        incidents.add(incident5);
        
        lvIncidents.setItems(OLincidents);
    }

}
