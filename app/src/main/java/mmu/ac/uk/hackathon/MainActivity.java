package mmu.ac.uk.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //must be before set content view
        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_main);

        //must be after setContentView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    ////////////////
    //MAPBOX STUFF//
    ////////////////
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap){
        //this is required
        mapboxMap.setStyle(Style.DARK);

        //tells the map where to show
        mapboxMap.setCameraPosition(
                new CameraPosition.Builder().
                        target(new LatLng(LocationVars.latitude,LocationVars.longitude))
                        .zoom(11.0)
                        .build()
        );
    }

    public void recenterMap() {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                //get the variables for the old camera position
                CameraPosition old = mapboxMap.getCameraPosition();

                //create a position for the new lat and long
                CameraPosition newPosition = new CameraPosition.Builder()
                        .target(new LatLng(LocationVars.latitude, LocationVars.longitude))
                        .zoom(old.zoom)
                        .build();

                //set the map to the new position
                mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(newPosition));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    ///////////////////////
    //END OF MAPBOX STUFF//
    ///////////////////////

    public void onClickSearch(View v){
        //////////////////////////
        //PERMISSION CHECK STUFF//
        //////////////////////////
        //list of permissions
        //still needed to be added to the manifest
        String[] requiredPermissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        boolean ok = true;
        //loops through permissions and checks if they have been granted
        for (int i=0; i<requiredPermissions.length; i++){
            int result = ActivityCompat.checkSelfPermission(this, requiredPermissions[i]);
            if(result != PackageManager.PERMISSION_GRANTED){
                ok = false;
            }
        }
        //if any permissions are not granted exit the app
        if (!ok){
            ActivityCompat.requestPermissions(this, requiredPermissions, 1);
            //that last permission must be > 0 or it fails silently
            System.exit(0);
        }else { //if it has permissions do this stuff:

            //get latitude and longitude and put them in variables
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //access the variables from LocationVars and put the lat/long in
                    LocationVars.latitude = location.getLatitude();
                    LocationVars.longitude = location.getLongitude();
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override
                public void onProviderEnabled(String provider) {}
                @Override
                public void onProviderDisabled(String provider) {}
            });

            //update the camera position of the mapbox map
            this.recenterMap();
            //call asyncTask
            new runNetworking().execute();

        }
    }


    //AsyncTask Networking
    public class runNetworking extends AsyncTask<String, Void, ArrayList<Station>>{

        @Override
        protected ArrayList<Station> doInBackground(String... strings) {
            LocationClient lc = new LocationClient();
            ArrayList<Station> stations = new ArrayList<Station>();

            //call the getURL method from the location client which in turn puts all the data from the website into an arraylist of objects
            stations = lc.getURL(LocationVars.latitude, LocationVars.longitude);

            //send the list of objects to onPostExecute
            return stations;
        }

        @Override
        protected void onPostExecute(ArrayList<Station> s){
            displayResults(s);
        }
    }


    private void displayResults(ArrayList<Station> s){
        //display the text results
        TextView txtview = findViewById(R.id.textView);
        txtview.setText("");
        //used to round the distance up in the format 00.00
        DecimalFormat df = new DecimalFormat("00.00");
        df.setRoundingMode(RoundingMode.UP);

        for(int i = 0; i < s.size(); i++){
            //calculate the distance between the user and the station and then save it in the object
            s.get(i).setDistance();
            txtview.append(df.format(s.get(i).getDistance()) +" km  to  " +s.get(i).getName()+ "\n");
        }
    }
}

