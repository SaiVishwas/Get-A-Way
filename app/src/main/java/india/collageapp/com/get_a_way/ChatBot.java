package india.collageapp.com.get_a_way;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Calendar;


public class ChatBot extends AppCompatActivity implements LocationListener {

    double mLatitude = 0;
    double mLongitude = 0;
    String[] questions = new String[3];
    String[] answers = new String[3];
    int i =0;
    TextView t1;
    EditText e;
    Button b;
    String ans;

    String PLACES_SEARCH_URL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        t1 = (TextView)findViewById(R.id.textView1);
        e = (EditText) findViewById(R.id.editText);
        b = (Button)findViewById(R.id.button1);

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.setEnabled(true);
            }
        });

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

        askQuestions();
    }

    public void askQuestions()
    {
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        String greeting="";
        Log.d("TIME", String.valueOf(hour));
        if(hour < 12)
            greeting = "Good Morning !!";
        else if(hour < 16)
            greeting = "Good Afternoon !!";
        else
            greeting = "Good Evening !!";


        String name = "Sharvel";


        questions[0] = greeting+"\nHey "+name+" !!! Where would you like to go ?";
        questions[1] = "How far do you want to travel ? (in Km)";
        questions[2] = "How much time do you have at hand ?";

        t1.setText(questions[0]);
    }

    public void getReply(View v)
    {
        boolean flag;
        b.setEnabled(false);
        ans = e.getText().toString();
        String ques = t1.getText().toString();
        i = Arrays.asList(questions).indexOf(ques);
        Log.d("INDEX",String.valueOf(i));
        if(ans == "")
        {
            flag = false;
        }
        flag = preprocess();
        e.setText("");
        if(! flag)
        {
            //make tooltip appear
        }
        else {
            if(i != 2)
            {
                ++i;
                getNext();
            }
            else
            {
                e.setEnabled(false);
                t1.setText("Locating nearby "+answers[0]+" for you ");
                formQueryString();
            }

        }
    }

    private boolean preprocess()
    {
        boolean result = true;
        if(i==0)
        {
            answers[i] = ans;
            //TODO
        }
        if(i == 1 || i == 2)
        {
            boolean check = isInteger(ans);
            if(check)
            {
                int distance = Integer.parseInt(ans);
                if(distance != 0) {
                    distance *= 1000; // km into metres
                    answers[i] = String.valueOf(distance);
                }
                else
                {
                    //enter non zero distance
                    result = false;
                }

            }
            else{
                //not an integer
                result = false;
            }
        }
        return result;
    }

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public void getNext()

    {
        t1.setText(questions[i]);
    }

    public void formQueryString()
    {
        StringBuilder sb = new StringBuilder(PLACES_SEARCH_URL);
        sb.append("location="+mLatitude+","+mLongitude);
        sb.append("&radius="+answers[1]);
        sb.append("&types="+answers[0]);
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyC1PUYZSljcphlf0zn2rN_Ae2MP8MWVlfU");

        PlacesTask placesTask = new PlacesTask();
        placesTask.execute(sb.toString());
    }
    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
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
            Intent intent = new Intent(getBaseContext(), ChatBotMaps.class);
            intent.putExtra("placeList", result);
            startActivity(intent);

        }

    }
}

