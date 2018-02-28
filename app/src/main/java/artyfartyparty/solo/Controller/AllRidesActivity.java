package artyfartyparty.solo.Controller;

import android.support.v4.app.Fragment;
import artyfartyparty.solo.Controller.AllRidesFragment;
import artyfartyparty.solo.Controller.SingleFragmentActivity;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that controlls all rides
 */

public class AllRidesActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AllRidesFragment();
    }
}
