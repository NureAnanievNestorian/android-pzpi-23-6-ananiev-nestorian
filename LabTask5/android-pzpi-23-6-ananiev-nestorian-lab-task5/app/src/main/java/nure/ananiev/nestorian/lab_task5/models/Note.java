package nure.ananiev.nestorian.lab_task5.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nure.ananiev.nestorian.lab_task5.enums.Importance;

public class Note {

    private final int id;
    private String title;
    private String description;
    private final Date creationDate;
    private Date purposeDate;
    private Importance importance;
    private String attachedImageUri;

    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    public Note(int id, Date creationDate, String title, String description, Date purposeDate, Importance importance, String attachedImageUri) {
        this.id = id;
        this.creationDate = creationDate;
        this.title = title;
        this.description = description;
        this.purposeDate = purposeDate;
        this.importance = importance;
        this.attachedImageUri = attachedImageUri;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormattedCreationDate() {
        return dateFormat.format(creationDate);
    }

    public String getFormattedPurposeDate() {
        return dateFormat.format(purposeDate);
    }

    public Date getPurposeDate() {
        return purposeDate;
    }

    public void setPurposeDate(Date purposeDate) {
        this.purposeDate = purposeDate;
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    public String getAttachedImageUri() {
        return attachedImageUri;
    }

    public void setAttachedImageUri(String attachedImageUri) {
        this.attachedImageUri = attachedImageUri;
    }
}
