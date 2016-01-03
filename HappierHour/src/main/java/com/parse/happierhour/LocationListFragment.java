package com.parse.happierhour;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p>
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class LocationListFragment extends ListFragment {

    ArrayList<ParseObject> bars = new ArrayList<>();
    LocationListAdapter adapter;
    LatLng location;
    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static LocationListFragment newInstance(ArrayList<ParseObject> barList, LatLng loc) {
        LocationListFragment fragment = new LocationListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.bars = barList;
        fragment.location = loc;
        return fragment;
    }


    public LocationListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new LocationListAdapter(getActivity(), 1, bars, location);

        if (getArguments() != null) {
        }

        setListAdapter(adapter);
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
