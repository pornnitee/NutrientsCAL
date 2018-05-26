package com.example.starius.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by STARIUS on 4/8/2018.
 */

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    TextView textViewUsername;
    EditText  textViewPassword,textViewEmail,textViewWeight,textViewHeight, textViewAge,textViewGender;
    RadioGroup radioGroupGender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewPassword = (EditText) findViewById(R.id.textViewPassword);
        textViewEmail = (EditText) findViewById(R.id.textViewEmail);
        textViewWeight = (EditText) findViewById(R.id.textViewWeight);
        textViewHeight = (EditText) findViewById(R.id.textViewHeight);
        textViewAge = (EditText) findViewById(R.id.textViewAge);
        radioGroupGender = (RadioGroup) findViewById(R.id.textViewGender);

        User user = SharedPrefManager.getInstance(this).getUser();
        textViewUsername.setText(user.getUsername());
        textViewPassword.setText(user.getPassword());
        textViewEmail.setText(user.getEmail());
        textViewWeight.setText(user.getWeight());
        textViewHeight.setText(user.getHeight());
        textViewAge.setText(String.valueOf(user.getAge()));
        // btnSave
        Button btnSave = (Button) findViewById(R.id.save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(SaveData())
                {
                    Intent i = new Intent(UpdateActivity.this,InfoActivity.class);
                    i.addCategory(Intent.CATEGORY_HOME);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }
            }
        });
    }

    public boolean SaveData()
    {
        final TextView txtUsername = (TextView)findViewById(R.id.textViewUsername);
        final EditText txtPassword = (EditText)findViewById(R.id.textViewPassword);
        final EditText txtEmail = (EditText)findViewById(R.id.textViewEmail);
        final EditText txtWeight = (EditText)findViewById(R.id.textViewWeight);
        final EditText txtHeight = (EditText)findViewById(R.id.textViewHeight);
        final EditText txtAge = (EditText)findViewById(R.id.textViewAge);
        final String gender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle("Error! ");
        ad.setIcon(android.R.drawable.btn_star_big_on);
        ad.setPositiveButton("Close", null);
        // Check Password
        if(txtPassword.getText().length() == 0)
        {
            ad.setMessage("Please input [Password] ");
            ad.show();
            txtPassword.requestFocus();
            return false;
        }
        // Check Name
        if(txtAge.getText().length() == 0)
        {
            ad.setMessage("Please input [Age] ");
            ad.show();
            txtAge.requestFocus();
            return false;
        }
        // Check Email
        if(txtEmail.getText().length() == 0)
        {
            ad.setMessage("Please input [Email] ");
            ad.show();
            txtEmail.requestFocus();
            return false;
        }
        // Check Tel
        if(txtWeight.getText().length() == 0)
        {
            ad.setMessage("Please input [Weight] ");
            ad.show();
            txtWeight.requestFocus();
            return false;
        }
        // Check Tel
        if(txtHeight.getText().length() == 0)
        {
            ad.setMessage("Please input [Height] ");
            ad.show();
            txtHeight.requestFocus();
            return false;
        }
        String url = URLs.HOST_URL + "UpdateActivity.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sUsername", txtUsername.getText().toString()));
        params.add(new BasicNameValuePair("sPassword", txtPassword.getText().toString()));
        params.add(new BasicNameValuePair("sEmail", txtEmail.getText().toString()));
        params.add(new BasicNameValuePair("sHeight", txtHeight.getText().toString()));
        params.add(new BasicNameValuePair("sWeight", txtWeight.getText().toString()));
        params.add(new BasicNameValuePair("sAge", txtAge.getText().toString()));
        params.add(new BasicNameValuePair("sGender", gender));
        /** Get result from Server (Return the JSON Code)
         * StatusID = ? [0=Failed,1=Complete]
         * Error	= ?	[On case error return custom error message]
         *
         * Eg Save Failed = {"StatusID":"0","Error":"Email Exists!"}
         * Eg Save Complete = {"StatusID":"1","Error":""}
         */
        String resultServer  = getHttpPost(url,params);
        /*** Default Value ***/
        String strStatusID = "0";
        String strError = "Unknow Status!";
        String strUsername = "";
        String strPassword = "";
        String strEmail = "";
        String strWeight = "";
        String strHeight = "";
        String strAge = "";
        String strGender = "";
        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strStatusID = c.getString("StatusID");
            strError = c.getString("Error");
            strUsername = c.getString("username");
            strPassword = c.getString("password");
            strEmail = c.getString("email");
            strWeight = c.getString("weight");
            strHeight = c.getString("height");
            strAge = c.getString("age");
            strGender = c.getString("gender");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Prepare Save Data
        if(strStatusID.equals("0"))
        {
            ad.setMessage(strError);
            ad.show();
            return false;
        }
        else
        {
            User user = new User(
                    //      userJson.getInt("id"),
                    strUsername,
                    strPassword,
                    strEmail,
                    strWeight,
                    strHeight,
                    strAge,
                    strGender
            );
            //storing the user in shared preferences
            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

            Toast.makeText(UpdateActivity.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
        }
        return true;
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
