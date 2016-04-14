package india.collageapp.com.get_a_way;

import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;



public class SelectTripPlan extends AppCompatActivity {

    private RadioGroup planGroup;
    private RadioButton planButton;
    private Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trip_plan);

        planGroup=(RadioGroup)findViewById(R.id.radioGroup);

        goButton=(Button)findViewById(R.id.goButton);

        Point p;
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = planGroup.getCheckedRadioButtonId();
                planButton = (RadioButton) findViewById(selectedId);
                if (planButton.getText().equals("Plan A New Trip")) {
                    // move to the activity to plan new trip
                    Intent intent = new Intent(getBaseContext(), MainMaps.class); //change here
                    startActivity(intent);
                } else {
                    //for the given user, check db for saved trips
                    //place them in pop up widow
                    //select tripName,place,location where userId = x
                    String trip = "";
                    if (trip != "") {
                        DialogFragment newFragment = new RetrieveTrip();
                        newFragment.show(getSupportFragmentManager(), "savedTrip");

                    } else {
                        DialogFragment newFragment = new NoTrip();
                        newFragment.show(getSupportFragmentManager(), "noTrip");
                    }
                }
            }
        });
    }
}

