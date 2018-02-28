package artyfartyparty.solo.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import artyfartyparty.solo.Controller.AddRideFragment;
import artyfartyparty.solo.Controller.SingleFragmentActivity;
import artyfartyparty.solo.R;

/**
 * Created by valas on 2/28/2018.
 */

public class AddRideActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new AddRideFragment();
    }
}
