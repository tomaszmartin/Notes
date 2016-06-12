package pl.codeinprogress.notes.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.firebase.FirebaseActivity;

public class LoginActivity extends FirebaseActivity {

    @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;
    @Bind(R.id.emailField) TextInputEditText emailField;
    @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
    @Bind(R.id.passwordField) TextInputEditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private boolean validate() {
        boolean result = true;
        String email = emailField.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(emailWrapper, getString(R.string.email_error));
            result = false;
        }
        String password = passwordField.getText().toString();
        if (password.length() < 7) {
            showError(passwordWrapper, getString(R.string.password_error));
            result = false;
        }

        return result;
    }

    @OnClick(R.id.loginButton)
    void login() {
        if (validate()) {
            getAuth().signInWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    private void showError(TextInputLayout field, String message) {
        field.setErrorEnabled(true);
        field.setError(message);
    }

    private void hideError(TextInputLayout field) {
        field.setErrorEnabled(false);
    }
}
