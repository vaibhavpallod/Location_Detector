package com.example.locationdetector;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private  final long MIN_TIME = 500; // 1 second
    private final long MIN_DIST = 2; //2 meters
    private LatLng latLng;


    FirebaseAuth mAuth;
    private Firebase mRef;

    Marker marker;
    TextView textView;
    String string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
        mRef = new Firebase("https://location-detector-e9936.firebaseio.com/");

       // textView = findViewById(R.id.textView);





    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        string = getIntent().getExtras().getString("Value");

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(marker!=null)
                {
                    marker.remove();
                }

    try{



        latLng = new LatLng(location.getLatitude(),location.getLongitude());
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("My Current Position"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17),2000,null);
        Firebase mRefchild = mRef.child(string).child("Latitude");
        mRefchild.setValue(location.getLatitude());

        mRefchild = mRef.child(string).child("Longitude");
        mRefchild.setValue(location.getLongitude());



    }
    catch (SecurityException e){
        e.printStackTrace();
    }

               Toast.makeText(getApplicationContext(),location.getLatitude()+" "+
                       location.getLongitude(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DIST,locationListener);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }

    }


    private static class UiSettings {
        public static void setZoomControlsEnabled(boolean b) {
        }
    }


}

