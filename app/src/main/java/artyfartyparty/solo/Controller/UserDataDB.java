package artyfartyparty.solo.Controller;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that creates a Singleton object of the database for User
 * Stores an instance of the collection class for User (UserData)
 *
 */

public class UserDataDB {
    private static UserDataDB sUserDataDB;
    private final UserData mUserData;

    public static UserDataDB get(Context context) {
        if (sUserDataDB == null) {
            sUserDataDB = new UserDataDB(context);
        }
        return sUserDataDB;
    }

    private UserDataDB(Context context) {
        AppDB db = Room.databaseBuilder(context.getApplicationContext(),
                AppDB.class, "user-room")
                .allowMainThreadQueries()
                .build();

        Log.i("UserDataDB", "database built");
        mUserData = db.userData();
    }
    public UserData getUserData() {
        return mUserData;
    }
}
