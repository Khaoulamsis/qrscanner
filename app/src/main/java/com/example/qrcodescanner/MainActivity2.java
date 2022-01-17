package com.example.qrcodescanner;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qrcodescanner.beans.Crenau;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    TextView idSalle;
    TextView creneau;
    String str;
    StringRequest request;
    StringRequest requestt;
    Button show;
    String insertUrll = "https://gestion-salles-blocs-ak.herokuapp.com/crenau/api";
    String insertUrl = "https://gestion-salles-blocs-ak.herokuapp.com/occupation/apic";
    private ArrayList<Crenau> crenau;
    String sh=null;
    String shId=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        show = (Button)findViewById(R.id.show);
        notify(show);
        idSalle = (TextView) findViewById(R.id.nomSalle);
        creneau = (TextView) findViewById(R.id.creanau);
        Intent intent = getIntent();

        str = intent.getStringExtra("idSalle");


        String time;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdfff = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDateandTime = sdfff.format(new Date());
        String currentDateandTimee = sdf.format(new Date());
        System.out.println(currentDateandTime+"tt");

        SimpleDateFormat sdff = new SimpleDateFormat("HH:mm");

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        request = new StringRequest(Request.Method.GET, insertUrll, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("ah");

                    crenau = new ArrayList<>();
                    Log.d(TAG, response);
                    Type type = new TypeToken<Collection<Crenau>>() {
                    }.getType();
                    Collection<Crenau> crenaux = new Gson().fromJson(response, type);
                    for (Crenau e : crenaux) {
                        Log.d(TAG, e.toString());
                        Date d1 = null;
                        Date d2 = null;
                        Date d3 = null;

                        try {
                            d2 = sdff.parse(currentDateandTimee);
                            d1 = sdff.parse(e.getHrdebut());
                            d3 = sdff.parse(e.getHrfin());

                        } catch (ParseException d) {
                            d.printStackTrace();
                        }
                        long elapsed = d2.getTime() - d1.getTime();
                        long elapsedd = d2.getTime() - d3.getTime();
                        System.out.println(elapsed+"elapsed");
                        System.out.println(elapsedd+"elapsedd");
                        if(elapsed>0 & elapsedd<0 ){
                            sh=e.getHrdebut()+" to "+e.getHrfin();
                            shId=e.get_id();
                        }
                        crenau.add(e);
                    }

                    idSalle.setText(str.substring(25));
                    creneau.setText(sh);


                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("walo");

                error.printStackTrace();
            }
        });
        requestQueue.add(request);

        System.out.println(sh);
        System.out.println(str.substring(25));

        show.setOnClickListener(v -> {

            requestt = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                    System.out.println("saif");
                    Toast.makeText(MainActivity2.this, "salle  bien résérvée", Toast.LENGTH_LONG).show();

                    try {
                        Log.d(TAG, response);

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("no");
                    Toast.makeText(MainActivity2.this, "salle  déja résérvée", Toast.LENGTH_LONG).show();

                    error.printStackTrace();
                }
            }) {
                //This is how you will send the data to API
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> map = new HashMap<>();
                    map.put("date", currentDateandTime);
                    map.put("namesalle", str.substring(25));
                    map.put("crenauhr", sh);
                    map.put("salle", str.substring(0,24));
                    map.put("crenau", shId);

                    return map;
                }
            };


            if(shId!=null) {
                requestQueue.add(requestt);

            }
            else{
                Toast.makeText(MainActivity2.this, "Impossible de ce réserver dans ce créneau", Toast.LENGTH_LONG).show();


            }
            Notification notification = new NotificationCompat.Builder(this,"id1" )
                    .setContentTitle("New notification")
                    .setSmallIcon(R.drawable.scanner)
                    .setContentText("You confirmed the reservation!!")
                    .build();
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);

        });


    }
    public void notify(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel("id1", "Notification Tuto",
                            NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Description");
            notificationChannel.setShowBadge(true);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{500, 1000, 500, 1000, 500});
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onClick(View view) {

    }
}