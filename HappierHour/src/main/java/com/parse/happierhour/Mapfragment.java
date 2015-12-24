package com.parse.happierhour;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Mapfragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Mapfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mapfragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = "MapFragment";

    GoogleMap map;
    ArrayList<ParseObject> bars = new ArrayList<>();
    Location location;
    LatLng myLocation;
    MainActivity main;
    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static Mapfragment newInstance(String param1, String param2) {
        Mapfragment fragment = new Mapfragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Mapfragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        if(bars.size() == 0){

            ParseGeoPoint userLocation = new ParseGeoPoint();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Locations");
            query.whereNear("coordinates", userLocation);
            query.setLimit(25);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e != null){
                        Log.d("Map ParseException ", e.toString());
                    }else if(objects.size() > 0) {
                        bars = (ArrayList) objects;
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_mapfragment, container, false);
        main = (MainActivity)getActivity();


        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
               //makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, locationListener);
        }catch(SecurityException e){
            Log.d("Security Exception", e.toString());
        }catch(Exception e){
            Log.d("Map Fragment", e.toString());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(googleMap == null){
            Log.d("Map Fragment", "Map returned from Async is null");
        }else {
            Log.d("Map Fragment", "onMapReady");
            map = googleMap;
            centerMapOnMyLocation();
            setMapMarkers();
            map.setOnInfoWindowClickListener(this);
        }
    }

    public void setMapMarkers(){
        for(ParseObject bar : bars) {
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(bar.getDouble("Lat"), bar.getDouble("Long")))
                    .title(bar.getString("Name"))
                    .snippet("Rating: " + bar.getDouble("Rating")));

        }
    }

    private void centerMapOnMyLocation() {

            try {

                map.setMyLocationEnabled(true);
                location = map.getMyLocation();

                if(bars.size() > 0) {
                    myLocation = new LatLng(bars.get(0).getDouble("Lat"), bars.get(0).getDouble("Long"));
                }else{
                    myLocation = new LatLng(37.243599, -121.949057);
                }
                if (myLocation != null) {
                    //myLocation = new LatLng(location.getLatitude(),location.getLongitude());
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                            11));
                }
            } catch (SecurityException e) {

            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }


    }

    private void centerMapOnLocation(Place place) {

        try {
            map.setMyLocationEnabled(true);
            location = map.getMyLocation();
            myLocation = place.getLatLng();
            if (myLocation != null) {
                //myLocation = new LatLng(location.getLatitude(),location.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                        11));
            }
        }catch(SecurityException e){

        }catch(Exception e){
            Log.d(TAG, e.toString());
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d(TAG, "Info Window Click");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Map Fragment", "On Resume");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Map Fragment", "On Destroy");
        bars.clear();
    }
}
