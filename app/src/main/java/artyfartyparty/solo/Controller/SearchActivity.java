package artyfartyparty.solo.Controller;

import android.support.v4.app.Fragment;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Activity that controls searching for a ride
 */

public class SearchActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new SearchFragment();
    }
}
