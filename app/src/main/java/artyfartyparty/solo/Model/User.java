package artyfartyparty.solo.Model;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that maintains the data of users
 */

public class User {
    private long id;
    private String name;
    private String uniMail;
    private String Address;
    private int phoneNumber;
    private String password;

    public User(long id, String name, String uniMail, String address, int phoneNumber, String password) {
        this.id = id;
        this.name = name;
        this.uniMail = uniMail;
        Address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public User() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUniMail() {
        return uniMail;
    }

    public void setUniMail(String uniMail) {
        this.uniMail = uniMail;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
