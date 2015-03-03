/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Sasa2905
 */
public class IncidentInfo {
    String situationDescription;
    int victims;
    int wounded;
    int sizeDangerZone;
    String typeOfToxic;
    double dangerLevel;
    ReinforcementRequest request;
    Map<String,String> imageAndDesc;
    List<String> videoImages;
    String weatherConditions;
    String airQuality;
    String floorPlan;
    String buildingPlan;
    String mediaSource;
    
    Object getInfo() {
       return null;
    }
    
    
    
}
