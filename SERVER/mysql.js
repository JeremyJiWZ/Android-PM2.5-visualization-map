var mysql = require('mysql');  
      
var AQnow = 'aqnow';  
var TABLE = 'airquality';  
  
//创建连接  
var client = mysql.createConnection({  
  host : 'localhost',
  user: 'root',  
  password: 'jiwenzhong',
  database: 'airquality'  
});  

client.connect();

client.query('select * from '+TABLE, function(err, rows, fields) {
  if (err) throw err;
  console.log(rows.length);
  for (var i = 0; i < rows.length; i++) {
    console.log(rows[i].location, rows[i].quality, rows[i].pm25); 
  }
});

client.end();