/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import database.DatabaseManager;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Sasa2905
 */
public class Incident implements Serializable {

    private String location;
    private String longitude;
    private String latitude;
    private String date;
    private String submitter;
    private String typeOfIncident;
    private String situationDescription;
    private IncidentInfo detailInfo;
    private boolean approved;
    private int id;

    public Incident(String location, String longitude, String latitude, String submitter, String typeOfIncident, String situationDescription, String date) {
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.submitter = submitter;
        this.typeOfIncident = typeOfIncident;
        this.detailInfo = new IncidentInfo();
        this.approved = false;
        this.situationDescription = situationDescription;
        this.id = -1;
    }

    public void approve() {
        approved = true;
    }

    public boolean requestReinforcement(List<RescuerType> reinforcement, String extraBriefing) {
        if (reinforcement.isEmpty()) {
            return false;
        }
        ReinforcementRequest request = new ReinforcementRequest(extraBriefing, this);
        return request.mapReinforcements(reinforcement);
    }

    @Override
    public String toString() {
        return this.typeOfIncident + " (" + location + ")";
    }

    public boolean isApproved() {
        return this.approved;
    }

    public String getType() {
        return this.typeOfIncident;
    }

    public String getDate() {
        String formattedDate = "error";
        try {
            Date newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(date);
            formattedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(newDate);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }
        return formattedDate;
    }

    public String getLocation() {
        return this.location;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getDescription() {
        return this.situationDescription;
    }

    public String getSubmitter() {
        return this.submitter;
    }
    
    public int getId()
    {
        if(this.id == -1)
        {
            this.id = DatabaseManager.getId(this.typeOfIncident, this.location, this.submitter);
        }
        
        return this.id;
    }
}
