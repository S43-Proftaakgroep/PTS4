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
        <div class="row container" id="rowCall" style="width:100%">
            <div class="col-md-6" id="col1">
                <div class="jumbotron" id="jum1">
                    <div class="container">
                        <%
                            if (request.getParameter("create") != null) {
                                if (request.getParameter("create").equals("success")) {
                                    out.write("Successfully Created Account");
                                }
                            }%>
                        <h2>Dichtsbijzijnde incident</h2>
                        <p>Dit is het dichtsbijzijnde incident.</p>
                        <%

                            List<Incident> incidentList = DatabaseManager.getIncidents();
                            Coordinates coord = (Coordinates) session.getAttribute("geolocation");
                            if (coord != null) {
                                this.closestIncident = coord.getClosestIncident(incidentList);
                                this.closestIncidentId = closestIncident.getId();
                            }

                        %>
                        <h1>
                            <a  class="btn btn-primary" href="incident/incident_detail.jsp?incident=<%=closestIncidentId%>">
                                <%
                                    if (closestIncident != null) {
                                        out.println(closestIncident.toString() + " " + closestIncident.getDate());
                                    }
                                %>
                            </a>
                        </h1>  
                        <h5><strong>Description: </strong> <br />
                            <%
                                if (closestIncident != null) {
                                    out.println(closestIncident.getDescription());%>
                            <h5><strong>Type: </strong> <br />
                                <%
                                        out.println(closestIncident.getType());
                                    }%></h5>
                            <br />
                            <p><a class="btn btn-success btn-lg" href="/CIMS/communications/index.jsp" role="button">Call about this incident &raquo;</a></p>
                            <hr style="border-color: #cccccc"/> 
                            <!--  Action is nog niet ingevuld, nog overleggen waar we de afbeeldingen/files opslaan-->
                            <form method="post" action="" enctype="multipart/form-data">
                                <h5><strong>Send Info</strong></h5>
                                <h5>Send message: <input type="text" name="message" /></h5>
                                <h5>Send file: <input type="file" name="file" /></h5>
                                <h5><submit class="btn btn-success">Send &raquo;</submit></h5>
                            </form>

                    </div>
                </div>
            </div>
            <div class="col-md-6" id="col2">
                <div class="jumbotron" id="jum2">
                    <p><a class="btn btn-success btn-lg" href="/CIMS/communications/index.jsp" role="button">Call HQ &raquo;</a></p>
                </div>
            </div>
        </div>

        <div class="container">
            <!-- Example row of columns -->
            <div class="row">
                <div class="col-md-4">
                    <h2>Heading</h2>
                    <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                    <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
                </div>
                <div class="col-md-4">
                    <h2>Heading</h2>
                    <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                    <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
                </div>
                <div class="col-md-4">
                    <h2>Heading</h2>
                    <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>
                    <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
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
