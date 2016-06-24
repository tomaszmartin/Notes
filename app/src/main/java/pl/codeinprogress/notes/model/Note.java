package pl.codeinprogress.notes.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tomaszmartin on 22.06.2016.
 */
public class Note {

    private String id;
    private String title;
    private String description;
    private String fileName;
    private ArrayList<String> imageFileNames;
    private long created;
    private long lastModified;
    private String tag;
    private boolean isPasswordProtected;

    public Note(String id) {
        this.id = id;
        long timestamp = new Date().getTime();
        this.created = timestamp;
        this.lastModified = timestamp;
        this.fileName = id + ".txt";
    }

    public Note() {
        this.imageFileNames = new ArrayList<>();
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
        int maxLength = 256;
        if (description.length() > maxLength) {
            description = description.substring(0, maxLength);
            description = description.substring(0, description.lastIndexOf(" "));
        }
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<String> getImageFileNames() {
        return imageFileNames;
    }

    public void setImageFileNames(ArrayList<String> imageFileNames) {
        this.imageFileNames = imageFileNames;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
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
