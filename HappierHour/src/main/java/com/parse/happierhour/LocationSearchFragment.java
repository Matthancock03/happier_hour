package com.parse.happierhour;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import im.delight.android.location.SimpleLocation;


public class LocationSearchFragment extends Fragment {
    private static final String TAG = "LocationFragment";
    private SimpleLocation position;
    Button sortBy, filterBy;
    ImageButton search;
    EditText query;
    String queryParameter = "SearchName";
    ListView searchResults;
    ArrayList<ParseObject> bars = new ArrayList<>();
    LocationListAdapter adapter;
    LatLng myLocation;
    MainActivity main;
    Context con;
    View view;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static LocationSearchFragment newInstance(String param1, String param2) {
        LocationSearchFragment fragment = new LocationSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LocationSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = new SimpleLocation(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location_search, container, false);
        if (!position.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(main);
        }
        myLocation = new LatLng(position.getLatitude(), position.getLongitude());

        con = getActivity();
        main = (MainActivity)getActivity();

        sortBy = (Button) view.findViewById(R.id.sort_button);
        filterBy = (Button) view.findViewById(R.id.filter_button);
        search = (ImageButton)view.findViewById(R.id.search_location_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        query = (EditText)view.findViewById(R.id.search_location_field);
        adapter = new LocationListAdapter(con, 1, bars, myLocation);
        searchResults = (ListView)view.findViewById(R.id.search_location_results_list);
        searchResults.setAdapter(adapter);
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

    public void search(){
        //Close keyboard after search.
        InputMethodManager imm = (InputMethodManager) con.getSystemService(
                con.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        String str = query.getText().toString().toLowerCase();
        query.setText("");

        if(str.equals("") || str.length() < 3){
            Toast.makeText(con, "Search query must be at least 3 characters",
                    Toast.LENGTH_SHORT).show();
        }else {
            ParseGeoPoint userLocation = new ParseGeoPoint(position.getLatitude(), position.getLongitude());

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Locations");
            query.whereNear("coordinates", userLocation);
            query.whereContains(queryParameter, str);
            query.setLimit(25);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e != null) {
                        Log.d("Map ParseException ", e.toString());
                    }else if(objects == null || objects.size() <= 0){
                        Toast.makeText(con, "No Results. Try a different query.",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        Log.d(TAG, "Num Results: " + objects.size());
                        bars = (ArrayList) objects;
                        adapter.data = bars;
                        main.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();

                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "On Resume");
        position.beginUpdates();
        if(getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onPause() {
        position.endUpdates();
        if(getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "On Destroy");
        bars.clear();
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
