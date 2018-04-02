package artyfartyparty.solo.Controller;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import artyfartyparty.solo.Model.User;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that collects data from the model class User.
 *
 * Gets all users, one user, deletes user, adds user, updates users.
 *
 */

@Dao
public interface UserData {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id=:id")
    User findOne(long id);

    @Insert
    void insertAll(User... user);

    @Delete
    void delete(User user);

    @Insert
    void addUser(User user);

}
