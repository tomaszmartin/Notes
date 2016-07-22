package pl.codeinprogress.notes.presenter.auth;

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
