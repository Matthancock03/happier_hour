package com.parse.happierhour;

import android.app.Activity;
import android.app.FragmentTransaction;
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
import android.widget.Button;

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
import java.util.HashMap;
import java.util.List;

import im.delight.android.location.SimpleLocation;


public class Mapfragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = "MapFragment";
    private SimpleLocation position;
    GoogleMap map;
    ArrayList<ParseObject> bars = new ArrayList<>();
    HashMap<String, Integer> markerMap = new HashMap<>();

    LatLng myLocation;
    MainActivity main;
    Mapfragment con;
    LocationListFragment listFrag;
    MapFragment mapFragment;
    Button mapButton;
    Button listButton;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static Mapfragment newInstance() {
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
        con = this;
        Log.i(TAG, "On Create");
        if (getArguments() != null) {
        }
        position = new SimpleLocation(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_mapfragment, container, false);
        main = (MainActivity) getActivity();

        Log.i(TAG, "On CreateView");

        if (!position.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(main);
        }

        mapButton = (Button) view.findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMap();
            }
        });

        listButton = (Button) view.findViewById(R.id.list_button);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadList();
            }
        });

        myLocation = new LatLng(position.getLatitude(), position.getLongitude());


        Log.i(TAG, "Latitude: " + position.getLatitude());
        Log.i(TAG, "Longitude: " + position.getLongitude());

        if (bars.size() == 0) {

            Log.i(TAG, "Bars Empty");
            ParseGeoPoint userLocation = new ParseGeoPoint(position.getLatitude(), position.getLongitude());

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Locations");
            query.whereNear("coordinates", userLocation);
            query.setLimit(25);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e != null) {
                        Log.d("Map ParseException ", e.toString());
                    } else {
                        bars = (ArrayList) objects;
                        loadList();
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "On Attach");

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
        if (googleMap == null) {
            Log.d("Map Fragment", "Map returned from Async is null");
        } else {
            Log.d("Map Fragment", "onMapReady");
            map = googleMap;
            centerMapOnMyLocation();
            setMapMarkers();
            map.setOnInfoWindowClickListener(this);
        }
    }

    public void loadList() {
        Log.i(TAG, "Load List");
        if (listFrag == null) {
            listFrag = LocationListFragment.newInstance(bars, myLocation);
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_frame, listFrag).commit();
    }

    public void loadMap() {
        Log.i(TAG, "Load Map");
        if (mapFragment == null) {
            mapFragment = new MapFragment();
            if (bars.size() <= 0) {
                Log.i(TAG, "No Bars Returned");
            }
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.map_frame, mapFragment).commit();
            mapFragment.getMapAsync(con);

        } else {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.map_frame, mapFragment).commit();
            mapFragment.getMapAsync(con);
        }
    }

    public void setMapMarkers() {
        Log.i(TAG, "Map Markers");
        Log.i(TAG, "Bars Size: " + bars.size());
        for (int x = 0; x < bars.size(); x++) {
            ParseObject bar = bars.get(x);

            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(bar.getDouble("Lat"), bar.getDouble("Long")))
                    .title(bar.getString("Name"))
                    .snippet("Rating: " + bar.getDouble("Rating")));

            markerMap.put(marker.getId(), x);
        }
    }

    private void centerMapOnMyLocation() {

        try {

            map.setMyLocationEnabled(true);

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

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.i(TAG, "Info Window Click");
        Log.i(TAG, marker.getId());
        int barIndex = markerMap.get(marker.getId());

        DisplayLocationFragment display = new DisplayLocationFragment();

        display.bar = bars.get(barIndex);
        main.swapFragment(display, false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
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
        Log.d(TAG, "On Resume");
        position.beginUpdates();
    }

    @Override
    public void onPause() {
        position.endUpdates();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "On Destroy");
        bars.clear();
        markerMap.clear();
    }
}
