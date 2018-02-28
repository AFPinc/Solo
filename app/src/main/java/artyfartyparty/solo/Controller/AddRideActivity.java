package artyfartyparty.solo.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import artyfartyparty.solo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that controlls adding a trip
 */

public class AddRideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtrip);

        Spinner fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
        Spinner toSpinner = (Spinner) findViewById(R.id.toSpinner);
        Spinner stopoverSpinner = (Spinner) findViewById(R.id.stopoverSpinner);
        Button stopoverButton = (Button)findViewById(R.id.stopoverButton);
        Button addButton = (Button)findViewById(R.id.addButton);

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(AddRideActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(AddRideActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        ArrayAdapter<String> stopoverAdapter = new ArrayAdapter<String>(AddRideActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        stopoverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stopoverSpinner.setAdapter(stopoverAdapter);

    }

    private void addRide(String locationFrom, String locationTo, String dateFrom, String dateTo) {
        String url = "https://solo-web-service.herokuapp.com/ride/add";
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            if (TextUtils.isEmpty(locationFrom)) {
                //locationFrom is empty
                Toast.makeText(this, "Please enter starting location", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if (TextUtils.isEmpty(locationTo)) {
                //locationTo is empty
                Toast.makeText(this, "Please enter final destination", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if (TextUtils.isEmpty(dateFrom)) {
                //dateFrom is empty
                Toast.makeText(this, "Please enter time of departure", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if (TextUtils.isEmpty(dateTo)) {
                //dateTo is empty
                Toast.makeText(this, "Please enter time of arrival", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON,
                    "{\"locationFrom\":\"" + locationFrom + "\", " +
                    "\"locationTo\":\"" + locationTo + "\", " +
                    "\"dateFrom\":\"" + dateFrom + "\", " +
                    "\"dateTo\": \"" + dateTo + "\"}");

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Sign up successful!",
                                    Toast.LENGTH_LONG).show();
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


                        }
                    });
                    Log.v("Tókst", response.body().string());
                }
            });
            Toast.makeText(this, "Added user", Toast.LENGTH_LONG).show();
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