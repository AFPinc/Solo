package artyfartyparty.solo.Controller;

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
 * DAO=Data access object
 *
 * Gets all users, one user, deletes, adds, updates users.
 *
 */

public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getUsers();

    @Query("SELECT * FROM users WHERE Id = id")
    User getUser ();

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Update
    void updateUsers(User... users);

    @Insert
    void addUser(User user);

    @Update
    void updateUser(User user);
}
