package artyfartyparty.solo.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import artyfartyparty.solo.Controller.AllRidesFragment;
import artyfartyparty.solo.Controller.SingleFragmentActivity;
import artyfartyparty.solo.R;

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
        Fragment f = new AllRidesFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("userId", getIntent().getLongExtra("userId", -1));
        f.setArguments(bundle);
        return f;
    }

}
