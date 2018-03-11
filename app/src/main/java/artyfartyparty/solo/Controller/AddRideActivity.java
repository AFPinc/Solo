package artyfartyparty.solo.Controller;

import android.support.v4.app.Fragment;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Activity that controls adding a ride
 */

public class AddRideActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new AddRideFragment();
    }
}
