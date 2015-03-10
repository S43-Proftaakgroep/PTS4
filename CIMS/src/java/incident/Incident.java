/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sasa2905
 */
public class Incident implements Serializable {

    private String location;
    private String date;
    private String submitter;
    private String typeOfIncident;
    private String situationDescription;
    private IncidentInfo detailInfo;
    private boolean approved;

    public Incident(String location, String submitter, String typeOfIncident, String situationDescription, String date) {
        this.location = location;
        this.date = date;
        this.submitter = submitter;
        this.typeOfIncident = typeOfIncident;
        this.detailInfo = new IncidentInfo();
        this.approved = false;
        this.situationDescription = situationDescription;
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
        return this.date;
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
