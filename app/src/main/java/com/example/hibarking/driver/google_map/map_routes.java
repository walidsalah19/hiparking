package com.example.hibarking.driver.google_map;

import android.location.Location;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.hibarking.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Color;

import java.util.ArrayList;
import java.util.List;

public class map_routes implements RoutingListener  {
    private GoogleMap mMap;
    private Fragment fragment;
    private LatLng end=null;
    private LatLng start=null;
    Location myLocation;
    String user_id,garage_id;
    FirebaseFirestore database;
    private List<Polyline> polylines=null;
    Location destinationLocation=null;
    public map_routes(GoogleMap mMap, Fragment fragment,Location myLocation) {
        this.mMap = mMap;
        this.fragment = fragment;
        this.myLocation=myLocation;
    }
    public void rout()
    {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        user_id=auth.getCurrentUser().getUid().toString();
        database=FirebaseFirestore.getInstance();
        database.collection("booking").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if (task.isSuccessful()) {
                   boolean found=false;
                   for (QueryDocumentSnapshot document : task.getResult()) {
                       String id=document.get("id").toString();
                       if (user_id.equals(id))
                       {
                           garage_id=document.get("garage_id").toString();
                           found=true;
                           break;
                       }
                   }
                   if (found)
                   {
                       get_garage_lcation();
                   }
               }
            }
        });
    }

    private void get_garage_lcation() {
        database.collection("garage_requist").document(garage_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                  if(task.getResult().exists())
                  {
                      end=new LatLng(Double.parseDouble(task.getResult().get("latitude").toString()),Double.parseDouble(task.getResult().get("longitude").toString()));
                      start=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                      //start route finding
                      Findroutes(start,end);
                  }
            }
        });
    }

    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End)
    {
        if(Start==null || End==null) {
            Toast.makeText(fragment.getActivity(),"Unable to get location",Toast.LENGTH_LONG).show();
        }
        else
        {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyBuYy5xMixaIvAlEPhwR47GO8MMARNQYPE")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout =fragment.getActivity().findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int k = 0; k <arrayList.size(); k++) {

            if (k == i) {
               // polyOptions.color(Color.RED_FIELD_NUMBER);
                polyOptions.width(7);
                polyOptions.addAll(arrayList.get(i).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int l = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(l - 1);
                polylines.add(polyline);

            } else {

            }
        }
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {

    }
}
