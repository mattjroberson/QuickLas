package com.example.quicklasdemo.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quicklasdemo.CoupleChartGestureListener;
import com.example.quicklasdemo.DatabaseHelper;
import com.example.quicklasdemo.R;
import com.example.quicklasdemo.data.Curve;
import com.example.quicklasdemo.data.Track;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {

    private static final String[] DEPTH_NAMES = {"DEPTH", "DEPT", "Depth", "Dept"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //adapting to full screen

        DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());
        String lasName = getIntent().getStringExtra("lasName");

        Map<String, List<Float>> lasData = db.getLasData(lasName);
        List<Track> tracks = db.getTrackList(lasName);

        int num_tracks = tracks.size(); // Gets the Number of Tracks Inputed by User

        LineChart[] mCharts = new LineChart[num_tracks]; // Sets the number of Charts

        switch (num_tracks) { // Takes Number of Tracks and Sets the Layout File *Resources are Compile Time Functions*
            case 1:
                setContentView(R.layout.line_chart);
                mCharts[0] = findViewById(R.id.chart1);
                break;
            case 2:
                setContentView(R.layout.double_chart);
                mCharts[0] = findViewById(R.id.chart1);
                mCharts[1] = findViewById(R.id.chart2);
                break;
        }

        String depth_name = GetDepthName(lasData);
        List<Float> depth_data = lasData.get(depth_name);

        if(mCharts.length > 1){
            mCharts[0].setOnChartGestureListener(new CoupleChartGestureListener(
                    mCharts[0], new Chart[] { mCharts[1] }));
            mCharts[1].setOnChartGestureListener(new CoupleChartGestureListener(
                    mCharts[1], new Chart[] { mCharts[0] }));
        }

        for (int trackIndex = 0; trackIndex < num_tracks; trackIndex++) { // variable t = tracks in for loop

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();

            Track track = tracks.get(trackIndex);
            List<Curve> curves = track.getCurveList();

            //Track Properties
            boolean showGrid = track.getShowGrid();
            boolean isLinear = track.isLinear();

            int verticalDivCount = track.getVerticalDivCount();
            int horizontalDivHeight = track.getHorizontalDivHeight();

            for (int c = 0; c < curves.size(); c++) { // variable c = curves in for loop
                ArrayList<Entry> points = new ArrayList<>();

                Curve curve = curves.get(c);
                String curve_name = curve.getCurveName();

                List<Float> curve_data = lasData.get(curve_name);
                float scaleMin = curve.getScaleMin(); // user selected Scale Min from settings
                float scaleMax = curve.getScaleMax(); // user selected Line Style from settings

                if(!isLinear){
                    scaleMax = scaleCbr(scaleMax);
                    scaleMin = scaleCbr(scaleMin);
                }

                for (int x = 0; x < curve_data.size(); x++) { // variable x = number of values for each curve

                    float x_val = curve_data.get(x); // value from curve (x-axis)
                    float y_val = depth_data.get(x); // value from depth (y-axis)

                    if (x_val != -999.2500) {
                        if (!isLinear){
                            x_val = scaleCbr(x_val);
                        }

                        x_val = (x_val - scaleMin) / (scaleMax-scaleMin);
                        points.add(new Entry(y_val, x_val));
                    }
                }

                Collections.sort(points, new EntryXComparator());

                String lineStyle = curve.getLineStyle(); // user selected Line Style from settings
                String curveColor = curve.getCurveColor(); // user selected Curve Color from settings

                LineDataSet set1 = new LineDataSet(points, curve_name);

                SetCurveColor(set1, curveColor);
                SetCurveStyle(set1, lineStyle);

                set1.setLineWidth(2f);
                set1.setDrawCircles(false);
                set1.setDrawHighlightIndicators(false);
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                dataSets.add(set1);
            }

            LineData data = new LineData(dataSets);
            ConfigureChart(mCharts, data, trackIndex, showGrid, verticalDivCount);

        }

        mCharts[0].getXAxis().setDrawLabels(true);
    }

    private void SetCurveStyle(LineDataSet data, String lineStyle) {
        switch (lineStyle) {
            case "Dotted":
                data.enableDashedLine(10f, 10f, 0f);
                break;
            case "Bold":
                data.setLineWidth(3f);
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

    private void ConfigureChart(LineChart[] mCharts, LineData data, int currChart, boolean showGrid, int vertDivCount) {
        mCharts[currChart].getAxisLeft().setAxisMinimum(0);
        mCharts[currChart].getAxisLeft().setAxisMaximum(1);
        mCharts[currChart].getAxisLeft().setLabelCount(vertDivCount);
        //mCharts[currChart].getAxisLeft().setDrawLabels(false);
        mCharts[currChart].getAxisRight().setEnabled(false);
        mCharts[currChart].getXAxis().setDrawLabels(false);

        mCharts[currChart].setData(data);

        mCharts[currChart].setVisibleXRangeMaximum(1000);

        mCharts[currChart].setDrawGridBackground(false);
        mCharts[currChart].setDrawBorders(false);
        mCharts[currChart].setScaleEnabled(false);
        mCharts[currChart].getLegend().setEnabled(false);

        //Show grid settings option
        mCharts[currChart].getAxisLeft().setDrawGridLines(showGrid);
        mCharts[currChart].getXAxis().setDrawGridLines(showGrid);
        Log.i("TEST", String.valueOf(showGrid));


        mCharts[currChart].setBackgroundColor(Color.DKGRAY);
        mCharts[currChart].getAxisLeft().setTextColor(Color.WHITE);
        //mCharts[currChart].getXAxis().setTextColor(Color.WHITE);

        mCharts[currChart].animateX(500);

        mCharts[currChart].notifyDataSetChanged();
        mCharts[currChart].invalidate();
    }

    // Initalizing Depth for Different LAS files
    private String GetDepthName(Map<String, List<Float>> lasData) {
        for (String depthOption : DEPTH_NAMES) {
            if (lasData.containsKey(depthOption)) {
                return depthOption;
            }
        }

        System.exit(-1);
        return null;
    }

    private float scaleCbr(float cbr) { //scales the values on x for linear
        return (float) Math.log10(cbr);
    }
}





