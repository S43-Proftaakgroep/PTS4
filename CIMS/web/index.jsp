<%@ page pageEncoding="UTF-8" import="authentication.UserBean" %>
<%@page import="java.util.List"%>
<%@page import="websockets.Coordinates"%>
<%@page import="cims.DatabaseManager"%>
<%@page import="incident.Incident"%>
<%@page import="api.WeatherFeed"%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<%!
    public Incident closestIncident;
    public int closestIncidentId;
    List<Incident> incidentList;
%>
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
        <!-- Main jumbotron for a primary marketing message or call to action -->
        <%
            if(request.getAttribute("showBanner") != null) {
            System.out.println("ShowBanner = " + request.getAttribute("showBanner"));
            boolean showBanner = Boolean.parseBoolean(request.getAttribute("showBanner").toString());
            String typeOfAlert = "danger";
            String text = "Bestand of bericht is niet toegevoegd!";
            String title = "Failed";
            if (showBanner) {
                typeOfAlert = "success";
                text = "Bestand of bericht is succesvol toegevoegd!";
                title = "Succes!";
            }

        %>
        <div class="alert alert-<%=typeOfAlert%> alert-dismissible" role="alert" style="position: fixed; width: 100%; z-index: 999;">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <strong><%=title%></strong> <%=text%>
        </div>
        <% } 
        incidentList = DatabaseManager.getIncidents();
        Coordinates coord = (Coordinates) session.getAttribute("geolocation");
        if (coord != null) {
        this.closestIncident = coord.getClosestIncident(incidentList);
        this.closestIncidentId = closestIncident.getId();
        }
        if (currentUser != null && currentUser.isValid()) {%>
        <div class="row container" id="rowCall" style="width:100%">
            <div class="col-md-6" id="col1">
                <div class="jumbotron" id="jum1">
                    <div class="container">
                        <%

                            if (request.getParameter ( 
                                "create") != null) {
                                if (request.getParameter("create").equals("success")) {
                                    out.write("Successfully Created Account");
                                }
                            }%>
                        <h2>Dichtstbijzijnde incident</h2>
                        <h1>
                            <a  class="btn btn-primary" href="incident/incident_detail.jsp?incident=<%=closestIncidentId%>">
                                <%
                                    if (closestIncident

                                    
                                        != null) {
                                        out.println(closestIncident.toString() + " " + closestIncident.getDate());
                                    }
                                %>
                            </a>
                        </h1>  
                        <h5><strong>Omschrijving:</strong> <br />
                            <%
                                if (closestIncident

                                
                                    != null) {
                                    out.println(closestIncident.getDescription());%>
                            <h5><strong>Type: </strong> <br />
                                <%
                                        out.println(closestIncident.getType());
                                    }%></h5>
                            <br />
                            <h5><a class="btn btn-success" href="/CIMS/communications/index.jsp" role="button">Bel over dit incident &raquo;</a></h5>
                            <hr style="border-color: #cccccc"/> 
                            <!--  Action is nog niet ingevuld, nog overleggen waar we de afbeeldingen/files opslaan-->
                            <form method="post" action="/CIMS/UploadFileFtp" enctype="multipart/form-data">
                                <h5><strong>Info verzenden</strong></h5>
                                <h5>Verstuur bericht: <input type="text" name="message" /></h5>
                                <h5>Verzend bestand: <input type="file" name="file" /></h5>
                                <input type="hidden" name="incidentId" value="<%=closestIncidentId%>"/>
                                <h5><button type="submit" class="btn btn-success">Send &raquo;</button></h5>
                            </form>

                    </div>
                </div>
            </div>
            <div class="col-md-6" id="col2">
                <div class="jumbotron" id="jum2">
                    <h2>Bellen voor nieuw incident</h2>
                    <h5><a class="btn btn-success" href="/CIMS/communications/index.jsp" role="button">Bellen &raquo;</a></h5>
                </div>
            </div>
        </div>
        <% } %>

        <div class="container">
            <!-- Example row of columns -->
            <div class="row">
                <div class="col-md-4">
                    <h2><% out.println(incidentList.get(0).getType() + ": " + incidentList.get(0).getLocation());%></h2>
                    <p><% out.println(incidentList.get(0).getDate()); %></p>
                    <p><% out.println(incidentList.get(0).getDescription());%></p>
                    <p><a class="btn btn-default" href="incident/incident_detail.jsp?incident=<%out.println(incidentList.get(0).getId());%>" role="button">Meer informatie &raquo;</a></p>
                </div>
                <div class="col-md-4">
                    <h2><% out.println(incidentList.get(1).getType() + ": " + incidentList.get(1).getLocation());%></h2>
                    <p><% out.println(incidentList.get(1).getDate()); %></p>
                    <p><% out.println(incidentList.get(1).getDescription());%></p>
                    <p><a class="btn btn-default" href="incident/incident_detail.jsp?incident=<%out.println(incidentList.get(1).getId());%>" role="button">Meer informatie &raquo;</a></p>
                </div>
                <div class="col-md-4">
                    <h2><%
                        out.println(incidentList.get(2).getType() + ": " + incidentList.get(2).getLocation());%></h2>
                    <p><% out.println(incidentList.get(2).getDate()); %></p>
                    <p><% out.println(incidentList.get(2).getDescription());%></p>
                    <p><a class="btn btn-default" href="incident/incident_detail.jsp?incident=<%out.println(incidentList.get(2).getId());%>" role="button">Meer informatie &raquo;</a></p>
                </div>
            </div>

            <hr>

            <footer>
                <p>&copy; <b>CIMS</b> 2015</p>
            </footer>
        </div> <!-- /container -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script>
            $(document).ready(function () {
                $('#rowCall').width($(window)).width();
                $('#col2').height($('#col1').height());
                $('#jum2').height($('#jum1').height());
            });
        </script>

        <script src="/CIMS/js/bootstrap.min.js"></script>
    </body>

</html>