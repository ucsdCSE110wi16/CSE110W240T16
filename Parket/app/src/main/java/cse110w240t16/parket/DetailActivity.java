package cse110w240t16.parket;

import android.app.Activity;
import android.app.Fragment;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.model.LatLngBounds;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.text.ParseException;

/**
 * Created by XiaoyuePu on 2/4/16.
 */
public class DetailActivity extends FragmentActivity implements ConnectionCallbacks,
                                                                OnConnectionFailedListener,
                                                                OnItemSelectedListener,
                                                                OnValueChangeListener{

    private GoogleApiClient mGoogleApiClint;
    private String placeID;
    private String parseID;
    private String string_avail;
    private String string_price;
    public static final String TAG = DetailActivity.class.getSimpleName();
    protected int pricing = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);

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

        parseID = getIntent().getStringExtra("parseID");
        System.out.println("Parse ID In Detail is: " + parseID);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
        query.getInBackground(parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, com.parse.ParseException e){
                if (e == null) {
                    string_avail = object.getString("availability");
                    if(string_avail == null){
                        string_avail = "No Availability Recorded";
                        Log.i(TAG, "No Availability");
                    }
                    else{
                        Log.i(TAG, "Have Availability On Parse: " + string_avail);
                    }

                    int stored_price = object.getInt("Price");
                    boolean reported = object.getBoolean("reported");
                    string_price = "$" + stored_price + "/hour";
                    if(stored_price == 0){
                        string_price = "Free";
                    }
                    if(!reported){
                        string_price = "No Pricing Available";
                    }
                    Log.i(TAG, "Have Pricing On Parse: " + string_price);
                    Log.i(TAG, "Place Retrieved From Parse: " + object.getString("name"));
                } else {
                    Log.i(TAG, "Retrieved Error From Parse");
                }
            }
        });

        SystemClock.sleep(5000);

        final TextView name = (TextView) findViewById(R.id.textName);
        final TextView address = (TextView) findViewById(R.id.textAddress);
        final TextView distance = (TextView) findViewById(R.id.textDistance);
        final TextView time = (TextView) findViewById(R.id.textTime);
        final TextView availability = (TextView) findViewById(R.id.textAvail);
        final TextView price = (TextView) findViewById(R.id.textPrice);
        Spinner spinner_avail = (Spinner) findViewById(R.id.spinnerAvail);
        spinner_avail.setOnItemSelectedListener(this);
        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMinValue(0);
        np.setMaxValue(50);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setOnValueChangedListener(this);

        placeID = getIntent().getStringExtra("placeID");
        Places.GeoDataApi.getPlaceById(mGoogleApiClint, placeID)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            final Place myPlace = places.get(0);
                            name.setText(myPlace.getName());
                            address.setText(myPlace.getAddress());
                            float[] result = new float[1];
                            Location.distanceBetween(MapsActivity.mLat, MapsActivity.mLng, myPlace.getLatLng().latitude, myPlace.getLatLng().longitude, result);
                            distance.setText(Float.toString(result[0]/1000) + " km");
                            time.setText(Float.toString(result[0]/1000/40*60) + " min via driving");
                            availability.setText(string_avail);
                            price.setText(string_price);
                            Log.i(TAG, "Place found From Google: " + myPlace.getName());
                        } else {
                            Log.e(TAG, "Place not found From Google");
                        }
                        places.release();
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
    public void onConnected(Bundle connectionHint){
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClint.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String item = parent.getItemAtPosition(position).toString();

        if(!item.contains("Please Select A Status")) {
            // Showing selected spinner item
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
            query.getInBackground(parseID, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, com.parse.ParseException e) {
                    if (e == null) {
                        object.put("availability", item);
                        object.saveInBackground();
                    } else {
                        Log.i(TAG, "Saved Error");
                    }
                }
            });
            Toast.makeText(parent.getContext(), "Availability Reported: " + item, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        this.pricing = picker.getValue();
        Log.i(TAG, "Value changed");
    }

    public void buttonOnClick(View v) {
        Log.i(TAG,"Price buttion clicked");
        //upload to parse
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
        query.getInBackground(parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, com.parse.ParseException e) {
                if (e == null) {
                    object.put("Price", pricing);
                    object.put("reported", true);
                    object.saveInBackground();
                } else {
                    Log.i(TAG, "Saved Error");
                }
            }
        });
        Toast.makeText(v.getContext(), "Pricing Reported: $" + pricing + "/hour", Toast.LENGTH_LONG).show();
    }
}
