package artyfartyparty.solo.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import artyfartyparty.solo.Model.Location;
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

import static android.R.layout.simple_list_item_1;
import static artyfartyparty.solo.Controller.DatePickerFragment.*;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Fragment that controls adding a ride
 */

public class AddRideFragment extends android.support.v4.app.Fragment {

    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private Spinner stopoverSpinner;

    private Button fromAtButton;
    private Button toAtButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_addride, container, false);

        fromSpinner = (Spinner) view.findViewById(R.id.fromSpinner);
        toSpinner = (Spinner) view.findViewById(R.id.toSpinner);
        stopoverSpinner = (Spinner) view.findViewById(R.id.stopoverSpinner);
        Button stopoverButton = (Button)view.findViewById(R.id.stopoverButton);
        Button addButton = (Button)view.findViewById(R.id.addButton);
        fromAtButton = (Button)view.findViewById(R.id.fromAtButton);
        updateFromDate();

        fromAtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(new Date());
                dialog.setTargetFragment(AddRideFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(1, "Sigurlaug", "sth301@hi.is", "Thingas 20", 6983135, "sigurlaug");
                addRide(fromSpinner.getSelectedItem().toString(), toSpinner.getSelectedItem().toString(), new Date().toString(), new Date().toString(), user);
            }
        });

        toAtButton = (Button)view.findViewById(R.id.toAtButton);
        updateToDate();
        toAtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(new Date());
                dialog.setTargetFragment(AddRideFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        setUpSpinners();

        return view;
    }

    private void setUpSpinners(){
        String url = "https://solo-web-service.herokuapp.com/location/all";
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    Log.v("Tókst", "Villa!");
                    //alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonData = response.body().string();
                    Location[] locations = new Location[0];
                    try {
                        locations = Parser.parseLocationDataArray(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final Location[] finalLocation = locations;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter fromAdapter = new ArrayAdapter(getActivity(),
                                    android.R.layout.simple_spinner_item, finalLocation);
                            fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            fromSpinner.setAdapter(fromAdapter);

                            ArrayAdapter toAdapter = new ArrayAdapter(getActivity(),
                                    android.R.layout.simple_spinner_item, finalLocation);
                            toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            toSpinner.setAdapter(toAdapter);

                            ArrayAdapter stopoverAdapter = new ArrayAdapter(getActivity(),
                                    android.R.layout.simple_spinner_item, finalLocation);
                            stopoverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            stopoverSpinner.setAdapter(stopoverAdapter);

                        }
                    });
                    Log.v("Tókst", "hæ");
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra( EXTRA_DATE);
            new Date();
            updateFromDate();
            updateToDate();
        }
    }

    private void updateFromDate() {
        fromAtButton.setText(new Date().toString());
    }

    private void updateToDate() {
        toAtButton.setText(new Date().toString());
    }

    private void addRide (String locationFrom, String locationTo, String timeFrom, String timeTo, User user) {
        String url = "https://solo-web-service.herokuapp.com/ride/add";
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            if(TextUtils.isEmpty(locationFrom)) {
                //email is empty
                Toast.makeText(getActivity(), "Please enter starting point", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if(TextUtils.isEmpty(locationTo)) {
                //email is empty
                Toast.makeText(getActivity(), "Please enter destination", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if(TextUtils.isEmpty(timeFrom)) {
                //email is empty
                Toast.makeText(getActivity(), "Please enter leaving time", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if(TextUtils.isEmpty(timeTo)) {
                //email is empty
                Toast.makeText(getActivity(), "Please enter arrival time", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if(TextUtils.isEmpty(user.getName())) {
                //email is empty
                Toast.makeText(getActivity(), "Please enter arrival time", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            String locationF = "{\"name\":\"" + locationFrom + "\"}";
            String locationT = "{\"name\":\"" + locationTo + "\"}";
            String u = "{\"id\":\"" + user.getId() + "\", " +
                    "\"name\":\"" + user.getName() + "\", " +
                    "\"uniMail\":\"" + user.getUniMail() + "\", " +
                    "\"address\":\"" + user.getAddress() + "\", " +
                    "\"phoneNumber\":\"" + user.getPhoneNumber() + "\", " +
                    "\"password\":\"" + user.getPassword() + "\"}";
            String bla = "{\"locationFrom\":" + locationF + ", " +
                    "\"locationTo\":" + locationT + ", " +
                    "\"timeFrom\":\"" + timeFrom + "\", " +
                    "\"timeTo\":\"" + timeFrom + "\", " +
                    "\" user\":" + u + "}";

            String json = "{\"locationFrom\":{\"id\":\"4\", " +
                                             "\"name\": \"Árbær\"}, " +
                           "\"locationTo\":{\"id\":\"3\", " +
                                           "\"name\":\"Háskóli Íslands\"}, " +
                           "\"timeFrom\":" + new Date().toString() + ", " +
                           "\"timeTo\":" + new Date().toString() + ", " +
                           "\"user\":{\"id\":\"1\", " +
                                     "\"name\":\"Sigurlaug\"}, " +
                                     "\"uniMail\":\"sth301\", " +
                                     "\"address\":\"Þingás 20\", " +
                                     "\"phoneNumber\":\"6983135\", " +
                                     "\"password\":\"s\"}";
            RequestBody body = RequestBody.create(JSON,json
                    );

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    Log.v("Tókst", "Villa!");
                    //alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    Log.v("Tókst", response.body().string());
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo!= null && networkInfo.isConnected()) isAvailable = true;
        return isAvailable;
    }

}
