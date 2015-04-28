<%-- 
    Document   : index
    Created on : 31-mrt-2015, 13:44:14
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
        <script src="/CIMS/js/communication.js"></script>
        <script src="/CIMS/js/MediaStreamRecorder.js"></script>
    </head>
    <body>
        <%@include file="/navigationBar.jsp" %>
        <div class="container">
            <h1>Audio!</h1>
            <video hidden="true" id="v"></video>	
            <canvas id="c"></canvas>
            <canvas id="r"></canvas>
            <footer>
                <p>&copy; <b>CIMS</b> 2015</p>
            </footer>
        </div>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="/CIMS/js/bootstrap.min.js"></script>
    </body>
</html>