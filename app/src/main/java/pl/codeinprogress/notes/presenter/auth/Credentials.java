package pl.codeinprogress.notes.presenter.auth;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by tomaszmartin on 12.06.16.
 */

public class Credentials {

    private String name;
    private String id;
    private String email;
    private String image;
    private boolean logged;

    public Credentials(String name, String id, String email, String image, boolean logged) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.logged = logged;
        this.image = image;
    }

    public Credentials(String name, String email, String image, boolean logged) {
        this(name, email.replace(".", ","), email, image, logged);
    }

    public static Credentials fromFirebaseUser(FirebaseUser user) {
        String image = null;
        if (user.getPhotoUrl() != null) {
            image = user.getPhotoUrl().toString();
        }

        return new Credentials(
                user.getDisplayName(),
                user.getUid(),
                user.getEmail(),
                image,
                true
        );
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isLogged() {
        return logged;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Credentials object with name: " + getName() + ", email: " + getEmail()
                + ", id: " + getId() + ", image: " + getImage() + ", logged: " + isLogged();
    }

}
