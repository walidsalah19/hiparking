package com.example.hibarking.garage_manager.garage_data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hibarking.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class map extends Fragment {

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
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
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
    }
}