package pl.codeinprogress.notes.view;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.presenter.auth.Credentials;

public class SignupActivity extends BaseActivity {

    public static final int SIGNED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void signup(View view) {
        TextInputEditText firstNameField = (TextInputEditText) findViewById(R.id.firstNameField);
        TextInputEditText lastNameField = (TextInputEditText) findViewById(R.id.lastNameField);
        TextInputEditText emailField = (TextInputEditText) findViewById(R.id.emailField);
        TextInputEditText passwordField = (TextInputEditText) findViewById(R.id.passwordField);


        String name = firstNameField.getText().toString() + " " + lastNameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        Credentials credentials = new Credentials(name, email, null, true);

        getAuthHandler().singup(credentials, password, this);
    }

}
