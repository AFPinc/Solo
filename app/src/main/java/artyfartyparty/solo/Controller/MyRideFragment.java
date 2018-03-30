package artyfartyparty.solo.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
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
    private TextView LocationFrom;
    private TextView LocationTo;
    private Button CancelButton;
    private long rideId;
    private Ride ride;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate( R.layout.activty_my_ride, container, false);

        LocationFrom = view.findViewById(R.id.my_ride_location_from);
        LocationTo = view.findViewById(R.id.my_ride_location_to);
        CancelButton = view.findViewById(R.id.button_cancel);

        mRequestRecyclerView = view.findViewById(R.id.request_recycler_view);
        mRequestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        rideId = getArguments().getLong("rideId", -1);

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelRide();
            }
        });
        updateId();
        getRequests();

        return view;
    }

    private void updateId() {
        String url = "https://solo-web-service.herokuapp.com/ride/" + rideId;

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
                    try {
                        ride = Parser.parseSingleRideData(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final Ride finalRide = ride;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LocationFrom.setText(finalRide.getLocationFrom().getName());
                            LocationTo.setText(finalRide.getLocationTo().getName());
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void CancelRide() {
        String url = "https://solo-web-service.herokuapp.com/ride/cancel";

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            String locationF = "{\"id\":\"" + ride.getLocationFrom().getId() + "\", \"name\":\"" + ride.getLocationFrom().getName() + "\"}";
            String locationT = "{\"id\":\"" + ride.getLocationTo().getId() + "\", \"name\":\"" + ride.getLocationTo().getName() + "\"}";
            String rideUser = "{\"id\":\"" + ride.getUser().getId() + "\", " +
                    "\"name\":\"" + ride.getUser().getName() + "\", " +
                    "\"uniMail\":\"" + ride.getUser().getUniMail() + "\", " +
                    "\"address\":\"" + ride.getUser().getAddress() + "\", " +
                    "\"phoneNumber\":\"" + ride.getUser().getPhoneNumber() + "\", " +
                    "\"password\":\"" + ride.getUser().getPassword() + "\"}";
            String jsonRide = "{\"id\":\"" + ride.getId() + "\", " +
                    "\"locationFrom\":" + locationF + ", " +
                    "\"locationTo\":" + locationT + ", " +
                    "\"fromDate\":\"" + ride.getDateFrom().getTime() + "\", " +
                    "\"toDate\":\"" + ride.getDateTo().getTime() + "\", " +
                    "\"deleted\":\"" + ride.isDeleted() + "\", " +
                    "\"user\":" + rideUser + "}";

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonRide);

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
                    Log.v("Tókst", "Villa!");
                    //alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.v("Tókst", response.body().string());
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void getRequests() {
        String url = "https://solo-web-service.herokuapp.com/request/byRide/" + rideId;

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
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void rejectRequest(artyfartyparty.solo.Model.Request req) {
        String url = "https://solo-web-service.herokuapp.com/request/rejct";

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            String locationF = "{\"id\":\"" + req.getRide().getLocationFrom().getId() + "\", \"name\":\"" + req.getRide().getLocationFrom().getName() + "\"}";
            String locationT = "{\"id\":\"" + req.getRide().getLocationTo().getId() + "\", \"name\":\"" + req.getRide().getLocationTo().getName() + "\"}";
            String rideUser = "{\"id\":\"" + req.getRide().getUser().getId() + "\", " +
                    "\"name\":\"" + req.getRide().getUser().getName() + "\", " +
                    "\"uniMail\":\"" + req.getRide().getUser().getUniMail() + "\", " +
                    "\"address\":\"" + req.getRide().getUser().getAddress() + "\", " +
                    "\"phoneNumber\":\"" + req.getRide().getUser().getPhoneNumber() + "\", " +
                    "\"password\":\"" + req.getRide().getUser().getPassword() + "\"}";
            String requestUser = "{\"id\":\"" + req.getUser().getId() + "\", " +
                    "\"name\":\"" + req.getUser().getName() + "\", " +
                    "\"uniMail\":\"" + req.getUser().getUniMail() + "\", " +
                    "\"address\":\"" + req.getUser().getAddress() + "\", " +
                    "\"phoneNumber\":\"" + req.getUser().getPhoneNumber() + "\", " +
                    "\"password\":\"" + req.getUser().getPassword() + "\"}";
            String requestRide = "{\"id\":\"" + req.getRide().getId() + "\", " +
                    "\"locationFrom\":" + locationF + ", " +
                    "\"locationTo\":" + locationT + ", " +
                    "\"fromDate\":\"" + req.getRide().getDateFrom().getTime() + "\", " +
                    "\"toDate\":\"" + req.getRide().getDateTo().getTime() + "\", " +
                    "\"deleted\":\"" + req.getRide().isDeleted() + "\", " +
                    "\"user\":" + rideUser + "}";

            String jsonReq = "{\"id\":" + req.getId() + ", " +
                    "\"ride\":" + requestRide + ", " +
                    "\"user\":" + requestUser + ", " +
                    "\"rejected\":" + req.isRejected() + ", " +
                    "\"accepted\":" + req.isAccepted() +"}";

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonReq);

            Request request = new Request.Builder()
                    .url(url)
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
                    Log.v("Tókst", "Villa!");
                    //alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.v("Tókst", response.body().string());
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void acceptRequest(artyfartyparty.solo.Model.Request req) {
        String url = "https://solo-web-service.herokuapp.com/request/accept";

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            String locationF = "{\"id\":\"" + req.getRide().getLocationFrom().getId() + "\", \"name\":\"" + req.getRide().getLocationFrom().getName() + "\"}";
            String locationT = "{\"id\":\"" + req.getRide().getLocationTo().getId() + "\", \"name\":\"" + req.getRide().getLocationTo().getName() + "\"}";
            String rideUser = "{\"id\":\"" + req.getRide().getUser().getId() + "\", " +
                    "\"name\":\"" + req.getRide().getUser().getName() + "\", " +
                    "\"uniMail\":\"" + req.getRide().getUser().getUniMail() + "\", " +
                    "\"address\":\"" + req.getRide().getUser().getAddress() + "\", " +
                    "\"phoneNumber\":\"" + req.getRide().getUser().getPhoneNumber() + "\", " +
                    "\"password\":\"" + req.getRide().getUser().getPassword() + "\"}";
            String requestUser = "{\"id\":\"" + req.getUser().getId() + "\", " +
                    "\"name\":\"" + req.getUser().getName() + "\", " +
                    "\"uniMail\":\"" + req.getUser().getUniMail() + "\", " +
                    "\"address\":\"" + req.getUser().getAddress() + "\", " +
                    "\"phoneNumber\":\"" + req.getUser().getPhoneNumber() + "\", " +
                    "\"password\":\"" + req.getUser().getPassword() + "\"}";
            String requestRide = "{\"id\":\"" + req.getRide().getId() + "\", " +
                    "\"locationFrom\":" + locationF + ", " +
                    "\"locationTo\":" + locationT + ", " +
                    "\"fromDate\":\"" + req.getRide().getDateFrom().getTime() + "\", " +
                    "\"toDate\":\"" + req.getRide().getDateTo().getTime() + "\", " +
                    "\"deleted\":\"" + req.getRide().isDeleted() + "\", " +
                    "\"user\":" + rideUser + "}";

            String jsonReq = "{\"id\":" + req.getId() + ", " +
                    "\"ride\":" + requestRide + ", " +
                    "\"user\":" + requestUser + ", " +
                    "\"rejected\":" + req.isRejected() + ", " +
                    "\"accepted\":" + req.isAccepted() +"}";

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonReq);

            Request request = new Request.Builder()
                    .url(url)
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
                    Log.v("Tókst", "Villa!");
                    //alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.v("Tókst", response.body().string());
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

    private TextView mRequestUserName;
    private TextView mRequestUserAddress;
    private TextView mRequestUserPhone;
    private Button mRequestAccept;
    private Button mRequestReject;

    private class RequestHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private artyfartyparty.solo.Model.Request mRequest;

        public RequestHolder (LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_request, parent, false));
            itemView.setOnClickListener(this);

            mRequestUserName = itemView.findViewById(R.id.request_username);
            mRequestUserAddress = itemView.findViewById(R.id.request_user_address);
            mRequestUserPhone = itemView.findViewById(R.id.request_user_phone);
            mRequestAccept = itemView.findViewById(R.id.button_accept);
            mRequestReject = itemView.findViewById(R.id.button_reject);

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
}
