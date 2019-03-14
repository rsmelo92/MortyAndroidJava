package com.solis.rafael.rickyandmortcharacters;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    void loadImage(String image_location) {
        URL imageURL = null;
        CircleImageView imageChar = (CircleImageView)findViewById(R.id.imageChar);
        if (image_location != null) {
            Picasso.get().load(image_location).into(imageChar);
        }
    }

    void getChar(RequestQueue queue, final ProgressDialog nDialog) {
        final int min = 1;
        final int max = 493;
        final int random = new Random().nextInt((max - min) + 1) + min;
        String url = "https://rickandmortyapi.com/api/character/"+random;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String name = jsonObject.getString("name");
                            String status = jsonObject.getString("status");
                            String species = jsonObject.getString("species");
                            JSONObject origin = jsonObject.getJSONObject("origin");
                            String originName = origin.getString("name");
                            String image = jsonObject.getString("image");

                            TextView nameChar = (TextView)findViewById(R.id.nameChar);
                            nameChar.setText(name);

                            TextView statusChar = (TextView)findViewById(R.id.statusChar);
                            statusChar.setText("Status: "+status);

                            TextView speciesChar = (TextView)findViewById(R.id.speciesChar);
                            speciesChar.setText("Species: "+species);

                            TextView originChar = (TextView)findViewById(R.id.originChar);
                            originChar.setText("Origin: "+originName);

                            loadImage(image);
                            nDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("E","Didn't work"+error);
            }
        });
        queue.add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#673AB7")));
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#512DA8"));

        final ProgressDialog nDialog;
        nDialog = new ProgressDialog(this);
        nDialog.setMessage("Loading..");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();

        final RequestQueue queue = Volley.newRequestQueue(this);
        getChar(queue, nDialog);

        final Button button = (Button) findViewById(R.id.fetchCharsButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getChar(queue, nDialog);
            }
        });
    }
}
