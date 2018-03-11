package artyfartyparty.solo.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import artyfartyparty.solo.Model.Ride;
import artyfartyparty.solo.R;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Fragment for search results
 */

public class SearchResultsFragment extends Fragment {
    private RecyclerView mRideRecyclerView;
    private RideAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_allrides, container, false);

        mRideRecyclerView = view.findViewById(R.id.ride_recycler_view);
        mRideRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String json = getArguments().getString( "data" );
        ArrayList<Ride> rides = null;
        try {
            rides = Parser.parseRideData( json );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateUI(rides);
        return view;
    }

    private void updateUI(List<Ride> rides) {
        mAdapter = new RideAdapter(rides);
        mRideRecyclerView.setAdapter(mAdapter);
    }

    private TextView mRideFrom;
    private TextView mRideTo;
    private TextView mRideDate;

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
            Toast.makeText(getActivity(),
                    mRide.getLocationFrom() + " - " + mRide.getLocationTo() +" clicked!",Toast.LENGTH_SHORT)
                    .show();
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
