 <%-- 
    Document   : new
    Created on : 3-mrt-2015, 10:16:51
    Author     : Joris
--%>

<%@page import="java.util.List"%>
<%@page import="incident.Incident"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.ObjectInputStream"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.net.Socket"%>
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
            <script type="text/javascript" src="/js/geolocation.js"></script>
            <% Socket newSocket = new Socket("145.93.104.228", 1099);
                OutputStream outSocket = newSocket.getOutputStream();
                InputStream inSocket = newSocket.getInputStream();
                ObjectOutputStream outWriter = new ObjectOutputStream(outSocket);
                ObjectInputStream inReader = new ObjectInputStream(inSocket);
                outWriter.writeObject("@2#");
                List<Incident> incidentList = (List<Incident>) inReader.readObject();
                outWriter.close();
                newSocket.close();
            %>
            <form ACTION="jspCheckBox.jsp">

                <table border="2">
                    <tr>
                        <td><strong><%out.write("Type incident " + "<BR>");%></strong></td>
                        <td><strong><%out.write("Datum/Tijd " + "<BR>");%></strong></td>
                        <td><strong><%out.write("Locatie " + "<BR>");%></strong> </td>
                        <td><strong><%out.write("Beschrijving  " + "<BR>");%></strong></td>
                    </tr>
                    <%
                        for (Incident i : incidentList) {
                    %>

                    <tr>
                        <td><%out.write(i.getType());%></td>
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
        <script src="js/bootstrap.min.js"></script>
    </body>
</html>
