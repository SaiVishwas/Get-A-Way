package india.collageapp.com.get_a_way;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    private static Button btn_places;
    private static Button btn_maps;
    private static Button btn_chatbot;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        // onClickButtonListner();
        userid = getIntent().getStringExtra("userid");
        Log.d("USERID",userid);
        btn_places=(Button)findViewById(R.id.button1);
        btn_places.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent(HomePage.this, Dashboard.class);
                                              startActivity(intent);
                                          }
                                      }
        );

        btn_maps=(Button)findViewById(R.id.button2);
        btn_maps.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent(HomePage.this,SelectTripPlan.class);
                                              intent.putExtra("userid", userid);
                                              startActivity(intent);
                                          }
                                      }
        );

        btn_chatbot=(Button)findViewById(R.id.button3);
        btn_chatbot.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent(HomePage.this,ChatBot.class);
                                              startActivity(intent);
                                          }
                                      }
        );


    }

    private static long back_pressed;
    @Override
    public void onBackPressed(){
        if (back_pressed + 2000 > System.currentTimeMillis()){
            finish();
        }
        else{
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}
