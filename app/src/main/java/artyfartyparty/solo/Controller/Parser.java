package artyfartyparty.solo.Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import artyfartyparty.solo.Model.Location;
import artyfartyparty.solo.Model.Request;
import artyfartyparty.solo.Model.Ride;
import artyfartyparty.solo.Model.User;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Parser class
 */

public final class Parser {

    private Parser() {}

    public static ArrayList<Ride> parseRideData(String jsonData) throws JSONException {
        ArrayList<Ride> rides = new ArrayList();
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++){

            JSONObject json = jsonArray.getJSONObject(i);
            Ride ride = parseSingleRideData(json.toString());
            rides.add(ride);
        }
        return rides;
    }

    public static Ride parseSingleRideData(String jsonData) throws JSONException {
        JSONObject json = new JSONObject(jsonData);
        Ride ride = new Ride();
        String idString = json.getString("id");
        long id = Integer.parseInt(idString);
        long dateFrom = Long.parseLong(json.getString("fromDate"));
        long dateTo = Long.parseLong(json.getString("toDate"));

        ride.setId(id);
        ride.setUser(parseUserData(json.getString("user")));
        ride.setLocationFrom(parseLocationData(json.getString("locationFrom")));
        ride.setLocationTo(parseLocationData(json.getString("locationTo")));
        ride.setDateFrom(new Date(dateFrom));
        ride.setDateTo(new Date(dateTo));
        ride.setDeleted(Boolean.parseBoolean(json.getString("deleted")));
        return ride;
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

    public static List<Request> parsRequestData(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        List<Request> requests = new ArrayList<>();
        for (int i = 0; i< jsonArray.length(); i++){
            JSONObject json = jsonArray.getJSONObject(i);
            Request req = new Request();
            req.setId(Integer.parseInt(json.getString("id")));
            req.setUser(parseUserData(json.getString("user")));
            req.setRide(parseSingleRideData(json.getString("ride")));
            req.setAccepted(Boolean.parseBoolean(json.getString("accepted")));
            req.setRejected(Boolean.parseBoolean(json.getString("rejected")));
            requests.add(req);
        }
        return requests;
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

    public static String parseRideToJSON(Ride ride) {
        String id = "";
        if (ride.getId() != null)
            id = "\"id\":\"" + ride.getId() + "\", ";

        String json = "{" + id +
                "\"locationFrom\":" + parseLocationToJSON(ride.getLocationFrom()) + ", " +
                "\"locationTo\":" + parseLocationToJSON(ride.getLocationTo()) + ", " +
                "\"fromDate\":\"" + ride.getDateFrom().getTime() + "\", " +
                "\"toDate\":\"" + ride.getDateTo().getTime() + "\", " +
                "\"user\":" + parseUserToJSON(ride.getUser()) + "}";

        return json;
    }

    public static String parseRequestToJSON(Request req) {
        String id = "";
        if (req.getId() != 0)
            id = "\"id\":" + req.getId() + ", ";

        String jsonReq = "{" + id +
                "\"ride\":" + parseRideToJSON(req.getRide()) + ", " +
                "\"user\":" + parseUserToJSON(req.getUser()) + ", " +
                "\"rejected\":" + req.isRejected() + ", " +
                "\"accepted\":" + req.isAccepted() +"}";

        return jsonReq;
    }

    public static String parseUserToJSON(User user){
        return "{\"id\":\"" + user.getId() + "\", " +
                "\"name\":\"" + user.getName() + "\", " +
                "\"uniMail\":\"" + user.getUniMail() + "\", " +
                "\"address\":\"" + user.getAddress() + "\", " +
                "\"phoneNumber\":\"" + user.getPhoneNumber() + "\", " +
                "\"password\":\"" + user.getPassword() + "\"}";
    }

    private static String parseLocationToJSON(Location location) {
        return "{\"id\":\"" + location.getId() + "\", " +
               "\"name\":\"" + location.getName() + "\"}";
    }
}
