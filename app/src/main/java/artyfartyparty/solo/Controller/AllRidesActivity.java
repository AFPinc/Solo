package artyfartyparty.solo.Controller;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import artyfartyparty.solo.Model.Location;
import artyfartyparty.solo.Model.Ride;
import artyfartyparty.solo.Model.User;
import artyfartyparty.solo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllRidesActivity extends Fragment{
    private RecyclerView mRideRecyclerView;
    //private RideAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_allrides, container, false);

        mRideRecyclerView = (RecyclerView) view.findViewById(R.id.ride_recycler_view);
        mRideRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI() {
        String url = "https://solo-web-service.herokuapp.com/rides/all";
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            final Context context = getActivity();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    String msg = "";
                    String jsonData = response.body().string();
                    User user = null;

                    final String finalMsg = msg;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, finalMsg, Toast.LENGTH_LONG).show();
                        }
                    });

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

    private Ride[] parseRideData(String jsonData) throws JSONException {
        ArrayList<Ride> rides = new ArrayList();
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++){

            JSONObject json = jsonArray.getJSONObject(i);

            Ride ride = new Ride();
            String idString = json.getString("id");
            long id = Integer.parseInt(idString);
            long dateFrom = Integer.parseInt(json.getString("dateFrom"));
            long dateTo = Integer.parseInt(json.getString("dateTo"));

            ride.setId(id);
            ride.setUser(parseUserData(json.getString("user")));
            ride.setLocationFrom(parseLocationData(json.getString("locationFrom")));
            ride.setLocationTo(parseLocationData(json.getString("locationTo")));
            ride.setDateFrom(new Date(dateFrom));
            ride.setDateTo(new Date(dateTo));

            rides.add(ride);
        }
        return (Ride[]) rides.toArray();
    }

    private User parseUserData(String jsonData) throws JSONException {
        JSONObject json = new JSONObject(jsonData);
        User user = new User();
        String idString = json.getString("id");
        int id = Integer.parseInt(idString);
        user.setId(id);
        user.setName(json.getString("name"));
        user.setAddress(json.getString("address"));
        user.setPassword(json.getString("password"));
        user.setPhoneNumber(Integer.parseInt(json.getString("phoneNumber")));
        user.setUniMail(json.getString("uniMail"));
        return user;
    }

    private Location parseLocationData(String jsonData) throws JSONException{
        JSONObject json = new JSONObject(jsonData);
        Location location = new Location();
        String idString = json.getString("id");
        long id = Integer.parseInt(idString);
        location.setId(id);
        location.setName(json.getString("name"));
        return location;
    }
}