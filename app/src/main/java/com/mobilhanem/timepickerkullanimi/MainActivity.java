package com.mobilhanem.timepickerkullanimi;

/**
 * Created by hakki on 07.02.2018.
 */

/**
 * algorithm
 * 0-0,1,2,3
 * 1-4,5,6,7
 * 2-8,9,10,11
 * 3-12,13,14,15
 * n-postion*n and n++
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    private LinearLayout mLinearScroll;
    private ListView mListView;
    private ArrayList<String> mArrayListFruit;
    private ArrayList<String> mArrayListFruitTemp;
    TextView gelenDataTexView;

    // change row size according to your need, how many row you needed per page
    int rowSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainz);

        mLinearScroll = (LinearLayout) findViewById(R.id.linear_scroll);
        mListView = (ListView) findViewById(R.id.listview1);
        final String startDate = getIntent().getStringExtra("startDate");
        final String endDate = getIntent().getStringExtra("endDate");
        final String minCount = getIntent().getStringExtra("minCount");
        final String maxCount = getIntent().getStringExtra("maxCount");



        final ArrayList[] glue = new ArrayList[]{new ArrayList<String>()};
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial
        String url = "https://getir-bitaksi-hackathon.herokuapp.com/searchRecords";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        try {
                            ///////////////////
                            //Configure Data //

                            JSONObject jsonObject = new JSONObject(response);//print "Accounts"
                            JSONArray subArray = jsonObject.getJSONArray("records");
                            //gelenDataTexView.setText(String.valueOf(subArray));
                            if (subArray != null) {
                                int len = subArray.length();
                                for (int i=0;i<len;i++){
                                    glue[0].add(subArray.get(i).toString());
                                }
                            }



                            /**
                             * add item into arraylist
                             */
                            mArrayListFruit = new ArrayList<String>();
                            mArrayListFruit = glue[0];
                            mArrayListFruitTemp = new ArrayList<String>();

                            for (int l = 1; l <= 400; l++) {
                                mArrayListFruit.add(mArrayListFruit.get(l) + l);
                            }

                            /**
                             * create dynamic button according the size of array
                             */
                            int rem = mArrayListFruit.size() % rowSize;
                            if (rem > 0) {
                                for (int i = 0; i < rowSize - rem; i++) {
                                    mArrayListFruit.add("");
                                }
                            }

                            /**
                             * add arraylist item into list on page load
                             */
                            addItem(0);

                            int size = mArrayListFruit.size() / rowSize;

                            for (int j = 0; j < size; j++) {
                                final int k;
                                k = j;
                                final Button btnPage = new Button(MainActivity.this);
                                LayoutParams lp = new LinearLayout.LayoutParams(120, LayoutParams.WRAP_CONTENT);
                                lp.setMargins(5, 2, 2, 2);
                                btnPage.setTextColor(Color.WHITE);
                                btnPage.setTextSize(26.0f);
                                btnPage.setId(j);
                                btnPage.setText(String.valueOf(j + 1));
                                mLinearScroll.addView(btnPage, lp);

                                btnPage.setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        /**
                                         * add arraylist item into list
                                         */
                                        addItem(k);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                gelenDataTexView.setText("That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("startDate", ""+startDate);
                params.put("endDate", ""+endDate);
                params.put("minCount", ""+minCount);
                params.put("maxCount", ""+maxCount);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);



    }

    private void addItem(int i) {
        // TODO Auto-generated method stub
        mArrayListFruitTemp.clear();
        i = i * rowSize;

        /**
         * fill temp array list to set on page change
         */
        for (int j = 0; j < rowSize; j++) {
            mArrayListFruitTemp.add(j, mArrayListFruit.get(i));
            i = i + 1;
        }

        // set view
        setView();
    }

    //set view method
    private void setView() {
        // TODO Auto-generated method stub
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1,
                mArrayListFruitTemp);
        mListView.setAdapter(adapter);

        /**
         * On item click listner
         */
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                System.out.println(mArrayListFruitTemp.get(position));
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
