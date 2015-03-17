<%-- 
    Document   : incident_detail
    Created on : 17-mrt-2015, 10:51:02
    Author     : Eric
--%>

<%@page import="incident.Incident"%>
<%@page import="javax.persistence.criteria.CriteriaBuilder.In"%>
<%@page import="cims.DatabaseManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    public int incidentId;
    public Incident currentIncident;
%>
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

            <%
                if (request.getParameter("incident") != null)
                {
                    String id = request.getParameter("incident");
                    Incident incident = DatabaseManager.getIncientById(Integer.parseInt(id));
                    if (incident != null)
                    {
                        this.incidentId = Integer.parseInt(id);
                        this.currentIncident = incident;
                    }
                    else
                    {
                        response.sendRedirect("all.jsp");
                    }

                }
            %>

            <h1><%= this.currentIncident.toString() %></h1>
            
            <p><%out.println("Naam: " + this.currentIncident.toString()); %></p>
            <p><%out.println("Locatie: " + this.currentIncident.getLocation()); %></p>
            <p><%out.println("Gemeld op: " + this.currentIncident.getDate()); %></p>
            <footer>
                <p>&copy; <b>CIMS</b> 2015</p>
            </footer>
        </div> <!-- /container -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="/CIMS/js/bootstrap.min.js"></script>
    </body>

</html>