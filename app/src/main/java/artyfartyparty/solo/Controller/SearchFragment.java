package artyfartyparty.solo.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import artyfartyparty.solo.Model.Location;
import artyfartyparty.solo.Model.Ride;
import artyfartyparty.solo.Model.User;
import artyfartyparty.solo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by valas on 3/10/2018.
 */

public class SearchFragment extends android.support.v4.app.Fragment {

    private Spinner searchFromSpinner;
    private Spinner searchToSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate( R.layout.activity_search, container, false);

        searchFromSpinner = (Spinner) view.findViewById(R.id.searchFromSpinner);
        searchToSpinner = (Spinner) view.findViewById(R.id.searchToSpinner);
        Button searchButton = (Button)view.findViewById(R.id.searchButton);
        /*
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(1, "Sigurlaug", "sth301@hi.is", "Thingas 20", 6983135, "sigurlaug");
                // Get rides
            }
        });
*/
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
                            searchFromSpinner.setAdapter(fromAdapter);

                            ArrayAdapter toAdapter = new ArrayAdapter(getActivity(),
                                    android.R.layout.simple_spinner_item, finalLocation);
                            toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            searchToSpinner.setAdapter(toAdapter);
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

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo!= null && networkInfo.isConnected()) isAvailable = true;
        return isAvailable;
    }
}
