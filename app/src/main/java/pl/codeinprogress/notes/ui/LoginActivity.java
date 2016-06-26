package pl.codeinprogress.notes.ui;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.auth.FormValidator;
import pl.codeinprogress.notes.firebase.FirebaseActivity;

public class LoginActivity extends FirebaseActivity {

    @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;
    @Bind(R.id.emailField) TextInputEditText emailField;
    @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
    @Bind(R.id.passwordField) TextInputEditText passwordField;

    public static final int SIGNUP_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.loginButton)
    public void login() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if (FormValidator.validateEmail(email) && FormValidator.validatePassword(password)) {
            getAuthHandler().login(email, password, this);
        } else {
            if (!FormValidator.validateEmail(email)) {
                emailWrapper.setError(getString(R.string.email_error));
                emailWrapper.setErrorEnabled(true);
            } else {
                emailWrapper.setErrorEnabled(false);
            }

            if (!FormValidator.validatePassword(password)) {
                passwordWrapper.setError(getString(R.string.password_error));
                passwordWrapper.setErrorEnabled(true);
            } else {
                passwordWrapper.setErrorEnabled(false);
            }
        }
    }

    @OnClick(R.id.signupButton)
    public void signup() {
        startActivityForResult(new Intent(this, SignupActivity.class), SIGNUP_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGNUP_REQUEST) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
