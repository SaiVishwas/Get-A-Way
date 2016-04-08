package india.collageapp.com.get_a_way;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    CardAdapter adapter;
    List<State> states;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        recyclerView=(RecyclerView)findViewById(R.id.cardList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        initializeData();

        adapter = new CardAdapter(states);
        recyclerView.setAdapter(adapter);
    }
    private void initializeData() {

        states = new ArrayList<>();
        states.add(new State("Mysore Place","des2", R.drawable.image1));
        states.add(new State("Chamundi Hills","des3", R.drawable.image2));
        states.add(new State("Mysore Zoo","des4", R.drawable.image3));
        states.add(new State("JaganMohan Place","des5", R.drawable.image4));
        states.add(new State("Lalitha Mahal","des5", R.drawable.image5));
        states.add(new State("Brindavan Garden","des5", R.drawable.image6));
        states.add(new State("St Philomena’s Church","des5", R.drawable.image7));
    }
}

