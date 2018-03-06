package artyfartyparty.solo.Controller;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import artyfartyparty.solo.Model.User;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Abstract class that sets up the database and references a specific model class.
 * Here we are referencing User.
 * Gets the collection class for User, which is UserData.
 */

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {

    public abstract UserData userData();
}
