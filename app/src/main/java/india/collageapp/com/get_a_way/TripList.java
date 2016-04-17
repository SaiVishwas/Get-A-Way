package india.collageapp.com.get_a_way;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;


public class TripList extends DialogFragment {
    int select = -1;
    String[] tripNames;

    public interface MyDialogFragmentListener {
        public void onReturnValue(int select);
    }

    static TripList newInstance(String[] trip) {
        TripList f = new TripList();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        //args.putInt("num", num);
        args.putStringArray("tripList",trip);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        tripNames = getArguments().getStringArray("tripList");
        Log.d("ARGUMENT", tripNames[0]);
        //String[] toppings = {"Onion", "Tomato", "Lettuce","Jalapenos","Sausage","Extra Cheese","Masala"};
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("SELECT YOUR TRIP")
                .setSingleChoiceItems(tripNames, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                Log.d("VALUE", String.valueOf(item));
                                select = item;
                            }
                        })
                        // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        MyDialogFragmentListener activity = (MyDialogFragmentListener) getActivity();
                        activity.onReturnValue(select);
                        dismiss();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();

    }
}
