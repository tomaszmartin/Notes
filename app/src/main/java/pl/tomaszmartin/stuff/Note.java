package pl.tomaszmartin.stuff;

import java.util.Date;

/**
 * Created by tomaszmartin on 24.03.15.
 */
public class Note {
    private Integer id;
    private String title;
    private Date date;
    private Date lastModified;
    private String fileName;
    private String description;
    private int category;
    private int type;
    private String image;
    public static final int LIST = 2;
    public static final int ARTICLE = 1;
    public static final int NOTE = 0;
    public static final int ALL = 0;
    public static final int PERSONAL = 1;
    public static final int WORK = 2;

    public Note(Integer id, String title, String fileName, Date date, int type, int cat) {
        this.id = id;
        this.title = title;
        this.fileName = fileName;
        this.date = date;
        this.description = "...";
        this.type = type;
        this.category = cat;
    }

    public Note(Integer id, String title, String fileName) {
        this(id, title, fileName, new Date(), 0, 1);
    }

    public Note(Integer id, String title, String fileName, int type) {
        this(id, title, fileName, new Date(), type, 1);
    }

    public String getImage() {
        return image;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
