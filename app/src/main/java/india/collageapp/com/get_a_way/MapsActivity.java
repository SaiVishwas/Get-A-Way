package india.collageapp.com.get_a_way;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by sai on 07-03-2016.
 */
public class MapsActivity extends FragmentActivity {

    /*
    // constructor
    public MapsActivity(GoogleMap map)
    {
        mMap = map;
    }
    */

   // private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
*/
    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link //#setUpMap()} once when {@link #//mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
/*
    public void onSearch(View view)
    {
        EditText location_tf = (EditText)findViewById(R.id.autoCompleteTextView);
        String location = location_tf.getText().toString() ;
        List<Address> addressList = null;

        if(location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e){
                e.printStackTrace();
            }

            Address address = addressList.get(0);

            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
*/
    public void setMarker(GoogleMap mMap , Place place)
    {
        LatLng latLng = place.getLatLng();
        mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName() + ""));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        //map = mMap;
    }
/*
    public void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
*/
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    /*
    public void setUpMap() {
        // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION);
            // namesake check for permission works without this (used to simply remove syntax errors)
        }
        mMap.setMyLocationEnabled(true);

    }

    */
}
