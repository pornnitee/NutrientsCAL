package com.example.starius.project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by STARIUS on 2/15/2018.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    EditText nameMenu, inputText;
    TextView err_ing;
    ListView listView1, listView;
    String meal;

    Button btnAdd, btnOk;
    ProgressBar progressBar;

    ArrayList<HashMap<String, String>> MyArrList;
    MyListAdapter myListAdapter;

    String [] MyList;
    ArrayList<HashMap<String, String>> MyArr;
    SimpleAdapter simpleAdapter;
    String nameSelected, idSelected;

    ArrayAdapter<String> adapter;

    private static final String TAG = "DetailActivity";
    private TextView mDisplayDate;
    private Button iconDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nameMenu = (EditText)findViewById(R.id.nameMenu);
        err_ing = (TextView) findViewById(R.id.err_ing);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  ");
        String dateString = sdf.format(date);
        mDisplayDate.setText(dateString);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        DetailActivity.this,
                        R.style.datepicker,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
        iconDisplayDate =(Button) findViewById(R.id.btnDate) ;
        iconDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(DetailActivity.this, R.style.datepicker, mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = year + "-" + month + "-" + day;
                mDisplayDate.setText(date);
            }
        };

        // Get reference of widgets from XML layout
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Initializing a String Array
        String[] plants = new String[]{
                "Breakfast",
                "Lunch",
                "Dinner",
                "Snack"
        };
        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.item_spinner,plants
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                meal=spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        showInfo();
        listView1 = (ListView) findViewById(R.id.ingredient);
        myListAdapter = new MyListAdapter();
        listView1.setAdapter(myListAdapter);
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView1,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    MyArrList.remove(position);
                                    myListAdapter.notifyDataSetChanged();
                                }
                            }
                        });
        listView1.setOnTouchListener(touchListener);

        btnAdd = (Button) findViewById(R.id.addItem);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialog();
            }
        });

        btnOk = (Button) findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputData();
            }
        });
    }

    public void AddDialog() {
        final Dialog dialog = new Dialog(DetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog);
        String key = "";
        SearchData(key);
        listView = (ListView) dialog.findViewById(R.id.addingredient);
        inputText = (EditText) dialog.findViewById(R.id.textsearch);
        Button btn_Cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_OK = (Button) dialog.findViewById(R.id.btn_ok);

        listView.setAdapter(adapter);

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String key = inputText.getText().toString();
                SearchData(key);
                listView.setAdapter(adapter);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = listView.getCheckedItemPosition();
                if (pos > -1) {
                    nameSelected = MyArr.get(pos).get("name_ing");
                    idSelected = MyArr.get(pos).get("id_ing");

                    HashMap<String, String> map;
                    map = new HashMap<String, String>();
                    map.put("name_ing", nameSelected);
                    map.put("id_ing", idSelected);
                    map.put("quantity", "1.0");
                    MyArrList.add(map);
                    myListAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void SearchData(String key) {
        String url = URLs.HOST_URL + "IngredientActivity.php";
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("txtKeyword", key));
        try {
            JSONArray data = new JSONArray(getJSONUrl(url, param));
            MyArr = new ArrayList<HashMap<String, String>>();
            MyList = new String[data.length()];
            HashMap<String, String> map;
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("name_ing", c.getString("name_ing"));
                MyList[i] = c.getString("name_ing");
                map.put("id_ing", c.getString("id_ing"));
                MyArr.add(map);
            }
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice, MyList);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(MyArrList != null && MyArrList.size() != 0){
                return MyArrList.size();
            }
            return 0;
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return MyArrList.get(position).get("name_ing");
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //ViewHolder holder = null;
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = DetailActivity.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_detail, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.nameIngredient);
                holder.editText1 = (EditText) convertView.findViewById(R.id.edQuantity);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ref = position;
            holder.textView1.setText(MyArrList.get(position).get("name_ing"));
            holder.editText1.setText(MyArrList.get(position).get("quantity"));
            holder.editText1.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(4,1)});
            holder.editText1.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    HashMap<String, String> map;
                    map = new HashMap<String, String>();
                    map.put("name_ing", MyArrList.get(holder.ref).get("name_ing"));
                    map.put("id_ing", MyArrList.get(holder.ref).get("id_ing"));
                    map.put("quantity", arg0.toString());
                    MyArrList.set(holder.ref, map);
                }
            });
            return convertView;
        }
        private class ViewHolder {
            TextView textView1;
            EditText editText1;
            int ref;
        }
    }

    public void showInfo()
    {
        String url = URLs.HOST_URL + "DetailActivity.php";
        Intent intent= getIntent();
        final String id_menu = intent.getStringExtra("id_menu");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_menu",id_menu));
        try {
            JSONArray data = new JSONArray(getJSONUrl(url, params));
            MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("name_ing", c.getString("name_ing"));
                map.put("id_ing", c.getString("id_ing"));
                map.put("quantity", c.getString("quantity"));
                MyArrList.add(map);
                String name_menu = c.getString("name_menu");
                nameMenu.setText(name_menu);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void InputData() {
        User user = SharedPrefManager.getInstance(this).getUser();
        final String username = user.getUsername().trim();
        final String menu = nameMenu.getText().toString().trim();
        final String date = mDisplayDate.getText().toString().trim();
        //first we will do the validations
        if (TextUtils.isEmpty(menu)) {
            nameMenu.setError("Please enter menu's name");
            nameMenu.requestFocus();
            return;
        }
        if (MyArrList.size()==0){
            err_ing.setError("Please selected ingredient");
            err_ing.requestFocus();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.INPUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent newActivity = new Intent(DetailActivity.this,NutrientActivity.class);
                                newActivity.putExtra("name_menu", menu);
                                newActivity.putExtra("date", date);
                                newActivity.putExtra("meal", meal);
                                startActivity(newActivity);
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("menu", menu);
                params.put("date", date);
                params.put("meal", meal);
                params.put("count", String.valueOf(MyArrList.size()));
                for (int i=0;i<MyArrList.size(); i++) {
                    String id_ing = MyArrList.get(i).get("id_ing").trim();
                    params.put("id_ing"+i,id_ing);
                    String q = (MyArrList.get(i)).get("quantity");
                    params.put("quantity"+i,q);
                }
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
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
