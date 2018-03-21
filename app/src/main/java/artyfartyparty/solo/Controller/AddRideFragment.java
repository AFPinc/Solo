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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
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

import static artyfartyparty.solo.Controller.DatePickerFragment.EXTRA_DATE;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Fragment for adding a ride
 */

public class AddRideFragment extends android.support.v4.app.Fragment {

    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private Spinner stopoverSpinner;

    private Ride ride;
    private Button fromAtButton;
    private Button toAtButton;
    private boolean fromClicked;
    private boolean toClicked;
    private long userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate( R.layout.activity_addride, container, false);

        fromSpinner = (Spinner) view.findViewById(R.id.fromSpinner);
        toSpinner = (Spinner) view.findViewById(R.id.toSpinner);
        stopoverSpinner = (Spinner) view.findViewById(R.id.stopoverSpinner);
        Button stopoverButton = (Button)view.findViewById(R.id.stopoverButton);
        Button addButton = (Button)view.findViewById(R.id.addButton);
        fromAtButton = (Button)view.findViewById(R.id.fromAtButton);
        ride = new Ride();
        ride.setDateTo( new Date() );
        ride.setDateFrom( new Date() );
        updateFromDate();
        fromClicked = false;
        toClicked = false;

        userId = getArguments().getLong("userId", -1);
        Log.v("uid", "" + userId);
        UserData userData = UserDataDB.get(getActivity().getApplication().getApplicationContext()).getUserData();
        final User user = userData.findOne(userId);
        Log.v("UserID", "" + user.getId());

        fromAtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromClicked = true;
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(ride.getDateFrom());
                dialog.setTargetFragment(AddRideFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRide(user);
                Bundle bundle = new Bundle();
                bundle.putLong("userId", userId);
                AllRidesFragment fragment = new AllRidesFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                                    .replace( R.id.fragment_container, fragment )
                                    .addToBackStack( null ).commit();
            }
        });

        toAtButton = (Button)view.findViewById(R.id.toAtButton);
        updateToDate();
        toAtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toClicked = true;
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(ride.getDateTo());
                dialog.setTargetFragment(AddRideFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        setUpSpinners();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg=" ";
        switch (item.getItemId()) {
            case R.id.add_ride:
                startActivity(new Intent(getActivity().getApplicationContext(), AddRideActivity.class));
                msg = "Add Ride";
                break;
            case R.id.search:
                startActivity(new Intent(getActivity().getApplicationContext(), SearchActivity.class));
                msg = "Search";
                break;
            case R.id.settings:
                msg = "Settings";
                break;
            case R.id.about:
                msg = "About";
                break;
        }
        return true;
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
            if (fromClicked) {
                ride.setDateFrom( date );
            }
            if (toClicked) {
                ride.setDateTo( date );
            }
            updateFromDate();
            updateToDate();
        }
        fromClicked = false;
        toClicked = false;
    }

    private void updateFromDate() {

        fromAtButton.setText(ride.getDateFrom().toString());
    }

    private void updateToDate() {

        toAtButton.setText(ride.getDateTo().toString());
    }

    private void addRide (User user) {
        String url = "https://solo-web-service.herokuapp.com/ride/add";
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            Location locationFrom = (Location) fromSpinner.getSelectedItem();
            Location locationTo = (Location) toSpinner.getSelectedItem();
            Date fromDate = ride.getDateFrom();
            Date toDate = ride.getDateTo();

            if(TextUtils.isEmpty(locationFrom.toString())) {
                //email is empty
                Toast.makeText(getActivity(), "Please enter starting point", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if(TextUtils.isEmpty(locationTo.toString())) {
                //email is empty
                Toast.makeText(getActivity(), "Please enter destination", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if(TextUtils.isEmpty(fromDate.toString())) {
                //email is empty
                Toast.makeText(getActivity(), "Please enter leaving time", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if(TextUtils.isEmpty(toDate.toString())) {
                //email is empty
                Toast.makeText(getActivity(), "Please enter arrival time", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if(TextUtils.isEmpty(user.getName())) {
                //email is empty
                Toast.makeText(getActivity(), "Please sign in as a user", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            String locationF = "{\"id\":\"" + locationFrom.getId() + "\", \"name\":\"" + locationFrom.getName() + "\"}";
            String locationT = "{\"id\":\"" + locationTo.getId() + "\", \"name\":\"" + locationTo.getName() + "\"}";
            String u = "{\"id\":\"" + user.getId() + "\", " +
                    "\"name\":\"" + user.getName() + "\", " +
                    "\"uniMail\":\"" + user.getUniMail() + "\", " +
                    "\"address\":\"" + user.getAddress() + "\", " +
                    "\"phoneNumber\":\"" + user.getPhoneNumber() + "\", " +
                    "\"password\":\"" + user.getPassword() + "\"}";
            String json = "{\"locationFrom\":" + locationF + ", " +
                    "\"locationTo\":" + locationT + ", " +
                    "\"fromDate\":\"" + fromDate.getTime() + "\", " +
                    "\"toDate\":\"" + toDate.getTime() + "\", " +
                    "\"user\":" + u + "}";

            String bla = "{\"locationFrom\":{\"id\":\"4\", " +
                                             "\"name\": \"Árbær\"}, " +
                           "\"locationTo\":{\"id\":\"3\", " +
                                           "\"name\":\"Háskóli Íslands\"}, " +
                           "\"timeFrom\":" + new Date().getTime() + ", " +
                           "\"timeTo\":" + new Date().getTime() + ", " +
                           "\"user\":{\"id\":\"8\", " +
                                     "\"name\":\"Sigurlaug\"}, " +
                                     "\"uniMail\":\"sth301\", " +
                                     "\"address\":\"Þingás 20\", " +
                                     "\"phoneNumber\":\"6983135\", " +
                                     "\"password\":\"s\"}";
            RequestBody body = RequestBody.create(JSON,json);

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
