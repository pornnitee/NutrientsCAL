package com.example.starius.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by STARIUS on 12/25/2017.
 */

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        showInfo();

        HorizontalBarChart chart = (HorizontalBarChart) findViewById(R.id.bar_chart);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        data.setValueTextSize(10);
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(2000, 2000);
        chart.invalidate();

        HorizontalBarChart chart2 = (HorizontalBarChart) findViewById(R.id.bar_chart2);
        BarData data2 = new BarData(getXAxisValues2(), getDataSet2());
        data2.setValueTextSize(10);
        chart2.setData(data2);
        chart2.setDescription("");
        chart2.animateXY(2000, 2000);
        chart2.invalidate();
    }

    String strProtein = "";
    String strFat = "";
    String strCarbohydrate = "";
    String strCalcium = "";
    String strIron = "";

    private void showInfo() {
        String url = URLs.HOST_URL + "ChartActivity.php";
        User user = SharedPrefManager.getInstance(this).getUser();
        Intent intent= getIntent();
        final String DateHistory = intent.getStringExtra("date");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", user.getUsername()));
        params.add(new BasicNameValuePair("sDate", DateHistory));

        final TextView tvEnergy = (TextView) findViewById(R.id.energy);
        final TextView tvDate = (TextView) findViewById(R.id.date);
        String resultServer  = getHttpPost(url,params);
        String strDate = "";
        String strEnergy = "";

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strDate = c.getString("strDate");
            strEnergy = c.getString("energy");
            strProtein = c.getString("protein");
            strFat = c.getString("fat");
            strCarbohydrate = c.getString("carbohydrate");
            strCalcium = c.getString("calcium");
            strIron = c.getString("iron");
            if(!strDate.equals(""))
            {
                tvDate.setText(strDate);
                tvEnergy.setText(strEnergy);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry(Float.valueOf(strCarbohydrate),0));
        valueSet1.add(new BarEntry(Float.valueOf(strFat),1));
        valueSet1.add(new BarEntry(Float.valueOf(strProtein),2));

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        valueSet2.add(new BarEntry(300f, 0));
        valueSet2.add(new BarEntry(65f, 1));
        valueSet2.add(new BarEntry(50f, 2));

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "ปริมาณพลังงานที่ได้รับ");
        barDataSet1.setColor(Color.rgb(247, 202, 201));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "ปริมาณพลังงานที่ควรได้รับ");
        barDataSet2.setColor(Color.rgb(146, 168, 209));

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<IBarDataSet> getDataSet2() {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry(Float.valueOf(strCalcium),0));
        valueSet1.add(new BarEntry(Float.valueOf(strIron),1));

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        valueSet2.add(new BarEntry(800f, 0));
        valueSet2.add(new BarEntry(15f, 1));

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "ปริมาณพลังงานที่ได้รับ");
        barDataSet1.setColor(Color.rgb(247, 202, 201));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "ปริมาณพลังงานที่ควรได้รับ");
        barDataSet2.setColor(Color.rgb(146, 168, 209));

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Carbohydrate");
        xAxis.add("Fat");
        xAxis.add("Protein");
        return xAxis;
    }

    private ArrayList<String> getXAxisValues2() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Calcium");
        xAxis.add("Iron");
        return xAxis;
    }

    public String getHttpPost(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
