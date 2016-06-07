package pl.codeinprogress.notes.ui;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.firebase.FirebaseActivity;

public class LoginActivity extends FirebaseActivity {

    @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;
    @Bind(R.id.emailField) TextInputEditText emailField;
    @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
    @Bind(R.id.passwordField) TextInputEditText passwordField;
    @Bind(R.id.loginButton) AppCompatButton loginButton;

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

    private void validate() {

    }

}
