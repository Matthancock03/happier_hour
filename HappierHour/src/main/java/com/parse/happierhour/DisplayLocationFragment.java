package com.parse.happierhour;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayLocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DisplayLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayLocationFragment extends Fragment {

    private static final String TAG = "DisplayFragment";
    private static HorizontalScrollView horizonalscrollview;
    private TextView namefield;
    private TextView addressfield;
    private TextView phonenumberfield;
    private TextView ratingfield, hours;
    private LinearLayout gallery;
    private ListView reviews;
    ParseObject bar;

    private OnFragmentInteractionListener mListener;

    public DisplayLocationFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DisplayLocationFragment newInstance(String param1, String param2) {
        DisplayLocationFragment fragment = new DisplayLocationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_display_location, container, false);

        horizonalscrollview = (HorizontalScrollView) view.findViewById(R.id.menuPictures);
        namefield = (TextView) view.findViewById(R.id.nameField);
        //addressfield = (TextView) view.findViewById(R.id.addressField);
        //phonenumberfield = (TextView) view.findViewById(R.id.phonenumberField);
        //ratingfield = (TextView) view.findViewById(R.id.ratingField);
        gallery = (LinearLayout) view.findViewById(R.id.menuPics);
        reviews = (ListView) view.findViewById(R.id.ReviewListView);
        hours = (TextView) view.findViewById(R.id.hoursField);


        if(bar != null){
            namefield.setText(bar.getString("Name"));
            //addressfield.setText(bar.getString("Address"));
            //phonenumberfield.setText(bar.getString("PhoneNumber"));
            //ratingfield.setText(String.valueOf(bar.getDouble("Rating")).substring(0,4));
            hours.setText(bar.getString("StartTime") + " - " + bar.getString("EndTime"));
        }

        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        /*
        String targetPath = ExternalStorageDirectoryPath + "/test/";

        Toast.makeText(getActivity(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);

        File[] files = targetDirector.listFiles();
        for (File file : files){
            gallery.addView(insertPhoto(file.getAbsolutePath()));
        }
        */

        // Inflate the layout for this fragment
        return view;
    }

    View insertPhoto(String path){
        Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new ViewGroup.LayoutParams(250, 250));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(220, 220));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);

        layout.addView(imageView);
        return layout;
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
        Bitmap bm = null;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        void onFragmentInteraction(Uri uri);
    }
}
