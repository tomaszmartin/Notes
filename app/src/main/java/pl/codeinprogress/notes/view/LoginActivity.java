package pl.codeinprogress.notes.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.presenter.auth.Validator;
import pl.codeinprogress.notes.databinding.ActivityLoginBinding;
import pl.codeinprogress.notes.model.data.firebase.FirebaseActivity;

public class LoginActivity extends FirebaseActivity {

    public static final int SIGNUP_REQUEST = 1;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
            }
        });
    }

    public void login(View view) {
        String email = binding.emailField.getText().toString();
        String password = binding.passwordField.getText().toString();
        if (Validator.validateEmail(email) && Validator.validatePassword(password)) {
            getAuthHandler().login(email, password, this);
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.action_authenticating));
            progressDialog.show();
        } else {
            if (!Validator.validateEmail(email)) {
                binding.emailWrapper.setError(getString(R.string.email_error));
                binding.emailWrapper.setErrorEnabled(true);
            } else {
                binding.emailWrapper.setErrorEnabled(false);
            }

            if (!Validator.validatePassword(password)) {
                binding.passwordWrapper.setError(getString(R.string.password_error));
                binding.passwordWrapper.setErrorEnabled(true);
            } else {
                binding.passwordWrapper.setErrorEnabled(false);
            }
        }
    }

    public void signup(View view) {
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
