package pl.codeinprogress.notes.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.presenter.auth.Credentials;
import pl.codeinprogress.notes.databinding.ActivitySignupBinding;
import pl.codeinprogress.notes.presenter.firebase.BaseActivity;

public class SignupActivity extends BaseActivity {

    public static final int SIGNED = 1;
    public ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
    }

    public void signup(View view) {
        String name = binding.firstNameField.getText().toString() + " " + binding.lastNameField.getText().toString();
        String email = binding.emailField.getText().toString();
        String password = binding.passwordField.getText().toString();
        Credentials credentials = new Credentials(name, email, null, true);

        getAuthHandler().singup(credentials, password, this);
    }

}
