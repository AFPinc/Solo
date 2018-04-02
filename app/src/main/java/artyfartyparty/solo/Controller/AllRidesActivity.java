package artyfartyparty.solo.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import artyfartyparty.solo.R;


/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Activity class that shows all rides
 */

public class AllRidesActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Fragment fragment = new ShowRidesFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("userId", getIntent().getLongExtra("userId", -1));
        bundle.putString( "url", getResources().getString(R.string.all_rides_url));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onBackPressed() {
    }
}
