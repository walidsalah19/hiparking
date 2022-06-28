package com.example.hibarking.garage_manager.garage_data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hibarking.R;
import com.example.hibarking.driver.google_map.MapsFragment;
import com.example.hibarking.driver.google_map.map_routes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class map extends Fragment {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean locationpermassion = false;
    private String provider;
    private FusedLocationProviderClient FusedLocationClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
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
            mMap=googleMap;
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&locationpermassion) {
                get_my_location();
            }
             if (move_location.getType().equals("shaw"))
             {
                 LatLng lat=new LatLng(Double.parseDouble(move_location.getLatitude()),Double.parseDouble( move_location.getLongitude()));
                 googleMap.addMarker(new MarkerOptions()
                         .position(lat)
                         .title(getString(R.string.garage)))
                 ;
                 googleMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
                 googleMap.getMinZoomLevel();
                 googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 20));
             }
             else {
                 googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                     @Override
                     public void onMapClick(@NonNull LatLng latLng) {
                         Log.d("location", latLng.longitude + "");
                         //Success
                         SweetAlertDialog pDialogSuccess = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                         pDialogSuccess.setTitleText(getString(R.string.select_location));
                         pDialogSuccess.setConfirmText(getString(R.string.yes));
                         pDialogSuccess.setCancelText(getString(R.string.no));
                         pDialogSuccess.setConfirmClickListener(sweetAlertDialog -> {
                             move_location.setLatitude(latLng.latitude + "");
                             move_location.setLongitude(latLng.longitude + "");
                             pDialogSuccess.dismiss();
                             getActivity().getSupportFragmentManager().beginTransaction().remove(map.this).commit();
                         });
                         pDialogSuccess.setCancelClickListener(sweetAlertDialog ->
                         {
                             pDialogSuccess.dismiss();
                         });
                         pDialogSuccess.setCancelable(false);
                         pDialogSuccess.show();
                     }
                 });
             }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        FusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        checkLocationPermission();
    }
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
    private void get_my_location()
    {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }getdeviceLocation();

    }
    private void getdeviceLocation()
    {

        try {

            FusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location !=null )
                    {
                        mMap.setMyLocationEnabled(true);
                        LatLng lat = new LatLng(location.getLatitude(),location.getLongitude() );
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
                        mMap.getMinZoomLevel();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 17));
                        // draw rout in map
                        map_routes rout=new map_routes(mMap, map.this,location);
                        rout.rout();
                    }
                }
            });
        }catch (SecurityException e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}