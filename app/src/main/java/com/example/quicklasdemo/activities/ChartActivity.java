package com.example.quicklasdemo.activities;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quicklasdemo.DatabaseHelper;
import com.example.quicklasdemo.R;
import com.example.quicklasdemo.data.Track;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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


        int num_tracks = tracks.size(); // Gets the Number of Tracks Inputed by User
        int num_curves = tracks.get(0).component2().size(); // Gets the Number of Curves in each Track

        LineChart[] mCharts = new LineChart[num_tracks]; // Sets the number of Charts

        if (num_tracks == 1) { // Takes Number of Tracks and Sets the Layout File *Resources are Compile Time Functions*
            setContentView(R.layout.line_chart);
            mCharts[0] = (LineChart) findViewById(R.id.chart1);
        } else if (num_tracks == 2) {
            setContentView(R.layout.double_chart);
            mCharts[0] = (LineChart) findViewById(R.id.chart1);
            mCharts[1] = (LineChart) findViewById(R.id.chart2);
        } else if (num_tracks == 3) {
            setContentView(R.layout.triple_chart);
            mCharts[0] = (LineChart) findViewById(R.id.chart1);
            mCharts[1] = (LineChart) findViewById(R.id.chart2);
            mCharts[2] = (LineChart) findViewById(R.id.chart3);
        }

        String Depth; // Seeks Curve Title for Depth for Y Value
        if (lasData.containsKey("DEPT")) {
            Depth = "DEPT";
        } else {
            Depth = "DEPTH";
        }


        for (int t = 0; t < num_tracks; t++){ // variable t = tracks in for loop

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();

            for (int c = 0; c < num_curves; c++) { // variable c = curves in for loop


                ArrayList<Entry> yVals = new ArrayList<>();


                int curve_size = lasData.get(tracks.get(0).component2().get(c).component1()).size();

                for (int x = 0; x < curve_size; x++) { // variable x = number of values for each curve

                    Float x_val = lasData.get(tracks.get(0).component2().get(c).component1()).get(x); // value from curve (x-axis)
                    Float y_val = lasData.get(Depth).get(x); // value from depth (y-axis)
                    Boolean isLinear = tracks.get(t).component4();

                    if (x_val == -999.2500){
                        System.out.println("invalid");
                    }
                    else if (isLinear) {
                        yVals.add(new Entry(scaleCbr(x_val), y_val)); // (Entry log(x), y)
                    }
                    else {
                        yVals.add(new Entry(x_val, y_val)); // (Entry x, y)
                    }
                }



                float num_values = lasData.get(tracks.get(0).component2().get(c).component1()).size();

                //System.out.println(num_values);

                String curve_name = tracks.get(0).component2().get(c).component1();

                String lineStyle = tracks.get(0).component2().get(c).component2(); // user selected Line Style from settings
                String curveColor = tracks.get(0).component2().get(c).component3(); // user selected Curve Color from settings
                float scaleMin = tracks.get(0).component2().get(c).component4(); // user selected Scale Min from settings
                float scaleMax = tracks.get(0).component2().get(c).component5(); // user selected Line Style from settings


                LineDataSet set1 = new LineDataSet(yVals, curve_name);

                switch (lineStyle) { // Setting Line Style
                    case "Normal":
                        set1.setLineWidth(1f);
                        break;
                    case "Dotted":
                        set1.enableDashedLine(2f, 2f, 2f);
                        break;
                    case "Bold":
                        set1.setLineWidth(2f);
                        break;
                }

                switch (curveColor) { // Setting Curve Color
                    case "FF0000":
                        set1.setColor(Color.RED);
                        break;
                    case "0000FF":
                        set1.setColor(Color.BLUE);
                        break;
                    case "00FF00":
                        set1.setColor(Color.GREEN);
                        break;
                }

                set1.setDrawValues(true);
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                set1.setCubicIntensity(0.2f);
                System.out.println("set >>>>>>>" + set1);
                dataSets.add(set1);

            }

        Boolean showGrid = tracks.get(0).component3();
        Boolean isLinear = tracks.get(0).component4();
        int verticalDivCount = tracks.get(0).component5();
        int horizontalDivHeight = tracks.get(0).component6();

        mCharts[t].getDescription().setEnabled(false);
        mCharts[t].setDrawGridBackground(showGrid);

        LineData data = new LineData(dataSets);


        mCharts[t].setData(data);

        //mCharts[t].setViewPortOffsets(120f,60f,0f,0f);
        mCharts[t].setDrawBorders(false);
        //mCharts[t].getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        //mCharts[t].getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        //mCharts[t].getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        //mCharts[t].setVisibleYRangeMinimum(200, YAxis.AxisDependency.RIGHT);
        mCharts[t].getLegend().setDrawInside(false);

        //log value
        if (isLinear) {
            mCharts[t].getAxisRight().setAxisMinimum(scaleCbr(0.001));
            mCharts[t].getAxisRight().setAxisMaximum(scaleCbr(100.0));
            mCharts[t].getAxisRight().setLabelCount(6, true);
            System.out.println("islinear");
        }




        //mCharts[t].getAxisRight().setAxisMinimum(-5f);
        //mCharts[t].getAxisRight().setAxisMaximum(105f);
        mCharts[t].setAutoScaleMinMaxEnabled(true);

        //mCharts[t].getAxisRight().setAxisMinimum(0f);
        //mCharts[t].getAxisLeft().setAxisMaximum(5000);
        mCharts[t].setBackgroundColor(Color.DKGRAY);
        mCharts[t].getAxisLeft().setTextColor(Color.WHITE);
        mCharts[t].getXAxis().setTextColor(Color.WHITE);
        mCharts[t].getLegend().setTextColor(Color.WHITE);
        mCharts[t].getDescription().setTextColor(Color.WHITE);
        mCharts[t].setDrawGridBackground(false); // Makes Graph Background Transparent to show Layout Background Color
        mCharts[t].invalidate();

            }
        }

    private float scaleCbr(double cbr) {
        return (float)(Math.log10(cbr));
    }

    private float unScaleCbr(double cbr) {
        double calcVal = Math.pow(10, cbr);
        return (float)(calcVal);
    }
}





