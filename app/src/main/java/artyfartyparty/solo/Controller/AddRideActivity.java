package artyfartyparty.solo.Controller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

import artyfartyparty.solo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.layout.simple_list_item_1;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class for letting user add rides
 */

public class AddRideActivity extends android.support.v4.app.Fragment {

    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_addtrip, container, false);



        Spinner fromSpinner = (Spinner) view.findViewById(R.id.fromSpinner);
        Spinner toSpinner = (Spinner) view.findViewById(R.id.toSpinner);
        Spinner stopoverSpinner = (Spinner) view.findViewById(R.id.stopoverSpinner);
        Button stopoverButton = (Button)view.findViewById(R.id.stopoverButton);
        Button addButton = (Button)view.findViewById(R.id.addButton);
        Button fromAtButton = (Button)view.findViewById(R.id.fromAtButton);

        fromAtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(new Date());
                dialog.setTargetFragment(AddRideActivity.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        Button toAtButton = (Button)view.findViewById(R.id.toAtButton);



        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(getActivity(),
                simple_list_item_1, getResources().getStringArray(R.array.names));
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(getActivity(),
                simple_list_item_1, getResources().getStringArray(R.array.names));
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        ArrayAdapter<String> stopoverAdapter = new ArrayAdapter<String>(getActivity(),
                simple_list_item_1, getResources().getStringArray(R.array.names));
        stopoverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stopoverSpinner.setAdapter(stopoverAdapter);

        return view;
    }

    private void addRide(String locationFrom, String locationTo, String dateFrom, String dateTo) {
        String url = "https://solo-web-service.herokuapp.com/ride/add";
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            if (TextUtils.isEmpty(locationFrom)) {
                //locationFrom is empty
                Toast.makeText(getActivity(),"Please enter starting location", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if (TextUtils.isEmpty(locationTo)) {
                //locationTo is empty
                Toast.makeText(getActivity(), "Please enter final destination", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if (TextUtils.isEmpty(dateFrom)) {
                //dateFrom is empty
                Toast.makeText(getActivity(), "Please enter time of departure", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if (TextUtils.isEmpty(dateTo)) {
                //dateTo is empty
                Toast.makeText(getActivity(), "Please enter time of arrival", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON,
                    "{\"locationFrom\":\"" + locationFrom + "\", " +
                            "\"locationTo\":\"" + locationTo + "\", " +
                            "\"dateFrom\":\"" + dateFrom + "\", " +
                            "\"dateTo\": \"" + dateTo + "\"}");

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
                            Toast.makeText(getActivity().getApplicationContext(), "Ride added!",
                                    Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(), "Ride added!", Toast.LENGTH_LONG).show();
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
