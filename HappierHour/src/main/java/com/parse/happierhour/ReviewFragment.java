package com.parse.happierhour;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

public class ReviewFragment extends Fragment {

    ParseQueryAdapter<ParseObject> reviewAdapter;
    MainActivity main;
    ListView reviewlistview2;
    ImageButton Addreviewbtn;
    EditText reviewInput;
    ReviewItemAdapter adp;


    private OnFragmentInteractionListener mListener;

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        // Inflate the layout for this fragment
        reviewlistview2 = (ListView) view.findViewById(R.id.ReviewListView2);
        Addreviewbtn = (ImageButton) view.findViewById(R.id.addreviewbtn);
        reviewInput = (EditText) view.findViewById(R.id.ReviewInput);

        loadReviews();

        //updateData();

        Addreviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview();
            }
        });



        return view;
    }

    public void loadReviews(){
        //Initialize ParseQueryAdapter
        reviewAdapter = new ParseQueryAdapter<>(getActivity(), "Reviews");
        reviewAdapter.setTextKey("Review");

        //Initialize ListView

        adp = new ReviewItemAdapter(getActivity());
        reviewlistview2.setAdapter(reviewAdapter);
        reviewAdapter.loadObjects();
    }

    public void addReview(){
        ParseObject reviews = new ParseObject("Reviews");

        DisplayLocationFragment dis = new DisplayLocationFragment();

        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        reviews.put("Name", DisplayLocationFragment.namefield.getText().toString());
        reviews.put("Review", reviewInput.getText().toString());
        reviews.setACL(acl);
        reviews.saveInBackground();
    }

    public void updateData(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Reviews");

        query.whereEqualTo("Name", DisplayLocationFragment.namefield.getText().toString());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {


            }
        });
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
        void onFragmentInteraction(Uri uri);
    }
}
