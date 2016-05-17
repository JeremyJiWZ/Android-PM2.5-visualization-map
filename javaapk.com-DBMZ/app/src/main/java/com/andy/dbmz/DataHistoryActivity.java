package com.andy.dbmz;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class DataHistoryActivity extends AppCompatActivity {

    //variables for line chart test
    private LineChartView chart;
    private LineChartData data = new LineChartData();
    private List<Line> lines = new ArrayList<Line>();
    private List<Integer> pm25s = new ArrayList<Integer>();
    private List<String> dates = new ArrayList<String>();

    private String url="http://10.180.64.30:8888/?";
    private String checkpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_history);

        Intent intent = getIntent();
        checkpoint = intent.getExtras().getString("checkpoint");
        url = url+checkpoint;


        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(checkpoint);
//        lineChartInit();
        new GetDataFromServer().execute(url);

    }

    private void lineChartInit(){
        //在xml中定义的视图
        chart=(LineChartView)findViewById(R.id.lineChart);
        int pm25;
        String date;

        //给linechart设置初始值
        ArrayList<PointValue> pointValues = new ArrayList<PointValue>();
        ArrayList<AxisValue> axisValues = new ArrayList<AxisValue>();
        for (int i=0;i<10;++i){
            pm25 = pm25s.get(i);
            date = dates.get(i);
            pointValues.add(new PointValue(i, pm25));
            axisValues.add(new AxisValue(i).setLabel(date));
        }
        Line line = new Line(pointValues).setColor(Color.BLUE).setCubic(false);
        lines.add(line);

        data.setLines(lines);

        //设置坐标轴
        Axis axisX = new Axis(); //X Axis
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.CYAN);
        axisX.setName("日期");
        axisX.setMaxLabelChars(10);
        axisX.setValues(axisValues);
        data.setAxisXBottom(axisX);

        Axis axisY = new Axis();//Y axis
        axisY.setHasTiltedLabels(true);
        axisY.setTextColor(Color.CYAN);
        axisY.setName("PM2.5");
        axisY.setMaxLabelChars(5);
        data.setAxisYLeft(axisY);

        chart.setInteractive(true);
        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.setLineChartData(data);
        chart.setVisibility(View.VISIBLE);
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in =null;
        int response =-1;
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        try{
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.connect();
            response = httpURLConnection.getResponseCode();
            if(response==HttpURLConnection.HTTP_OK)
                in = httpURLConnection.getInputStream();
        }
        catch (Exception ex){
            Log.e("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }

    private String DownloadText(String URL){
        int BUFFER_SIZE=2000;
        InputStream in =null;
        try{
            in = OpenHttpConnection(URL);
        }
        catch (IOException ex){
            Log.e("Networking", ex.getLocalizedMessage());
            return "";
        }
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str="";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try{
            while((charRead=isr.read(inputBuffer))>0){
                String readString = String.copyValueOf(inputBuffer,0,charRead);
                str+=readString;
                inputBuffer=new char[BUFFER_SIZE];

            }
        }catch (IOException ex){
            Log.e("Networking", ex.getLocalizedMessage());
            return "";
        }
        return str;
    }

    private class GetDataFromServer extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
//            Log.e("Error", "downloading..:" + result);
            try {
                JSONObject json = new JSONObject(result);
                Log.e("Map2", "" + json.getBoolean("status"));
                if (json.getBoolean("status")) {
                    setData(json.getJSONArray("data"));
                } else {
                    Log.e("Error", "status false");
                }
            } catch (JSONException e) {
                Log.e("Map2", e.getMessage());
            }
        }
    }

    private void setData(JSONArray jsonData) throws JSONException{
        Integer pm2_5;
        String date;
        JSONObject json;
        for (int i=0;i<jsonData.length();i++){
            json = jsonData.getJSONObject(i);

            pm2_5 = new Integer(json.getString("pm25"));
            date = json.getString("time");
            date = date.substring(5, 9);
            pm25s.add(pm2_5);
            dates.add(date);
        }
        lineChartInit();
    }

}
