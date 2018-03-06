package artyfartyparty.solo.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that maintains the data of users
 */

@Entity(tableName="user")
public class User {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="uniMail")
    private String uniMail;

    @ColumnInfo(name="address")
    private String address;

    @ColumnInfo(name="phoneNumber")
    private int phoneNumber;

    @ColumnInfo(name="password")
    private String password;

    public User(long id, String name, String uniMail, String address, int phoneNumber, String password) {
        this.id = id;
        this.name = name;
        this.uniMail = uniMail;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public User() {}

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getUniMail() { return uniMail; }

    public void setUniMail(String uniMail) { this.uniMail = uniMail; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public int getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(int phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

}
