package com.oa.task2do;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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

public class LocationActivity extends FragmentActivity {

    private final static DecimalFormat DF = new DecimalFormat("#.##");

    private EditText mLocationIn;
    private TextView mLocationOut;
    private Geocoder mGeocoder;
    private GoogleMap mGoogleMap = null;
    private Marker marker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.location);

        //bind to layout
        mLocationIn = (EditText) findViewById(R.id.location_input);
        mLocationOut = (TextView) findViewById(R.id.location_output);
        mGeocoder = new Geocoder(this);

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
}
