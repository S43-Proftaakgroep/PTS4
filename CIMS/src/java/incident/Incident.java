/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import cims.Account;
import java.util.List;

/**
 *
 * @author Sasa2905
 */
public class Incident {
    private String location;
    private String date;
    private Account submitter;
    private String typeOfIncident;
    private String situationDescription;
    private IncidentInfo detailInfo;
    private boolean approved;
    
    Incident(String location, Account submitter, String typeOfIncident, String situationDescription) {
        this.location = location;
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
        if(reinforcement.isEmpty()) {
            return false;
        }
        ReinforcementRequest request = new ReinforcementRequest(extraBriefing,this);
        return request.mapReinforcement(reinforcement);
    }
    
    
    
}
