package com.andy.dbmz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

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

/**
 * Created by jiwentadashi on 16/5/15.
 */
public class MapFragment extends android.support.v4.app.Fragment {


    private MapView mMapView;
    private BaiduMap baiduMap = null;
    private List<Marker> mMarkers = new ArrayList<Marker>();



    public static MapFragment newInstance(){
        MapFragment fragment = new MapFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView)view.findViewById(R.id.map1);
        baiduMap=mMapView.getMap();

        //改变地图位置参数

        MapStatus mapStatus = new MapStatus.Builder()
                .target(new LatLng(30.270651,120.130983))
                .zoom(12)
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.setMapStatus(mapStatusUpdate);

        new GetDataFromServer().execute("http://10.180.64.30:8000/");
        baiduMap.setOnMarkerClickListener(new MarkerShowInfo());

        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    class MarkerShowInfo implements BaiduMap.OnMarkerClickListener{
        @Override
        public boolean onMarkerClick(final Marker marker) {
            Button button = new Button(getActivity().getApplicationContext());
            button.setBackgroundResource(R.drawable.popup);
            Bundle info = marker.getExtraInfo();
            String pm25 = info.getString("PM2.5");
            final String checkpoint = info.getString("Checkpoint");
            button.setText("监测站:"+checkpoint+"\n"
                    +"PM2.5: "+pm25);
            button.setTextColor(0xff000000);
            InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {
                    baiduMap.hideInfoWindow();

                    Bundle extraInfos = new Bundle();
                    extraInfos.putString("checkpoint", checkpoint);

                    Intent intent =new Intent();
                    intent.putExtras(extraInfos);
                    intent.setClass(getActivity(),DataHistoryActivity.class);

                    startActivity(intent);
                }
            };
            InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button),
                    marker.getPosition(),-40,listener);
            baiduMap.showInfoWindow(mInfoWindow);
            return true;
        }
    }

    public void drawMarkerCicle(LatLng latLng,String checkpoint,Double pm2_5){
        BitmapDescriptor pmG0 = BitmapDescriptorFactory.fromAsset("pmg0.png");
        BitmapDescriptor pmG1 = BitmapDescriptorFactory.fromAsset("pmg1.png");
        BitmapDescriptor pmG2 = BitmapDescriptorFactory.fromAsset("pmg2.png");
        BitmapDescriptor pmG3 = BitmapDescriptorFactory.fromAsset("pmg3.png");
        BitmapDescriptor pmG4 = BitmapDescriptorFactory.fromAsset("pmg4.png");
        BitmapDescriptor pmG5 = BitmapDescriptorFactory.fromAsset("pmg5.png");

        MarkerOptions item;
        if(pm2_5<=50){
            item = new MarkerOptions()
                    .position(latLng)
                    .icon(pmG0)
                    .zIndex(0)
                    .perspective(false);}
        else if (pm2_5<=100){
            item = new MarkerOptions()
                    .position(latLng)
                    .icon(pmG1)
                    .zIndex(0)
                    .perspective(false);}
        else if (pm2_5<=150){
            item = new MarkerOptions()
                    .position(latLng)
                    .icon(pmG2)
                    .zIndex(0)
                    .perspective(false);
        }
        else if (pm2_5<=200){
            item = new MarkerOptions()
                    .position(latLng)
                    .icon(pmG3)
                    .zIndex(0)
                    .perspective(false);
        }
        else if (pm2_5<=500){
            item = new MarkerOptions()
                    .position(latLng)
                    .icon(pmG4)
                    .zIndex(0)
                    .perspective(false);
        }
        else {
            item = new MarkerOptions()
                    .position(latLng)
                    .icon(pmG5)
                    .zIndex(0)
                    .perspective(false);
        }
        Marker marker = (Marker) baiduMap.addOverlay(item);

        //add info to the marker
        String lat,lng;
        lat = latLng.latitude+"";
        lat = lat.substring(0,8);
        lng = latLng.longitude+"";
        lng = lng.substring(0,8);
        Bundle info = new Bundle();
        info.putString("Location","("+lat+","+lng+")");
        info.putString("PM2.5",pm2_5.toString());
        info.putString("Checkpoint",checkpoint);
        marker.setExtraInfo(info);
        mMarkers.add(marker);

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
                    drawMarkers(json.getJSONArray("data"));
                } else {
                    Log.e("Error", "status false");
                }
            } catch (JSONException e) {
                Log.e("Map2", e.getMessage());
            }
        }
    }

    private void drawMarkers(JSONArray jsonArray) throws JSONException{
        JSONObject json;
        LatLng latLng;
        Double pm2_5;
        String checkpoint;
        for (int i = 0; i < jsonArray.length(); ++i) {
            json = jsonArray.getJSONObject(i);
            String pm25orNone = json.getString("pm25").trim();
            if (pm25orNone.contains("—")) {
                continue;
            }

            Double lng = new Double(json.getString("long"));
            Double lat = new Double(json.getString("lat"));
            latLng = new LatLng(lat,lng);
            pm2_5 = new Double(json.getString("pm25").trim());
            checkpoint = json.getString("checkpoint");
            drawMarkerCicle(latLng,checkpoint,pm2_5);
        }
    }



}
