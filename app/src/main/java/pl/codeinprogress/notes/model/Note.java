package pl.codeinprogress.notes.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;

public class Note extends RealmObject {

    private String id;
    private String title;
    private String description;
    private String fileName;
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
        this.id = UUID.randomUUID().toString();
        long timestamp = new Date().getTime();
        this.created = timestamp;
        this.lastModified = timestamp;
        this.fileName = id + ".txt";
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

    public String getKey() {
        return "noteId";
    }

}
