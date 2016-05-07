package jeremyjwz.testproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

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

public class Map1 extends AppCompatActivity {

    private MapView mMapView = null;
    private BaiduMap baiduMap = null;
    private List<Marker> mMarkers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map1);

        mMapView=(MapView)findViewById(R.id.map1);
        baiduMap=mMapView.getMap();

        //改变地图位置参数

        MapStatus mapStatus = new MapStatus.Builder()
                .target(new LatLng(30.270651,120.130983))
                .zoom(12)
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.setMapStatus(mapStatusUpdate);

//        new GetDataFromServer().execute("http://10.214.149.168:8888/server.php?action=checkpoints");
//        baiduMap.setOnMarkerClickListener(new MarkerShowInfo());
    }

    class MarkerShowInfo implements BaiduMap.OnMarkerClickListener{
        @Override
        public boolean onMarkerClick(final Marker marker) {
            Button button = new Button(getApplicationContext());
            button.setBackgroundResource(R.drawable.popup);
            Bundle info = marker.getExtraInfo();
            String location = info.getString("Location");
            String pm25 = info.getString("PM2.5");
            String checkpoint = info.getString("Checkpoint");
            button.setText("Checkpoint:"+checkpoint+"\nLocation:\n"+location+"\n"
            +"PM2.5: "+pm25);
            button.setTextColor(0xff000000);
            InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {
                    baiduMap.hideInfoWindow();
                }
            };
            InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button),
                    marker.getPosition(),-40,listener);
            baiduMap.showInfoWindow(mInfoWindow);
            return true;
        }
    }
    //paint the graph in the map
//    public void addGraphCircle(){
//        int circleNum = 1000;
//        List<LatLng> circleCenters = new ArrayList<LatLng>();
//        List<Integer> circleColors = new ArrayList<Integer>();
////        List<InfoWindow> mInfoWindows = new ArrayList<InfoWindow>();
//        //add circle centers to the list
//        //generate the circle randomly
//        for(int i = 0;i<circleNum;i++){
//            double lat,lgt;
//            lat=Math.random()*1.3+29.2;
//            lgt=Math.random()*1.15+119.35;
//            LatLng p1= new LatLng(lat,lgt);
//            circleCenters.add(p1);
//        }
//        //generate the color randomly
//        for (int i=0;i<circleNum;i++){
//            Integer color;
//            color=new Integer((int)(Math.random()*255*255*255));
//            color=color+0xff000000;
//            circleColors.add(color);
//        }
//        //add circle to the map
//        for(int i=0;i<circleNum;i++){
////            Log.e("Map1", "center: " + circleCenters.get(i) + " " +
////                    "color: " + circleColors.get(i) +
////                    "radius: " + 15);
//            OverlayOptions aCircle = new CircleOptions()
//                    .radius(200).center(circleCenters.get(i))
//                    .fillColor(circleColors.get(i))
//                    .stroke(new Stroke(5,circleColors.get(i)))
//                    ;
////            mInfoWindows.get(i)= new InfoWindow(aCircle,circleCenters.get(i),5);
////            baiduMap.showInfoWindow(mInfoWindows.get(i));
//            baiduMap.addOverlay(aCircle);
//        }
//    }
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
        else if (pm2_5<=300){
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
            Log.e("Error", "downloading..:" + result);
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
            Double lng = new Double(json.getString("longitude"));
            Double lat = new Double(json.getString("latitude"));
            latLng = new LatLng(lat,lng);
            pm2_5 = new Double(json.getString("pm2_5"));
            checkpoint = json.getString("checkpoint");
            drawMarkerCicle(latLng,checkpoint,pm2_5);
        }
    }
    /**
     * initialize the method
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}
