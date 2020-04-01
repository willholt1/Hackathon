package mmu.ac.uk.hackathon;

import android.location.Location;

import static android.location.Location.distanceBetween;

public class Station {

    String name;
    Double latitude;
    Double longitude;
    float distance;

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }

    //find the distance between the users latitude and longitude and the latitude and longitude for the train station
    public void setDistance(){
        Location locA = new Location("User");

        locA.setLatitude(LocationVars.latitude);
        locA.setLongitude(LocationVars.longitude);

        Location locB = new Location("Station");

        locB.setLatitude(this.latitude);
        locB.setLongitude(this.longitude);
        //set the distance variable and convert from meters to km
        this.distance = locA.distanceTo(locB)/1000;
    }

    //getters
    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public float getDistance(){
        return distance;
    }

}
