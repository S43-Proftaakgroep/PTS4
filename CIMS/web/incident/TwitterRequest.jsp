<%-- 
    Document   : TwitterRequest
    Created on : 2-jun-2015, 13:41:53
    Author     : Etienne
--%>

<%@page import="org.json.simple.JSONArray"%>
<%@page import="api.TwitterFeed"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%  
	    // Merely returns TwitterFeed.getByLocation() for now.
	    String action = request.getParameter("action");
	    
	    double longitude = Double.parseDouble(request.getParameter("longitude"));
	    double latitude = Double.parseDouble(request.getParameter("latitude"));
	    ArrayList<String> tweets = new TwitterFeed().getByLocation(latitude, longitude, 5, "", 25);
	    
	    for (String tweet : tweets)
	    {
	    	out.println("<li><a href='http://twitter.com/search?q=" + tweet + "'>" + tweet + "</a></li>" + "<br>");
	    }
%>
