package artyfartyparty.solo.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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
    private Toolbar toolbar;
    private TextView locationFrom;
    private TextView locationTo;
    private TextView timeFrom;
    private TextView timeTo;
    private TextView username;
    private TextView phoneNumber;
    private TextView email;
    private TextView address;

    private User user;
    private Ride ride;
    private long userId;
    private long rideId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        toolbar = findViewById(R.id.toolbar);
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

        userId = getIntent().getLongExtra("userId", -1);
        rideId = getIntent().getLongExtra("rideId", -1);

        setRideAndUserInfo(rideId);

        UserData userData = UserDataDB.get(getApplication().getApplicationContext()).getUserData();
        user = userData.findOne(userId);

        requestRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerRide();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logo_home:
                intent = new Intent(getApplicationContext(), AllRidesActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.add_ride:
                intent = new Intent(getApplicationContext(), AddRideActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.search:
                intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.profile:
                intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerRide () {
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            artyfartyparty.solo.Model.Request req = new artyfartyparty.solo.Model.Request();
            req.setUser(user);
            req.setRide(ride);
            req.setRejected(false);
            req.setAccepted(false);
            req.setId(0);

            String json = Parser.parseRequestToJSON(req);
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.add_request))
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
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, getResources().getString(R.string.send_request), Toast.LENGTH_LONG).show();
                            Intent startIntent = new Intent(getApplicationContext(), AllRidesActivity.class);
                            startIntent.putExtra("userId", user.getId());
                            startActivity(startIntent);
                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void setRideAndUserInfo(long rideId) {
        String url = getResources().getString(R.string.ride_by_ride_id) + rideId;
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
                }
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    String jsonData = response.body().string();
                    ride = null;
                    try {
                        ride = Parser.parseSingleRideData(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (ride == null)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, getResources().getString(R.string.get_ride_failed), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {
                                locationFrom.setText(ride.getLocationFrom().getName());
                                locationTo.setText(ride.getLocationTo().getName());
                                timeFrom.setText(ride.getDateFrom().toInstant()
                                        .atOffset( ZoneOffset.UTC )
                                        .format( DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm" ) ));
                                timeTo.setText(ride.getDateTo().toInstant()
                                        .atOffset( ZoneOffset.UTC )
                                        .format( DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm" ) ));
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
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
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
