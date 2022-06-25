package com.example.hibarking.driver.google_map.map_class;

import android.location.Address;
import android.location.Geocoder;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.hibarking.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class map_search {
    public static  void search_method(View v, GoogleMap mMap, Fragment fragment)
    {
        SearchView search=(SearchView) v.findViewById(R.id.search_view);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                go_to_search_location(s,mMap,fragment);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // go_to_search_location(s);
                return false;
            }
        });
    }
    private static void go_to_search_location(String s, GoogleMap mMap, Fragment fragment)
    {
        List<Address> arr=new ArrayList<>();
        Geocoder geocoder=new Geocoder(fragment.getActivity());
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

    }}
