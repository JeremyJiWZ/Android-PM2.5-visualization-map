package jeremyjwz.testproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Map1 extends AppCompatActivity {

    private MapView mMapView = null;
    private BaiduMap baiduMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map1);

        mMapView=(MapView)findViewById(R.id.map1);
        baiduMap=mMapView.getMap();

        addGraphCircle();

    }

    //paint the graph in the map
    public void addGraphCircle(){
        int circleNum = 100;
        List<LatLng> circleCenters = new ArrayList<LatLng>();
        List<Integer> circleColors = new ArrayList<Integer>();
        //add circle centers to the list
        //generate the circle randomly
        for(int i = 0;i<circleNum;i++){
            double lat,lgt;
            lat=Math.random()+39.0;
            lgt=Math.random()+116.0;
            LatLng p1= new LatLng(lat,lgt);
            circleCenters.add(p1);
        }
        //generate the color randomly
        for (int i=0;i<circleNum;i++){
            Integer color;
            color=new Integer((int)Math.random()*255);
            circleColors.add(color);
        }
        //add circle to the map
        for(int i=0;i<circleNum;i++){
            Log.e("Map1", "center: " + circleCenters.get(i) + " " +
                    "color: " + circleColors.get(i) +
                    "radius: " + 15);

            OverlayOptions aCircle = new CircleOptions()
                    .radius(1400).center(circleCenters.get(i))
                    .fillColor(0xff00ff00)
                    .stroke(new Stroke(5,0xaa000000))
                    ;
            baiduMap.addOverlay(aCircle);
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
