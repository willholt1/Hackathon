package mmu.ac.uk.hackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this is a test
        //this is another test
    }

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
            //TODO


        }
    }
}
