/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centraleapp;

import database.DatabaseManager;
import incident.Incident;
import incident.IncidentContainer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import server.CentralServer;

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

    @FXML
    Tab tabGebruiker;

    @FXML
    TableView table;

    @FXML
    TableColumn columName;

    IncidentContainer instance = IncidentContainer.getInstance();
    ObservableList<Incident> OLincidents = FXCollections.observableArrayList();

    Incident selectedIncident = null;

    @FXML
    private void btnValidateUser_Click(ActionEvent event) {
        String s = (String) listViewNewUsers.getSelectionModel().getSelectedItem();
        if (DatabaseManager.authUser(s)) {
            listViewNewUsers.getItems().remove(s);
        }
    }

    @FXML
    private void btnDenyUser_Click(ActionEvent event) {
        String s = (String) listViewNewUsers.getSelectionModel().getSelectedItem();
        if (DatabaseManager.denyUser(s)) {
            listViewNewUsers.getItems().remove(s);
        }
    }

    @FXML
    private void btnApprove_Click(ActionEvent event) {
        if (selectedIncident != null) {
            instance.approveIncident(selectedIncident);
            DatabaseManager.authIncident(selectedIncident.getType(), selectedIncident.getLocation());
        }
    }

    @FXML
    private void btnDeny_Click(ActionEvent event) {
        if (selectedIncident != null) {
            instance.deleteIncident(selectedIncident);
            DatabaseManager.denyIncident(selectedIncident.getType(), selectedIncident.getLocation());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance.addObserver(this);
        OLincidents.clear();
        OLincidents.addAll(instance.getIncidents());
        CentralServer server = new CentralServer();
        server.initServer();
        lvIncidents.setItems(OLincidents);
        updateUsers();
        lvIncidents.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (lvIncidents.getItems().size() > 0) {
                    showInfoSelectedIncident();
                }
            }
        });

        tabGebruiker.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                if (tabGebruiker.isSelected()) {
                    updateUsers();
                }
            }
        });
        ObservableList<Incident> incidents = FXCollections.observableArrayList(DatabaseManager.getIncidents(1));
        table.setEditable(true);

        TableColumn firstNameCol = new TableColumn("Naam");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<>("type"));

        TableColumn lastNameCol = new TableColumn("Locatie");
        lastNameCol.setMinWidth(200);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<>("location"));

        TableColumn emailCol = new TableColumn("Beschrijving");
        emailCol.setMinWidth(240);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<>("description"));

        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
        table.setItems(incidents);
//        try {
//            initMap();
//        } catch (IOException ex) {
//            Logger.getLogger(IncidentValidatieController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    void initMap() throws IOException {
        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452");
        URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
        in.close();
    }

    @FXML
    public void btnRefresh_Click(ActionEvent event) {
        updateUsers();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!arg.equals("")) {
            int sizeOldList = OLincidents.size();
            OLincidents.clear();
            OLincidents.addAll((List<Incident>) arg);
            int sizeNewList = OLincidents.size();
            if (sizeOldList > sizeNewList) {
                selectFirstFromListView();
            }
        }
    }

    public void selectFirstFromListView() {
        if (lvIncidents.getItems().size() > 0) {
            lvIncidents.getSelectionModel().select(0);
            showInfoSelectedIncident();
        } else {
            showNoInfo();
        }
    }

    public void showInfoSelectedIncident() {
        String incidentName = lvIncidents.getSelectionModel().getSelectedItem().toString();

        Incident incidentCurrent = instance.getIncidentByName(incidentName);
        if (incidentCurrent != null) {
            System.out.println("Selected incident: " + incidentCurrent.toString());
            selectedIncident = incidentCurrent;

            lblName.setText(selectedIncident.toString());
            lblDate.setText(selectedIncident.getDate());
            lblLocation.setText(selectedIncident.getLocation());
            lblSubmitter.setText(selectedIncident.getSubmitter());
            lblDescription.setText(selectedIncident.getDescription());
        } else {
            System.out.println("No incidents found");
        }
    }

    public void showNoInfo() {
        String noIncidentSelected = "No incident selected";
        lblName.setText(noIncidentSelected);
        lblDate.setText(noIncidentSelected);
        lblLocation.setText(noIncidentSelected);
        lblSubmitter.setText(noIncidentSelected);
        lblDescription.setText(noIncidentSelected);
    }

    private void updateUsers() {
        List<String> users = DatabaseManager.getUnApprovedUsers();
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(users);
        listViewNewUsers.setItems(list);
    }
}
