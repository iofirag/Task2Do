package com.oa.task2do;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends FragmentActivity {

    private final static DecimalFormat DF = new DecimalFormat("#.##");

    private EditText mLocationIn;
    private TextView mLocationOut;
    private Geocoder mGeocoder;
    private GoogleMap mGoogleMap = null;
    private Marker marker = null;

    private double longitude =-1.;
    private double latitude =-1.;
    private int radius =500;


    /**
     * google analytics
     */
    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        try{
                latitude = getIntent().getExtras().getDouble("mapLatitude");
                longitude = getIntent().getExtras().getDouble("mapLongitude");
        }catch (Exception e){
            e.printStackTrace();
        }

        //bind to layout
        mLocationIn = (EditText) findViewById(R.id.location_input);
        mLocationOut = (TextView) findViewById(R.id.location_output);
        //Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Locale lHebrew = new Locale("he");
        mGeocoder = new Geocoder(this, lHebrew);

        //initialize the map object
        SupportMapFragment supportMapFragment =
                (SupportMapFragment)getSupportFragmentManager().findFragmentByTag("mapFragment");

        mGoogleMap = supportMapFragment.getMap();
        if (mGoogleMap!=null) {
            //map available
            mGoogleMap.setMyLocationEnabled(true);
        }

        mLocationIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    lookUp(mLocationIn.getText().toString());
                }
                return false;
            }
        });

        showLocationIfExist();
    }

    public void showLocationIfExist(){
        if (latitude!= -1. && longitude!= -1. ){
            EditText etLocationInput = (EditText) findViewById(R.id.location_input);

            //get address from longitude & latitude
            String address = getCompleteAddressString(latitude, longitude);

            //update location input
            etLocationInput.setText(address);

            // update whole map
            lookUp(address);
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        try {
            List<Address> addresses = mGeocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    /**
     * Lookup the address in input, format an output string and update map if possible
     */
    private void lookUp(String addressString) {
        String out;
        try {
            //TODO: move geocoding to async task
            List<Address> addresses = mGeocoder.getFromLocationName(addressString, 1);
            if (addresses.size() >= 1) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                out = address.getAddressLine(0) + " ("
                        + DF.format(address.getLatitude()) + " , "
                        + DF.format(address.getLongitude()) + ")";
                updateMap(latLng);
                longitude = address.getLongitude();
                latitude = address.getLatitude();
                RadioGroup g = (RadioGroup) findViewById(R.id.radioGroup);
                int selected = g.getCheckedRadioButtonId();
                RadioButton b = (RadioButton) findViewById(selected);
                radius=Integer.parseInt(b.getText().toString());
            } else {
                out = "Not found";
            }
        } catch (IOException e) {
            out = "Not available";
        }
        mLocationOut.setText(out);
    }

    /**
     * Display a marker on the map and reposition the camera according to location
     * @param latLng
     */
    private void updateMap(LatLng latLng){
        if (mGoogleMap==null){
            return; //no play services
        }

        if (marker!=null){
            marker.remove();
        }

        marker = mGoogleMap.addMarker(new MarkerOptions().position(latLng));

        //reposition camera
        CameraPosition newPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newPosition));
        System.out.println("update-map-seccessfully");
    }


    public void locationSetButton (View v){
        Intent data = new Intent();
        //---set the data to pass back---
        data.putExtra("longitude", longitude);
        data.putExtra("latitude", latitude);
        data.putExtra("radius", radius);
        setResult(RESULT_OK, data);
        //---closes the activity---
        finish();
    }
    public void locationCancelButton (View v){
        finish();
    }
}
