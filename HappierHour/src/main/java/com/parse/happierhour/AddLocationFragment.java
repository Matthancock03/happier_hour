package com.parse.happierhour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.parse.ParseACL;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddLocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddLocationFragment extends Fragment {

    private static final String TAG = "AddLocation";
    MainActivity main;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Place place;
    Button saveButton;
    EditText tags, startTime, endTime;
    TextView name, address, phone;
    CheckBox mon,tues,wed,thurs,fri,sat,sun;
    Context con;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static AddLocationFragment newInstance(String param1, String param2) {
        AddLocationFragment fragment = new AddLocationFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public AddLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_add_location, container, false);
        main = (MainActivity)getActivity();

        saveButton = (Button)view.findViewById(R.id.add_location_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocation();
            }
        });
        name = (TextView) view.findViewById(R.id.location_name);
        address = (TextView) view.findViewById(R.id.location_address);
        phone = (TextView) view.findViewById(R.id.location_phone);
        tags = (EditText) view.findViewById(R.id.tags);
        startTime = (EditText) view.findViewById(R.id.start_time);
        endTime = (EditText) view.findViewById(R.id.end_time);
        mon = (CheckBox)view.findViewById(R.id.monday);
        tues = (CheckBox)view.findViewById(R.id.tuesday);
        wed = (CheckBox)view.findViewById(R.id.wednesday);
        thurs = (CheckBox)view.findViewById(R.id.thursday);
        fri = (CheckBox)view.findViewById(R.id.friday);
        sat = (CheckBox)view.findViewById(R.id.saturday);
        sun = (CheckBox)view.findViewById(R.id.sunday);
        con = getActivity();
        // Inflate the layout for this fragment
        findPlace(view);
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    //Start Google Places search
    public void findPlace(View view) {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                    .build();
            //Might try .setTypeFilter(Place.TYPE_BAR);
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    public void saveLocation(){

        Mapfragment map = new Mapfragment();
        Log.i(TAG, "Save Location ");


        if(place != null) {
            ParseGeoPoint point = new ParseGeoPoint();
            point.setLatitude(place.getLatLng().latitude);
            point.setLongitude(place.getLatLng().longitude);

            ParseACL acl = new ParseACL();
            acl.setPublicReadAccess(true);
            acl.setPublicWriteAccess(true);
            ParseObject bar = new ParseObject("Locations");

            bar.put("Id", place.getId());
            bar.put("Name", place.getName());
            bar.put("Lat", place.getLatLng().latitude);
            bar.put("Long", place.getLatLng().longitude);
            bar.put("Address", place.getAddress());
            bar.put("PhoneNumber", place.getPhoneNumber());
            bar.put("Rating", place.getRating());
            bar.put("coordinates", point);
            bar.put("StartTime", startTime.getText().toString());
            bar.put("EndTime", endTime.getText().toString());
            checkDays(bar);
            bar.setACL(acl);
            bar.saveInBackground();
            //map.bars.add(bar);
        }
        main.swapFragment(map, false); //Go Back to Home Page
    }


    private void checkDays(ParseObject bar) {

        if(mon.isChecked()){
           bar.put("monday", true);
        }else{
            bar.put("monday", false);
        }
        if(tues.isChecked()){
            bar.put("tuesday", true);
        }else{
            bar.put("tuesday", false);
        }
        if(wed.isChecked()){
            bar.put("wednesday", true);
        }else{
            bar.put("wednesday", false);
        }
        if(thurs.isChecked()){
            bar.put("thursday", true);
        }else{
            bar.put("thursday", false);
        }
        if(fri.isChecked()){
            bar.put("friday", true);
        }else{
            bar.put("friday", false);
        }
        if(sat.isChecked()){
            bar.put("saturday", true);
        }else{
            bar.put("saturday", false);
        }
        if(sun.isChecked()){
            bar.put("sunday", true);
        }else{
            bar.put("sunday", false);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                place = PlaceAutocomplete.getPlace(con, data);
                Log.i(TAG, "Name: " + place.getName());
                Log.i(TAG, "Id: " + place.getId());
                Log.i(TAG, "LatLng: " + place.getLatLng());
                Log.i(TAG, "Address: " + place.getAddress());
                Log.i(TAG, "Phone Number: " + place.getPhoneNumber());
                Log.i(TAG, "Rating: " + place.getRating());

                name.setText(place.getName());
                address.setText(place.getAddress());
                phone.setText(place.getPhoneNumber());
                List<Integer> types =  place.getPlaceTypes();

                /*
                 * Bar == 9
                 * Restaurant == 79
                 * Food == 38

                for(int x: types){
                    Log.i(TAG, "Type: " + x);
                }
                */

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(con, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
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

}
