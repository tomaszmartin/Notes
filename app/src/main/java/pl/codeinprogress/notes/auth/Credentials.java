package pl.codeinprogress.notes.auth;

/**
 * Created by tomaszmartin on 12.06.16.
 */
public class Credentials {

    private String firstName;
    private String lastName;
    private String id;
    private String email;
    private boolean logged;

    public Credentials(String firstName, String lastName, String id, String email, boolean logeed) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.email = email;
        this.logged = logeed;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
}
