package artyfartyparty.solo.Controller;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Fragment class for adding a new ride
 */

public class AddRideFragment extends android.support.v4.app.Fragment {

    private static final String STATE_LOCAL_DATE_TIME = "state.local.date.time";

    private Date mLocalDateTime = new Date();

    private Spinner fromSpinner;
    private Spinner toSpinner;
    private Ride ride;

    private Button fromAtButton;
    private Button toAtButton;
    private Button addButton;
    private Toolbar toolbar;

    private long userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate( R.layout.activity_addride, container, false);

        if (savedInstanceState != null) {
            mLocalDateTime = (Date) savedInstanceState.getSerializable(STATE_LOCAL_DATE_TIME);
        }

        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        fromSpinner = view.findViewById(R.id.fromSpinner);
        toSpinner = view.findViewById(R.id.toSpinner);
        addButton = view.findViewById(R.id.addButton);
        fromAtButton = view.findViewById(R.id.fromAtButton);
        toAtButton = view.findViewById(R.id.toAtButton);

        ride = new Ride();
        ride.setDateTo( new Date() );
        ride.setDateFrom( new Date() );

        userId = getArguments().getLong("userId", -1);
        UserData userData = UserDataDB.get(getActivity().getApplication().getApplicationContext()).getUserData();
        final User user = userData.findOne(userId);

        fromAtButton.setText( "When will you depart?" );
        toAtButton.setText( "When will you arrive?" );

        fromAtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
                Date dateToUse = (mLocalDateTime == null) ? new Date() : mLocalDateTime;
                DatePickerFragment datePickerFragment =
                        FactoryFragment.createDatePickerFragment(dateToUse, "The", DatePickerFragment.BOTH,
                                new DatePickerFragment.ResultHandler() {
                                    @Override
                                    public void setDate(Date result) {
                                        mLocalDateTime = new Date(result.getTime());
                                        ride.setDateFrom( mLocalDateTime );
                                        updateFromDate();
                                    }
                                });
                datePickerFragment.show( fragmentManager,  DatePickerFragment.DIALOG_TAG);
            }
        });

        toAtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                Date dateToUse = (mLocalDateTime == null) ? new Date() : mLocalDateTime;
                DatePickerFragment datePickerFragment =
                        FactoryFragment.createDatePickerFragment(dateToUse, "The", DatePickerFragment.BOTH,
                                new DatePickerFragment.ResultHandler() {
                                    @Override
                                    public void setDate(Date result) {
                                        mLocalDateTime = new Date(result.getTime());
                                        ride.setDateTo( mLocalDateTime );
                                        updateToDate();
                                    }
                                });
                datePickerFragment.show( fragmentManager,  DatePickerFragment.DIALOG_TAG);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRide(user);
                Bundle bundle = new Bundle();
                bundle.putLong("userId", userId);
                bundle.putString("url", getResources().getString(R.string.all_rides_url));
                ShowRidesFragment fragment = new ShowRidesFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace( R.id.fragment_container, fragment )
                        .addToBackStack( null ).commit();
            }
        });

        setUpSpinners();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.logo_home:
                intent = new Intent(getActivity().getApplicationContext(), AllRidesActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.add_ride:
                intent = new Intent(getActivity().getApplicationContext(), AddRideActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.search:
                intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.profile:
                intent = new Intent(getActivity().getApplicationContext(), MyProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
        }
        if ( intent != null){
            intent.putExtra("userId", userId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_LOCAL_DATE_TIME, mLocalDateTime); }

    private void setUpSpinners(){
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.all_locations_url))
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
                    alertUserAboutError();
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
                            ArrayAdapter<Location> fromAdapter = new ArrayAdapter<>(getActivity(),
                                    android.R.layout.simple_spinner_item, finalLocation);
                            fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            fromSpinner.setAdapter(fromAdapter);

                            ArrayAdapter<Location> toAdapter = new ArrayAdapter<>(getActivity(),
                                    android.R.layout.simple_spinner_item, finalLocation);
                            toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            toSpinner.setAdapter(toAdapter);

                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void addRide (User user) {
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            ride.setLocationFrom((Location) fromSpinner.getSelectedItem());
            ride.setLocationTo((Location) toSpinner.getSelectedItem());
            ride.setUser(user);

            if(TextUtils.isEmpty(ride.getLocationFrom().toString())) {
                Toast.makeText(getActivity(), getResources().getString(R.string.loc_from_missing), Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(ride.getLocationTo().toString())) {
                Toast.makeText(getActivity(), getResources().getString(R.string.loc_to_missing), Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(ride.getDateFrom().toString())) {
                Toast.makeText(getActivity(), getResources().getString(R.string.date_from_missing), Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(ride.getDateTo().toString())) {
                Toast.makeText(getActivity(), getResources().getString(R.string.date_to_missing), Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(ride.getUser().getName())) {
                Toast.makeText(getActivity(), getResources().getString(R.string.user_missing), Toast.LENGTH_SHORT).show();
                return;
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String json = Parser.parseRideToJSON(ride);
            RequestBody body = RequestBody.create(JSON,json);

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.add_ride_url))
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
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), getResources().getString(R.string.new_ride), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        alertUserAboutError();
                    }
                }
            });
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateFromDate() {
        fromAtButton.setText(ride.getDateFrom().toInstant()
                .atOffset( ZoneOffset.UTC )
                .format( DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm" ) ));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateToDate() {
        toAtButton.setText(ride.getDateTo().toInstant()
                .atOffset( ZoneOffset.UTC )
                .format( DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm" ) ));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo!= null && networkInfo.isConnected()) isAvailable = true;
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
