var http = require('http');
var url = require('url');
var mysql = require('mysql');
var async = require('async');
var sqlJson = {
	'status': true,
	'data' : []
};


//创建mysql连接  
var client = mysql.createConnection({  
  host : 'localhost',
  user: 'root',  
  password: 'jiwenzhong',
  database: 'airquality'  
});  
client.connect();



function constructJson(input,result){
	result['status'] = true;
	for (var i = 0; i < input.length; i++) {
		checkName = input[i].location;
		pm25 = input[i].pm25;
		time = input[i].update_time;
		result['data'][i]={
		'checkpoint' : checkName,
		'pm25' : pm25,
		'time' : time
		};
	}
	// console.log(JSON.stringify(result));
	return result;
}

function route(pathname){
    console.log("About to route a request for" + pathname);
}

function   utf8CodeToChineseChar(strUtf8) 
{ 
      var   iCode,   iCode1,   iCode2; 
      iCode   =   parseInt("0x"   +   strUtf8.substr(1,   2)); 
      iCode1   =   parseInt("0x"   +   strUtf8.substr(4,   2)); 
      iCode2   =   parseInt("0x"   +   strUtf8.substr(7,   2)); 
       
      return   String.fromCharCode(((iCode   &   0x0F)   <<   12)   |    
	((iCode1   &   0x3F)   <<     6)   | 
	(iCode2   &   0x3F)); 
} 

//UTF-8编码的汉字转换为字符，特殊字符未处理，
function   chineseFromUtf8Url(strUtf8)    
{ 
var   bstr   =   ""; 
var   nOffset   =   0; //   processing   point   on   strUtf8 
   
if(   strUtf8   ==   ""   ) 
      return   ""; 
   
strUtf8   =   strUtf8.toLowerCase(); 
nOffset   =   strUtf8.indexOf("%e"); 
if(   nOffset   ==   -1   ) 
      return   strUtf8; 
       
while(   nOffset   !=   -1   ) 
{ 
      bstr   +=   strUtf8.substr(0,   nOffset); 
      strUtf8   =   strUtf8.substr(nOffset,   strUtf8.length   -   nOffset); 
      if(   strUtf8   ==   ""   ||   strUtf8.length   <   9   )       //   bad   string 
          return   bstr; 
       
      bstr   +=   utf8CodeToChineseChar(strUtf8.substr(0,   9)); 
      strUtf8   =   strUtf8.substr(9,   strUtf8.length   -   9); 
      nOffset   =   strUtf8.indexOf("%e"); 
} 
   
return   bstr   +   strUtf8; 
} 

function onRequest(request, response){
    var pathname = url.parse(request.url).pathname; //提取url
    var query = url.parse(request.url).search;  //提取query
    if(query==null) return;


    query = query.substring(1);
    console.log(query);
    query = chineseFromUtf8Url(query);
    // query = utf8CodeToChineseChar(query);

    route(pathname) //路由
    response.writeHead(200, {"Content-Type": "text/plain"});

    // async.series([
    // 	function(callback){
    // 		callback(null,'one');
    // 		console.log("async tasks begin");
    // 	},
    // 	function(callback){
    // 		client.query('select * from airquality where location = \''+query+'\';',function(err,rows,fields)
    // 		{
				// if(err) throw err;
				// sqlJson = constructJson(rows,sqlJson);
    // 		});
    // 		callback(null,'two');
    // 	},
    // 	function(callback){
    // 		callback(null,'3');
    // 		response.write(JSON.stringify(sqlJson));
    // 		response.end();
    // 	}
    // 	]);
    client.query('select * from airquality where location = \''+query+'\';',function(err,rows,fields)
    		{
				if(err) throw err;
				sqlJson = constructJson(rows,sqlJson);
    		});
    setTimeout(function(){response.write(JSON.stringify(sqlJson));
    		response.end();}, 100);


    console.log(query);
}

http.createServer(onRequest).listen(8888);

console.log("Server has started...")

