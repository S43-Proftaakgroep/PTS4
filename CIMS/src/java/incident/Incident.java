/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Sasa2905
 */
public class Incident implements Serializable {

    private String location, longtitude, latitude, submitter, typeOfIncident, situationDescription;
    private Date date;
    private IncidentInfo detailInfo;
    private boolean approved;

    /**
     *
     * @param location
     * @param coordinates
     * @param submitter
     * @param typeOfIncident
     * @param situationDescription
     */
    Incident(String location, String longtitude, String latitude, String submitter, String typeOfIncident, String situationDescription) {
        this.location = location;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.date = new Date();
        this.location = location;
        this.submitter = submitter;
        this.typeOfIncident = typeOfIncident;
        this.detailInfo = new IncidentInfo();
        this.approved = false;
        this.situationDescription = situationDescription;
    }

    /**
     *
     */
    public void approve() {
        approved = true;
    }

    /**
     *
     * @param reinforcement
     * @param extraBriefing
     * @return
     */
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
        return this.date.toString();
    }

    public String getLocation() {
        return this.location;
    }

    public String getDescription() {
        return this.situationDescription;
    }

    public String getSubmitter() {
        return this.submitter;
    }
}
