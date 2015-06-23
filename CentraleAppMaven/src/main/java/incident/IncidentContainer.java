/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import database.DatabaseManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javafx.application.Platform;

/**
 *
 * @author Eric
 */
public class IncidentContainer extends Observable {

    private List<Incident> incidents;
    private List<Incident> approved;
    private static IncidentContainer instance = null;

    protected IncidentContainer() {
        // Exists only to defeat instantiation.
        incidents = new ArrayList<>();
        approved = new ArrayList<>();
        incidents = DatabaseManager.getIncidents(0);
    }

    public static IncidentContainer getInstance() {
        if (instance == null) {
            instance = new IncidentContainer();
        }
        return instance;
    }

    public Incident getIncidentByName(String name) {
        for (Incident incident : incidents) {
            if (incident.toString().equals(name)) {
                return incident;
            }
        }
        return null;
    }

    public List<Incident> getIncidents() {
        return this.incidents;
    }

    public List<Incident> getApprovedIncidents() {
        return approved;
    }

    public void addIncident(String location, String longitude, String latitude, String submitter, String typeOfIncident, String situationDescription, String date, int priority, String victims, String dangerLevel) {
        Incident incident = new Incident(location, longitude, latitude, submitter, typeOfIncident, situationDescription, date, priority,victims,dangerLevel);
        this.incidents.add(incident);
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                setChanged();
                notifyObservers(getIncidents());
            }
        });

    }

    public void deleteIncident(Incident incident) {
        this.incidents.remove(incident);
        setChanged();
        notifyObservers(this.incidents);
    }

    public void approveIncident(Incident incident) {
        incident.approve();
        approved.add(incident);
        deleteIncident(incident);
        setChanged();
        notifyObservers("");
    }
}
