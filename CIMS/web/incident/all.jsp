<%@page import="cims.DatabaseManager"%>
<%-- 
    Document   : new
    Created on : 3-mrt-2015, 10:16:51
    Author     : Joris
--%>

<%@page import="java.util.List"%>
<%@page import="incident.Incident"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Crisis Informatie Management Systeem</title>
        <link href="/CIMS/css/bootstrap.min.css" rel="stylesheet">
        <link href="/CIMS/css/Site.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="/navigationBar.jsp" %>
        <div class="container">
            <h1>Alle incidenten</h1>
            <p>Hier kunnen je alle incidenten zien.</p>
            <div id="output"></div>            
            <%
                List<Incident> incidentList = DatabaseManager.getIncidents();
            %>
            <form ACTION="jspCheckBox.jsp">
                <table border="2">
                    <tr>
                        <td><strong><%out.write("Naam " + "<BR>");%></strong></td>
                        <td><strong><%out.write("Datum/Tijd " + "<BR>");%></strong></td>
                        <td><strong><%out.write("Locatie " + "<BR>");%></strong> </td>
                        <td><strong><%out.write("Beschrijving  " + "<BR>");%></strong></td>
                    </tr>
                    <%
                        for (Incident i : incidentList) {
                            String url = "incident_detail.jsp" + i.getType();
                    %>
                    <tr>
                        <td><a href="incident_detail.jsp"><%out.write(i.getType() + "("+ i.getLocation() +") ");%></a></td>
                        <td><%out.write(i.getDate());%> </td>
                        <td><%out.write(i.getLocation());%> </td>
                        <td><%out.write(i.getDescription());%></td>
                    </tr>
                    <%
                        }
                    %>
                </table>
            </form>
            <br>
            <footer>
                <p>&copy; <b>CIMS</b> 2015</p>
            </footer>
        </div> <!-- /container -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="/CIMS/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="/CIMS/js/geolocation.js"></script>
    </body>
</html>
