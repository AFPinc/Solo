package artyfartyparty.solo.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;


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
        String url = "https://solo-web-service.herokuapp.com/ride/all";
        Fragment f = new ShowRidesFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("userId", getIntent().getLongExtra("userId", -1));
        bundle.putString( "url", url);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onBackPressed() {
    }
}
