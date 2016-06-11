package pl.codeinprogress.notes.ui;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Patterns;

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

    @OnClick(R.id.loginButton)
    private void validate() {
        boolean isValid = true;
        String email = emailField.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(emailWrapper, getString(R.string.email_error));
            isValid = false;
        }
        String password = passwordField.getText().toString();
        if (password.length() < 7) {
            showError(passwordWrapper, getString(R.string.password_error));
            isValid = false;
        }

        if (isValid) {
            login();
        }
    }

    private void login() {
        
    }

    private void showError(TextInputLayout field, String message) {
        field.setErrorEnabled(true);
        field.setError(message);
    }

    private void hideError(TextInputLayout field) {
        field.setErrorEnabled(false);
    }
}
