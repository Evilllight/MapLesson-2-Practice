package serkan.com.haritaders;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //System.out.println("Konum : "+location.toString());

                /*mMap.clear();
                LatLng userLocation=new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Konumunuz"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,18));
                 */
                /*Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());//getDefault Telefonun diline göre adres bilgisini getiriyor.

                try {
                    List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if(addressList!=null && addressList.size()>0){
                        System.out.println("Adresimiz : "+addressList.get(0).toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */


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



        if(Build.VERSION.SDK_INT>=23)
        {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,50,locationListener);
                Location lastlocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastlocation!=null) {
                    LatLng userlastLocation = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().title("Konumunuz").position(userlastLocation));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlastLocation, 18));
                }
            }
            else
            {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,50,locationListener);
                Location lastlocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastlocation!=null) {
                    LatLng userlastLocation = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().title("Konumunuz").position(userlastLocation));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlastLocation, 18));
                }



            }
        }




        mMap.setOnMapLongClickListener(this);

        // Add a marker in Sydney and move the camera
        /*LatLng elginkan = new LatLng(40.7634571, 29.8918582);
        mMap.addMarker(new MarkerOptions().position(elginkan).title("Ahmet Elginkan Vakfı"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(elginkan,15));*/

        /*LatLng saatkulesi = new LatLng(40.7628622, 29.9194145);
        mMap.addMarker(new MarkerOptions().position(saatkulesi).title("Saat Kulesi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(saatkulesi));*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults.length>0){
            if(requestCode==1){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,50,locationListener);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onMapLongClick(LatLng latLng) {

        mMap.clear();

        Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
        String address="";

        try {
            List<Address> addressList=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if(addressList!=null && addressList.size()>0){
                if(addressList.get(0).getThoroughfare()!=null){
                    address+=addressList.get(0).getThoroughfare();
                    if(addressList.get(0).getSubThoroughfare()!=null){
                        address+=" "+addressList.get(0).getSubThoroughfare();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(address.matches("")){
            address="Adres Bulunamadı";
        }

        mMap.addMarker(new MarkerOptions().position(latLng).title(address));


    }
}
