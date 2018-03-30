package artyfartyparty.solo.Controller;

import android.os.Bundle;
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
        Fragment f = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("userId", getIntent().getLongExtra("userId", -1));
        f.setArguments(bundle);
        return f;
    }
}
