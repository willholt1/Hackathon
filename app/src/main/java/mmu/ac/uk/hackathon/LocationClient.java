package mmu.ac.uk.hackathon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LocationClient {

    public ArrayList<Station> getURL(Double lat, Double lng){
        //arraylist to store results in
        ArrayList<Station> stations = new ArrayList<Station>();
        try{
            //construct the URL
            URL u = new URL("http://10.0.2.2:8080/stations?lat="+lat+"&lng="+lng);

            //put the results into the arraylist
            stations = this.runQuery(u);

            //send the list of objects back to the asyncTask
            return stations;
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return stations;
    }

    private ArrayList<Station> runQuery(URL u){
        //TODO run the query to the railway web service
        ArrayList<Station> stations = new ArrayList<Station>();
        try {
            //conect to the server
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            InputStreamReader isr = new InputStreamReader(c.getInputStream());
            BufferedReader in = new BufferedReader(isr);

            String line;

            //put the json from the website into an arraylist of objects
            while ((line = in.readLine()) != null) {
                JSONArray ja = new JSONArray(line);
                for (int i = 0; i < ja.length(); i++) {
                    Station stat = new Station();
                    JSONObject jo = (JSONObject) ja.get(i);
                    stat.setName(jo.getString("StationName"));
                    stat.setLatitude(jo.getDouble("Latitude"));
                    stat.setLongitude(jo.getDouble("Longitude"));
                    stations.add(stat);
                }
            }
            return stations;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stations;
    }
}
