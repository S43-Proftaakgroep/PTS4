/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centraleapp;

import incident.Incident;
import incident.IncidentContainer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

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

    //nog aan te passen via init
    IncidentContainer instance = IncidentContainer.getInstance();  
    public final List<Incident> incidents = IncidentContainer.getInstance().getIncidents();
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
        //Nu nog hardcoded
        instance.addIncident("Eindhoven", "Eric", "Explosion", "Er was een dikke explosie");
        instance.addIncident("Weert", "Meny", "Gaslek", "Er was een dikke explosie");
        instance.addIncident("Best", "Joris", "Gifwolk", "Er was een dikke explosie");
        instance.addIncident("'s-Hertogenbosch", "Aanslag", "Explosion", "Er was een dikke explosie");
        instance.addIncident("Breda", "Henk", "Is Breda (niks aan te doen)", "Er was een dikke explosie");
        
//        incidents.add(incident1);
//        incidents.add(incident2);
//        incidents.add(incident3);
//        incidents.add(incident4);
//        incidents.add(incident5);
        
        lvIncidents.setItems(OLincidents);
        
        lvIncidents.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event)
            {
                String incident = lvIncidents.getSelectionModel().getSelectedItem().toString();
                
                
                Incident incident1 = instance.getIncidentByName(incident);
                if(incident1 != null)
                {
                    System.out.println(incident1.toString());
                }
                else
                {
                    System.out.println("No incidents found");
                }
                
            }
    });
    }
    
    @FXML
    private void lvItem_Click(MouseEvent arg0)
    {
        //System.out.println("test: " + arg0.toString());
        String incident0 = arg0.toString();
        System.out.println("Incident: " + incident0);
    }

}
