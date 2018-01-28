package com.example.fixxxer.busapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MakeRequests().execute(getResources().getStringArray(R.array.urls));
            }
        });
        new MakeRequests().execute(getResources().getStringArray(R.array.urls));

    }

    @SuppressLint("StaticFieldLeak")
    private class MakeRequests extends AsyncTask<String[], Void, ArrayList<ArrayList<Long>>> {

        @Override
        protected void onPreExecute() {
            ((Button) findViewById(R.id.button)).setText(R.string.buttontext);
            ((Button) findViewById(R.id.button)).setTextColor(Color.WHITE);
            (findViewById(R.id.button)).setEnabled(false);

        }

        @Override
        protected ArrayList<ArrayList<Long>> doInBackground(String[]... params) {
            ArrayList<ArrayList<Long>> allBuses = new ArrayList<>();

            for (String url : params[0]) {
                ArrayList<Long> currentBus = new ArrayList<>();
                BufferedReader reader;
                InputStream is;
                JSONObject response;
                try {
                    StringBuilder responseBuilder = new StringBuilder();
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    is = conn.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is));
                    for (String line; (line = reader.readLine()) != null; ) {
                        responseBuilder.append(line).append("\n");
                    }
                    reader.close();
                    response = new JSONObject(responseBuilder.toString());
                    JSONArray arrivals = response.getJSONArray("lines").getJSONObject(0).getJSONArray("arrivals");
                    String line = response.getJSONArray("lines").getJSONObject(0).getString("name");
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    String currentTime = format.format(Calendar.getInstance().getTime());
                    for (int i = 0; i < arrivals.length(); i++) {
                        Date timeNow = format.parse(currentTime);
                        Date timeOfArrival = format.parse(arrivals.getJSONObject(i).getString("time"));
                        long diffMinutes = (timeOfArrival.getTime() - timeNow.getTime()) / (60 * 1000) % 60;
                        currentBus.add(diffMinutes);
                    }
                    Collections.sort(currentBus);
                    currentBus.add(Long.valueOf(line));
                    allBuses.add(currentBus);
                } catch (JSONException | IOException | ParseException e) {
                    e.printStackTrace();
                }
            }

            return allBuses;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<Long>> all) {
            TableLayout layout = findViewById(R.id.content_table);
            Log.e("Array", all.toString());
            for (int i =0; i < all.size(); i++) {
                Log.e("Result", all.get(i).toString());
                switch (String.valueOf(all.get(i).get(all.get(i).size()-1))) {
                    case "64":
                        for (int j = 0; j < 4; j++) {
                            View child = layout.getChildAt(j);
                            if (child instanceof TableRow) {
                                TableRow row = (TableRow) child;
                                View view = row.getChildAt(0);
                                try{
                                    if(all.get(i).get(j)==-1){
                                        ((TextView) view).setText(String.valueOf(0));
                                    }else{
                                        ((TextView) view).setText(String.valueOf(all.get(i).get(j)));
                                    }
                                }catch (IndexOutOfBoundsException e){
                                    ((TextView) view).setText("--");
                                }

                            }
                        }
                        break;
                    case "93":
                        for (int j = 0; j < 4; j++) {
                            View child = layout.getChildAt(j);
                            if (child instanceof TableRow) {
                                TableRow row = (TableRow) child;
                                View view = row.getChildAt(1);
                                try{
                                    if(all.get(i).get(j)==-1){
                                        ((TextView) view).setText(String.valueOf(0));
                                    }else{
                                        ((TextView) view).setText(String.valueOf(all.get(i).get(j)));
                                    }
                                }catch (IndexOutOfBoundsException e){
                                    ((TextView) view).setText("--");
                                }

                            }
                        }
                        break;
                    case "98":
                        for (int j = 0; j < 4; j++) {
                            View child = layout.getChildAt(j);
                            if (child instanceof TableRow) {
                                TableRow row = (TableRow) child;
                                View view = row.getChildAt(2);
                                try{
                                    if(all.get(i).get(j)==-1){
                                        ((TextView) view).setText(String.valueOf(0));
                                    }else{
                                        ((TextView) view).setText(String.valueOf(all.get(i).get(j)));
                                    }
                                }catch (IndexOutOfBoundsException e){
                                    ((TextView) view).setText("--");
                                }

                            }
                        }
                        break;
                    case "122":
                        for (int j = 0; j < 4; j++) {
                            View child = layout.getChildAt(j);
                            if (child instanceof TableRow) {
                                TableRow row = (TableRow) child;
                                View view = row.getChildAt(3);
                                try{
                                    if(all.get(i).get(j)==-1){
                                        ((TextView) view).setText(String.valueOf(0));
                                    }else{
                                        ((TextView) view).setText(String.valueOf(all.get(i).get(j)));
                                    }
                                }catch (IndexOutOfBoundsException e){
                                    ((TextView) view).setText("--");
                                }

                            }
                        }
                        break;
                }

            }
            int lowest = Integer.MAX_VALUE;
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                if (child instanceof TableRow) {
                    TableRow row = (TableRow) child;

                    for (int x = 0; x < row.getChildCount(); x++) {
                        TextView text  = (TextView) row.getChildAt(x);
                        if(!text.getText().equals("--")){
                            if(Integer.valueOf(String.valueOf(text.getText())) < lowest){
                                lowest = Integer.valueOf(String.valueOf(text.getText()));
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < 1; i++) {
                View child = layout.getChildAt(i);
                if (child instanceof TableRow) {
                    TableRow row = (TableRow) child;

                    for (int x = 0; x < row.getChildCount(); x++) {
                        TextView text  = (TextView) row.getChildAt(x);
                        if(!text.getText().equals("--")){
                            if(Integer.valueOf(String.valueOf(text.getText())) == lowest){
                                text.setTextColor(Color.GREEN);
                                text.setPaintFlags(text.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                                break;
                            }else{
                                text.setTextColor(Color.WHITE);
                                text.setPaintFlags(text.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                            }
                        }
                    }
                }
            }
            ((Button) findViewById(R.id.button)).setText(R.string.buttonbefore);
            (findViewById(R.id.button)).setEnabled(true);
        }


    }
}


