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
                <% 
                for(int i = 0; i < 10; i++){
                        out.write("Type incident:" + "<BR>" );
                        out.write("Locatie:" + " <BR>");
                        out.write("Beschrijving:" +i + " <BR><BR>");
                }
                 %>
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
