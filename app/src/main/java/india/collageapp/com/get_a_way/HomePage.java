package india.collageapp.com.get_a_way;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    private static Button btn_places;
    private static Button btn_maps;
    private static Button btn_chatbot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        // onClickButtonListner();

        btn_places=(Button)findViewById(R.id.button1);
        btn_places.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent(HomePage.this, dashboard.class);
                                              startActivity(intent);
                                          }
                                      }
        );

        btn_maps=(Button)findViewById(R.id.button2);
        btn_maps.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent(HomePage.this,MainMaps.class);
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
}
