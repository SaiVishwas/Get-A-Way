package india.collageapp.com.get_a_way;

/**
 * Created by admin on 14-Mar-16.
 */
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignupActivity extends AppCompatActivity implements AsyncResponse {
    private static final String TAG = "SignupActivity";
    String result= new String("SlowNetwork");
    int i = 0;

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String username = email;

        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate = this;
        backgroundWorker.execute(type,name,email,username,password);

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                       // onSignupSuccess();
                        // onSignupFailed();
                        //progressDialog.dismiss();
                        if (result.equals("")) {
                            System.out.println("Not here1");
                            //waitForIt(progressDialog);
                            onSignupFailed();
                            progressDialog.dismiss();
                        }
                        else if(result.equals("SlowNetwork")){
                            System.out.println("Not here2");
                            waitForIt(progressDialog);

                        }

                        else if (result.equals("Insert Successful")) {
                            onSignupSuccess();
                            progressDialog.dismiss();
                        } else {
                            System.out.println("Why come here1");
                            onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }
                }, 3000);
    }
    protected void waitForIt(final ProgressDialog progressDialog) {

        if (i >= 5) {
            onSignupFailed();
            progressDialog.dismiss();
        } else {
            i++;
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            if (result.equals("")) {
                                System.out.println("Infinite Loop 1");
                                onSignupFailed();
                                progressDialog.dismiss();
                            }
                            else if(result.equals("SlowNetwork")){
                                System.out.println("Infinite Loop 2");
                                waitForIt(progressDialog);

                            }
                            else if (result.equals("Insert Successful")) {

                                onSignupSuccess();
                                progressDialog.dismiss();
                            } else {
                                System.out.println("Why come here2");
                                onSignupFailed();
                                progressDialog.dismiss();
                            }
                            // onLoginFailed();

                        }
                    }, 6000);


        }
    }


    public void onSignupSuccess() {
        i=0;
        result="SlowNetwork";
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        i=0;
        result="SlowNetwork";
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }
    public void processFinish(String output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        result=output;
        System.out.println("The final attempt in SignUp activity is" + output);

    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

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

