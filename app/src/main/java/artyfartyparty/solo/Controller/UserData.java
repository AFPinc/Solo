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
 * Gets all users, one user, deletes, adds, updates users.
 *
 */

@Dao
public interface UserData {
    @Query("SELECT * FROM user")
    List<User> getAll();

    /*@Query("SELECT * FROM user where unimail LIKE :uniMail AND password LIKE password")
    User findByEmail (String uniMail, String password);*/

    @Insert
    void insertAll(User... user);

    @Delete
    void delete(User user);

    /*@Update
    void updateUsers(User... users);*/

    @Insert
    void addUser(User user);

    /*@Update
    void updateUser(User user);*/
}
