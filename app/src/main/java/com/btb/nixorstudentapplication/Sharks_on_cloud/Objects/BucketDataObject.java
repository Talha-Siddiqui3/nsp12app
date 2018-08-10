package com.btb.nixorstudentapplication.Sharks_on_cloud.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Date;

public class BucketDataObject implements Parcelable {
private boolean folder;
private Date date;
private Timestamp dateTimeStamp;
private String name;
private String PhotoUrlThumbnail;
private String ID;

public BucketDataObject(){

}


    public BucketDataObject(Parcel in) {
        folder = in.readByte() != 0;
        dateTimeStamp = in.readParcelable(Timestamp.class.getClassLoader());
        name = in.readString();
        PhotoUrlThumbnail = in.readString();
        ID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (folder ? 1 : 0));
        dest.writeParcelable(dateTimeStamp, flags);
        dest.writeString(name);
        dest.writeString(PhotoUrlThumbnail);
        dest.writeString(ID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BucketDataObject> CREATOR = new Creator<BucketDataObject>() {
        @Override
        public BucketDataObject createFromParcel(Parcel in) {
            return new BucketDataObject(in);
        }

        @Override
        public BucketDataObject[] newArray(int size) {
            return new BucketDataObject[size];
        }
    };

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Timestamp getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(Timestamp dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrlThumbnail() {
        return PhotoUrlThumbnail;
    }

    public void setPhotoUrlThumbnail(String photoUrlThumbnail) {
        PhotoUrlThumbnail = photoUrlThumbnail;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
