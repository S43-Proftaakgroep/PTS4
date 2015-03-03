/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sasa2905
 */
public class ReinforcementRequest {
    Map<RescuerType,Integer> reinforcementsNeeded;
    String briefing;
    Incident information;

    public ReinforcementRequest(String briefing, Incident information) {
        this.information = information;
        this.briefing = briefing;
    }
    
    public boolean mapReinforcements(List<RescuerType> reinforcements) {
        int numberOfReinforcements = 2;//dangerlevel
        this.reinforcementsNeeded = new HashMap<>();
        for(RescuerType type : reinforcements) {
            reinforcementsNeeded.put(type, numberOfReinforcements);
        }
        return !reinforcementsNeeded.isEmpty();
    }
}
