package india.collageapp.com.get_a_way;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sai on 10-03-2016.
 */




public class PathGoogleMapActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;

    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543,
            -73.998585);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
    private static final LatLng WALL_STREET = new LatLng(40.7064, -74.0094);

    private static final String LOG_TAG = "PathGoogleMapActivity";
    GoogleMap mMap;
    private ArrayList<String> selectedPlaces_id = new ArrayList<String>() ;
    private ArrayList<Place> selectedPlaces = new ArrayList<Place>() ;
    final String TAG = "PathGoogleMapActivity";
    double current_latitude;
    double current_longitude ;
    String waypoints_ ;
    private int got_places_flag = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.route);
        setUpMapIfNeeded();
        ReadTask downloadTask = new ReadTask();

        mGoogleApiClient = new GoogleApiClient.Builder(PathGoogleMapActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();


        Bundle bundle = getIntent().getExtras();
        selectedPlaces_id = (ArrayList<String>)bundle.getSerializable("selectedPlaces_id");
        waypoints_ = (String)bundle.getSerializable("waypoints");

        Log.e("waypoints: " , waypoints_);

         Log.e("sel places : ", selectedPlaces_id.toString());
        if(selectedPlaces_id != null)
        {
            for (String s : selectedPlaces_id) {
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, s);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);


                Log.e("Called getPlaceById for" , s);

            }
        }



        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation())
        {
            current_latitude = gps.getLatitude(); // returns latitude
            current_longitude = gps.getLongitude(); // returns longitude
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + current_latitude + "\nLong: " + current_longitude, Toast.LENGTH_LONG).show();

        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        //LatLng curr_loc = new LatLng(current_latitude,current_longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(current_latitude, current_longitude), 12.0f));


        String url = getMapsApiDirectionsUrl();
        downloadTask.execute(url);




    }


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());

                places.release();

                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            //CharSequence attributions = places.getAttributions();

            selectedPlaces.add(place);
            Log.e("place name : ", place.getAddress() + "");

            addMarkers(mMap,place);

            places.release();
            Log.e("sel len : ", selectedPlaces.size() + "");

            got_places_flag =1;

        }

            //selectedPlaces_id.add(place.getId()+"");



    };

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION);
            // namesake check for permission works without this (used to simply remove syntax errors)
        }
        mMap.setMyLocationEnabled(true);

    }

    private String getMapsApiDirectionsUrl() {


        /*
        String waypoints = "waypoints=optimize:true|"
                + LOWER_MANHATTAN.latitude + "," + LOWER_MANHATTAN.longitude
                + "|" + BROOKLYN_BRIDGE.latitude + "," + BROOKLYN_BRIDGE.longitude
                + "|" + WALL_STREET.latitude + "," + WALL_STREET.longitude;
*/
        double dst_lat = current_latitude + 0.00001;
        double dst_long = current_longitude + 0.00001;

        String sensor = "sensor=false";
       String origin ="origin=" + current_latitude + "," + current_longitude;

        String destination = "destination=" + dst_lat + "," + dst_long;

        String waypoints = waypoints_;

        String params = origin + "&" + destination + "&" + waypoints + "&" + sensor;


        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        Log.e("url : " , url);
        return url;
    }

    private void addMarkers(GoogleMap mMap, Place place) {
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions().position(place.getLatLng())
                    .title(place.getName()+""));

        }
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
    public void onConnected(Bundle bundle) {
        Log.i(LOG_TAG, "Google Places API connected.");

    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("exc doInBackground", e.toString());

            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            if(routes!=null)
            {
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(10);
                    polyLineOptions.color(Color.BLUE);
                }
            }

 //           googleMap.addPolyline(polyLineOptions);
            if(polyLineOptions!=null)
                mMap.addPolyline(polyLineOptions);
        }


    }
}