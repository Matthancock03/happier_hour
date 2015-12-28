/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.happierhour;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.FacebookSdk;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseAnalytics;
import com.parse.Parse;
import com.parse.ParseObject;
import com.google.android.gms.*;

public class MainActivity extends AppCompatActivity implements HomePageFragment.OnFragmentInteractionListener, Mapfragment.OnFragmentInteractionListener,
AddLocationFragment.OnFragmentInteractionListener, LocationSearchFragment.OnFragmentInteractionListener, DisplayLocationFragment.OnFragmentInteractionListener{

    Fragment fragment;
    FragmentTransaction transaction;
    private static final String TAG = "Main Activity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      ParseAnalytics.trackAppOpenedInBackground(getIntent());


      if (findViewById(R.id.fragment_container) != null) {

          // However, if we're being restored from a previous state,
          // then we don't need to do anything and should return or else
          // we could end up with overlapping fragments.
          if (savedInstanceState != null) {
              return;
          }

          // Create a new Fragment to be placed in the activity layout
          fragment = new HomePageFragment();

          // In case this activity was started with special instructions from an
          // Intent, pass the Intent's extras to the fragment as arguments
          fragment.setArguments(getIntent().getExtras());

          transaction = getFragmentManager().beginTransaction();

          transaction.replace(R.id.fragment_container, fragment);
          transaction.addToBackStack(null);
          transaction.commit();
      }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

    public void swapFragment(Fragment frag, boolean keep){

        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, frag);
        if(keep) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "On Back Pressed");
        if (getFragmentManager().getBackStackEntryCount() > 1 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
