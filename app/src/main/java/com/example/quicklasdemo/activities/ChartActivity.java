package com.example.quicklasdemo.activities;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quicklasdemo.DatabaseHelper;
import com.example.quicklasdemo.R;
import com.example.quicklasdemo.data.Curve;
import com.example.quicklasdemo.data.Track;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {

    private static final String TAG = "ChartActivity";
    private static final String[] DEPTH_NAMES =  {"DEPTH", "DEPT", "Depth", "Dept"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //adapting to full screen

        DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());
        String lasName = getIntent().getStringExtra("lasName");
        Map<String, List<Float>> lasData = db.getLasData(lasName);
        List<Track> tracks = db.getTrackList(lasName);

        int num_curves = tracks.get(0).component2().size();
        LineChart[] mCharts = new LineChart[num_curves];

        if (num_curves == 1) {
            setContentView(R.layout.line_chart);
            mCharts[0] = (LineChart) findViewById(R.id.chart1);
        } else if (num_curves == 2) {
            setContentView(R.layout.double_chart);
            mCharts[0] = (LineChart) findViewById(R.id.chart1);
            mCharts[1] = (LineChart) findViewById(R.id.chart2);
        } else if (num_curves == 3) {
            setContentView(R.layout.triple_chart);
            mCharts[0] = (LineChart) findViewById(R.id.chart1);
            mCharts[1] = (LineChart) findViewById(R.id.chart2);
            mCharts[2] = (LineChart) findViewById(R.id.chart3);
        }

        String Depth = GetDepthName(lasData);

        //Track Properties
        Track track = tracks.get(0);

        Boolean showGrid = track.component3();
        Boolean isLinear = track.component4();
        int verticalDivCount = track.component5();
        int horizontalDivHeight = track.component6();

        //Curve Properties
        List<Curve> curveList = track.component2();
        List<Float> depth = lasData.get(Depth);
        Log.i("TEST", String.valueOf(depth.size()));

        for (int curveIndex = 0; curveIndex < curveList.size(); curveIndex++) {
            ArrayList<Entry> points = new ArrayList<>();
            Curve curve = curveList.get(curveIndex);

            String curveName = curve.component1();
            List<Float> curvePoints = lasData.get(curveName);

            for (int x = 0; x < curvePoints.size(); x++) {
                Entry point = new Entry(curvePoints.get(x),
                        depth.get(x));
                points.add(point);
            }

            //float num_values = lasData.get(tracks.get(0).component2().get(i).component1()).size();

            String lineStyle = curve.component2();
            String curveColor = curve.component3();
            float scaleMin = curve.component4();
            float scaleMax = curve.component5();

            LineDataSet set1 = new LineDataSet(points, curveName);
            SetCurveStyle(set1, lineStyle);
            SetCurveColor(set1, curveColor);

            set1.setDrawValues(true);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);

            LineData data = new LineData(set1);

            ConfigureChart(mCharts, data, curveIndex);


        }
    }

    private void SetCurveStyle(LineDataSet data, String lineStyle) {
        switch (lineStyle) {
            case "Normal":
                data.setLineWidth(1f);
                break;
            case "Dotted":
                data.enableDashedLine(2f, 2f, 2f);
                break;
            case "Bold":
                data.setLineWidth(2f);
                break;
        }
    }

    private void SetCurveColor(LineDataSet data, String curveColor) {
        switch (curveColor) {
            case "FF0000":
                data.setColor(Color.RED);
                break;
            case "0000FF":
                data.setColor(Color.BLUE);
                break;
            case "00FF00":
                data.setColor(Color.GREEN);
                break;
        }
    }

    private void ConfigureChart(LineChart[] mCharts, LineData data, int currChart){
        mCharts[currChart].getDescription().setEnabled(false);
        //mCharts[currChart].setDrawGridBackground(showGrid);

        mCharts[currChart].setData(data);

        mCharts[currChart].setViewPortOffsets(120f, 60f, 0f, 0f);
        //mCharts[currChart].getXAxis().setAxisMinimum(-5f);
        //mCharts[currChart].getXAxis().setAxisMaximum(105f);
        mCharts[currChart].getAxisRight().setAxisMinimum(0f);
        mCharts[currChart].getAxisRight().setAxisMaximum(100f);
        mCharts[currChart].setBackgroundColor(Color.DKGRAY);
        mCharts[currChart].getAxisLeft().setTextColor(Color.WHITE);
        mCharts[currChart].getXAxis().setTextColor(Color.WHITE);
        mCharts[currChart].getLegend().setTextColor(Color.WHITE);
        mCharts[currChart].getDescription().setTextColor(Color.WHITE);
        mCharts[currChart].setDrawGridBackground(false);
        mCharts[currChart].invalidate();
    }

    // Initalizing Depth for Different LAS files
    private String GetDepthName(Map<String, List<Float>> lasData){
        for(String depthOption : DEPTH_NAMES){
            if (lasData.containsKey(depthOption)) {
                return depthOption;
            }
        }

        System.exit(-1);
        return null;
    }
}





