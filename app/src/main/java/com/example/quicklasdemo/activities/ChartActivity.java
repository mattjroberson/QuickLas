package com.example.quicklasdemo.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quicklasdemo.CoupleChartGestureListener;
import com.example.quicklasdemo.DatabaseHelper;
import com.example.quicklasdemo.R;
import com.example.quicklasdemo.data.Curve;
import com.example.quicklasdemo.data.Track;
import com.example.quicklasdemo.labels.LabelContainer;
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

        int num_tracks = excludeEmpty(tracks);

        LineChart[] mpCharts = new LineChart[num_tracks];
        LabelContainer[] labelContainers = new LabelContainer[num_tracks];

        switch (num_tracks) { // Takes Number of Tracks and Sets the Layout File *Resources are Compile Time Functions*
            case 1:
                setContentView(R.layout.chart_single);
                mpCharts[0] = findViewById(R.id.chart1);
                labelContainers[0] = new LabelContainer(findViewById(R.id.chart1Labels));
                break;
            case 2:
                setContentView(R.layout.chart_double);
                mpCharts[0] = findViewById(R.id.chart2);
                mpCharts[1] = findViewById(R.id.chart1);
                labelContainers[0] = new LabelContainer(findViewById(R.id.chart2Labels));
                labelContainers[1] = new LabelContainer(findViewById(R.id.chart1Labels));
                break;
        }

        String depth_name = GetDepthName(lasData);
        List<Float> depth_data = lasData.get(depth_name);

        if(mpCharts.length > 1){
            mpCharts[0].setOnChartGestureListener(new CoupleChartGestureListener(
                    mpCharts[0], new Chart[] { mpCharts[1] }));
            mpCharts[1].setOnChartGestureListener(new CoupleChartGestureListener(
                    mpCharts[1], new Chart[] { mpCharts[0] }));
        }

        for (int trackIndex = 0; trackIndex < num_tracks; trackIndex++) { // variable t = tracks in for loop

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();

            Track track = tracks.get(trackIndex);
            List<Curve> curves = track.getCurveList();

            //Track Properties
            boolean showGrid = track.getShowGrid();
            boolean isLinear = track.isLinear();
            int verticalDivCount = track.getVerticalDivCount();

            for (int c = 0; c < curves.size(); c++) { // variable c = curves in for loop
                ArrayList<Entry> points = new ArrayList<>();

                Curve curve = curves.get(c);
                String curve_name = curve.getCurveName();
                String lineStyle = curve.getLineStyle(); // user selected Line Style from settings
                String curveColor = curve.getCurveColor(); // user selected Curve Color from settings

                List<Float> curve_data = lasData.get(curve_name);
                float scaleMin = curve.getScaleMin(); // user selected Scale Min from settings
                float scaleMax = curve.getScaleMax(); // user selected Line Style from settings

                if(!isLinear){
                    scaleMax = scaleCbr(scaleMax);
                    scaleMin = scaleCbr(scaleMin);
                }

                //Assign the Curve Info to the Track Label
                labelContainers[trackIndex].getLabel(c).setData(scaleMin, scaleMax, curve_name, curveColor);
                labelContainers[trackIndex].setTrackName(track.getTrackName());

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
            ConfigureChart(mpCharts, data, trackIndex, showGrid, verticalDivCount);

        }

        mpCharts[mpCharts.length-1].getXAxis().setDrawLabels(true);
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
        //Axis Setup
        mCharts[currChart].getAxisLeft().setAxisMinimum(0);
        mCharts[currChart].getAxisLeft().setAxisMaximum(1);
        mCharts[currChart].getAxisLeft().setLabelCount(vertDivCount);
        mCharts[currChart].getAxisLeft().setDrawLabels(false);

        mCharts[currChart].getAxisRight().setEnabled(false);

        mCharts[currChart].getXAxis().setTextColor(Color.WHITE);
        mCharts[currChart].getXAxis().setDrawLabels(false);

        //Show grid settings option
        mCharts[currChart].getAxisLeft().setDrawGridLines(showGrid);
        mCharts[currChart].getXAxis().setDrawGridLines(showGrid);

        //Apply Data
        mCharts[currChart].setData(data);

        //Viewport setup
        mCharts[currChart].setVisibleXRangeMaximum(500);
        mCharts[currChart].setDrawGridBackground(false);
        mCharts[currChart].setDrawBorders(false);
        mCharts[currChart].getDescription().setEnabled(false);
        mCharts[currChart].setScaleEnabled(false);
        mCharts[currChart].getLegend().setEnabled(false);
        mCharts[currChart].setBackgroundColor(Color.DKGRAY);
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

    //Runs through tracklist and counts the ones that have data in them
    private int excludeEmpty(List<Track> tracks){
        int trackCount = 0;
        for(Track track : tracks){
            if(track.getCurveList().size() > 0){
                trackCount++;
            }
        }
        return trackCount;
    }
}





