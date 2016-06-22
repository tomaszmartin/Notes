package pl.codeinprogress.notes.data;

import java.util.ArrayList;

/**
 * Created by tomaszmartin on 22.06.2016.
 */
public class Note {

    private String id;
    private String title;
    private String description;
    private String textFileAdress;
    private ArrayList<String> imageFilesAdresses;
    private double created;
    private double lastModified;
    private String tag;
    private boolean isPasswordProtected;

    public Note() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTextFileAdress() {
        return textFileAdress;
    }

    public void setTextFileAdress(String textFileAdress) {
        this.textFileAdress = textFileAdress;
    }

    public ArrayList<String> getImageFilesAdresses() {
        return imageFilesAdresses;
    }

    public void setImageFilesAdresses(ArrayList<String> imageFilesAdresses) {
        this.imageFilesAdresses = imageFilesAdresses;
    }

    public double getCreated() {
        return created;
    }

    public void setCreated(double created) {
        this.created = created;
    }

    public double getLastModified() {
        return lastModified;
    }

    public void setLastModified(double lastModified) {
        this.lastModified = lastModified;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isPasswordProtected() {
        return isPasswordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        isPasswordProtected = passwordProtected;
    }
}
