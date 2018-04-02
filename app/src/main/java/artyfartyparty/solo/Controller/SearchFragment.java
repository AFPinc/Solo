package artyfartyparty.solo.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import artyfartyparty.solo.Model.Location;
import artyfartyparty.solo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Fragment for searching a ride
 */

public class SearchFragment extends android.support.v4.app.Fragment {

    private Spinner searchFromSpinner;
    private Spinner searchToSpinner;
    private Toolbar toolbar;
    private long userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate( R.layout.activity_search, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        searchFromSpinner = view.findViewById(R.id.searchFromSpinner);
        searchToSpinner = view.findViewById(R.id.searchToSpinner);
        Button searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        userId = getArguments().getLong("userId", -1);

        setUpSpinners();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpSpinners(){
        String url = getResources().getString(R.string.all_locations_url);
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
                            searchFromSpinner.setAdapter(fromAdapter);

                            ArrayAdapter<Location> toAdapter = new ArrayAdapter<>(getActivity(),
                                    android.R.layout.simple_spinner_item, finalLocation);
                            toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            searchToSpinner.setAdapter(toAdapter);
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void search() {
        Location locationFrom = (Location) searchFromSpinner.getSelectedItem();
        Location locationTo = (Location) searchToSpinner.getSelectedItem();
        String url = getResources().getString(R.string.search_rides) + locationFrom.getId()+ "/" + locationTo.getId();

        Bundle bundle = new Bundle();
        bundle.putString( "url", url);
        ShowRidesFragment fragment = new ShowRidesFragment();
        fragment.setArguments( bundle );
        getFragmentManager().beginTransaction().replace( R.id.fragment_container, fragment ).addToBackStack( null ).commit();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService( Context.CONNECTIVITY_SERVICE);
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
