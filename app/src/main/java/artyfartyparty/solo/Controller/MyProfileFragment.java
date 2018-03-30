package artyfartyparty.solo.Controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import artyfartyparty.solo.R;

/**
 * Created by Ása Júlía on 29.3.2018.
 */

public class MyProfileFragment extends FragmentActivity {

    private Button myRidesButton;
    private Button myRequestsButton;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle1 = getIntent().getExtras();

        myRidesButton = (Button) findViewById(R.id.myRidesButton);
        myRequestsButton = (Button) findViewById(R.id.myRequestsButton);

        fragmentManager = getSupportFragmentManager();
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
    }

    Button.OnClickListener btnOnClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            Fragment newFragment;
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            int user = bundle.getInt("id");

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
            bundle1.putInt("user", user);
            newFragment.setArguments(bundle1);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.profileDetailsFragment, newFragment);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    };
}
