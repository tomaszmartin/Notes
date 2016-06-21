package pl.codeinprogress.notes.auth;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by tomaszmartin on 21.06.2016.
 */
public class CredentialsTest {

    @Test
    public void testFromFirebaseUser() throws Exception {
        FirebaseUser user = new FirebaseUser() {
            @NonNull
            @Override
            public String getUid() {
                return "123456789";
            }

            @NonNull
            @Override
            public String getProviderId() {
                return "self";
            }

            @Override
            public boolean isAnonymous() {
                return false;
            }

            @Nullable
            @Override
            public List<String> getProviders() {
                return null;
            }

            @NonNull
            @Override
            public List<? extends UserInfo> getProviderData() {
                return null;
            }

            @NonNull
            @Override
            public FirebaseUser zzN(@NonNull List<? extends UserInfo> list) {
                return null;
            }

            @Override
            public FirebaseUser zzaK(boolean b) {
                return null;
            }

            @NonNull
            @Override
            public FirebaseApp zzOl() {
                return null;
            }

            @Nullable
            @Override
            public String getDisplayName() {
                return "Firstname Lastname";
            }

            @Nullable
            @Override
            public Uri getPhotoUrl() {
                return null;
            }

            @Nullable
            @Override
            public String getEmail() {
                return "email@example.com";
            }

            @NonNull
            @Override
            public String zzOm() {
                return null;
            }

            @Override
            public void zzhG(@NonNull String s) {

            }
        };
        Credentials credentials = Credentials.fromFirebaseUser(user);

        
    }

}