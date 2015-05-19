<!-- Navigation bar, to be included right after <body> on every page. -->
<%@ page pageEncoding="UTF-8" import="authentication.UserBean" %>
<%!
    public boolean isLoggedIn = false;
    public UserBean currentUser;
%>
<script>
    // This is called with the results from from FB.getLoginStatus().
    function statusChangeCallback(response) {
        console.log('statusChangeCallback');
        console.log(response);
        // The response object is returned with a status field that lets the
        // app know the current login status of the person.
        // Full docs on the response object can be found in the documentation
        // for FB.getLoginStatus().
        if (response.status === 'connected') {
            // Logged into your app and Facebook.
            testAPI();
        } else if (response.status === 'not_authorized') {
            // The person is logged into Facebook, but not your app.
            document.getElementById('status').innerHTML = 'Please log ' +
                    'into this app.';
        } else {
            // The person is not logged into Facebook, so we're not sure if
            // they are logged into this app or not.
            document.getElementById('status').innerHTML = 'Please log ' +
                    'into Facebook.';
        }
    }

    // This function is called when someone finishes with the Login
    // Button.  See the onlogin handler attached to it in the sample
    // code below.
    function checkLoginState() {
        FB.getLoginStatus(function (response) {
            statusChangeCallback(response);
        });
    }

    window.fbAsyncInit = function () {
        FB.init({
            appId: '1596719313900409',
            cookie: true, // enable cookies to allow the server to access 
            // the session
            xfbml: true, // parse social plugins on this page
            version: 'v2.2' // use version 2.2
        });

        // Now that we've initialized the JavaScript SDK, we call 
        // FB.getLoginStatus().  This function gets the state of the
        // person visiting this page and can return one of three states to
        // the callback you provide.  They can be:
        //
        // 1. Logged into your app ('connected')
        // 2. Logged into Facebook, but not your app ('not_authorized')
        // 3. Not logged into Facebook and can't tell if they are logged into
        //    your app or not.
        //
        // These three cases are handled in the callback function.

        FB.getLoginStatus(function (response) {
            statusChangeCallback(response);
        });

    };

    // Load the SDK asynchronously
    (function (d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id))
            return;
        js = d.createElement(s);
        js.id = id;
        js.src = "//connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));

    // Here we run a very simple test of the Graph API after login is
    // successful.  See statusChangeCallback() for when this call is made.
    function testAPI() {
        console.log('Welcome!  Fetching your information.... ');
        FB.api('/me', function (response) {
            console.log('Successful login for: ' + response.name);
            $("#username").val(response.name);
            $("#faceBook").val("FB");
            console.log(<%=isLoggedIn%>);
            if (<%=isLoggedIn%> === false)
            {
                document.getElementById('LoginForm').submit();
                console.log("logged in met die handel");
            }
            else
            {
                console.log("Al ingelogd");
                if(<%=currentUser%> !== null)
                {
                    location.href = '/CIMS/index.jsp';
                }
            }

        });
    }


    function logOut()
    {
        FB.logout(function (response) {
            // Person is now logged out
            <% isLoggedIn = false; 
            System.out.println(isLoggedIn); %>
            location.href = '/CIMS/LogoutServlet';
        });

    }

</script>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigatie</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/CIMS/index.jsp">CIMS</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">

            <ul class="nav navbar-nav">
                <%
                    currentUser = (UserBean) session.getAttribute("currentSessionUser");
                    if (currentUser != null && currentUser.isValid())
                    {
                        System.out.println("Userbean: " + currentUser + " name: " + currentUser.getUsername());
                        isLoggedIn = true;
                %>
                <li <% if (request.getServletPath().equals("/incident/new.jsp"))
                    {
                        out.write("class=\"active\"");
                    }%>
                    ><a href="/CIMS/incident/new.jsp">Nieuw incident</a></li>
                
                    <% }
                        else
                        {
                            System.out.println(isLoggedIn);
                        }%>
                <li <% if (request.getServletPath().equals("/incident/all.jsp"))
                    {
                        out.write("class=\"active\"");
                    }%>
                    ><a href="/CIMS/ViewIncidentsServlet">Incidenten</a></li>
            </ul>
            <%
                if (currentUser != null && currentUser.isValid())
                {
            %>
            <ul class="nav navbar-nav navbar-right">
                <li <% if (request.getServletPath().equals("/manage/index.jsp"))
                    {
                        out.write("class=\"active\"");
                    }%>
                    ><a href="/CIMS/manage/index.jsp" title="Beheer je account">Hallo, <%=currentUser.getUsername()%></a></li>
                <li><button type="button" class="btn btn-success" onclick="logOut();">Log uit</button></li>

            </ul>
            <% }
            else
            {%>

            <form id="LoginForm" class="navbar-form navbar-right" action="/CIMS/LoginServlet" method="POST">
                <div class="form-group">
                    <input id="username" name="username" type="text" placeholder="Gebruikersnaam" class="form-control">
                </div>
                <div class="form-group">
                    <input name="password" type="password" placeholder="Wachtwoord" class="form-control">
                </div>
                <input hidden="true" id="faceBook" name="faceBook" type="text">
                <button type="submit" class="btn btn-success">Log in</button>
                <fb:login-button scope="public_profile,email" onlogin="checkLoginState();">
                </fb:login-button>
                <button type="button" class="btn btn-success" onclick="location.href = '/CIMS/manage/newUser.jsp'">Maak account</button>
            </form>
            <% }%>
        </div><!--/.navbar-collapse -->
    </div>
</nav>
