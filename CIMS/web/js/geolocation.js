/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var wsUri = "ws://" + document.location.host + "/CIMS/geolocation";
var websocket = new WebSocket(wsUri);

websocket.onerror = function (evt) {
    onError(evt);
};

function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

var output = document.getElementById("output");

websocket.onopen = function (evt) {
    onOpen(evt);
    tryGeolocation();
};

websocket.onmessage = function (evt) {
    onMessage(evt);
};

function tryGeolocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(sendGeolocation);
    } else {
        writeToScreen("Geolocation is not supported by this browser.");
    }
}

function sendGeolocation(location) {
    var json = JSON.stringify({
        "long": location.coords.latitude,
        "lat": location.coords.longitude
    });
    console.log("sending text: " + json);
    websocket.send(json);
}

// For testing purposes
function onMessage(evt) {
    console.log("received: " + evt.data);
}

function writeToScreen(message) {
    output.innerHTML += message + "<br>";
}

function onOpen() {
    console.log("Connected to " + wsUri);
}

function showError(error) {
    switch(error.code) {
        case error.PERMISSION_DENIED:
            writeToScreen("User denied the request for Geolocation.");
            break;
        case error.POSITION_UNAVAILABLE:
            writeToScreen("Location information is unavailable.");
            break;
        case error.TIMEOUT:
            writeToScreen("The request to get user location timed out.");
            break;
        case error.UNKNOWN_ERROR:
            writeToScreen("An unknown error occurred.");
            break;
    }
}
// End test functions
