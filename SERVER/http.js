var http = require('http');

var server = new http.Server;
server.listen(8000);

server.on("request",function(request,response){
	// console.log(request.message.headers);
	console.log(request.headers);
	response.writeHead(200);
	response.write("hello world" );
	response.end();
});

console.log("server started");