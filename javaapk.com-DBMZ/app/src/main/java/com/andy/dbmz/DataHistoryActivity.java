package com.andy.dbmz;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_history);

        lineChartInit();

    }

    private void lineChartInit(){
        //在xml中定义的视图
        chart=(LineChartView)findViewById(R.id.lineChart);
        int randomData;

        //给linechart设置初始值
        ArrayList<PointValue> pointValues = new ArrayList<PointValue>();
        ArrayList<AxisValue> axisValues = new ArrayList<AxisValue>();
        for (int i=0;i<10;++i){
            randomData = new Random().nextInt(10);
            pointValues.add(new PointValue(i, randomData));
            axisValues.add(new AxisValue(i).setLabel(""+(i+1)));
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


}
