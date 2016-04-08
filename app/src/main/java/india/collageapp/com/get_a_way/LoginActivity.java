package india.collageapp.com.get_a_way;

/**
 * Created by admin on 14-Mar-16.
 */
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity implements AsyncResponse{
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    String result = new String("SloNetwork");
    int i=0;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                result="SloNetwork";
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;
        backgroundWorker.execute(type, email, password);

       new android.os.Handler().postDelayed(
               new Runnable() {
                   public void run() {
                       // On complete call either onLoginSuccess or onLoginFailed
                       if (result.equals("")) {
                           System.out.println("Not here1");
                           //waitForIt(progressDialog);
                           onLoginFailedV2();
                           progressDialog.dismiss();
                       } else if (result.equals("SloNetwork")) {
                           System.out.println("Not here2");
                           waitForIt(progressDialog);

                       } else if (result.equals("Login Success")) {
                           onLoginSuccess();
                           progressDialog.dismiss();
                       } else {
                           System.out.println("Why come here1");
                           onLoginFailed();
                           progressDialog.dismiss();
                       }
                       // onLoginFailed();

                   }
               }, 3000);

    }

    protected void waitForIt(final ProgressDialog progressDialog) {

        if (i >= 5) {
            onLoginFailedV3();
            progressDialog.dismiss();
        } else {
            i++;
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            if (result.equals("")) {
                                System.out.println("Infinite Loop 1");
                                onLoginFailedV2();
                                progressDialog.dismiss();
                            }
                            else if(result.equals("SloNetwork")){
                                System.out.println("Infinite Loop 2");
                                progressDialog.setMessage("Authenticating...Slow Network connection");
                                waitForIt(progressDialog);

                            }
                            else if (result.equals("Login Success")) {

                                onLoginSuccess();
                                progressDialog.dismiss();
                            } else {
                                System.out.println("Why come here2");
                                onLoginFailed();
                                progressDialog.dismiss();
                            }
                            // onLoginFailed();

                        }
                    }, 5000);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                              // this.finish();
                Intent p = new Intent(LoginActivity.this, HomePage.class);
                p.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(p);
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", null).show();
    }

    public void onLoginSuccess() {
        i=0;
        result="sloNetwork";
        _loginButton.setEnabled(true);
        Intent p = new Intent(LoginActivity.this, HomePage.class);
        p.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(p);

       // finish();
    }
    public void processFinish(String output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        result=output;
        System.out.println("The final attempt in LoginActivity is" + output);

    }

    public void onLoginFailed() {
        i=0;
        result="SloNetwork";
        Toast.makeText(getBaseContext(), "Login failed due to incorrect Username or Password", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }
    public void onLoginFailedV2() {
        i=0;
        result="SloNetwork";
        Toast.makeText(getBaseContext(), "No Network", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }
    public void onLoginFailedV3() {
        i=0;
        result="SloNetwork";
        Toast.makeText(getBaseContext(), "Extremely Slow Network", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
