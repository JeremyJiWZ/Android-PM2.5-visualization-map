var http = require('http');
var mysql = require('mysql');  
      
var AQnow = 'aqnow';  
var TABLE = 'airquality';
var sqlResponseHttp = {
	'status': true,
	'data': []
};

//checkpoints 查找表
var checkpointsMap = new Map();

//sql 结果转换为需要的json结构
function constructJson( input, result){
	result['status']=true;
	// console.log(checkpointsMap.size);
	for (var i = 0; i < input.length; i++) {
		checkName = input[i].location;
		var lat;
		var long;
		if(checkpointsMap.get(checkName)) lat = checkpointsMap.get(checkName)['lat'];
		if(checkpointsMap.get(checkName)) long = checkpointsMap.get(checkName)['long'];
		result['data'][i]={
		'checkpoints' : checkName,
		'lat'  : lat,
		'long' : long,
		'pm25' : input[i].pm25
		};
	}
	// console.log(JSON.stringify(result));
	return result;
}

//sql查询得到检查站的坐标，存在全局的数据里
function constructMap(input){
	for (var i = 0; i < input.length; i++) {
		checkName=input[i].name;
		long = input[i].longitude;
		lat = input[i].latitude;
		checkpointsMap.set(checkName,{'lat': lat, 'long': long});
	}
	console.log('the checkpoints length: '+checkpointsMap.size);
}

//创建mysql连接  
var client = mysql.createConnection({  
  host : 'localhost',
  user: 'root',  
  password: 'jiwenzhong',
  database: 'airquality'  
});  
client.connect();

//创建map
client.query('select * from checkpoints',function(err,rows,fields){
	if(err) throw err;
	constructMap(rows);
});

//获取数据
client.query('select * from '+AQnow, function(err, rows, fields) {
    if (err) throw err;
    console.log('the query result length is: '+rows.length);

    //构造json
    sqlResponseHttp = constructJson(rows, sqlResponseHttp);

});

client.end();


//create a http server and listen
var server = new http.Server;
server.listen(8000);
console.log("server listening...");


//handle the http request
server.on("request",function(request,response){

	response.writeHead(200);
	
	response.write(JSON.stringify(sqlResponseHttp));

	response.end();
});
