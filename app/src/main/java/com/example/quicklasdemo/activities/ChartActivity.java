package com.example.quicklasdemo.activities;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quicklasdemo.DatabaseHelper;
import com.example.quicklasdemo.R;
import com.example.quicklasdemo.data.Track;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ChartActivity extends AppCompatActivity {

    private static final String TAG = "ChartActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //adapting to full screen

        DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());
        String lasName = getIntent().getStringExtra("lasName");
        Map<String, List<Float>> lasData = db.getLasData(lasName);
        List<Track> tracks = db.getTrackList(lasName);

        int num_curves = tracks.get(0).component2().size();
        LineChart[] mCharts = new LineChart[num_curves];

        System.out.println(num_curves);

        if (num_curves == 1) {
            setContentView(R.layout.line_chart);
            mCharts[0] = (LineChart) findViewById(R.id.chart1);
        }
        else if (num_curves == 2){
            setContentView(R.layout.double_chart);
            mCharts[0] = (LineChart) findViewById(R.id.chart1);
            mCharts[1] = (LineChart) findViewById(R.id.chart2);
        }
        else if (num_curves == 3){
            setContentView(R.layout.triple_chart);
            mCharts[0] = (LineChart) findViewById(R.id.chart1);
            mCharts[1] = (LineChart) findViewById(R.id.chart2);
            mCharts[2] = (LineChart) findViewById(R.id.chart3);
        }

        for (int i = 0; i < num_curves; i++){
            ArrayList<Entry> yVals = new ArrayList<>();
            for(int x = 0; x < lasData.get(tracks.get(0).component2().get(i).component1()).size(); x++) {
                yVals.add(new Entry(lasData.get(tracks.get(0).component2().get(0).component1()).get(x),lasData.get("DEPT").get(x)));
            }



            String curveName = tracks.get(0).component2().get(i).component1();
            String lineStyle = tracks.get(0).component2().get(i).component2();
            String curveColor = tracks.get(0).component2().get(i).component3();
            float scaleMin = tracks.get(0).component2().get(i).component4();
            float scaleMax = tracks.get(0).component2().get(i).component5();

            LineDataSet set1 = new LineDataSet(yVals, curveName);

            switch (lineStyle) {
                case "Normal":
                    set1.setLineWidth(1);
                    break;
                case "Dotted":
                    set1.enableDashedLine(1, 1, 1);
                    break;
                case "Bold":
                    set1.setLineWidth(4);
                    break;
            }

            switch (curveColor) {
                case "Red":
                    set1.setColor(Color.RED);
                    break;
                case "Blue":
                    set1.setColor(Color.BLUE);
                    break;
                case "Green":
                    set1.setColor(Color.GREEN);
                    break;
            }

            Boolean showGrid = tracks.get(0).component3();
            Boolean isLinear = tracks.get(0).component4();
            int verticalDivCount = tracks.get(0).component5();
            int horizontalDivHeight = tracks.get(0).component6();

            set1.setColor(Color.RED);
            set1.setDrawValues(false);
            LineData data = new LineData(set1);
            mCharts[i].isLogEnabled();
            mCharts[i].getDescription().setEnabled(false);
            mCharts[i].setDrawGridBackground(showGrid);
            mCharts[i].setData(data);
        }

    }
}




