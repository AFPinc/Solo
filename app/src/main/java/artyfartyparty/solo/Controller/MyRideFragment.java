package artyfartyparty.solo.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import artyfartyparty.solo.Model.Ride;
import artyfartyparty.solo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyRideFragment extends Fragment{
    private RecyclerView mRequestRecyclerView;
    private RequestAdapter mAdapter;
    private AcceptedRequestAdapter mAcceptedAdapter;
    private TextView LocationFrom;
    private TextView LocationTo;
    private TextView TimeFrom;
    private TextView TimeTo;
    private Button CancelButton;
    private Button CurrReqButton;
    private Button AcceptedReqButton;
    private Toolbar toolbar;
    private long rideId;
    private Ride ride;
    private long userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate( R.layout.activity_my_ride, container, false);

        LocationFrom = view.findViewById(R.id.my_ride_location_from);
        LocationTo = view.findViewById(R.id.my_ride_location_to);
        TimeFrom = view.findViewById(R.id.my_ride_location_from_time);
        TimeTo = view.findViewById(R.id.my_ride_location_to_time);
        CancelButton = view.findViewById(R.id.button_cancel);
        CurrReqButton = view.findViewById(R.id.button_current_requests);
        AcceptedReqButton = view.findViewById(R.id.button_accepted_requests);

        mRequestRecyclerView = view.findViewById(R.id.request_recycler_view);
        mRequestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        rideId = getArguments().getLong("rideId", -1);
        userId = getArguments().getLong("userId", -1);

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelRide();
            }
        });
        CurrReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRequests();
            }
        });
        AcceptedReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAcceptedRequests();
            }
        });

        updateId();
        getRequests();

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
        return super.onOptionsItemSelected(item);
    }

    private void updateId() {
        String url = getResources().getString(R.string.ride_by_ride_id) + rideId;
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
                    try {
                        ride = Parser.parseSingleRideData(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final Ride finalRide = ride;
                    getActivity().runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            LocationFrom.setText(finalRide.getLocationFrom().getName());
                            TimeFrom.setText(finalRide.getDateFrom().toInstant()
                                    .atOffset( ZoneOffset.UTC )
                                    .format( DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm" ) ));
                            LocationTo.setText(finalRide.getLocationTo().getName());
                            TimeTo.setText(finalRide.getDateTo().toInstant()
                                    .atOffset( ZoneOffset.UTC )
                                    .format( DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm" ) ));
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void CancelRide() {
        String url = getResources().getString(R.string.cancel_ride);

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String json = Parser.parseRideToJSON(ride);
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(url)
                    .delete(body)
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
                    Intent intent = new Intent(getActivity().getApplicationContext(), MyProfileActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
            });
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("ResourceType")
    private void getRequests() {
        AcceptedReqButton.setBackgroundColor(Color.parseColor(getString(R.color.soloBlue)));
        AcceptedReqButton.setTextColor(Color.parseColor(getString(R.color.soloWhite)));
        CurrReqButton.setBackgroundColor(Color.parseColor(getString(R.color.soloWhite)));
        CurrReqButton.setTextColor(Color.parseColor(getString(R.color.soloBlue)));

        String url = getResources().getString(R.string.request_by_ride) + rideId;

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
                    List<artyfartyparty.solo.Model.Request> requests = new ArrayList<>();
                    try {
                        requests = Parser.parsRequestData(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final List<artyfartyparty.solo.Model.Request> finalRequests = requests;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        mAdapter = new RequestAdapter(finalRequests);
                        mRequestRecyclerView.setAdapter(mAdapter);
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("ResourceType")
    private void getAcceptedRequests() {
        CurrReqButton.setBackgroundColor(Color.parseColor(getString(R.color.soloBlue)));
        CurrReqButton.setTextColor(Color.parseColor(getString(R.color.soloWhite)));
        AcceptedReqButton.setBackgroundColor(Color.parseColor(getString(R.color.soloWhite)));
        AcceptedReqButton.setTextColor(Color.parseColor(getString(R.color.soloBlue)));

        String url = getResources().getString(R.string.accepted_request_by_ride) + rideId;

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
                    List<artyfartyparty.solo.Model.Request> requests = new ArrayList<>();
                    try {
                        requests = Parser.parsRequestData(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final List<artyfartyparty.solo.Model.Request> finalRequests = requests;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        mAcceptedAdapter = new AcceptedRequestAdapter(finalRequests);
                        mRequestRecyclerView.setAdapter(mAcceptedAdapter);
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void rejectRequest(artyfartyparty.solo.Model.Request req) {
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String json = Parser.parseRequestToJSON(req);
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.reject_request))
                    .put(body)
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
                    getRequests();
                }
            });
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void acceptRequest(artyfartyparty.solo.Model.Request req) {
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String json = Parser.parseRequestToJSON(req);
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.accept_request))
                    .put(body)
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
                    getRequests();
                }
            });
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
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

    private class RequestHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private TextView mRequestUserName;
        private TextView mRequestUserAddress;
        private TextView mRequestUserPhone;

        private artyfartyparty.solo.Model.Request mRequest;

        public RequestHolder (LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_request, parent, false));
            itemView.setOnClickListener(this);

            mRequestUserName = itemView.findViewById(R.id.request_username);
            mRequestUserAddress = itemView.findViewById(R.id.request_user_address);
            mRequestUserPhone = itemView.findViewById(R.id.request_user_phone);
            Button mRequestAccept = itemView.findViewById(R.id.button_accept);
            Button mRequestReject = itemView.findViewById(R.id.button_reject);

            mRequestAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("ReqId", "" + mRequest.getId());
                    acceptRequest(mRequest);
                }
            });

            mRequestReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rejectRequest(mRequest);
                }
            });
        }
        public void bind (artyfartyparty.solo.Model.Request request) {
            mRequest = request;
            mRequestUserName.setText(mRequest.getUser().getName());
            mRequestUserAddress.setText(mRequest.getUser().getAddress());
            mRequestUserPhone.setText("" +  mRequest.getUser().getPhoneNumber());
        }

        @Override
        public void onClick(View v) {

        }
    }
    private class RequestAdapter extends RecyclerView.Adapter<RequestHolder> {
        private List<artyfartyparty.solo.Model.Request> mRequests;
        public RequestAdapter(List<artyfartyparty.solo.Model.Request> requests) {
            mRequests = requests;
        }
        @Override
        public RequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RequestHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder (RequestHolder holder, int position) {
            artyfartyparty.solo.Model.Request req = mRequests.get(position);
            holder.bind(req);
        }
        @Override
        public int getItemCount() {
            return mRequests.size();
        }
    }

    private class AcceptedRequestHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private artyfartyparty.solo.Model.Request mRequest;
        private TextView mAcceptedRequestUserName;
        private TextView mAcceptedRequestUserAddress;
        private TextView mAcceptedRequestUserPhone;

        public AcceptedRequestHolder (LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_accepted_request, parent, false));
            itemView.setOnClickListener(this);

            mAcceptedRequestUserName = itemView.findViewById(R.id.accepted_request_username);
            mAcceptedRequestUserAddress = itemView.findViewById(R.id.accepted_request_user_address);
            mAcceptedRequestUserPhone = itemView.findViewById(R.id.accepted_request_user_phone);
        }
        public void bind (artyfartyparty.solo.Model.Request request) {
            mRequest = request;
            mAcceptedRequestUserName.setText(mRequest.getUser().getName());
            mAcceptedRequestUserAddress.setText(mRequest.getUser().getAddress());
            mAcceptedRequestUserPhone.setText("" +  mRequest.getUser().getPhoneNumber());
        }

        @Override
        public void onClick(View v) {

        }
    }
    private class AcceptedRequestAdapter extends RecyclerView.Adapter<AcceptedRequestHolder> {
        private List<artyfartyparty.solo.Model.Request> mAcceptedRequests;
        public AcceptedRequestAdapter(List<artyfartyparty.solo.Model.Request> requests) {
            mAcceptedRequests = requests;
        }
        @Override
        public AcceptedRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new AcceptedRequestHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder (AcceptedRequestHolder holder, int position) {
            artyfartyparty.solo.Model.Request req = mAcceptedRequests.get(position);
            holder.bind(req);
        }
        @Override
        public int getItemCount() {
            return mAcceptedRequests.size();
        }
    }
}
