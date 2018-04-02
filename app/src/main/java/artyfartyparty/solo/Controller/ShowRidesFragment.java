package artyfartyparty.solo.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import artyfartyparty.solo.Model.Ride;
import artyfartyparty.solo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowRidesFragment extends Fragment {
    private RecyclerView mRideRecyclerView;
    private RideAdapter mAdapter;
    Toolbar toolbar;
    private long userId;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_allrides, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mRideRecyclerView = view.findViewById(R.id.ride_recycler_view);
        mRideRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userId = getArguments().getLong("userId", -1);
        url = getArguments().getString("url");

        return view;
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

    private void updateUI() {
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
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    String jsonData = response.body().string();
                    ArrayList<Ride> rides = new ArrayList<Ride>();
                    try {
                        Log.v("Hæ", jsonData);
                        rides = Parser.parseRideData(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final ArrayList<Ride> finalRides = rides;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new RideAdapter(finalRides);
                            mRideRecyclerView.setAdapter(mAdapter);
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

    private TextView mRideFrom;
    private TextView mRideTo;
    private TextView mRideDate;

    public Context getApplicationContext() {
        return getActivity().getApplication().getApplicationContext();
    }

    private class RideHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private Ride mRide;

        public RideHolder (LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_ride, parent, false));
            itemView.setOnClickListener(this);

            mRideFrom = itemView.findViewById(R.id.ride_from);
            mRideTo = itemView.findViewById(R.id.ride_to);
            mRideDate = itemView.findViewById(R.id.ride_date);

        }
        public void bind (Ride ride) {
            mRide = ride;
            mRideFrom.setText(mRide.getLocationFrom().getName());
            mRideTo.setText(mRide.getLocationTo().getName());
            mRideDate.setText(mRide.getDateFrom().toString());
        }

        @Override
        public void onClick(View view) {
            Intent startIntent = new Intent(getApplicationContext(), MyRideActivity.class);
            startIntent.putExtra("userId", userId);
            startIntent.putExtra("rideId", mRide.getId());
            startActivity(startIntent);
        }

    }
    private class RideAdapter extends RecyclerView.Adapter<RideHolder> {
        private List<Ride> mRides;
        public RideAdapter(List<Ride> rides) {
            mRides = rides;
        }
        @Override
        public RideHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RideHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder (RideHolder holder, int position) {
            Ride crime = mRides.get(position);
            holder.bind(crime);
        }
        @Override
        public int getItemCount() {
            return mRides.size();
        }
    }
}
