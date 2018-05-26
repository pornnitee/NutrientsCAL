package com.example.starius.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by STARIUS on 3/10/2018.
 */

public class MenuHistoryActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuhistory);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ShowData();
    }

    public void ShowData()
    {
        final ListView listView1 = (ListView) findViewById(R.id.dateHistory);
        String url = URLs.HOST_URL + "StatisticActivity.php";
        User user = SharedPrefManager.getInstance(this).getUser();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", user.getUsername()));
        try {
            JSONArray data = new JSONArray(getJSONUrl(url, params));
            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("date", c.getString("date"));
                map.put("strDate" , c.getString("strDate"));
                MyArrList.add(map);
            }
            SimpleAdapter simpleAdapterData;
            simpleAdapterData = new SimpleAdapter(MenuHistoryActivity.this, MyArrList, R.layout.list_item,
                    new String[]{"strDate"}, new int[]{R.id.txtitem});
            listView1.setAdapter(simpleAdapterData);

            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String DateHistory = MyArrList.get(position).get("date");

                    Intent newActivity = new Intent(MenuHistoryActivity.this,HistoryActivity.class);
                    newActivity.putExtra("date", DateHistory);
                    startActivity(newActivity);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
                /*public void onItemClick(AdapterView<?> myAdapter, View myView,
                                        int position, long mylng) {

                    String DateHistory = MyArrList.get(position).get("date").toString();
                //    String name_menu = MyArrList.get(position).get("name_menu").toString();


                    Intent newActivity = new Intent(StatisticActivity.this,ChartActivity.class);
                    newActivity.putExtra("date", DateHistory);
                    startActivity(newActivity);
            }*/
            });
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getJSONUrl(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader_buffer = new BufferedReader
                        (new InputStreamReader(content));
                String line;
                while ((line = reader_buffer.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download file..");
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
