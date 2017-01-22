package pl.codeinprogress.notes.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.presenter.auth.Validator;

public class LoginActivity extends BaseActivity {

    public static final int SIGNUP_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button signupButton = (Button) findViewById(R.id.signupButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
            }
        });
    }

    public void login(View view) {
        hideKeyboard();
        TextInputEditText emailField = (TextInputEditText) findViewById(R.id.emailField);
        TextInputEditText passwordField = (TextInputEditText) findViewById(R.id.passwordField);

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        setEmailError(!Validator.validateEmail(email));
        setPasswordError(!Validator.validatePassword(password));
        if (Validator.validateEmail(email) && Validator.validatePassword(password)) {
            getAuthHandler().login(email, password, this);
            ProgressDialog progressDialog = new ProgressDialog(this, R.style.LoginDialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.action_authenticating));
            progressDialog.show();
        }
    }

    public void signup(View view) {
        hideKeyboard();
        startActivityForResult(new Intent(this, SignupActivity.class), SIGNUP_REQUEST);
    }

    private void setPasswordError(boolean isError) {
        TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

        passwordWrapper.setError(getString(R.string.password_error));
        passwordWrapper.setErrorEnabled(isError);
    }

    private void setEmailError(boolean isError) {
        TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);

        emailWrapper.setError(getString(R.string.email_error));
        emailWrapper.setErrorEnabled(isError);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGNUP_REQUEST) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
