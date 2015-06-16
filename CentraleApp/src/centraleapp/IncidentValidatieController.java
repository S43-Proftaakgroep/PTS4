/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centraleapp;

import database.DatabaseManager;
import incident.Incident;
import incident.IncidentContainer;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
    ListView lvApproveIncidents;

    @FXML
    TableView tableIncidents;

    @FXML
    ListView listViewNewUsers;

    @FXML
    Tab tabGebruiker;

    IncidentContainer instance = IncidentContainer.getInstance();
    ObservableList<Incident> OLincidents = FXCollections.observableArrayList();

    Incident selectedIncident = null;

    @FXML
    private void btnValidateUser_Click(ActionEvent event)
    {
        String s = (String) listViewNewUsers.getSelectionModel().getSelectedItem();
        if (DatabaseManager.authUser(s))
        {
            listViewNewUsers.getItems().remove(s);
        }
    }

    @FXML
    private void btnDenyUser_Click(ActionEvent event)
    {
        String s = (String) listViewNewUsers.getSelectionModel().getSelectedItem();
        if (DatabaseManager.denyUser(s))
        {
            listViewNewUsers.getItems().remove(s);
        }
    }

    @FXML
    private void btnApprove_Click(ActionEvent event)
    {
        if (selectedIncident != null)
        {
            DatabaseManager.authIncident(selectedIncident.getType(), selectedIncident.getLocation());
            instance.approveIncident(selectedIncident);
        }
    }

    @FXML
    private void btnDeny_Click(ActionEvent event)
    {
        if (selectedIncident != null)
        {
            DatabaseManager.denyIncident(selectedIncident.getType(), selectedIncident.getLocation());
            instance.deleteIncident(selectedIncident);
        }

    }

    @FXML
    private void btnRefreshIncident_Click(ActionEvent event)
    {
        ObservableList<Incident> incidents = FXCollections.observableArrayList(DatabaseManager.getIncidents(1));
        tableIncidents.setItems(incidents);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        instance.addObserver(this);
        OLincidents.clear();
        OLincidents.addAll(instance.getIncidents());
        lvApproveIncidents.setItems(OLincidents);
        updateUsers();
        lvApproveIncidents.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event)
            {
                if (lvApproveIncidents.getItems().size() > 0)
                {
                    showInfoSelectedIncident();
                }
            }
        });

        tabGebruiker.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t)
            {
                if (tabGebruiker.isSelected())
                {
                    updateUsers();
                }
            }
        });

        ObservableList<Incident> incidents = FXCollections.observableArrayList(DatabaseManager.getIncidents(1));
        tableIncidents.setEditable(false);

        TableColumn nameCol = new TableColumn("Naam");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("type"));

        TableColumn locationCol = new TableColumn("Locatie");
        locationCol.setMinWidth(200);
        locationCol.setCellValueFactory(
                new PropertyValueFactory<>("location"));

        TableColumn descriptionCol = new TableColumn("Beschrijving");
        descriptionCol.setMinWidth(240);
        descriptionCol.setCellValueFactory(
                new PropertyValueFactory<>("description"));

        TableColumn dateCol = new TableColumn("Datum");
        dateCol.setMinWidth(140);
        dateCol.setCellValueFactory(
                new PropertyValueFactory<>("date"));
        dateCol.setSortType(TableColumn.SortType.DESCENDING);

        tableIncidents.getColumns().addAll(nameCol, locationCol, descriptionCol, dateCol);
        tableIncidents.setItems(incidents);
        tableIncidents.getSortOrder().add(dateCol);

        //        try {
        //            initMap();
        //        } catch (IOException ex) {
        //            Logger.getLogger(IncidentValidatieController.class.getName()).log(Level.SEVERE, null, ex);
        //        }
    }

    void initMap() throws IOException
    {
        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452");
        URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null)
        {
            System.out.println(inputLine);
        }
        in.close();
    }

    @FXML
    public void btnRefresh_Click(ActionEvent event)
    {
        updateUsers();
    }

    @FXML
    public void btnDetailsIncident_Click(ActionEvent event)
    {
        try
        {
            Incident incidentCurrent = (Incident) tableIncidents.getSelectionModel().getSelectedItem();

            if (incidentCurrent != null)
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("IncidentDetail.fxml"));
                Stage stage = new Stage();
                stage.setTitle(incidentCurrent.toString());
                stage.setScene(new Scene((Pane) loader.load()));
                IncidentDetailController controller = loader.<IncidentDetailController>getController();
                stage.show();
                controller.init(incidentCurrent);
            }
        }
        catch (IOException ex)
        {
            System.out.println("Error changing scene from IncidentValidatie to IncidentDetail " + ex.toString());
        }

    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (!arg.equals(""))
        {
            int sizeOldList = OLincidents.size();
            OLincidents.clear();
            OLincidents.addAll((List<Incident>) arg);
            int sizeNewList = OLincidents.size();
            if (sizeOldList > sizeNewList)
            {
                selectFirstFromListView();
            }
        }
    }

    public void selectFirstFromListView()
    {
        if (lvApproveIncidents.getItems().size() > 0)
        {
            lvApproveIncidents.getSelectionModel().select(0);
            showInfoSelectedIncident();
        }
        else
        {
            showNoInfo();
        }
    }

    public void showInfoSelectedIncident()
    {
        String incidentName = lvApproveIncidents.getSelectionModel().getSelectedItem().toString();

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

    private void updateUsers()
    {
        List<String> users = DatabaseManager.getUnApprovedUsers();
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(users);
        listViewNewUsers.setItems(list);
    }

}
