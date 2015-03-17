/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websockets;

import incident.Incident;
import java.util.List;

/**
 *
 * @author Joris
 */
public class Coordinates {

    private double longitude;
    private double latitude;

    public Coordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public Incident getClosestIncident(List<Incident> incidentList) {
        Incident incident = incidentList.get(0);
        for (Incident i : incidentList) {
            if (Math.sqrt(
                    Math.pow((incident.getLatitude() - this.latitude), 2)
                    + Math.pow((incident.getLongitude() - this.longitude), 2))
                    > Math.sqrt(
                            Math.pow((i.getLatitude() - this.latitude), 2)
                            + Math.pow((i.getLongitude() - this.longitude), 2))) {
                incident = i;
            }
        }
        return incident;
    }
}
