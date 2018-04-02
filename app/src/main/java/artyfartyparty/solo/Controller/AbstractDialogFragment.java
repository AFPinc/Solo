package artyfartyparty.solo.Controller;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Fragment class to open a dialog fragment
 */

public abstract class AbstractDialogFragment extends DialogFragment {

    protected abstract void processFragmentArguments();

    @Override
    public abstract Dialog onCreateDialog(Bundle savedInstanceState);

    protected abstract void saveInstanceState(Bundle outState);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveInstanceState(outState);
    }
}
