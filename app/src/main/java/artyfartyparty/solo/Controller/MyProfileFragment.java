package artyfartyparty.solo.Controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import artyfartyparty.solo.Model.User;
import artyfartyparty.solo.R;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Fragment for profile information
 */

public class MyProfileFragment extends Fragment {

    private Button myRidesButton;
    private Button myRequestsButton;
    private Toolbar toolbar;
    private long userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profile, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        myRidesButton = (Button) view.findViewById(R.id.myRidesButton);
        myRequestsButton = (Button) view.findViewById(R.id.myRequestsButton);

        myRidesButton.setOnClickListener(btnOnClickListener);
        myRequestsButton.setOnClickListener(btnOnClickListener);

        userId = getArguments().getLong("userId", -1);
        Log.v("uid", "" + userId);
        UserData userData = UserDataDB.get(getActivity().getApplication().getApplicationContext()).getUserData();
        final User user = userData.findOne(userId);
        Log.v("UserID", "" + user.getId());

        return view;
    }



    Button.OnClickListener btnOnClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            Bundle bundle = getArguments();
            int userId = bundle.getInt("userId");

            if (v == myRidesButton){
                myRidesButton.setTextColor(Color.RED);
                myRequestsButton.setTextColor(Color.BLACK);
                }
            Bundle bundle1 = new Bundle();
            bundle1.putInt("userId", userId);
        }
    };


}