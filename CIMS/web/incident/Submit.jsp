<%-- 
    Document   : enter
    Created on : Mar 10, 2015, 9:25:24 AM
    Author     : Sasa2905
--%>

<%@page import="java.io.ObjectInputStream"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.BufferedWriter"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.net.ConnectException"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.net.Socket"%>
<%@page import="authentication.UserBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1><%
            String name = request.getParameter("name");
            String location = request.getParameter("location");
            String description = request.getParameter("description");
            UserBean currentUser = (UserBean) session.getAttribute("currentSessionUser");
            String submitter = currentUser.getUsername();
            try {
                name = name.replace("|", "");
                location = location.replace("|", "");
                description = description.replace("|", "");
                String infoString = "@1#" + name + "|" + location + "|" + description + "|" + submitter;
                Socket socket = new Socket("145.93.105.17", 1099);
                OutputStream outSocket = socket.getOutputStream();
                ObjectOutputStream outWriter = new ObjectOutputStream(outSocket);
                outWriter.writeObject(infoString);
                outWriter.close();
                socket.close();
                response.setStatus(response.SC_MOVED_TEMPORARILY);
                response.setHeader("Location", "new.jsp");
            } catch (ConnectException e) {
                e.printStackTrace();
            }
            %>
        </h1>
    </body>
</html>
