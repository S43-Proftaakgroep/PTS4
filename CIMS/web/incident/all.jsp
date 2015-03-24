<%@page import="java.io.IOException"%>
<%@page import="java.net.UnknownHostException"%>
<%@page import="java.io.ObjectInputStream"%>
<%@page import="java.net.Socket"%>
<%@page import="cims.DatabaseManager"%>
<%-- 
    Document   : new
    Created on : 3-mrt-2015, 10:16:51
    Author     : Joris
--%>

<%@page import="java.util.List" %>
<%@page import="incident.Incident" %>
<%@page import="websockets.Coordinates" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    public Incident closestIncident;
    public int closestIncidentId;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="/CIMS/js/geolocation.js"></script>
        <title>Crisis Informatie Management Systeem</title>
        <link href="/CIMS/css/bootstrap.min.css" rel="stylesheet">
        <link href="/CIMS/css/Site.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="/navigationBar.jsp" %>
        <div class="container">
            <h1>Dichtsbijzijnde incident</h1>
            <p>Dit is het dichtsbijzijnde incident. Andere incidenten staan hieronder.</p>
            <div id="output"></div>            
            <%
                List<Incident> incidentList = DatabaseManager.getIncidents();
                Coordinates coord = (Coordinates) session.getAttribute("geolocation");
                if (coord != null) {
                    this.closestIncident = coord.getClosestIncident(incidentList);
                    this.closestIncidentId = closestIncident.getId();
                }
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                        Socket socket = new Socket("127.0.0.1", 1099);
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        String socketString = (String) input.readObject();
                        System.out.println(socketString);
                        }
                        catch(ClassNotFoundException exception){
                            exception.printStackTrace();
                        }
                        catch(IOException ex) {}
                    }
                });
            %>
            <h1>
                <a  class="btn btn-primary" href="incident_detail.jsp?incident=<%=closestIncidentId%>">
                    <%
                        if (closestIncident != null) {
                            out.println(closestIncident.toString() + " " + closestIncident.getDate());
                        }
                    %>
                </a>
            </h1>
            <br>
            <br>
            <h1>Alle incidenten</h1>
            <p>Hier kunnen je alle incidenten zien.</p>
            <table class="table table-striped">
                <tr>
                    <td><strong><%="Naam " + "<BR>"%></strong></td>
                    <td><strong><%="Datum/Tijd " + "<BR>"%></strong></td>
                    <td><strong><%="Locatie " + "<BR>"%></strong> </td>
                    <td><strong><%="Beschrijving  " + "<BR>"%></strong></td>
                </tr>
                <%
                    for (Incident i : incidentList) {
                        String url = "incident_detail.jsp?incident=" + i.getId();
                %>
                <tr>
                    <td><a href=<%=url%>><%out.write(i.getType() + "(" + i.getLocation() + ") ");%></a></td>
                    <td><%out.write(i.getDate());%> </td>
                    <td><%out.write(i.getLocation());%> </td>
                    <td><%out.write(i.getDescription());%></td>
                </tr>
                <%
                    }
                %>
            </table>
            <br>
            <footer>
                <p>&copy; <b>CIMS</b> 2015</p>
            </footer>
        </div> <!-- /container -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="/CIMS/js/bootstrap.min.js"></script>
    </body>
</html>
