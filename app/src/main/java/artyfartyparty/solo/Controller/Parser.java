package artyfartyparty.solo.Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

import artyfartyparty.solo.Model.Location;
import artyfartyparty.solo.Model.Ride;
import artyfartyparty.solo.Model.User;

/**
 * Created by Sigurlaug on 28/02/2018.
 */

public final class Parser {

    private Parser() {}

    public static ArrayList<Ride> parseRideData(String jsonData) throws JSONException {
        ArrayList<Ride> rides = new ArrayList();
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++){

            JSONObject json = jsonArray.getJSONObject(i);

            Ride ride = new Ride();
            String idString = json.getString("id");
            long id = Integer.parseInt(idString);
            long dateFrom = Integer.parseInt(json.getString("dateFrom"));
            long dateTo = Integer.parseInt(json.getString("dateTo"));

            ride.setId(id);
            ride.setUser(parseUserData(json.getString("user")));
            ride.setLocationFrom(parseLocationData(json.getString("locationFrom")));
            ride.setLocationTo(parseLocationData(json.getString("locationTo")));
            ride.setDateFrom(new Date(dateFrom));
            ride.setDateTo(new Date(dateTo));

            rides.add(ride);
        }
        return rides;
    }

    public static User parseUserData(String jsonData) throws JSONException {
        JSONObject json = new JSONObject(jsonData);
        User user = new User();
        String idString = json.getString("id");
        int id = Integer.parseInt(idString);
        user.setId(id);
        user.setName(json.getString("name"));
        user.setAddress(json.getString("address"));
        user.setPassword(json.getString("password"));
        user.setPhoneNumber(Integer.parseInt(json.getString("phoneNumber")));
        user.setUniMail(json.getString("uniMail"));
        return user;
    }

    public static Location parseLocationData(String jsonData) throws JSONException{
        JSONObject json = new JSONObject(jsonData);
        Location location = new Location();
        String idString = json.getString("id");
        long id = Integer.parseInt(idString);
        location.setId(id);
        location.setName(json.getString("name"));
        return location;
    }

    public static Location[] parseLocationDataArray(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        Location[] locations = new Location[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            Location loc = parseLocationData(jsonArray.getJSONObject(i).toString());
            locations[i] = loc;
        }
        return locations;
    }

}
