package artyfartyparty.solo.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Activity class for adding a new ride
 */

public class AddRideActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        Fragment f = new AddRideFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("userId", getIntent().getLongExtra("userId", -1));
        f.setArguments(bundle);
        return f;
    }
}
