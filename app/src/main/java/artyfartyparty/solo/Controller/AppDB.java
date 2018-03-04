package artyfartyparty.solo.Controller;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import artyfartyparty.solo.Model.User;

/**
 * Created by Ása Júlía on 4.3.2018.
 */

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
