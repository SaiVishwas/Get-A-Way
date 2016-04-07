package india.collageapp.com.get_a_way;

/**
 * Created by sai on 05-04-2016.
 */


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;


public class Suggestions extends AppCompatActivity {

    String PLACES_SEARCH_URL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
   // String listName="Hello World\n";
    String suggestion_name = "";
    String suggestion_latlng ="";
    //int total = 2;
    //int i=0;

    String waypoints_ ;
    String places_list ;

    int no_of_places = 0 ;
    int places_counter = 0;
    int completed_flag = 0;

    private Hashtable<String, String> places_dict = new Hashtable<String, String>();
    private Hashtable<String, String> selected_places_dict = new Hashtable<String, String>();


    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        //   TextView t = (TextView)findViewById(R.id.suggest);

        Bundle bundle = getIntent().getExtras();
        //   selectedPlaces_id = (ArrayList<String>)bundle.getSerializable("selectedPlaces_id");

        // waypoints contains lat and lng
        waypoints_ = (String)bundle.getSerializable("waypoints");
        // places list contains place names
        places_list = (String)bundle.getSerializable("places_list");



        Log.e("waypoints: " , waypoints_);
        Log.e("places list : ", places_list);

        String[] data = waypoints_.split("\\|");
        String[] place_names = places_list.split("\\|");

        no_of_places = data.length - 1;
        //Log.e("list : " , Arrays.toString(data));
        if(data.length > 1)
        {
            for (int i = 1; i < data.length; ++i)
            {
                String[] lat_lng = data[i].split(",");

                LatLng loc1 = new LatLng(Double.parseDouble(lat_lng[0]),Double.parseDouble(lat_lng[1]) );

                //addMarkers(mMap,tmp,place_names[i]);
                PlacesTask placesTask1 = new PlacesTask();
                String options="amusement_park|aquarium|art_gallery|museum|park|zoo|" +
                        "campground|church|hindu_temple|stadium|shopping_mall|rv_park";

                StringBuilder sb = new StringBuilder(PLACES_SEARCH_URL);
                sb.append("location=" + loc1.latitude + "," + loc1.longitude);
                sb.append("&radius=5000");
                sb.append("&types=" + options);
                sb.append("&sensor=true");
                sb.append("&key=AIzaSyC1PUYZSljcphlf0zn2rN_Ae2MP8MWVlfU");

                placesTask1.execute(sb.toString());




            }



        }

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PathGoogleMapActivity.class);
                //     intent.putExtra("selectedPlaces_id", selectedPlaces);

                // TODO : update waypoints here

                for (String key : selected_places_dict.keySet()) {

                    // System.out.println("key: " + key + " value: " + places_dict.get(key));
                    // waypoints = waypoints + "|" + place.getLatLng().latitude + "," + place.getLatLng().longitude ;
                    waypoints_ = waypoints_ + "|" + selected_places_dict.get(key) ;
                    places_list = places_list + "|" + key ;
                }
                intent.putExtra("waypoints",waypoints_);
                intent.putExtra("places_list",places_list);
                startActivity(intent);
            }
        });





/*
        PlacesTask placesTask1 = new PlacesTask();
        PlacesTask placesTask2 = new PlacesTask();

        String options="amusement_park|aquarium|art_gallery|museum|park|spa|zoo|cafe|campground|church|hindu_temple|stadium|shopping_mall|rv_park|park|night_club|museum";
        LatLng loc1 = new LatLng(13.0648845,77.553149);

        StringBuilder sb = new StringBuilder(PLACES_SEARCH_URL);
        sb.append("location=" + loc1.latitude + "," + loc1.longitude);
        sb.append("&radius=5000");
        sb.append("&types=" + options);
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyC1PUYZSljcphlf0zn2rN_Ae2MP8MWVlfU");

        placesTask1.execute(sb.toString());


        LatLng loc2 = new LatLng(19.0760,72.8777);
        StringBuilder sb1 = new StringBuilder(PLACES_SEARCH_URL);
        sb1.append("location=" + loc2.latitude + "," + loc2.longitude);
        sb1.append("&radius=5000");
        sb1.append("&types=" + options);
        sb1.append("&sensor=true");
        sb1.append("&key=AIzaSyC1PUYZSljcphlf0zn2rN_Ae2MP8MWVlfU");

        placesTask2.execute(sb1.toString());
*/
        Log.e("Final places @ : " , suggestion_name);
        Log.e("Final latlng  @ : " , suggestion_latlng);

    }

    void add_cards()
    {
        container = (LinearLayout)findViewById(R.id.container);

        String[] sugg_names = suggestion_name.split("\\|");
        String[] sugg_latlng = suggestion_latlng.split("\\|");

        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i = 1 ; i< sugg_names.length ; ++i) {


            final View addView = layoutInflater.inflate(R.layout.suggestion_row, null);
            final TextView textOut = (TextView) addView.findViewById(R.id.textout);
            textOut.setText(sugg_names[i]);

            places_dict.put(sugg_names[i], sugg_latlng[i]);


            Button buttonRemove = (Button) addView.findViewById(R.id.remove);
            buttonRemove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    /*((LinearLayout) addView.getParent()).removeView(addView);
                    Log.e("Removed : ", textOut.getText() + ""); */
                    /*
                    places_dict.remove(textOut.getText() + "");
                    Log.e("place dict", places_dict.toString()); */

                    final RelativeLayout bar = (RelativeLayout)addView.findViewById(R.id.bar);
                    bar.setBackgroundColor(Color.parseColor("#FF00FFE1"));


                    final TextView textOut = (TextView) addView.findViewById(R.id.textout);
                    String place_name = textOut.getText() + "";

                    selected_places_dict.put(place_name,places_dict.get(place_name));

                    /*waypoints_ = waypoints_ + "|" + places_dict.get(place_name) ;
                    places_list = places_list + "|" + place_name ;
                    */




                }
            });

            container.addView(addView);

        }
    }

    private class PlacesTask extends AsyncTask<String, Integer, String>
    {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                Log.d("LINK", url[0]);
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
        Log.d("DATA", content.toString());
        return content.toString();
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
            // TextView t = (TextView)findViewById(R.id.suggest);
            //++i;

            // not all places have 6 suggestions

            if( list.size() < 6)
            {
                for (int i = 0; i < list.size(); i++)   // this line is correct actually
                {
                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);
                    // Getting name
                    if (hmPlace != null) {
                        String name = hmPlace.get("place_name");
                        String lat = hmPlace.get("lat") + "";
                        String lng = hmPlace.get("lng") + "";
                        suggestion_name += "|" + name;
                        suggestion_latlng += "|" + lat + "," + lng;
                    }
                }
            }

            else
            {
                for (int i = 0; i < 6; i++) {
                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);
                    // Getting name
                    if (hmPlace != null) {
                        String name = hmPlace.get("place_name");
                        String lat = hmPlace.get("lat") + "";
                        String lng = hmPlace.get("lng") + "";
                        suggestion_name += "|" + name;
                        suggestion_latlng += "|" + lat + "," + lng;
                    }
                }
            }
            //t.setText(listName);
            /*t.setText(listName);
            if(i==total) {
                Log.d("LIST", listName);
                t.setText(listName);
            }
            */

            Log.e("suggested place names: ",suggestion_name);
            Log.e("suggested latlng : ",suggestion_latlng);

            ++ places_counter;

            Log.e("count : " , places_counter + "   ,    " + no_of_places + "");

            if(places_counter == no_of_places)
            {
                completed_flag = 1;
                Log.e("Final places : " , suggestion_name);
                Log.e("Final latlng : " , suggestion_latlng);

                add_cards();
            }
        }
    }
}

