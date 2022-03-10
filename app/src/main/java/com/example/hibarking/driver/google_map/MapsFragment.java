package com.example.hibarking.driver.google_map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hibarking.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsFragment extends Fragment {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private FusedLocationProviderClient FusedLocationClient;
    private boolean locationpermassion = false;
    private SupportMapFragment mapFragment;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&locationpermassion) {
                get_my_location();


            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(callback);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        FusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        checkLocationPermission();
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&locationpermassion) {
            alart_permation();
        }
        else
        {
            mapFragment.getMapAsync(callback);
        }
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            locationpermassion=true;
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationpermassion = true;
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        locationpermassion = true;

                       locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) getActivity());
                    }

                } else {
                }
                return;
            }

        }
    }
    private void getdeviceLocation()
    {

        try {
            FusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location !=null )
                    {
                        add_location_zoom_in_map(location);
                    }
                }
            });
        }catch (SecurityException e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void add_location_zoom_in_map(Location currentLocation)
    {
        //Location currentLocation=(Location) task.getResult();
        LatLng location = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude() );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        //mMap.addMarker(new MarkerOptions().position(location).title("you"));
        mMap.getMinZoomLevel();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(17)
                .bearing(90)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    private void alart_permation()
    {
        new AlertDialog.Builder(getActivity())
                .setTitle("GPS")
                .setMessage("get Gps")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        locationpermassion=true;
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                         turnGPSOn();
                    }
                })
                .create()
                .show();
    }
    private void get_my_location()
    {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        getdeviceLocation();

    }

    private void turnGPSOn(){
        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        getActivity().startActivity(intent);
        }
}
