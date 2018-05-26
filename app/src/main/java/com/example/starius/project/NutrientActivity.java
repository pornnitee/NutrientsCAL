package com.example.starius.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by STARIUS on 4/7/2018.
 */

public class NutrientActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrient);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        showInfo();
    }

    public void showInfo()
    {
        User user = SharedPrefManager.getInstance(this).getUser();
        final String User = user.getUsername();
        Intent intent= getIntent();
        final String nameMenu = intent.getStringExtra("name_menu");
        final String Date = intent.getStringExtra("date");
        final String Meal = intent.getStringExtra("meal");

        final TextView tvnameMenu = (TextView) findViewById(R.id.nameMenu);
        final TextView tvenergy = (TextView) findViewById(R.id.energy);
        final TextView tvprotein = (TextView) findViewById(R.id.protein);
        final TextView tvfat = (TextView) findViewById(R.id.fat);
        final TextView tvcarbohydrate = (TextView) findViewById(R.id.carbohydrate);
        final TextView tviron = (TextView) findViewById(R.id.iron);
        final TextView tvcalcium = (TextView) findViewById(R.id.calcium);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.NUTRIENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            tvnameMenu.setText(obj.getString("name_menu"));
                            tvenergy.setText(obj.getString("energy"));
                            tvprotein.setText(obj.getString("protein"));
                            tvfat.setText(obj.getString("fat"));
                            tvcarbohydrate.setText(obj.getString("carbohydrate"));
                            tvcalcium.setText(obj.getString("calcium"));
                            tviron.setText(obj.getString("iron"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", User);
                params.put("date", Date);
                params.put("name_menu", nameMenu);
                params.put("meal", Meal);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}