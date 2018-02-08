package com.mobilhanem.timepickerkullanimi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.util.HashMap;

public class Anasayfa extends Activity {
	Button endDateButton,startDateButton,sendButton,otherDataButton;
	TextView endDateTextView,startDateTextView,responseTextView;
	EditText minCountEditTextView,maxCountEditTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anasayfa);
		
		//Oluşturduğumuz XML'den gerekli referansları atıyoruz
		otherDataButton = (Button) findViewById(R.id.other_data);

		endDateButton = (Button) findViewById(R.id.endDate_button);
		endDateTextView = (TextView) findViewById(R.id.endDate);
		startDateButton = (Button) findViewById(R.id.startDate_button);
		startDateTextView = (TextView) findViewById(R.id.startDate);
		minCountEditTextView = (EditText) findViewById(R.id.minCount);
		maxCountEditTextView = (EditText) findViewById(R.id.maxCount);


		responseTextView = (TextView) findViewById(R.id.response);
		sendButton = (Button) findViewById(R.id.Send);



		//Başlangıçtaki gözükecek örnek tarih
		int year = 2016;
		int month = 1;
		int day = 21;
		startDateTextView.setText(year+"-"+month+ "-"+day );

		int yearT = 2017;
		int monthT = 2;
		int dayT = 2;
		endDateTextView.setText(yearT+"-"+monthT+ "-"+dayT);

		startDateButton.setOnClickListener(new View.OnClickListener() {//tarihButona Click Listener ekliyoruz

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar mcurrentTime = Calendar.getInstance();
				int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
				int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
				int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

				DatePickerDialog datePicker;//Datepicker objemiz
				datePicker = new DatePickerDialog(Anasayfa.this, new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
										  int dayOfMonth) {
						// TODO Auto-generated method stub
						startDateTextView.setText( year + "-" + monthOfYear+ "-"+dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

					}
				},2016,1,21);//başlarken set edilcek değerlerimizi atıyoruz
				datePicker.setTitle("Tarih Seçiniz");
				datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
				datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

				datePicker.show();

			}
		});

		endDateButton.setOnClickListener(new View.OnClickListener() {//saatButona Click Listener ekliyoruz
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar mcurrentTime = Calendar.getInstance();
				int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
				int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
				int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

				DatePickerDialog datePicker;//Datepicker objemiz
				datePicker = new DatePickerDialog(Anasayfa.this, new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
										  int dayOfMonth) {
						// TODO Auto-generated method stub
						endDateTextView.setText(year + "-" + monthOfYear+ "-"+dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

					}
				},2017,2,2);//başlarken set edilcek değerlerimizi atıyoruz
				datePicker.setTitle("Tarih Seçiniz");
				datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
				datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

				datePicker.show();
			}
		});

		final String[] glue = new String[1];
		//hooking onclick listener of button
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				responseTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				// Instantiate the RequestQueue.
				RequestQueue queue = Volley.newRequestQueue(Anasayfa.this);
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
									glue[0] = String.valueOf(subArray);

									///////////////////
									//FIND MIN VALUE //

									String minData =String.valueOf(subArray.getJSONObject(0));
									int minValue = Integer.parseInt(subArray.getJSONObject(0).getString("totalCount"));
									for(int i = 0; i < subArray.length(); i++){
										if (Integer.parseInt(subArray.getJSONObject(i).getString("totalCount")) < minValue) {
											minData = String.valueOf(subArray.getJSONObject(i));
										}
									}
									Log.d("MINVALUE AND MINDATA ", String.valueOf(minData));

									///////////////////
									//FIND MAX VALUE //

									String maxData =String.valueOf(subArray.getJSONObject(0));
									int maxValue = Integer.parseInt(subArray.getJSONObject(0).getString("totalCount"));
									for(int i = 0; i < subArray.length(); i++){
										if (Integer.parseInt(subArray.getJSONObject(i).getString("totalCount")) > maxValue) {
											maxData = String.valueOf(subArray.getJSONObject(i));
										}
									}
									Log.d("MAXVALUE AND MAXDATA ", String.valueOf(maxData));

									//////////////////////////////////////////////////////////////////////
									//ALL VALUE CREATED NEW ARRAY WITH GSON BUILDER FOR PRETTY PRINTING //

									String totalValue = "{\"code\":0," + "\"msg\":\"Success\"," + "\"records\":[" + String.valueOf(minData) + "," + String.valueOf(maxData) + "]}";
									String[] strArray = new String[] {totalValue};
									Gson gson = new GsonBuilder().setPrettyPrinting().create();
									JsonParser jp = new JsonParser();
									JsonElement je = jp.parse(totalValue);
									String prettyJsonString = gson.toJson(je);

									////////////////////////
									//DISPLAY EDITED DATA //
									responseTextView.setText(String.valueOf(prettyJsonString));
									responseTextView.setMovementMethod(new ScrollingMovementMethod());
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}

						}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						responseTextView.setText("That didn't work!");
					}
				}) {
					//adding parameters to the request
					@Override
					protected Map<String, String> getParams() throws AuthFailureError {
						Map<String, String> params = new HashMap<>();
						params.put("startDate", ""+startDateTextView.getText().toString());
						params.put("endDate", ""+endDateTextView.getText().toString());
						params.put("minCount", ""+minCountEditTextView.getText().toString());
						params.put("maxCount", ""+maxCountEditTextView.getText().toString());
						return params;
					}
			};
				// Add the request to the RequestQueue.
				queue.add(stringRequest);
			}
		});
		otherDataButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				Intent intent = new Intent(Anasayfa.this, MainActivity.class);
				intent.putExtra("startDate", startDateTextView.getText().toString());
				intent.putExtra("endDate", endDateTextView.getText().toString());
				intent.putExtra("minCount", minCountEditTextView.getText().toString());
				intent.putExtra("maxCount", maxCountEditTextView.getText().toString());
				Anasayfa.this.startActivity(intent);
				Anasayfa.this.finish();
			}
		});
	}

}
