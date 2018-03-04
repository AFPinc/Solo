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
    private static UserDataDB userDataDB;
    private final UserData userData;

    public static UserDataDB get(Context context) {
        if (userDataDB == null) {
            userDataDB = new UserDataDB(context);
        }
        return userDataDB;
    }

    private UserDataDB(Context context) {
        AppDB db = Room.databaseBuilder(context.getApplicationContext(),
                AppDB.class, "user-room")
                .allowMainThreadQueries()
                .build();

        Log.i("UserDataDB", "database built");
        userData = db.userData();
    }
    public UserData getUserData() { return userData; }
}
