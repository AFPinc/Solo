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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import artyfartyparty.solo.Model.Ride;
import artyfartyparty.solo.Model.User;
import artyfartyparty.solo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

    User user;
    Ride ride;

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
        address = findViewById(R.id.userAddress);

        Button requestRideButton = findViewById(R.id.request_ride_button);

        final long userId = getIntent().getLongExtra("userId", -1);
        final long rideId = getIntent().getLongExtra("rideId", -1);

        setRideAndUserInfo(userId, rideId);

        UserData userData = UserDataDB.get(getApplication().getApplicationContext()).getUserData();
        user = userData.findOne(userId);

        requestRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerRide();
            }
        });
    }

    private void registerRide () {
        String url = "https://solo-web-service.herokuapp.com/request/add";
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            String locationF = "{\"id\":\"" + ride.getLocationFrom().getId() + "\", \"name\":\"" + ride.getLocationFrom().getName() + "\"}";
            String locationT = "{\"id\":\"" + ride.getLocationTo().getId() + "\", \"name\":\"" + ride.getLocationTo().getName() + "\"}";
            String rideUser = "{\"id\":\"" + ride.getUser().getId() + "\", " +
                    "\"name\":\"" + ride.getUser().getName() + "\", " +
                    "\"uniMail\":\"" + ride.getUser().getUniMail() + "\", " +
                    "\"address\":\"" + ride.getUser().getAddress() + "\", " +
                    "\"phoneNumber\":\"" + ride.getUser().getPhoneNumber() + "\", " +
                    "\"password\":\"" + ride.getUser().getPassword() + "\"}";
            String requestUser = "{\"id\":\"" + user.getId() + "\", " +
                    "\"name\":\"" + user.getName() + "\", " +
                    "\"uniMail\":\"" + user.getUniMail() + "\", " +
                    "\"address\":\"" + user.getAddress() + "\", " +
                    "\"phoneNumber\":\"" + user.getPhoneNumber() + "\", " +
                    "\"password\":\"" + user.getPassword() + "\"}";
            String requestRide = "{\"id\":\"" + ride.getId() + "\", " +
                    "\"locationFrom\":" + locationF + ", " +
                    "\"locationTo\":" + locationT + ", " +
                    "\"fromDate\":\"" + ride.getDateFrom().getTime() + "\", " +
                    "\"toDate\":\"" + ride.getDateTo().getTime() + "\", " +
                    "\"user\":" + rideUser + "}";

            String req = "{\"ride\":" + requestRide + ", " +
                    "\"user\":" + requestUser + "}";

            Log.v("json", req);
            RequestBody body = RequestBody.create(JSON, req);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
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
                    Log.v("Tókst", "Villa!");
                    //alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Your request has been sent", Toast.LENGTH_LONG).show();
                            Intent startIntent = new Intent(getApplicationContext(), AllRidesActivity.class);
                            startIntent.putExtra("userId", user.getId());
                            startActivity(startIntent);
                        }
                    });
                    Log.v("Tókst", response.body().string());
                }
            });
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
        }
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
                    ride = null;
                    try {
                        ride = Parser.parseSingleRideData(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
