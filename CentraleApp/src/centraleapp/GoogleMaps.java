/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centraleapp;

/**
 *
 * @author Joris
 */
public class GoogleMaps {

    private static String HTML = "<html>\n"
            + "<head>\n"
            + "    <script type=\"text/javascript\"\n"
            + "        src=\"http://maps.google.com/maps/api/js?sensor=false\">\n"
            + "    </script>\n"
            + "        <script type=\"text/javascript\">\n"
            + "                    function initialize() {\n"
            + "                    var latlng = new google.maps.LatLng( %latitude% , %longitude% );\n"
            + "                            var myOptions = {\n"
            + "                            zoom: 10,\n"
            + "                                    center: latlng,\n"
            + "                                    mapTypeId: google.maps.MapTypeId.ROADMAP\n"
            + "                            };\n"
            + "                            var map = new google.maps.Map(document.getElementById(\"map_canvas\"),\n"
            + "                                    myOptions);\n"
            + "                            // Creating a marker and positioning it on the map  \n"
            + "                            var marker = new google.maps.Marker({\n"
            + "                            position: new google.maps.LatLng( %latitude% , %longitude% ),\n"
            + "                                    map: map\n"
            + "                            });\n"
            + "                    }\n"
            + "        </script>\n"
            + "</head>\n"
            + "<body onload=\"initialize()\">\n"
            + "    <div id=\"map_canvas\" style=\"width:100%; height:100%\"></div>\n"
            + "</body>\n"
            + "</html>";
    
    public static String getURL(String latitude, String longitude){
        String result = HTML.replace("%latitude%", latitude);
        return result.replace("%longitude%", longitude);
    }
}
