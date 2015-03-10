<%-- 
    Document   : new
    Created on : 3-mrt-2015, 10:16:51
    Author     : Joris
--%>

<%@page import="cims.DatabaseManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Crisis Informatie Management Systeem</title>
        <link href="/CIMS/css/bootstrap.min.css" rel="stylesheet">
        <link href="/CIMS/css/Site.css" rel="stylesheet">
        <script src="/CIMS/js/jquery-1.8.3.min.js" type="text/javascript"></script>
        <!-- Addresspicker -->
        <script>
            $(function () {
                var addresspickerMap = $("#addresspicker_map").addresspicker(
                        {
                            regionBias: "nl",
                            map: "#map_canvas",
                            typeaheaddelay: 1000,
                            mapOptions: {
                                zoom: 6,
                                center: new google.maps.LatLng(52.090737, 5.1214201)
                            }

                        });

                addresspickerMap.on("addressChanged", function (evt, address) {
                    $("#longtitude").val(address.geometry.location.lng());
                    $("#latitude").val(address.geometry.location.lat());
                    console.dir(address);
                });
                addresspickerMap.on("positionChanged", function (evt, markerPosition) {
                    markerPosition.getAddress(function (address) {
                        if (address) {
                            $("#addresspicker_map").val(address.formatted_address);
                        }
                    });
                });
            });
        </script>
    </head>
    <body>
        <%@include file="/navigationBar.jsp" %>
        <div class="container">
            <form role="form" class="form-create" action="/CIMS/CreateIncidentServlet" method="POST">
                <h2 class="form-signin-heading">Meld incident</h2>
                <div class="form-group">
                    <label for="name">Incident naam:</label>
                    <input name="name" type="text" class="form-control" id="name">
                </div>
                <div class="form-group">
                    <label for="descr">Incident beschrijving:</label>
                    <input name="descr" type="text" class="form-control" id="descr">
                </div>
                <div class="form-group">
                    <label for="addresspicker_map">Locatie:</label>
                    <input name="address" class="form-control" id="addresspicker_map" />
                    <div style="width:300px;height:300px;margin-top:20px;">
                        <div id="map_canvas" style="width:100%; height:100%"></div>
                        <div id="location" class=""></div>
                    </div>
                    <input name="longtitude" hidden="true" id="longtitude" />
                    <input name="latitude" hidden="true" id="latitude" />
                </div>
                <button type="submit" class="btn btn-default">Melden</button>
            </form>
            <br>
            <footer>
                <p>&copy; <b>CIMS</b> 2015</p>
            </footer>
        </div> <!-- /container -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
        <script src="/CIMS/js/bootstrap.min.js"></script>
        <script src="/CIMS/js/jquery.addresspicker.js" type="text/javascript"></script>
        <script src="/CIMS/js/bootstrap-typeahead.js" type="text/javascript"></script>
    </body>
</html>
