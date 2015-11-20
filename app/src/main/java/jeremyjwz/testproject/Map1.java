package jeremyjwz.testproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

public class Map1 extends AppCompatActivity {

    private MapView mMapView = null;
    private BaiduMap baiduMap = null;
    private int circleNum = 100;
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

        addMarkerCicle();
        baiduMap.setOnMarkerClickListener(new MarkerShowInfo());
    }

    class MarkerShowInfo implements BaiduMap.OnMarkerClickListener{
        @Override
        public boolean onMarkerClick(final Marker marker) {
            Button button = new Button(getApplicationContext());
            button.setBackgroundResource(R.drawable.popup);
            Bundle info = marker.getExtraInfo();
            String location = info.getString("Location");
            String pm25 = info.getString("PM2.5");
            button.setText("Location:\n"+location+"\n"
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

    public void addMarkerCicle(){
        List<LatLng> circleCenters = new ArrayList<LatLng>();
        List<Integer> pm25s = new ArrayList<Integer>();
        BitmapDescriptor pmG0 = BitmapDescriptorFactory.fromAsset("pmg0.png");
        BitmapDescriptor pmG1 = BitmapDescriptorFactory.fromAsset("pmg1.png");
        BitmapDescriptor pmG2 = BitmapDescriptorFactory.fromAsset("pmg2.png");
        BitmapDescriptor pmG3 = BitmapDescriptorFactory.fromAsset("pmg3.png");
        BitmapDescriptor pmG4 = BitmapDescriptorFactory.fromAsset("pmg4.png");
        BitmapDescriptor pmG5 = BitmapDescriptorFactory.fromAsset("pmg5.png");
        //generate  the center randomly
        for(int i = 0;i<circleNum;i++){
            double lat,lgt;
            lat=Math.random()*1.3+29.2;
            lgt=Math.random()*1.15+119.35;
            LatLng p1= new LatLng(lat,lgt);
            circleCenters.add(p1);
        }
        //generate the PM2.5 randomly
        for (int i =0;i<circleNum;i++){
            int tmp = (int)(Math.random()*500);
            pm25s.add(tmp);
        }
        //create them and add them to the map
        for (int i =0;i<circleNum;i++){
            //get a marker and add it to the map
//            MarkerOptions item = new MarkerOptions()
//                    .position(circleCenters.get(i))
//                    .icon(pmG0)
//                    .zIndex(0)
//                    .perspective(false);
            MarkerOptions item;
            if(pm25s.get(i)<=50){
                item = new MarkerOptions()
                        .position(circleCenters.get(i))
                        .icon(pmG0)
                        .zIndex(0)
                        .perspective(false);}
            else if (pm25s.get(i)<=100){
                item = new MarkerOptions()
                        .position(circleCenters.get(i))
                        .icon(pmG1)
                        .zIndex(0)
                        .perspective(false);}
            else if (pm25s.get(i)<=150){
                item = new MarkerOptions()
                        .position(circleCenters.get(i))
                        .icon(pmG2)
                        .zIndex(0)
                        .perspective(false);
            }
            else if (pm25s.get(i)<=300){
                item = new MarkerOptions()
                        .position(circleCenters.get(i))
                        .icon(pmG3)
                        .zIndex(0)
                        .perspective(false);
            }
            else if (pm25s.get(i)<=500){
                item = new MarkerOptions()
                        .position(circleCenters.get(i))
                        .icon(pmG4)
                        .zIndex(0)
                        .perspective(false);
            }
            else {
                item = new MarkerOptions()
                        .position(circleCenters.get(i))
                        .icon(pmG5)
                        .zIndex(0)
                        .perspective(false);
            }
            Marker marker = (Marker) baiduMap.addOverlay(item);

            //add info to the marker
            String lat,lng;
            lat = circleCenters.get(i).latitude+"";
            lat = lat.substring(0,8);
            lng = circleCenters.get(i).longitude+"";
            lng = lng.substring(0,8);
            Bundle info = new Bundle();
            info.putString("Location","("+lat+","+lng+")");
            info.putString("PM2.5",pm25s.get(i).toString());
            marker.setExtraInfo(info);

            mMarkers.add(marker);

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
