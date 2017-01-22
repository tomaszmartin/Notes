package pl.codeinprogress.notes.model;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Note extends RealmObject {

    @PrimaryKey
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

    public Note(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.description = note.getDescription();
        this.fileName = note.getFileName();
        this.created = note.getCreated();
        this.lastModified = note.getLastModified();
        this.tag = note.getTag();
        this.isPasswordProtected = note.isPasswordProtected();
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

    public Note copy() {
        try {
            return (Note) this.clone();
        } catch (CloneNotSupportedException e) {
            return new Note();
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", fileName='" + fileName + '\'' +
                ", created=" + created +
                ", lastModified=" + lastModified +
                ", tag='" + tag + '\'' +
                ", isPasswordProtected=" + isPasswordProtected +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (created != note.created) return false;
        if (lastModified != note.lastModified) return false;
        if (isPasswordProtected != note.isPasswordProtected) return false;
        if (id != null ? !id.equals(note.id) : note.id != null) return false;
        if (title != null ? !title.equals(note.title) : note.title != null) return false;
        if (description != null ? !description.equals(note.description) : note.description != null)
            return false;
        if (fileName != null ? !fileName.equals(note.fileName) : note.fileName != null)
            return false;
        return tag != null ? tag.equals(note.tag) : note.tag == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (int) (created ^ (created >>> 32));
        result = 31 * result + (int) (lastModified ^ (lastModified >>> 32));
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (isPasswordProtected ? 1 : 0);
        return result;
    }
}
