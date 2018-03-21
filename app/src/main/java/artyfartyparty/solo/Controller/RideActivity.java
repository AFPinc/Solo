package artyfartyparty.solo.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import artyfartyparty.solo.Model.Ride;
import artyfartyparty.solo.Model.User;
import artyfartyparty.solo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Sigurlaug on 21/03/2018.
 */

public class RideActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView locationFrom;
    TextView locationTo;
    TextView timeFrom;
    TextView timeTo;
    TextView username;
    TextView phoneNumber;
    TextView email;
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationFrom = findViewById(R.id.location_from);
        locationTo = findViewById(R.id.location_to);
        timeFrom = findViewById(R.id.time_from);
        timeTo = findViewById(R.id.time_to);
        username = findViewById(R.id.user_name);
        phoneNumber = findViewById(R.id.phone_number);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);

        long userId = getIntent().getLongExtra("userId", -1);
        long rideId = getIntent().getLongExtra("rideId", -1);

        setRideAndUserInfo(userId, rideId);
    }

    private void setRideAndUserInfo(long userId, long rideId) {
        String url = "https://solo-web-service.herokuapp.com/ride/" + rideId;
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            final Context context = this;

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    Log.v("Logintest", "Failure");
                    //alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    String msg = "";
                    String jsonData = response.body().string();
                    Ride r = null;
                    try {
                        r = Parser.parseSingleRideData(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final Ride ride = r;
                    if (ride == null)
                    {
                        // msg = "Login failed";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Could not get ride", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("hæ", "" + ride.getUser().getPhoneNumber());
                                locationFrom.setText(ride.getLocationFrom().getName());
                                locationTo.setText(ride.getLocationTo().getName());
                                timeFrom.setText(ride.getDateFrom().toString());
                                timeTo.setText(ride.getDateTo().toString());
                                username.setText(ride.getUser().getName());
                                phoneNumber.setText("" + ride.getUser().getPhoneNumber());
                                email.setText(ride.getUser().getUniMail());
                                address.setText(ride.getUser().getAddress());
                            }
                        });
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo!= null && networkInfo.isConnected()) isAvailable = true;
        return isAvailable;
    }
}
