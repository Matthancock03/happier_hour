package com.parse.happierhour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
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
    Button saveButton, addSpecial;
    EditText tags, startTime, endTime, item, price;
    TextView name, address, phone, specials;
    CheckBox mon, tues, wed, thurs, fri, sat, sun;
    Context con;
    public boolean exists = false;

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
        main = (MainActivity) getActivity();

        name = (TextView) view.findViewById(R.id.location_list_name);
        address = (TextView) view.findViewById(R.id.location_address);
        phone = (TextView) view.findViewById(R.id.location_phone);
        specials = (TextView) view.findViewById(R.id.specials_display);

        tags = (EditText) view.findViewById(R.id.tags);
        startTime = (EditText) view.findViewById(R.id.start_time);
        endTime = (EditText) view.findViewById(R.id.end_time);
        item = (EditText) view.findViewById(R.id.item_name);
        price = (EditText) view.findViewById(R.id.item_price);

        mon = (CheckBox) view.findViewById(R.id.monday);
        tues = (CheckBox) view.findViewById(R.id.tuesday);
        wed = (CheckBox) view.findViewById(R.id.wednesday);
        thurs = (CheckBox) view.findViewById(R.id.thursday);
        fri = (CheckBox) view.findViewById(R.id.friday);
        sat = (CheckBox) view.findViewById(R.id.saturday);
        sun = (CheckBox) view.findViewById(R.id.sunday);
        con = getActivity();

        saveButton = (Button) view.findViewById(R.id.add_location_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocation();
            }
        });
        addSpecial = (Button) view.findViewById(R.id.submit_item);
        addSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = specials.getText().toString();
                temp += item.getText().toString() + " " + price.getText().toString() + "\n";
                specials.setText(temp);
                item.setText("");
                price.setText("");
            }
        });
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


    public void saveLocation() {

        Log.i(TAG, "Save Location ");
        if (place != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Locations");
            query.whereEqualTo("Id", place.getId());
            query.setLimit(1);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e != null) {
                        Toast.makeText(con, "Error on Save. Please try again.",
                                Toast.LENGTH_SHORT).show();
                        HomePageFragment home = new HomePageFragment();
                        main.swapFragment(home, false);

                    } else if (objects != null && objects.size() > 0) {
                        Toast.makeText(con, "Location already exists.",
                                Toast.LENGTH_SHORT).show();
                        HomePageFragment home = new HomePageFragment();
                        main.swapFragment(home, false);
                    } else {
                        Mapfragment map = new Mapfragment();
                        ParseGeoPoint point = new ParseGeoPoint();
                        point.setLatitude(place.getLatLng().latitude);
                        point.setLongitude(place.getLatLng().longitude);

                        ParseACL acl = new ParseACL();
                        acl.setPublicReadAccess(true);
                        acl.setPublicWriteAccess(true);
                        ParseObject bar = new ParseObject("Locations");

                        bar.put("Id", place.getId());
                        bar.put("Name", place.getName());
                        bar.put("SearchName", place.getName().toString().toLowerCase().replaceAll("'", ""));
                        bar.put("Lat", place.getLatLng().latitude);
                        bar.put("Long", place.getLatLng().longitude);
                        bar.put("Address", place.getAddress());
                        bar.put("PhoneNumber", place.getPhoneNumber());
                        bar.put("Rating", place.getRating());
                        bar.put("coordinates", point);
                        bar.put("StartTime", startTime.getText().toString());
                        bar.put("EndTime", endTime.getText().toString());
                        bar.put("Specials", specials.getText().toString());
                        checkDays(bar);
                        bar.setACL(acl);
                        bar.saveInBackground();
                        main.swapFragment(map, false);
                    }
                }
            });
        }

    }


    private void checkDays(ParseObject bar) {

        if (mon.isChecked()) {
            bar.put("monday", true);
        } else {
            bar.put("monday", false);
        }
        if (tues.isChecked()) {
            bar.put("tuesday", true);
        } else {
            bar.put("tuesday", false);
        }
        if (wed.isChecked()) {
            bar.put("wednesday", true);
        } else {
            bar.put("wednesday", false);
        }
        if (thurs.isChecked()) {
            bar.put("thursday", true);
        } else {
            bar.put("thursday", false);
        }
        if (fri.isChecked()) {
            bar.put("friday", true);
        } else {
            bar.put("friday", false);
        }
        if (sat.isChecked()) {
            bar.put("saturday", true);
        } else {
            bar.put("saturday", false);
        }
        if (sun.isChecked()) {
            bar.put("sunday", true);
        } else {
            bar.put("sunday", false);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                place = PlaceAutocomplete.getPlace(con, data);
                name.setText(place.getName());
                address.setText(place.getAddress());
                phone.setText(place.getPhoneNumber());
                List<Integer> types = place.getPlaceTypes();

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

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
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

}
