package india.collageapp.com.get_a_way;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;


public class SelectTripPlan extends AppCompatActivity implements TripList.MyDialogFragmentListener{

    private RadioGroup planGroup;
    private RadioButton planButton;
    private Button goButton;

    String[] tripnames ;
    String[] places;
    String[] location;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trip_plan);
        userid = getIntent().getStringExtra("userid");
        planGroup=(RadioGroup)findViewById(R.id.radioGroup);

        goButton=(Button)findViewById(R.id.goButton);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = planGroup.getCheckedRadioButtonId();
                planButton = (RadioButton) findViewById(selectedId);
                if (planButton.getText().equals("   Plan A New Trip   ")) {
                    // move to the activity to plan new trip
                    Intent intent = new Intent(getBaseContext(), MainMaps.class); //change here
                    startActivity(intent);
                } else {
                    //for the given user, check db for saved trips
                    //place them in pop up widow
                    //select tripName,place,location where userId = x
                    SendRequest s = new SendRequest();
                    s.execute("HELLO WORLD");
                }
            }
        });
    }

    private class SendRequest  extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... arg0) {

            try{
                Log.d("RESULT", arg0[0]);
                //String username = (String)arg0[0];
                //String password = (String)arg0[1];
                String link = "http://travelapp.freevar.com/getTrip.php?username="+userid;
                Log.d("RESULT",link);
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    Log.d("RESULT", line);
                    break;
                }
                in.close();
                Log.d("RESULT",sb.toString());
                return sb.toString();
            }

            catch(Exception e){
                Log.d("Exception",e.getMessage());
                return new String("Exception: " + e.getMessage());
            }
        }


        @Override
        protected void onPostExecute(String result){
            JSONObject object = null;
            JSONArray jsonArray = null;
            try {
                object = new JSONObject(result);
                jsonArray = object.getJSONArray("products");

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            try {
                if(jsonArray != null) {
                    int n = jsonArray.length();
                    tripnames = new String[n];
                    places = new String[n];
                    location = new String[n];

                    Log.d("RESULT",String.valueOf(n));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object1 = (JSONObject) jsonArray.get(i);
                        tripnames[i] = object1.getString("tripname");
                        places[i] = object1.getString("places");
                        location[i] = object1.getString("location");

                    }

                    //Log.d("RESULT",places[0]);
                    //Log.d("RESULT",places[1]);
                    //Log.d("RESULT", places[2]);


                    DialogFragment newFragment = TripList.newInstance(tripnames);
                    newFragment.show(getSupportFragmentManager(), "tripList");
                }
                else
                {
                    Log.d("RESULT","empty");//no trips saved
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectTripPlan.this);
                    builder.setMessage("NO TRIPS SAVED !!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    AlertDialog alert11 = builder.create();
                    alert11.show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }

    @Override
    public void onReturnValue(int select) {
        if(select == -1)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectTripPlan.this);
            builder.setMessage("NO TRIP SELECTED !!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert2 = builder.create();
            alert2.show();
        }
        else
        {
            //move to the next activity
            Log.d("RESULT", String.valueOf(select));
            Intent intent = new Intent(getBaseContext(),DisplayAllPlaces.class);
            String add=" waypoints=optimize:true"+location[select];
            intent.putExtra("places_list",places[select]);
            intent.putExtra("waypoints", add);
            startActivity(intent);
        }
    }
}

