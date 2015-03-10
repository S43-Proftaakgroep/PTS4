/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centraleapp;

import database.DatabaseManager;
import incident.Incident;
import incident.IncidentContainer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Eric
 */
public class IncidentValidatieController implements Initializable, Observer {

    @FXML
    Label lblName;

    @FXML
    Label lblDate;

    @FXML
    Label lblLocation;

    @FXML
    Label lblSubmitter;

    @FXML
    Label lblDescription;

    @FXML
    ListView lvIncidents;
    
    @FXML
    ListView listViewNewUsers;

    //nog aan te passen via init
    IncidentContainer instance = IncidentContainer.getInstance();
    ObservableList<Incident> OLincidents = FXCollections.observableArrayList();

    Incident selectedIncident = null;
    
    @FXML
    private void btnValidateUser_Click(ActionEvent event)
    {
        String s = (String)listViewNewUsers.getSelectionModel().getSelectedItem();
        if(DatabaseManager.authUser(s))
            listViewNewUsers.getItems().remove(s);
    }
    
    @FXML
    private void btnDenyUser_Click(ActionEvent event)
    {
        String s = (String)listViewNewUsers.getSelectionModel().getSelectedItem();
        if(DatabaseManager.denyUser(s))
            listViewNewUsers.getItems().remove(s);  
    }

    @FXML
    private void btnApprove_Click(ActionEvent event)
    {
        if (selectedIncident != null)
        {
            instance.approveIncident(selectedIncident);
        }
    }

    @FXML
    private void btnDeny_Click(ActionEvent event)
    {
        if (selectedIncident != null)
        {
            instance.deleteIncident(selectedIncident);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        instance.addObserver(this);

        //test data
        instance.addIncident("Eindhoven", "Eric", "Explosion", "Er was een dikke explosie", "Today");
        instance.addIncident("Weert", "Meny", "Gaslek", "Er was een dikke explosie", "Today");
        instance.addIncident("Best", "Joris", "Gifwolk", "Er was een dikke explosie", "Today");
        instance.addIncident("'s-Hertogenbosch", "Aanslag", "Explosion", "Er was een dikke explosie", "Today");
        instance.addIncident("Breda", "Henk", "Is Breda (niks aan te doen)", "wauw", "Today");

        lvIncidents.setItems(OLincidents);
        UpdateUsers();
        lvIncidents.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event)
            {
                showInfoSelectedIncident();
            }
        });
    }

    @Override
    public void update(Observable o, Object arg)
    {
        int sizeOldList = OLincidents.size();
        OLincidents.clear();
        OLincidents.addAll((List<Incident>) arg);
        int sizeNewList = OLincidents.size();
        if(sizeOldList > sizeNewList)
        {
            selectFirstFromListView();
        }
    }

    public void selectFirstFromListView()
    {
        if (lvIncidents.getItems().size() > 0)
        {
            lvIncidents.getSelectionModel().select(0);
            showInfoSelectedIncident();
        }
        else
        {
            showNoInfo();
        }
    }

    public void showInfoSelectedIncident()
    {
        String incidentName = lvIncidents.getSelectionModel().getSelectedItem().toString();

        Incident incidentCurrent = instance.getIncidentByName(incidentName);
        if (incidentCurrent != null)
        {
            System.out.println("Selected incident: " + incidentCurrent.toString());
            selectedIncident = incidentCurrent;

            lblName.setText(selectedIncident.toString());
            lblDate.setText(selectedIncident.getDate());
            lblLocation.setText(selectedIncident.getLocation());
            lblSubmitter.setText(selectedIncident.getSubmitter());
            lblDescription.setText(selectedIncident.getDescription());
        }
        else
        {
            System.out.println("No incidents found");
        }
    }

    public void showNoInfo()
    {
        String noIncidentSelected = "No incident selected";
        lblName.setText(noIncidentSelected);
        lblDate.setText(noIncidentSelected);
        lblLocation.setText(noIncidentSelected);
        lblSubmitter.setText(noIncidentSelected);
        lblDescription.setText(noIncidentSelected);
    }
    
    private void UpdateUsers()
    {
        List<String> users = DatabaseManager.getUnApprovedUsers();
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(users);
        listViewNewUsers.setItems(list);
    }
}
