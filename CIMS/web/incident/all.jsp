<%-- 
    Document   : new
    Created on : 3-mrt-2015, 10:16:51
    Author     : Joris
--%>

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
            <form ACTION="jspCheckBox.jsp">

                <table border="2">
                    <tr>
                        <td><strong><%out.write("Type incident " + "<BR>");%></strong></td>
                        <td><strong><%out.write("Locatie " + "<BR>");%></strong> </td>
                        <td><strong><%out.write("Beschrijving  " + "<BR>");%></strong></td>
                    </tr>
                    <%
                        for (int i = 0; i < 10; i++) {
                    %>

                    <tr>
                        <td><%out.write(i + " Type ");%></td>
                        <td><%out.write(i + " Locatie ");%> </td>
                        <td><%out.write(i + " Beschrijving ");%></td>
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
