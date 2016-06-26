package pl.codeinprogress.notes.ui;

import android.databinding.DataBindingUtil;
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
import pl.codeinprogress.notes.databinding.ActivitySignupBinding;
import pl.codeinprogress.notes.firebase.FirebaseActivity;

public class SignupActivity extends FirebaseActivity {

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
