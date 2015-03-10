/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package incident;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eric
 */
public class IncidentContainer {
    
   private List<Incident> incidents;
   private static IncidentContainer instance = null;
   protected IncidentContainer() {
      // Exists only to defeat instantiation.
       incidents = new ArrayList<>();
   }
   
   public static IncidentContainer getInstance()
   {
       if(instance == null)
       {
           instance = new IncidentContainer();
       }
       return instance;
   }
    
    public Incident getIncidentByName(String name)
    {
        for(Incident incident : incidents)
        {
            if(incident.toString().equals(name))
            {
                return incident;
            }
        }
        return null;
    }
    
    public List<Incident> getIncidents()
    {
        return this.incidents;
    }
    
    public void addIncident(String location, String submitter, String typeOfIncident, String situationDescription)
    {
        Incident incident = new Incident(location, submitter, typeOfIncident, situationDescription);
        this.incidents.add(incident);
    }
}
