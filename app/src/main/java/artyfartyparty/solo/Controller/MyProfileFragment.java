package artyfartyparty.solo.Controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import artyfartyparty.solo.R;

/**
 * Created by Ása Júlía on 29.3.2018.
 */

public class MyProfileFragment extends Fragment {

    private Button myRidesButton;
    private Button myRequestsButton;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_allrides, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        Bundle bundle1 = getArguments();

        myRidesButton = (Button) view.findViewById(R.id.myRidesButton);
        myRequestsButton = (Button) view.findViewById(R.id.myRequestsButton);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        ProfileDetailsUser profileDetailsUser = new ProfileDetailsUser();
        profileDetailsUser.setArguments(bundle1);
        fragmentTransaction.add(R.id.profileFragment, profileDetailsUser);

        MyRideFragment myRideFragment = new MyRideFragment();
        myRideFragment.setArguments(bundle1);
        fragmentTransaction.add(R.id.profileDetailsFragment, myRideFragment);

        MyRequestsFragment profileDetailsMyRequests = new MyRequestsFragment();
        profileDetailsMyRequests.setArguments(bundle1);
        fragmentTransaction.add(R.id.profileDetailsFragment, profileDetailsMyRequests);

        fragmentTransaction.commit();

        myRidesButton.setOnClickListener(btnOnClickListener);
        myRequestsButton.setOnClickListener(btnOnClickListener);

        return view;
    }

    Button.OnClickListener btnOnClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            Fragment newFragment;
            Bundle bundle = getArguments();
            int userId = bundle.getInt("id");

            if (v == myRidesButton){
                myRidesButton.setTextColor(Color.RED);
                myRequestsButton.setTextColor(Color.BLACK);
                newFragment= new MyRideFragment();
            }
            else if (v == myRequestsButton) {
                myRidesButton.setTextColor(Color.BLACK);
                myRequestsButton.setTextColor(Color.RED);
                newFragment = new MyRequestsFragment();
            }
            else{
                newFragment = new EmptyFragment();
            }
            Bundle bundle1 = new Bundle();
            bundle1.putInt("id", userId);
            newFragment.setArguments(bundle1);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.profileDetailsFragment, newFragment);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    };
}