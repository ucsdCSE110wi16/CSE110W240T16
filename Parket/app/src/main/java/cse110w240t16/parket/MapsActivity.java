package cse110w240t16.parket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.net.Uri;
import android.preference.DialogPreference;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.graphics.Typeface;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.BitSet;
import java.util.List;
import java.util.jar.Manifest;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener,
        PlaceSelectionListener, OnMarkerClickListener, OnInfoWindowClickListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClint;
    private Location mLastLocation;
    private Marker marker;
    private boolean once = false;
    private boolean parseInit = false;
    private String placeID;
    private String parseID;

    public static final float ZOOM = (float)15.2;
    public static final String TAG = MapsActivity.class.getSimpleName();
    public static final int PLACE_PICKER_REQUEST = 1;
    public static float mLng;
    public static float mLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create a GoogleApiClient instance
        if (mGoogleApiClint == null) {
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClint = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .addApi(AppIndex.API).build();
        }

        // Autocomplete widget
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Search For A Parking Lot");
        autocompleteFragment.setOnPlaceSelectedListener(this);

        //Initialize Parse ONLY ONCE
        if(!parseInit){
            Parse.initialize(this);
            parseInit = true;
        }
    }

    /* Find Location From Place Picker Widget */
    public void placePickerOnClick(View v) {
        // place picker
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMyLocationEnabled(true);
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).visible(false));

        /* Marker And Window Listener */
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View popup = getLayoutInflater().inflate(R.layout.info_window, null);

                TextView infoTitle = (TextView) popup.findViewById(R.id.txtInfoWindowTitle);
                infoTitle.setText(marker.getTitle());

                TextView contents = (TextView) popup.findViewById(R.id.txtInfoWindowEventType);
                contents.setText(marker.getSnippet());
                Typeface mytypeface1 = Typeface.createFromAsset(getAssets(), "OptimusPrincepsSemiBold.ttf");
                Typeface mytypeface2 = Typeface.createFromAsset(getAssets(), "OptimusPrinceps.ttf");
                infoTitle.setTypeface(mytypeface1);
                contents.setTypeface(mytypeface2);

                return (popup);
            }
        });
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClint.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://cse110w240t16.parket/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClint, viewAction);
    }

    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://cse110w240t16.parket/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClint, viewAction);
        if (mGoogleApiClint.isConnected())
            mGoogleApiClint.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
       // mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClint);
       // mLat = (float) mLastLocation.getLatitude();
       // mLng = (float) mLastLocation.getLongitude();

        // display current location when start
        //if (!once) {
          //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(),
            //                                                                mLastLocation.getLongitude()), ZOOM));
           // once = true;
        //}
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClint.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    /* After Selecting A Place From AutoComplete */
    @Override
    public void onPlaceSelected(final Place place) {

        /* Check the type of the selected place */
        if(place.getPlaceTypes().contains(70)|| place.getName().toString().toLowerCase().contains("parking")){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), ZOOM));
            marker.setPosition(place.getLatLng());
            marker.setTitle((String) place.getName());
            marker.setSnippet("Click Here For More Details");
            marker.setVisible(true);
            marker.hideInfoWindow();
            placeID = place.getId();
            /*mMap.addMarker(new MarkerOptions().position(place.getLatLng()).
                    title((String) place.getName()).
                    snippet("Click Here For More Details"));*/

            /* Parse */
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Place").
                                            whereEqualTo("placeID", placeID).
                                            setLimit(3);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {

                    /* Create a new parse object */
                    if (object == null) {
                        final ParseObject placeObject = new ParseObject("Place");
                        placeObject.put("placeID", placeID);
                        placeObject.put("name", place.getName());
                        placeObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    parseID = placeObject.getObjectId();
                                    System.out.println("ParseID in MapsActivity is: " + parseID);
                                    Log.i(TAG, "Saved Succesfully To Parse");
                                } else {
                                    Log.i(TAG, "Not Saved Succesfully To Parse");
                                }
                            }
                        });
                        Log.i(TAG, "Place Not In Parse. Created");
                    }
                    /* This place exists */
                    else {
                        parseID = object.getObjectId();
                        Log.i(TAG, "Place Exists In Parse.");
                    }
                }
            });
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Please Choose A Parking Lot From The List");
            alert.setCancelable(false).setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alert.create();
            alert.show();
        }
    }

    /* After Clicking Marker */
    @Override
    public boolean onMarkerClick(Marker marker){
        marker.showInfoWindow();
        return false;
    }

    /* After Clicking Info Window */
    @Override
    public void onInfoWindowClick(Marker marker){

        Intent intent = new Intent(getBaseContext(), DetailActivity.class);
        intent.putExtra("placeID", placeID);
        intent.putExtra("parseID", parseID);
        // Starting the Place Details Activity
        startActivity(intent);
    }

    @Override
    public void onError(Status status) {
        Log.i(TAG, "An error occurred: " + status);
    }

    /* After Selecting A Place From Place Picker */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(this, data);
                if (place.getPlaceTypes().contains(70) || place.getName().toString().toLowerCase().contains("parking")) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), ZOOM));
                    marker.setPosition(place.getLatLng());
                    marker.setTitle((String) place.getName());
                    marker.setSnippet("Click here for More Details");
                    marker.setVisible(true);
                    marker.hideInfoWindow();
                    placeID = place.getId();

                    /* Parse */
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Place").
                            whereEqualTo("placeID", placeID).
                            setLimit(3);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {

                            /* Create a new parse object */
                            if (object == null) {
                                ParseObject placeObject = new ParseObject("Place");
                                placeObject.put("placeID", placeID);
                                placeObject.put("name", place.getName());
                                placeObject.saveInBackground();
                                parseID = placeObject.getObjectId();
                                Log.i(TAG, "Place Not On Parse. Created");
                            }
                            /* This place exists */
                            else {
                                parseID = object.getObjectId();
                                Log.i(TAG, "Place Exists On Parse.");
                            }
                        }
                    });
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Please Choose A Parking Lot From The List");
                    alert.setCancelable(false).setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.create();
                    alert.show();
                }
            }
        }
    }
}