package artyfartyparty.solo.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
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
 * Fragment for profile information
 */

public class MyProfileFragment extends Fragment {

    private TextView Name;
    private TextView Email;
    private TextView Address;
    private TextView PhoneNumber;
    private Button myRidesButton;
    private Button myRequestsButton;
    private RecyclerView mRideRecyclerView;
    private MyProfileFragment.RideAdapter mAdapter;
    private MyProfileFragment.RequestAdapter mRequestAdapter;
    private Button logOutButton;
    private Toolbar toolbar;
    private long userId;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        Name = view.findViewById(R.id.name);
        Email = view.findViewById(R.id.email);
        Address = view.findViewById(R.id.address);
        PhoneNumber = view.findViewById(R.id.phoneNumber);

        userId = getArguments().getLong("userId", -1);
        Log.v("uuid", "" + userId);
        UserData userData = UserDataDB.get(getActivity().getApplication().getApplicationContext()).getUserData();
        final User user = userData.findOne(userId);

        Name.setText(user.getName());
        Email.setText(user.getUniMail());
        Address.setText(user.getAddress());
        PhoneNumber.setText("" + user.getPhoneNumber());

        myRidesButton = (Button) view.findViewById(R.id.myRidesButton);
        myRequestsButton = (Button) view.findViewById(R.id.myRequestsButton);

        mRideRecyclerView = view.findViewById(R.id.myRides_recycler_view);
        mRideRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myRidesButton.setBackgroundColor(Color.WHITE);
        myRidesButton.setTextColor(Color.rgb(0,51,79));
        myRequestsButton.setBackgroundColor(Color.rgb(0,51,79));
        myRequestsButton.setTextColor(Color.WHITE);
        url = "https://solo-web-service.herokuapp.com/ride/byUser/" + userId;
        GetMyRides();

        myRidesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == myRidesButton){
                    myRidesButton.setBackgroundColor(Color.WHITE);
                    myRidesButton.setTextColor(Color.rgb(0,51,79));
                    myRequestsButton.setBackgroundColor(Color.rgb(0,51,79));
                    myRequestsButton.setTextColor(Color.WHITE);
                }
                else if (v == myRequestsButton){
                    myRidesButton.setBackgroundColor(Color.rgb(0,51,79));
                    myRidesButton.setTextColor(Color.WHITE);
                    myRequestsButton.setBackgroundColor(Color.WHITE);
                    myRequestsButton.setTextColor(Color.rgb(0,51,79));
                }
                url = "https://solo-web-service.herokuapp.com/ride/byUser/" + userId;
                GetMyRides();
            }
        });

        myRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == myRidesButton){
                    myRidesButton.setBackgroundColor(Color.WHITE);
                    myRidesButton.setTextColor(Color.rgb(0,51,79));
                    myRequestsButton.setBackgroundColor(Color.rgb(0,51,79));
                    myRequestsButton.setTextColor(Color.WHITE);
                }
                else if (v == myRequestsButton){
                    myRidesButton.setBackgroundColor(Color.rgb(0,51,79));
                    myRidesButton.setTextColor(Color.WHITE);
                    myRequestsButton.setBackgroundColor(Color.WHITE);
                    myRequestsButton.setTextColor(Color.rgb(0,51,79));
                }

                url = "https://solo-web-service.herokuapp.com/request/byUser/" + userId;
                GetMyRequests();
            }
        });

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        logOutButton = (Button) view.findViewById(R.id.logOutButton);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(startIntent);
            }
        });

        return view;
    }

    private void GetMyRides() {
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
                            mAdapter = new MyProfileFragment.RideAdapter(finalRides);
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

    private void GetMyRequests() {
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
                    ArrayList<artyfartyparty.solo.Model.Request> requests = new ArrayList<artyfartyparty.solo.Model.Request>();
                    try {
                        Log.v("Hæ", jsonData);
                        requests = (ArrayList<artyfartyparty.solo.Model.Request>) Parser.parsRequestData(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final ArrayList<artyfartyparty.solo.Model.Request> finalRequests = requests;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRequestAdapter = new MyProfileFragment.RequestAdapter( finalRequests );
                            mRideRecyclerView.setAdapter(mRequestAdapter);
                        }
                    });

                }
            });
        }
        else {
            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
        }
    }

    Button.OnClickListener btnOnClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            Bundle bundle = getArguments();
            int userId = bundle.getInt("userId");

            if (v == myRidesButton){
                myRidesButton.setBackgroundColor(Color.RED);
                myRequestsButton.setBackgroundColor(Color.BLACK);
                }
                else {
                myRidesButton.setBackgroundColor(Color.BLACK);
                myRequestsButton.setBackgroundColor(Color.RED);
            }
            Bundle bundle1 = new Bundle();
            bundle1.putInt("userId", userId);
        }
    };

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService( Context.CONNECTIVITY_SERVICE);
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
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind (Ride ride) {
            mRide = ride;
            mRideFrom.setText(mRide.getLocationFrom().getName());
            mRideTo.setText(mRide.getLocationTo().getName());
            mRideDate.setText(mRide.getDateFrom().toInstant()
                    .atOffset( ZoneOffset.UTC )
                    .format( DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm" ) ));
        }

        @Override
        public void onClick(View view) {
            Intent startIntent = new Intent(getApplicationContext(), MyRideActivity.class);
            startIntent.putExtra("userId", userId);
            startIntent.putExtra("rideId", mRide.getId());
            startActivity(startIntent);
        }

    }

    private class RideAdapter extends RecyclerView.Adapter<MyProfileFragment.RideHolder> {
        private List<Ride> mRides;

        public RideAdapter(List<Ride> rides) {
            mRides = rides;
        }

        @Override
        public MyProfileFragment.RideHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from( getActivity() );
            return new MyProfileFragment.RideHolder( layoutInflater, parent );
        }

        @Override
        public void onBindViewHolder(MyProfileFragment.RideHolder holder, int position) {
            Ride crime = mRides.get( position );
            holder.bind( crime );
        }

        @Override
        public int getItemCount() {
            return mRides.size();
        }
    }

    private TextView mRequestFrom;
    private TextView mRequestTo;
    private TextView mRequestDate;
    private TextView mRequestDriver;
    private TextView mRequestStatus;

    private class RequestHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private artyfartyparty.solo.Model.Request mRequest;

        public RequestHolder (LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_myrequests, parent, false));
            itemView.setOnClickListener(this);

            mRequestFrom = itemView.findViewById(R.id.myRequest_from);
            mRequestTo = itemView.findViewById(R.id.myRequest_to);
            mRequestDate = itemView.findViewById(R.id.myRequest_date);
            mRequestDriver = itemView.findViewById(R.id.myRequest_driver);
            mRequestStatus = itemView.findViewById(R.id.myRequest_status);
            Button mRequestCancel = itemView.findViewById(R.id.button_cancel);
            mRequestCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelRequest(mRequest);
                }
            });
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind (artyfartyparty.solo.Model.Request request) {
            mRequest = request;
            mRequestFrom.setText(mRequest.getRide().getLocationFrom().getName());
            mRequestTo.setText(mRequest.getRide().getLocationTo().getName());
            mRequestDate.setText(mRequest.getRide().getDateFrom().toInstant()
                    .atOffset( ZoneOffset.UTC )
                    .format( DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm" ) ));
            mRequestDriver.setText(mRequest.getRide().getUser().getName());


            if (mRequest.isAccepted() == true) {
                mRequestStatus.setText(String.valueOf("Accepted"));
            }
            else if (mRequest.isRejected() == true){
                mRequestStatus.setText(String.valueOf("Rejected"));
            }
            else if ((mRequest.isRejected() == false) && (mRequest.isAccepted() == false)){
                mRequestStatus.setText(String.valueOf("Pending"));
            }


        }

        @Override
        public void onClick(View view) {
        }

    }

    private void cancelRequest(artyfartyparty.solo.Model.Request req) {
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String json = Parser.parseRequestToJSON(req);
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.cancel_request))
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
                    GetMyRequests();
                }
            });
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private class RequestAdapter extends RecyclerView.Adapter<MyProfileFragment.RequestHolder> {
        private List<artyfartyparty.solo.Model.Request> mRequests;

        public RequestAdapter(List<artyfartyparty.solo.Model.Request> requests) {
            mRequests = requests;
        }

        @Override
        public MyProfileFragment.RequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from( getActivity() );
            return new MyProfileFragment.RequestHolder( layoutInflater, parent );
        }

        @Override
        public void onBindViewHolder(MyProfileFragment.RequestHolder holder, int position) {
            artyfartyparty.solo.Model.Request crime = mRequests.get( position );
            holder.bind( crime );
        }

        @Override
        public int getItemCount() {
            return mRequests.size();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
}