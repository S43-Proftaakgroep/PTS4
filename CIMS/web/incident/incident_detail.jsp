<%-- 
    Document   : incident_detail
    Created on : 17-mrt-2015, 10:51:02
    Author     : Eric
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="api.TwitterFeed"%>
<%@page import="api.WeatherFeed"%>
<%@page import="java.util.List"%>
<%@page import="incident.Incident"%>
<%@page import="javax.persistence.criteria.CriteriaBuilder.In"%>
<%@page import="cims.DatabaseManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    public int incidentId;
    public Incident currentIncident;
    public List<String> advice;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Crisis Informatie Management Systeem</title>
        <link href="/CIMS/css/bootstrap.min.css" rel="stylesheet">
        <link href="/CIMS/css/Site.css" rel="stylesheet">
	<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
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
                        advice = DatabaseManager.getAdviceById(Integer.parseInt(id));
                    }
                    else
                    {
                        response.sendRedirect("all.jsp");
                    }

                }
            %>

            <div class="jumbotron">
                <h2><%out.println(this.currentIncident.toString()); %></h2>
                <p><%out.println("Locatie: " + this.currentIncident.getLocation()); %></p>
                <p><% out.println("Gemeld op: " + this.currentIncident.getDate());%></p>

                <%
                    if (advice.size() > 0)
                    {%>
                <p><%out.println("Advies: "); %></p>
                <% for (String singleAdvice : advice)
                    { %>
                <p><% out.println(singleAdvice); %></p>
                <% }
                }
                else
                { %>
                <p><% out.println("Nog geen advies beschikbaar. Blijf deze pagina in de gaten houden voor advies."); %></p>
                <% }
                %>
                <p><%
                    String location = this.currentIncident.getLocation().replace(", Nederland", "");
                    WeatherFeed wf = new WeatherFeed(location, WeatherFeed.Query.TEMPERATURE);
                    out.println("Weer: " + wf.getData() + " en ");
                    wf.setQuery(WeatherFeed.Query.DESCRIPTION);
                    out.println(wf.getData());%></p>
                <div style="height:400px;overflow:scroll;">
                    <h3>Laatste updates over dit incident op Twitter:</h3>		    
                    <ul id="tweets"> bezig met laden van tweets...
			<script>
			    var result = 
				$.post(
				    // Address of the request recipient
				    "./TwitterRequest.jsp",
				    
				    // Data to send
				    {
					"action" : "requestTweets", // overhead for future requesthandling
					"longitude" : <% out.println(this.currentIncident.getLongitude()); %>,
					"latitude" : <% out.println(this.currentIncident.getLatitude()); %>
				    },

				    // Handler for the response.
				    function(data){
					//alert(data); debug: displays data.
					document.getElementById("tweets").innerHTML = data;
				    }
				)

				.done(function() {
				    //alert( "AJAX/post succesvol uitgevoerd."); debug: shows on success.
				})
				.fail(function() {
				    alert( "Oeps! Ergens ging er iets mis. :(" );
				})
				.error(function(){
				    alert("wow, dikke faal. #fontysict");
				})
			    ;
			</script>
                    </ul>
                </div>
            </div>

            <%
                currentUser = (UserBean) session.getAttribute("currentSessionUser");
                if (currentUser != null && currentUser.isValid())
                {
            %>
            <form action="/CIMS/SendInfoToCentral" role="form" class="form-create" method="POST">
                <p>Type een bericht om de centrale te informeren</p>
                <p><textarea name="messageText" class="form-control" rows="3" ></textarea></p>
                <input type="hidden" name="incidentId" value="<%=incidentId%>">
                <p><input type="submit" value="Verstuur naar centrale" name="submit"></p>
            </form>
            <%
                }
                else
                {
                    %>
                   U moet ingelogd zijn om informatie naar de centrale te kunnen versturen.
            <%
                }
            %>




            <footer>
                <p>&copy; <b>CIMS</b> 2015</p>
            </footer>
        </div> <!-- /container -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="/CIMS/js/bootstrap.min.js"></script>
    </body>

</html>