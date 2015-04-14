/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function () {

    window.addEventListener('DOMContentLoaded', function () {
        var isStreaming = false,
                v = document.getElementById('v'),
                c = document.getElementById('c'),
                r = document.getElementById('r');
        con = c.getContext('2d');
        w = 600, h = 420;

        var ws = new WebSocket("ws://" + document.location.host + "/CIMS/livevideo");
        ws.onopen = function () {
            console.log("Openened connection to websocket");
        };

        // Cross browser
        navigator.getUserMedia = (navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia);
        if (navigator.getUserMedia) {
            // Request access to video and audio
            navigator.getUserMedia(
                    {
                        video: true,
                        audio: true
                    },
            function (stream) {
                // Cross browser checks
                var url = window.URL || window.webkitURL;
                v.src = url ? url.createObjectURL(stream) : stream;
                // Set the video to play
                v.play();
            },
                    function (error) {
                        alert('Something went wrong. (error code ' + error.code + ')');
                        return;
                    }
            );
        }
        else {
            alert('Sorry, the browser you are using doesn\'t support getUserMedia');
            return;
        }

        // Wait until the video stream can play
        v.addEventListener('canplay', function (e) {
            if (!isStreaming) {
                // videoWidth isn't always set correctly in all browsers
                if (v.videoWidth > 0)
                    h = v.videoHeight / (v.videoWidth / w);
                c.setAttribute('width', w);
                c.setAttribute('height', h);
                // Reverse the canvas image
                con.translate(w, 0);
                con.scale(-1, 1);
                isStreaming = true;
            }
        }, false);

        // Wait for the video to start to play
        v.addEventListener('play', function () {
            // Every 33 milliseconds copy the video image to the canvas
            setInterval(function () {
                if (v.paused || v.ended)
                    return;
                con.fillRect(0, 0, w, h);
                con.drawImage(v, 0, 0, w, h);
                var data = c.toDataURL('image/jpeg', 0.5);
                newblob = convertToBinary(data);
                ws.send(newblob);
            }, 120);
        }, false);

    });
})();

function convertToBinary(dataURI) {
    // convert base64 to raw binary data held in a string
    // doesn't handle URLEncoded DataURIs
    var byteString = atob(dataURI.split(',')[1]);

    // separate out the mime component
    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

    // write the bytes of the string to an ArrayBuffer
    var ab = new ArrayBuffer(byteString.length);
    var ia = new Uint8Array(ab);
    for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }

    // write the ArrayBuffer to a blob, and you're done
    var bb = new Blob([ab], {type: mimeString});
    return bb;
}