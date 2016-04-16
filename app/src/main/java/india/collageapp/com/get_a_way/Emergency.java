package india.collageapp.com.get_a_way;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

public class Emergency extends FragmentActivity implements OnMapReadyCallback {

    Spinner mSprPlaceType;
    String[] mPlaceType = null;
    String[] mPlaceTypeName = null;
    double mLatitude = 0;
    double mLongitude = 0;
    String PLACES_SEARCH_URL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_emergency);
        mPlaceType = getResources().getStringArray(R.array.place_type);

        // Array of place type names
        mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);

        // Creating an array adapter with an array of Place types
        // to populate the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mPlaceTypeName);

        // Getting reference to the Spinner
        mSprPlaceType = (Spinner) findViewById(R.id.spinner);

        // Setting adapter on Spinner to set place types
        mSprPlaceType.setAdapter(adapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation())
        {
            mLatitude = gps.getLatitude(); // returns latitude
            mLongitude = gps.getLongitude(); // returns longitude
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + mLatitude + "\nLong: " + mLongitude, Toast.LENGTH_LONG).show();

        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }

    public void findPlaces(View v)
    {
        int selectedPosition = mSprPlaceType.getSelectedItemPosition();
        String type = mPlaceType[selectedPosition];

        StringBuilder sb = new StringBuilder(PLACES_SEARCH_URL);
        sb.append("location="+mLatitude+","+mLongitude);
        sb.append("&radius=1000");
        sb.append("&types="+type);
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyC1PUYZSljcphlf0zn2rN_Ae2MP8MWVlfU");

        PlacesTask placesTask = new PlacesTask();
        placesTask.execute(sb.toString());
    }

    private String downloadUrl(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("DATA",content.toString());
        return content.toString();
    }


    private class PlacesTask extends AsyncTask<String, Integer, String>
    {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                Log.d("LINK",url[0]);
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result)
        {
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }

    }
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            Log.d("REACHED", "ParseRTask");
            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);
                places = placeJsonParser.parse(jObject);
                Log.d("REACHED", "parsed");

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        protected void onPostExecute(List<HashMap<String, String>> list)
        {
            mMap.clear();
            int max = (list.size() > 8)? 8: list.size();
            for (int i = 0; i < max; i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");

                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                //This will be displayed on taping the marker
                markerOptions.title(name + " : " + vicinity);

                // Placing a marker on the touched position
                Marker m = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            }
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(mLatitude,mLongitude);
            markerOptions.position(latLng);
            markerOptions.title("You're currently here !").icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            Marker m = mMap.addMarker(markerOptions);
            m.showInfoWindow();
        }
    }
}
