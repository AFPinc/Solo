package artyfartyparty.solo.Controller;

import android.support.v4.app.Fragment;

/**
 * Created by valas on 2/28/2018.
 */

public class AddRideActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new AddRideFragment();
    }
}
