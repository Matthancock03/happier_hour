package com.parse.happierhour;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayLocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DisplayLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayLocationFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "DisplayFragment";
    private static HorizontalScrollView horizonalscrollview;
    private TextView namefield;
    private TextView addressfield;
    private TextView phonenumberfield;
    private TextView ratingfield, hours, specials;
    private LinearLayout gallery;
    private ListView reviews;
    private Button editHours, editSpecials, takePicture;
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
        specials = (TextView) view.findViewById(R.id.specialsField);
        editHours = (Button)view.findViewById(R.id.edit_hours);
        editHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        editSpecials = (Button)view.findViewById(R.id.edit_specials);
        editSpecials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        takePicture = (Button)view.findViewById(R.id.take_photo);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        if(bar != null){
            namefield.setText(bar.getString("Name"));
            //addressfield.setText(bar.getString("Address"));
            //phonenumberfield.setText(bar.getString("PhoneNumber"));
            //ratingfield.setText(String.valueOf(bar.getDouble("Rating")).substring(0,4));
            hours.setText(bar.getString("StartTime") + " - " + bar.getString("EndTime"));
            specials.setText(bar.getString("Specials"));
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = getResizedBitmap(imageBitmap, 500);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 0, 0, 0);
            ImageView image = new ImageView(getActivity());
            image.setMaxHeight(100);
            image.setMaxWidth(100);
            image.setLayoutParams(lp);
            image.setImageBitmap(imageBitmap);
            gallery.addView(image);
        }
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    // Use this to convert image before saving to Firebase
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    //Use this to convert retrieved image to bitmap for displaying
    public static Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
