package com.example.hibarking.driver.google_map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hibarking.R;
import com.example.hibarking.data_class.garage_model;
import com.example.hibarking.data_class.user_mechanical_data;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsFragment extends Fragment {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider,garage_id=null;
    private FusedLocationProviderClient FusedLocationClient;
    private boolean locationpermassion = false;
    private SupportMapFragment mapFragment;
    private ArrayList<garage_model> latLngArrayList;
    private ArrayList<user_mechanical_data> mechanical_arr;
    private int count=0;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&locationpermassion) {
                get_my_location();
            }
            String t=getArguments().getString("type");
            if (t.equals("garage")) {
                getdata_from_map();
            }
            else {
                get_mechanical_data();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_maps, container, false);
        map_search.search_method(v,mMap,MapsFragment.this);
        imagebottom_my_location(v);
        map_type_method(v);
        search_method(v);
        return v;
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
                        mMap.setMyLocationEnabled(true);
                        LatLng lat = new LatLng(location.getLatitude(),location.getLongitude() );
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
                        mMap.getMinZoomLevel();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 17));
                        // draw rout in map
                     map_routes rout=new map_routes(mMap,MapsFragment.this,location);
                        rout.rout();
                    }
                }
            });
        }catch (SecurityException e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void alart_permation()
    {
        new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme)
                .setTitle("GPS")
                .setMessage("open your Gps")
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
        }getdeviceLocation();

    }

    private void turnGPSOn(){
        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        getActivity().startActivity(intent);
        }
    private void imagebottom_my_location(View v)
    {
      ImageView imageButton = (ImageView) v.findViewById(R.id.imagebottom_my_location);
      imageButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              get_my_location();
          }
      });
    }
    private void getdata_from_map()
    {
        get_garage_data garage=new get_garage_data(mMap,MapsFragment.this);
        latLngArrayList=garage.get_garage_data();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                for (int i = 0; i < latLngArrayList.size(); i++) {
                    if(latLngArrayList.get(i).getLatitude()==marker.getPosition().latitude&&latLngArrayList.get(i).getLongitude()==marker.getPosition().longitude)
                    {

                        garage_id=latLngArrayList.get(i).getGarage_id();
                        garage.get_number_of_booking(garage_id,latLngArrayList.get(i).getGarage_name(),latLngArrayList.get(i).getHour_price()+" ",latLngArrayList.get(i).getUnit_number()+"");
                    }
                }


                return false;
            }
        });
    }
    public  void map_type_method(View v)
    {
        ImageView map_type;
        map_type=(ImageView)v.findViewById(R.id.view_map_type);
        map_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_map_type_alart();
            }
        });
    }
    private void get_map_type_alart()
    {
        View view= LayoutInflater.from(this.getContext()).inflate(R.layout.map_type, null);
        RadioGroup radioGroup=view.findViewById(R.id.rediogroup_map_typ);
        radioGroup.check(R.id.normal_map);
        new AlertDialog.Builder(this.getActivity(),R.style.MyDialogTheme)
                .setTitle("map type")
                .setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int id=   radioGroup.getCheckedRadioButtonId();
                        if(id==R.id.normal_map)
                        {
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        }
                        else if(id==R.id.hybrid_map)
                        {
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        }
                        else if(id==R.id.satellite_map)
                        {
                            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        }
                        else if(id==R.id.terrain_map)
                        {
                            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        }
                    }
                })
                .create()
                .show();
    }
    private void get_mechanical_data()
    {
        get_mechanical_data mech=new get_mechanical_data(mMap,MapsFragment.this);
        mechanical_arr=mech.get_garage_data();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                for (int i = 0; i < mechanical_arr.size(); i++) {
                    if(mechanical_arr.get(i).getLatitude()==marker.getPosition().latitude&&mechanical_arr.get(i).getLongitude()==marker.getPosition().longitude)
                    {

                       String  mechanical_id=mechanical_arr.get(i).getId();
                        mech.get_rate(mechanical_id,mechanical_arr.get(i).getName());
                    }
                }


                return false;
            }
        });
    }
    public   void search_method(View v)
    {
        SearchView search=(SearchView) v.findViewById(R.id.search_view);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                go_to_search_location(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // go_to_search_location(s);
                return false;
            }
        });
    }
    private void go_to_search_location(String s)
    {
        List<Address> arr=new ArrayList<>();
        Geocoder geocoder=new Geocoder(getActivity());
        try {
            arr = geocoder.getFromLocationName(s, 1);
        }catch (Exception e)
        {

        }
        if(arr.size()>0)
        {
            Address address=arr.get(0);
            LatLng l=  new LatLng(address.getLatitude(),address.getLongitude());
            // add_location_zoom_in_map(new LatLng(address.getLatitude(),address.getLongitude()),16,address.getAddressLine(0));
            mMap.addMarker(new MarkerOptions().position(l).title(address.getAddressLine(0)));
            //mMap.getMinZoomLevel();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 16));
        }

    }
}
