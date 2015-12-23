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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker.IntentBuilder;
import com.google.android.gms.location.places.ui.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {
    private static final String TAG = "HomePage";
    MainActivity main;
    Button mapButton;
    Button addLocation;
    Button searchLocation;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Context con;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomePageFragment() {
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
                R.layout.fragment_home_page, container, false);
        main = (MainActivity)getActivity();

        //Map Button
        mapButton = (Button) view.findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Home Fragment", "Clicky Clicky");
                main.swapFragment(new Mapfragment());
            }
        });

        //Add Button
        addLocation = (Button) view.findViewById(R.id.location_add);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace(v);
            }
        });

        //Search Button
        searchLocation = (Button)view.findViewById(R.id.location_search);
        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Home Fragment", "Clicky Clicky");
                main.swapFragment(new LocationSearchFragment());
            }
        });

        con = getActivity();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(con, data);
                Log.i(TAG, "Place: " + place.getName());
                Log.i(TAG, "LatLng: " + place.getLatLng());
                Log.i(TAG, "Address: " + place.getAddress());
                Log.i(TAG, "Phone Number: " + place.getPhoneNumber());


                main.swapFragment(new AddLocationFragment());
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
