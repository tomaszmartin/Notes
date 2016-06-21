package pl.codeinprogress.notes.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.util.Patterns;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.auth.Credentials;
import pl.codeinprogress.notes.firebase.FirebaseActivity;

public class SignupActivity extends FirebaseActivity {

    @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;
    @Bind(R.id.emailField) TextInputEditText emailField;
    @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
    @Bind(R.id.passwordField) TextInputEditText passwordField;
    @Bind(R.id.firstNameWrapper) TextInputLayout firstNameWrapper;
    @Bind(R.id.firstNameField) TextInputEditText firstNameField;
    @Bind(R.id.lastNameWrapper) TextInputLayout lastNameWrapper;
    @Bind(R.id.lastNameField) TextInputEditText lastNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.signupButton)
    public void signup(View view) {
        String name = firstNameField.getText().toString() + " " + lastNameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        Credentials credentials = new Credentials(name, email, null, true);

        getAuthHandler().singup(credentials, password);
    }

}
