package com.example.starius.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.swipe.SwipeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by STARIUS on 12/25/2017.
 */

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private final static String TAG = HistoryActivity.class.getSimpleName();

    ArrayList<HashMap<String, String>> MyArrB;
    ArrayList<HashMap<String, String>> MyArrL;
    ArrayList<HashMap<String, String>> MyArrD;
    ArrayList<HashMap<String, String>> MyArrS;

    ListView listView1,listView2,listView3,listView4;
    TextView textView;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    ProgressBar progressBar;

    MyListAdapterB BAdapter;
    MyListAdapterL LAdapter;
    MyListAdapterD DAdapter;
    MyListAdapterS SAdapter;

    //@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        textView = (TextView) findViewById(R.id.date);
        textView1 = (TextView) findViewById(R.id.Breakfast);
        textView2 = (TextView) findViewById(R.id.Lunch);
        textView3 = (TextView) findViewById(R.id.Dinner);
        textView4 = (TextView) findViewById(R.id.Snack);
        listView1 = (ListView) findViewById(R.id.menuBreakfast);
        listView2 = (ListView) findViewById(R.id.menuLunch);
        listView3 = (ListView) findViewById(R.id.menuDinner);
        listView4 = (ListView) findViewById(R.id.menuSnack);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        showInfo();

        BAdapter = new MyListAdapterB();
        listView1.setAdapter(BAdapter);
        LAdapter = new MyListAdapterL();
        listView2.setAdapter(LAdapter);
        DAdapter = new MyListAdapterD();
        listView3.setAdapter(DAdapter);
        SAdapter = new MyListAdapterS();
        listView4.setAdapter(SAdapter);
    }

    private class MyListAdapterB extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(MyArrB != null && MyArrB.size() != 0){
                return MyArrB.size();
            }
            return 0;
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return MyArrB.get(position).get("name_menu");
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = HistoryActivity.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_history, null);
                holder.tvItem = (TextView) convertView.findViewById(R.id.txtitem);
                holder.tvCal = (TextView) convertView.findViewById(R.id.txtCal);
                holder.btnDelete = convertView.findViewById(R.id.delete);

                holder.swipeLayout = (SwipeLayout)convertView.findViewById(R.id.swipe_layout);
                holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ref = position;
            holder.tvItem.setText(MyArrB.get(position).get("name_menu"));
            holder.tvCal.setText(MyArrB.get(position).get("energy"));
            holder.btnDelete.setOnClickListener(onDeleteListener(holder.ref, holder));
            return convertView;
        }
        private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = SharedPrefManager.getInstance(HistoryActivity.this).getUser();
                    final String username = user.getUsername().trim();
                    Intent intent= getIntent();
                    final String date = intent.getStringExtra("date");
                    final String meal = MyArrB.get(position).get("meal");
                    final String name_menu = MyArrB.get(position).get("name_menu");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.DELETE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        showInfo();
                                        BAdapter = new MyListAdapterB();
                                        listView1.setAdapter(BAdapter);
                                        //converting response to json object
                                        JSONObject obj = new JSONObject(response);
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
                            params.put("username", username);
                            params.put("date", date);
                            params.put("name_menu", name_menu);
                            params.put("meal", meal);
                            return params;
                        }
                    };
                    VolleySingleton.getInstance(HistoryActivity.this).addToRequestQueue(stringRequest);
                    MyArrB.remove(position);
                    holder.swipeLayout.close();
                }
            };
        }
        private class ViewHolder {
            TextView tvItem;
            TextView tvCal;
            View btnDelete;
            int ref;
            private SwipeLayout swipeLayout;
        }
    }

    private class MyListAdapterL extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(MyArrL != null && MyArrL.size() != 0){
                return MyArrL.size();
            }
            return 0;
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return MyArrL.get(position).get("name_menu");
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = HistoryActivity.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_history, null);
                holder.tvItem = (TextView) convertView.findViewById(R.id.txtitem);
                holder.tvCal = (TextView) convertView.findViewById(R.id.txtCal);
                holder.btnDelete = convertView.findViewById(R.id.delete);

                holder.swipeLayout = (SwipeLayout)convertView.findViewById(R.id.swipe_layout);
                holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ref = position;
            holder.tvItem.setText(MyArrL.get(position).get("name_menu"));
            holder.tvCal.setText(MyArrL.get(position).get("energy"));
            holder.btnDelete.setOnClickListener(onDeleteListener(holder.ref, holder));
            return convertView;
        }
        private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = SharedPrefManager.getInstance(HistoryActivity.this).getUser();
                    final String username = user.getUsername().trim();
                    Intent intent= getIntent();
                    final String date = intent.getStringExtra("date");
                    final String meal = MyArrL.get(position).get("meal");
                    final String name_menu = MyArrL.get(position).get("name_menu");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.DELETE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        showInfo();
                                        LAdapter = new MyListAdapterL();
                                        listView2.setAdapter(LAdapter);
                                        //converting response to json object
                                        JSONObject obj = new JSONObject(response);
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
                            params.put("username", username);
                            params.put("date", date);
                            params.put("name_menu", name_menu);
                            params.put("meal", meal);
                            return params;
                        }
                    };
                    VolleySingleton.getInstance(HistoryActivity.this).addToRequestQueue(stringRequest);
                    MyArrL.remove(position);
                    holder.swipeLayout.close();
                }
            };
        }
        private class ViewHolder {
            TextView tvItem;
            TextView tvCal;
            View btnDelete;
            int ref;
            private SwipeLayout swipeLayout;
        }
    }

    private class MyListAdapterD extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(MyArrD != null && MyArrD.size() != 0){
                return MyArrD.size();
            }
            return 0;
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return MyArrD.get(position).get("name_menu");
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = HistoryActivity.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_history, null);
                holder.tvItem = (TextView) convertView.findViewById(R.id.txtitem);
                holder.tvCal = (TextView) convertView.findViewById(R.id.txtCal);
                holder.btnDelete = convertView.findViewById(R.id.delete);

                holder.swipeLayout = (SwipeLayout)convertView.findViewById(R.id.swipe_layout);
                holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ref = position;
            holder.tvItem.setText(MyArrD.get(position).get("name_menu"));
            holder.tvCal.setText(MyArrD.get(position).get("energy"));
            holder.btnDelete.setOnClickListener(onDeleteListener(holder.ref, holder));
            return convertView;
        }
        private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = SharedPrefManager.getInstance(HistoryActivity.this).getUser();
                    final String username = user.getUsername().trim();
                    Intent intent= getIntent();
                    final String date = intent.getStringExtra("date");
                    final String meal = MyArrD.get(position).get("meal");
                    final String name_menu = MyArrD.get(position).get("name_menu");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.DELETE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        showInfo();
                                        DAdapter = new MyListAdapterD();
                                        listView3.setAdapter(DAdapter);
                                        //converting response to json object
                                        JSONObject obj = new JSONObject(response);
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
                            params.put("username", username);
                            params.put("date", date);
                            params.put("name_menu", name_menu);
                            params.put("meal", meal);
                            return params;
                        }
                    };
                    VolleySingleton.getInstance(HistoryActivity.this).addToRequestQueue(stringRequest);
                    MyArrD.remove(position);
                    holder.swipeLayout.close();
                }
            };
        }
        private class ViewHolder {
            TextView tvItem;
            TextView tvCal;
            View btnDelete;
            int ref;
            private SwipeLayout swipeLayout;
        }
    }

    private class MyListAdapterS extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(MyArrS != null && MyArrS.size() != 0){
                return MyArrS.size();
            }
            return 0;
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return MyArrS.get(position).get("name_menu");
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = HistoryActivity.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_history, null);
                holder.tvItem = (TextView) convertView.findViewById(R.id.txtitem);
                holder.tvCal = (TextView) convertView.findViewById(R.id.txtCal);
                holder.btnDelete = convertView.findViewById(R.id.delete);

                holder.swipeLayout = (SwipeLayout)convertView.findViewById(R.id.swipe_layout);
                holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ref = position;
            holder.tvItem.setText(MyArrS.get(position).get("name_menu"));
            holder.tvCal.setText(MyArrS.get(position).get("energy"));
            holder.btnDelete.setOnClickListener(onDeleteListener(holder.ref, holder));
            return convertView;
        }
        private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = SharedPrefManager.getInstance(HistoryActivity.this).getUser();
                    final String username = user.getUsername().trim();
                    Intent intent= getIntent();
                    final String date = intent.getStringExtra("date");
                    final String meal = MyArrS.get(position).get("meal");
                    final String name_menu = MyArrS.get(position).get("name_menu");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.DELETE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        showInfo();
                                        SAdapter = new MyListAdapterS();
                                        listView4.setAdapter(SAdapter);
                                        //converting response to json object
                                        JSONObject obj = new JSONObject(response);
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
                            params.put("username", username);
                            params.put("date", date);
                            params.put("name_menu", name_menu);
                            params.put("meal", meal);
                            return params;
                        }
                    };
                    VolleySingleton.getInstance(HistoryActivity.this).addToRequestQueue(stringRequest);
                    MyArrS.remove(position);
                    holder.swipeLayout.close();
                }
            };
        }
        private class ViewHolder {
            TextView tvItem;
            TextView tvCal;
            View btnDelete;
            int ref;
            private SwipeLayout swipeLayout;
        }
    }

    public void showInfo()
    {
        User user = SharedPrefManager.getInstance(this).getUser();
        final String username = user.getUsername().trim();
        Intent intent= getIntent();
        final String date = intent.getStringExtra("date");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String strdate = obj.getString("strDate");
                            if (!strdate.equals("")){
                                textView.setText(strdate);
                            } else {
                                textView.setText("ไม่มีรายการอาหาร");
                            }
                            MyArrB = new ArrayList<HashMap<String, String>>();
                            //if no error in response
                            if (obj.getBoolean("Breakfast")) {
                                HashMap<String, String> map;
                                JSONArray Json = obj.getJSONArray("arrB");
                                for (int i = 0; i < Json.length(); i++) {
                                    JSONObject object = Json.getJSONObject(i);
                                    map = new HashMap<String, String>();
                                    map.put("name_menu", object.getString("name_menu"));
                                    map.put("energy" , object.getString("energy"));
                                    map.put("date" , object.getString("date"));
                                    map.put("meal" , object.getString("meal"));
                                    MyArrB.add(map);
                                }
                                textView1.setVisibility(View.VISIBLE);
                                listView1.setVisibility(View.VISIBLE);

                                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String nameMenu = MyArrB.get(position).get("name_menu");
                                        String Date = MyArrB.get(position).get("date");
                                        String Meal = MyArrB.get(position).get("meal");

                                        Intent newActivity = new Intent(HistoryActivity.this,NutrientActivity.class);
                                        newActivity.putExtra("name_menu", nameMenu);
                                        newActivity.putExtra("date", Date);
                                        newActivity.putExtra("meal", Meal);
                                        startActivity(newActivity);
                                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    }
                                });
                            } else {
                                textView1.setVisibility(View.GONE);
                                listView1.setVisibility(View.GONE);
                            }
                            MyArrL = new ArrayList<HashMap<String, String>>();
                            if (obj.getBoolean("Lunch")) {
                                HashMap<String, String> map;
                                JSONArray Json = obj.getJSONArray("arrL");
                                for (int i = 0; i < Json.length(); i++) {
                                    JSONObject object = Json.getJSONObject(i);
                                    map = new HashMap<String, String>();
                                    map.put("name_menu", object.getString("name_menu"));
                                    map.put("energy" , object.getString("energy"));
                                    map.put("date" , object.getString("date"));
                                    map.put("meal" , object.getString("meal"));
                                    MyArrL.add(map);
                                }
                                textView2.setVisibility(View.VISIBLE);
                                listView2.setVisibility(View.VISIBLE);

                                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String nameMenu = MyArrL.get(position).get("name_menu");
                                        String Date = MyArrL.get(position).get("date");
                                        String Meal = MyArrL.get(position).get("meal");

                                        Intent newActivity = new Intent(HistoryActivity.this,NutrientActivity.class);
                                        newActivity.putExtra("name_menu", nameMenu);
                                        newActivity.putExtra("date", Date);
                                        newActivity.putExtra("meal", Meal);
                                        startActivity(newActivity);
                                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    }
                                });
                            } else {
                                textView2.setVisibility(View.GONE);
                                listView2.setVisibility(View.GONE);
                            }
                            MyArrD = new ArrayList<HashMap<String, String>>();
                            if (obj.getBoolean("Dinner")) {
                                HashMap<String, String> map;
                                JSONArray Json = obj.getJSONArray("arrD");
                                for (int i = 0; i < Json.length(); i++) {
                                    JSONObject object = Json.getJSONObject(i);
                                    map = new HashMap<String, String>();
                                    map.put("name_menu", object.getString("name_menu"));
                                    map.put("energy" , object.getString("energy"));
                                    map.put("date" , object.getString("date"));
                                    map.put("meal" , object.getString("meal"));
                                    MyArrD.add(map);
                                }
                                textView3.setVisibility(View.VISIBLE);
                                listView3.setVisibility(View.VISIBLE);

                                listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String nameMenu = MyArrD.get(position).get("name_menu");
                                        String Date = MyArrD.get(position).get("date");
                                        String Meal = MyArrD.get(position).get("meal");

                                        Intent newActivity = new Intent(HistoryActivity.this,NutrientActivity.class);
                                        newActivity.putExtra("name_menu", nameMenu);
                                        newActivity.putExtra("date", Date);
                                        newActivity.putExtra("meal", Meal);
                                        startActivity(newActivity);
                                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    }
                                });
                            } else {
                                textView3.setVisibility(View.GONE);
                                listView3.setVisibility(View.GONE);
                            }
                            MyArrS = new ArrayList<HashMap<String, String>>();
                            if (obj.getBoolean("Snack")) {
                                HashMap<String, String> map;
                                JSONArray Json = obj.getJSONArray("arrS");
                                for (int i = 0; i < Json.length(); i++) {
                                    JSONObject object = Json.getJSONObject(i);
                                    map = new HashMap<String, String>();
                                    map.put("name_menu", object.getString("name_menu"));
                                    map.put("energy" , object.getString("energy"));
                                    map.put("date" , object.getString("date"));
                                    map.put("meal" , object.getString("meal"));
                                    MyArrS.add(map);
                                }
                                textView4.setVisibility(View.VISIBLE);
                                listView4.setVisibility(View.VISIBLE);

                                listView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String nameMenu = MyArrS.get(position).get("name_menu");
                                        String Date = MyArrS.get(position).get("date");
                                        String Meal = MyArrS.get(position).get("meal");

                                        Intent newActivity = new Intent(HistoryActivity.this,NutrientActivity.class);
                                        newActivity.putExtra("name_menu", nameMenu);
                                        newActivity.putExtra("date", Date);
                                        newActivity.putExtra("meal", Meal);
                                        startActivity(newActivity);
                                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                    }
                                });
                            } else {
                                textView4.setVisibility(View.GONE);
                                listView4.setVisibility(View.GONE);
                            }
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
                params.put("username", username);
                params.put("date", date);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MenuHistoryActivity.class);
        i.addCategory(Intent.CATEGORY_HOME);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}


