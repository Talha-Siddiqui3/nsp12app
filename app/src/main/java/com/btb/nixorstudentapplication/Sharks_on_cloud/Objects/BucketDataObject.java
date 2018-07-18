package com.btb.nixorstudentapplication.Sharks_on_cloud.Objects;

import java.util.Date;

public class BucketDataObject {
private boolean folder;
private Date date;
private String name;
private String PhotoUrlThumbnail;

    public String getPhotoUrlThumbnail() {
        return PhotoUrlThumbnail;
    }

    public void setPhotoUrlThumbnail(String photoUrlThumbnail) {
        this.PhotoUrlThumbnail = photoUrlThumbnail;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean isfolder) {
        this.folder = isfolder;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
