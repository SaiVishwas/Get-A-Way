package india.collageapp.com.get_a_way;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Set;


public class MainMaps extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private static final String LOG_TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;

    private Button buttonAdd;
    private LinearLayout container;
    private String textIn = "";
    private String tlon = "";
    private  String tlat = "";
    private Hashtable<String, String> places_dict = new Hashtable<String, String>();
    private PathGoogleMapActivity mPathGoogleMapActivity ;

    private ArrayList<Place> selectedPlaces = new ArrayList<Place>();
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(11.0, 74.0), new LatLng(19.0, 79.0));

    private int clear_flag = 0;
    String waypoints = "waypoints=optimize:true";
    String places_list = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mGoogleApiClient = new GoogleApiClient.Builder(MainMaps.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        // map related

//Submit = (Button)loginDialog.findViewById(R.id.Submit);


        Log.e(LOG_TAG, "entered on create");
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //     intent.putExtra("selectedPlaces_id", selectedPlaces);

                // TODO : update waypoints here

                for (String key : places_dict.keySet()) {

                    // System.out.println("key: " + key + " value: " + places_dict.get(key));
                    // waypoints = waypoints + "|" + place.getLatLng().latitude + "," + place.getLatLng().longitude ;
                    waypoints = waypoints + "|" + places_dict.get(key);
                    places_list = places_list + "|" + key;
                }
                if (places_dict.isEmpty()) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainMaps.this);
                    builder1.setMessage("Please enter a destination ");
                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();


                } else {
                    Intent intent = new Intent(v.getContext(), PathGoogleMapActivity.class);
                    intent.putExtra("waypoints", waypoints);
                    intent.putExtra("places_list", places_list);
                    Intent service = new Intent(getBaseContext(), ChatHeadService.class);
                    startService(service);
                    startActivity(intent);

                }
            }
        });

        Button suggestion_btn = (Button) findViewById(R.id.suggestion_button);
        suggestion_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //     intent.putExtra("selectedPlaces_id", selectedPlaces);

                // TODO : update waypoints here

                for (String key : places_dict.keySet()) {

                    // System.out.println("key: " + key + " value: " + places_dict.get(key));
                    // waypoints = waypoints + "|" + place.getLatLng().latitude + "," + place.getLatLng().longitude ;
                    waypoints = waypoints + "|" + places_dict.get(key) ;
                    places_list = places_list + "|" + key ;
                }
                if (places_dict.isEmpty()) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainMaps.this);
                    builder1.setMessage("Please enter a destination ");
                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else {
                    Intent intent = new Intent(v.getContext(), Suggestions.class);
                    intent.putExtra("waypoints", waypoints);
                    intent.putExtra("places_list", places_list);
                    startActivity(intent);
                }
            }
        });

        buttonAdd = (Button)findViewById(R.id.add_button);
        container = (LinearLayout)findViewById(R.id.container);


        buttonAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAutocompleteTextView.setText("");


                // disable add button here

                buttonAdd.setEnabled(false);

                if(textIn != "") {

                    if (places_dict.containsKey(textIn)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainMaps.this);
                        builder.setMessage("Destination has already been entered ");
                        builder.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else{
                        places_dict.put(textIn, tlat + "," + tlon);

                        LayoutInflater layoutInflater =
                                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView = layoutInflater.inflate(R.layout.row, null);
                        final TextView textOut = (TextView) addView.findViewById(R.id.textout);
                        textOut.setText(textIn);
                        Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                        buttonRemove.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ((LinearLayout) addView.getParent()).removeView(addView);
                                Log.e("Removed : ", textOut.getText() + "");
                                places_dict.remove(textOut.getText() + "");
                                Log.e("place dict", places_dict.toString());


                            }
                        });

                        textIn = "";
                        tlat = "";
                        tlon = "";

                        container.addView(addView);

                    }


                }
            }

        });

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // enable add_button here

            buttonAdd.setEnabled(true);

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();


            textIn = place.getName() + "";

            //places_dict.put(textIn,place.getLatLng().latitude + "," + place.getLatLng().longitude);
            tlat = place.getLatLng().latitude + "";
            tlon = place.getLatLng().longitude + "";
            selectedPlaces.add(place);
         //   waypoints = waypoints + "|" + place.getLatLng().latitude + "," + place.getLatLng().longitude ;
        }
    };


    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}



