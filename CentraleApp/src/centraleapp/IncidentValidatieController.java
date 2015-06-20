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
import java.util.logging.Logger;

import incident.Priority;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    Label lblPriority;

    @FXML
    Label lblDate;

    @FXML
    ChoiceBox cbPriority;

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

    @FXML
    ChoiceBox cbPriorityFilter;

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
            int priority = translatePriority(cbPriority.getValue());
            DatabaseManager.authIncident(selectedIncident.getType(), priority, selectedIncident.getLocation());
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
        ObservableList<Incident> currentIncidents = tableIncidents.getItems();
        ObservableList<Incident> incidents = FXCollections.observableArrayList(DatabaseManager.getIncidents(1));
        tableIncidents.setVisible(false);
        tableIncidents.getItems().removeAll(currentIncidents);
        tableIncidents.getItems().addAll(incidents);
        tableIncidents.setVisible(true);
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
        
        TableColumn priorityCol = new TableColumn("Prioriteit");
        priorityCol.setMinWidth(100);
        priorityCol.setCellValueFactory(
                new PropertyValueFactory<>("priority"));
        
        TableColumn victimsCol = new TableColumn("Slachtoffers");
        victimsCol.setMinWidth(100);
        victimsCol.setCellValueFactory(
                new PropertyValueFactory<>("victims"));
        
        TableColumn dangerCol = new TableColumn("Gevaar");
        dangerCol.setMinWidth(60);
        dangerCol.setCellValueFactory(
                new PropertyValueFactory<>("danger"));

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
        dateCol.setSortType(TableColumn.SortType.ASCENDING);

        tableIncidents.getColumns().addAll(nameCol, priorityCol,victimsCol,dangerCol, locationCol, descriptionCol, dateCol);
        tableIncidents.getSortOrder().add(dateCol);
        tableIncidents.setItems(incidents);

        cbPriorityFilter.setItems(FXCollections.observableArrayList("Alle", /*Ongevalideerd",*/ "Laag", "Normaal", "Hoog"));
        cbPriorityFilter.setValue("Alle");
        cbPriorityFilter.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int priority = translatePriority(cbPriorityFilter.getValue());
                filterBy(priority);// FIXME: lijst wordt pas ge?pdate na tweede event.
            }
        });
        //        try {
        //            initMap();
        //        } catch (IOException ex) {
        //            Logger.getLogger(IncidentValidatieController.class.getName()).log(Level.SEVERE, null, ex);
        //        }
    }

    /**
     * Priority filter in incident overview.
     * @param priority
     */
    private void filterBy(int priority) {

        ObservableList<Incident> filteredList = FXCollections.observableArrayList();
        ObservableList<Incident> all = FXCollections.observableArrayList();

        // Get all approved incidents.
        all.addAll(DatabaseManager.getIncidents(1));

        if (priority < 0) { // Show all incidents.
            filteredList = all;
        }
        else {
            for (Incident i : all) {
                if (i.getPriority() == priority)
                    filteredList.add(i);
            }
        }

        ObservableList olditems = tableIncidents.getItems();

        tableIncidents.getItems().clear();//.removeAll(olditems);
        tableIncidents.setItems(filteredList);
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
            System.out.println(incidentCurrent.getDescription());
            selectedIncident = incidentCurrent;

            lblName.setText(selectedIncident.toString());
            lblPriority.setText(selectedIncident.getPriority() + "");
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
        lblPriority.setText(noIncidentSelected);
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

    /**
     * Translates a ChoicBox item to an incidents' priority integer.
     */
    private int translatePriority(Object priority){
        switch (priority.toString()){
            case "Alle":
                return -1;
            case "Ongevalideerd":
                return 0;
            case "Laag":
                return 1;
            case "Normaal":
                return 2;
            case "Hoog":
                return 3;
            default:
                throw new UnsupportedOperationException("Error: Invalid priority! (" + priority.toString() + ").");
        }
    }
}
