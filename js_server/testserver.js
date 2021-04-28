var http = require('http');
var osc = require("osc");
//for OSC 

//sending OSC messages to supercollider 
var udpPort = new osc.UDPPort({
    localAddress: "127.0.0.1",
    localPort: 57121,
    metadata: true
});

udpPort.open();

// When the port is read, send an OSC message to, say, SuperCollider
// udpPort.on("ready", function() {
//     udpPort.send({
//         address: "/makePing3",
//         args: [{
//             type: "i",
//             value: "2000"
//         }],
//     }, "127.0.0.1", 57120);

//     console.log("sending!")

// });

// udpPort.on("ready", function() {
//     udpPort.send({
//         address: "/addOne",
//         args: [{
//             type: "i",
//             value: "2000"
//         }],
//     }, "127.0.0.1", 57120);

//     console.log("sending!")

// });


function inc() {
    udpPort.send({
        address: "/addOne",
        args: [{
            type: "i",
            value: "2000"
        }],
    }, "127.0.0.1", 57120);

    console.log("sending one more along...!")

}

//create a server object:
var myServer = http.createServer(function(req, res) {
    res.write('meow meow! we got it.'); //write a response to the client
    res.end(); //end the response
}).listen(8080); //the server object listens on port 8080

myServer.addListener('request', function(req, res) {
    //reaching out to supercollider
    inc()
});

console.log("now listening on port8080")