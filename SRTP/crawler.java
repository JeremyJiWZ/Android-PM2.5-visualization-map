import java.io.*;
import java.net.*;
import java.sql.*;

public class crawler{
	private String sourceURL;
	private Connection conn;
	private Statement statement;
	String tablename="AirQuality";
	String sql="";
	
	public crawler(){
		try{
			//连接数据库
			Class.forName("com.mysql.jdbc.Driver");
//			sourceURL = "jdbc:mysql://localhost/AirQuality";
			sourceURL = "jdbc:mysql://localhost:3306/airquality?user=root&password=jiwenzhong&useUnicode=true&characterEncoding=utf-8";
//			conn = DriverManager.getConnection(sourceURL,"root","jiwenzhong");
			conn = DriverManager.getConnection(sourceURL);
			statement = conn.createStatement();
		}
		catch(Exception sqle){
			System.err.println(sqle);
		}
	}
	
	public void insert_history(String location,String AQI,String quality,String pm25,String update_time) throws SQLException{
		//如果数据库中已经有相同地点、相同更新时间的记录，则不需要重复插入
		//sql = "select count(*) from " + tablename + " where location = '" + location + "' and update_time = '" + update_time + "';";
		sql = "select count(*) from " + "AirQuality" + " where location = '" + location + "' and update_time = '" + update_time + "';";
		ResultSet rs = statement.executeQuery(sql);
		String count="";
		while(rs.next()){
			count=rs.getString(1);
		}
		if(count.equals("0")){
			//sql = "insert into " + tablename + " values('" + location + "','" + AQI + "','" + quality + "','" +pm25 +"','" +update_time + "');";
			sql = "insert into " + "AirQuality" + " values('" + location + "','" + AQI + "','" + quality + "','" +pm25 +"','" +update_time + "');";
			statement.executeUpdate(sql);
		}
		return;
	}
	
	public void insert_now(String location,String AQI,String quality,String pm25,String update_time) throws SQLException{
		//每次插入之前都清空数据库中原有的数据，所以不需要检测是否有相同记录，虽然这样好像比较大费周章
		sql = "delete from " + "AQnow" + " where location = '" + location + "';";
		statement.executeUpdate(sql);
		sql = "insert into " + "AQnow" + " values('" + location + "','" + AQI + "','" + quality + "','" +pm25 +"','" +update_time + "');";
		statement.executeUpdate(sql);
		return;
	}
	
	public void clear_AQnow(){
		try {
			statement.executeUpdate("delete from AQnow");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public static void main(String[] args){
		crawler database = new crawler();
		//打开包含监测点网址的文件
		File file = new File("cityname.txt");
		String htm;
		
		if(file.exists()){
			if(file.isFile()){
				database.clear_AQnow();
				
				try{           
					BufferedReader input = new BufferedReader (new FileReader(file));
					while((htm = input.readLine()) != null){
						try{
			            URL url = new URL(htm);
			            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
			 
			            String buf;
			            String update_time="";    //type:datetime
			            String location="";       //type:varchar(20)
			            String AQI="";            //type:smallint
			            String quality="";        //type:varchar(20)
			            String pm25="";           //type:varchar(10)  不能用smallint：因为有时候是-
			            String str="";
			            int start=0;
			            int begin,end,flag;
			            
			            while(!(null==(buf=in.readLine()))){
			            	//获取更新时间
			            	if((buf.indexOf("citydata_updatetime"))>0){
			            		begin=buf.indexOf("更新时间：")+5;
			            		end=buf.lastIndexOf("<");
			            		update_time=buf.substring(begin,end);
			            		System.out.println(update_time);
			            	}
			            	if((buf.indexOf("pj_area_data_item_active"))>0){
			            		start+=1;
			            	}
			            	if(start==2){
			            		break;
			            	}
			            	if(start==1){
			            		//获取监测点
			            		if(buf.indexOf("pjadt_location")>0){
			            			begin=buf.indexOf(">")+1;
			            			end=buf.lastIndexOf("<");//坑爹的千岛湖把我的<吞了
			            			//begin=buf.indexOf(">")+1;
			            			//end=buf.lastIndexOf("a")-2;
			            			location=buf.substring(begin,end);
			            			System.out.println(location);
			            			str+=location;
			            			str+=" ";
    					  
			            		}
			            		//获取AQI
			            		else if(buf.indexOf("pjadt_aqi")>0){
			            			begin=buf.indexOf(">")+1;
			            			end=buf.indexOf("<",begin);
			            			AQI=buf.substring(begin,end);
			            			System.out.println(AQI);
			            			str+=AQI;
			            			str+=" ";
			            		}
			            		//获取空气质量等级
			            		else if(buf.indexOf("pjadt_quality")>0){
			            			flag=buf.indexOf("pjadt_quality_bglevel_");//1优2良3轻度污染4中度污染5重度污染
			            			begin=buf.indexOf(">",flag)+1;
			            			end=buf.indexOf("<",begin);
			            			quality=buf.substring(begin,end);
			            			System.out.println(quality);
			            			str+=quality;
			            			str+=" ";
			            		}
			            		//获取pm2.5
			            		else if(buf.indexOf("pjadt_pm25")>0){
			            			begin=buf.indexOf(">")+1;
			            			end=buf.indexOf("<",begin);
			            			pm25=buf.substring(begin,end);
			            			System.out.println(pm25);
			            			str+=pm25;
			            			str+=" ";
			            			database.insert_history(location, AQI, quality, pm25,update_time);
			            			database.insert_now(location,AQI,quality,pm25,update_time);
			            		}
			            		str+="\n";
			            	}
			            }
			            System.out.println(str);
			            in.close();
						}catch(IOException ioException){
							System.err.println("File Error!" + ioException);
						}
					}
			        input.close();
				}catch(SQLException sqle){    //报错是顺序问题
					System.out.println("Error:" + sqle);
				}catch(Exception e){
					System.out.println("Error:" + e);
				}finally{
	   	    	 //conn.close(); //Cannot make a static reference to the non-static field conn
				}
			}
		}
	}
}

/*
爬杭州的数据时，发现了这个bug：美国标准的时候名字都是西溪

坑爹的千岛湖……千岛湖有时候的pm25是"-"，在APP中的显示要特别考虑，要么用特优符号表示，但是为了省麻烦我直接用AQI来做了，AQI总不可能是-的

delete from airquality where update_time<'2015-12-24 21:00'  //但是必须有后面的21:00 估计是把它当字符串处理了
因为有的地区显示的数据不是当天的，所以不能抓取时间来判定

File Error!java.io.IOException: Server returned HTTP response code: 500 for URL: http://www.pm25.com/city/kezilesuzhou.html
http://www.pm25.com/city/kezilesuzhou.html有点问题
绿色呼吸这个网站的锅，这锅我不背……
*/