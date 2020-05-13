package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView Longitude;
    TextView Latitude;
    TextView Altitude;
    TextView Accuracy;
    TextView Address;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults!=null && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    public void setlocation(Location location){

        Longitude.setText(String.format("Longitude%s", Double.toString(location.getLongitude())));
        Latitude.setText(String.format("Latitude :%s", location.getLatitude()));
        Accuracy.setText(String.format("Accuracy :%s", location.getAccuracy()));
        Altitude.setText(String.format("Altitude :%s", location.getAltitude()));

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<android.location.Address> ListAddersses =  geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if (ListAddersses !=null && ListAddersses.size()>0){
                //Log.i("PlaceAddress",ListAddersses.get(0).toString());
                //Toast.makeText(getApplicationContext(),ListAddersses.get(0).toString(),Toast.LENGTH_SHORT).show();
                String address = "";


                if(ListAddersses.get(0).getThoroughfare()!= null){
                    address += ListAddersses.get(0).getThoroughfare() +"\n";
                }


                if(ListAddersses.get(0).getFeatureName() != null){
                    address += ListAddersses.get(0).getFeatureName() +" ";
                }

                if(ListAddersses.get(0).getLocality() != null){
                    address += ListAddersses.get(0).getLocality() + " ";
                }


                if(ListAddersses.get(0).getPostalCode() != null){
                    address += ListAddersses.get(0).getPostalCode();
                }if(ListAddersses.get(0).getAdminArea() != null){
                    address += ListAddersses.get(0).getAdminArea() + ",";
                }

//                Log.i("PlaceAddress",address);
//                Toast.makeText(getApplicationContext(),address,Toast.LENGTH_SHORT).show();
                Address.setText("Address:" + address);


            }else {
                Address.setText("Address: Could not find Address");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Longitude = findViewById(R.id.textView3);
        Latitude = findViewById(R.id.textView2);
        Altitude = findViewById(R.id.textView4);
        Accuracy = findViewById(R.id.textView5);
        Address = findViewById(R.id.textView6);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            setlocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            setlocation(lastlocation);
        }
    }

}
