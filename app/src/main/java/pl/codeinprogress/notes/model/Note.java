package pl.codeinprogress.notes.model;

import io.realm.RealmObject;

public class Note extends RealmObject {

    private String id;
    private String title;
    private String description;
    private String path;
    private long created;
    private long modified;
    private boolean secured;

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
        if (null != description && description.length() > maxLength) {
            description = description.substring(0, maxLength);
            description = description.substring(0, description.lastIndexOf(" "));
        }
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public boolean isSecured() {
        return secured;
    }

    public void setSecured(boolean secured) {
        this.secured = secured;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", path='" + path + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", secured=" + secured +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (created != note.created) return false;
        if (modified != note.modified) return false;
        if (secured != note.secured) return false;
        if (id != null ? !id.equals(note.id) : note.id != null) return false;
        if (title != null ? !title.equals(note.title) : note.title != null) return false;
        if (description != null ? !description.equals(note.description) : note.description != null)
            return false;
        return path != null ? path.equals(note.path) : note.path == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (int) (created ^ (created >>> 32));
        result = 31 * result + (int) (modified ^ (modified >>> 32));
        result = 31 * result + (secured ? 1 : 0);
        return result;
    }

}
